import businessLogic.BLService;
import businessLogic.BLserviceUtils.ModelManager;
import jakarta.persistence.*;
import model.embeddables.CrachaId;
import model.tables.Cracha;
import model.tables.Jogo;

public class ConcurrencyErrorTest {
    private final BLService blService = new BLService();
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final EntityTransaction transaction;
    private final Jogo jogo;


    public ConcurrencyErrorTest() {
        emf = Persistence.createEntityManagerFactory("JPAex");
        em = emf.createEntityManager();
        transaction = em.getTransaction();
        ModelManager modelManager = new ModelManager(em);
        jogo = modelManager.getGameByName("Valorant");
    }


    public void close() {
        em.close();
        emf.close();
    }

    public void setupTestData() {
        // Check if the TestCracha already exists
        em.getTransaction().begin();
        CrachaId crachaId = new CrachaId();
        crachaId.setNome("TestCracha");
        crachaId.setJogo(jogo.getId());
        Cracha crachaIfExists = em.find(Cracha.class, crachaId);
        em.remove(crachaIfExists);
        em.getTransaction().commit();
        // Create a TestCracha
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Cracha cracha = new Cracha();
        cracha.setId(crachaId);
        cracha.setLimite(100); // Initial limit value
        cracha.setVersion(1); // Initial version value
        cracha.setUrl("testCracha.com");
        cracha.setJogo(jogo);
        em.persist(cracha);
        transaction.commit();
    }

    private void rollbackUpdateChanges(String idJogo) {
        transaction.begin();
        em.createQuery("UPDATE Cracha c SET c.limite = c.limite / 1.2, c.version = c.version - 1 " +
                        "WHERE c.id.nome = :nomeCracha AND c.id.jogo = :idJogo")
                .setParameter("nomeCracha", "TestCracha")
                .setParameter("idJogo", idJogo)
                .executeUpdate();
        transaction.commit();
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrencyErrorTest test = new ConcurrencyErrorTest();
        Jogo jogo = test.jogo;
        test.setupTestData();

        Thread thread1 = new Thread(
                () -> test.executeConcurrentUpdate(jogo.getId())
        );

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000); // Introduce a delay before executing the update
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            test.executeConcurrentUpdate(jogo.getId());
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();


        // Rollback the update changes
        if (test.successfulUpdate(jogo.getId())) {
            test.rollbackUpdateChanges(jogo.getId());
        }

        test.close();
    }

    public void executeConcurrentUpdate(String idJogo) {
        String nomeCracha = "TestCracha";
        blService.aumentarPontosOptimistic(nomeCracha, idJogo);
    }

    private boolean successfulUpdate(String idJogo) {
        String nomeCracha = "TestCracha";

        String selectQuery = "SELECT c FROM Cracha c WHERE c.id.nome = :nomeCracha AND c.id.jogo = :idJogo";
        TypedQuery<Cracha> selectTypedQuery = em.createQuery(selectQuery, Cracha.class);
        selectTypedQuery.setParameter("nomeCracha", nomeCracha);
        selectTypedQuery.setParameter("idJogo", idJogo);
        Cracha cracha = selectTypedQuery.getSingleResult();

        // Check if the limit and version values have been updated
        return cracha.getLimite() > 100 && cracha.getVersion() > 1;
    }
}
