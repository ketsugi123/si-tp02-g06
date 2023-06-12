import businessLogic.BLService;
import jakarta.persistence.*;
import model.embeddables.CrachaId;
import model.tables.Cracha;

public class ConcurrencyErrorTest {
    private final BLService blService = new BLService();
    private final EntityManagerFactory emf;
    private final EntityManager em;
    private final EntityTransaction transaction;

    public ConcurrencyErrorTest() {
        emf = Persistence.createEntityManagerFactory("YourPersistenceUnitName");
        em = emf.createEntityManager();
        transaction = em.getTransaction();
    }

    public void close() {
        em.close();
        emf.close();
    }

    public void setupTestData() {
        // Create a Cracha entity with initial values
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Cracha cracha = new Cracha();
        CrachaId crachaId = new CrachaId();
        crachaId.setNome("TestCracha");
        crachaId.setJogo("TestJogo");
        cracha.setId(crachaId);
        cracha.setLimite(100); // Initial limit value
        cracha.setVersion(1L); // Initial version value
        cracha.setUrl("testCracha.com");
        em.persist(cracha);
        transaction.commit();
    }

    private void rollbackUpdateChanges() {
        transaction.begin();
        em.createQuery("UPDATE Cracha c SET c.limite = c.limite / 1.2, c.version = c.version - 1 " +
                        "WHERE c.id.nome = :nomeCracha AND c.id.jogo = :idJogo")
                .setParameter("nomeCracha", "TestCracha")
                .setParameter("idJogo", "TestJogo")
                .executeUpdate();
        transaction.commit();
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrencyErrorTest test = new ConcurrencyErrorTest();
        test.setupTestData();

        Thread thread1 = new Thread(test::executeConcurrentUpdate);

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000); // Introduce a delay before executing the update
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            test.executeConcurrentUpdate();
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        // Rollback the update changes
        if (test.successfulUpdate()) {
            test.rollbackUpdateChanges();
        }

        test.close();
    }

    public void executeConcurrentUpdate() {
        String nomeCracha = "TestCracha";
        String idJogo = "TestJogo";
        blService.aumentarPontosOptimistic(nomeCracha, idJogo);
    }

    private boolean successfulUpdate() {
        String nomeCracha = "TestCracha";
        String idJogo = "TestJogo";

        String selectQuery = "SELECT c FROM Cracha c WHERE c.id.nome = :nomeCracha AND c.id.jogo = :idJogo";
        TypedQuery<Cracha> selectTypedQuery = em.createQuery(selectQuery, Cracha.class);
        selectTypedQuery.setParameter("nomeCracha", nomeCracha);
        selectTypedQuery.setParameter("idJogo", idJogo);
        Cracha cracha = selectTypedQuery.getSingleResult();

        // Check if the limit and version values have been updated
        return cracha.getLimite() > 100 && Cracha.getVersion() > 1L;
    }
}
