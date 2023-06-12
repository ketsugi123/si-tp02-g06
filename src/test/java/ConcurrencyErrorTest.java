import businessLogic.BLService;
import businessLogic.BLserviceUtils.ModelManager;
import jakarta.persistence.*;
import model.embeddables.CrachaId;
import model.tables.Cracha;
import model.tables.Jogo;

import java.util.ArrayList;

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
        Cracha crachaIfExists = em.find(Cracha.class, crachaId, LockModeType.OPTIMISTIC);
        if(crachaIfExists != null ) em.remove(crachaIfExists);
        em.getTransaction().commit();
        // Create a TestCracha
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        Cracha cracha = new Cracha();
        cracha.setId(crachaId);
        cracha.setLimite(100); // Initial limit value// Initial version value
        cracha.setUrl("testCracha.com");
        cracha.setJogo(jogo);
        em.persist(cracha);
        transaction.commit();
    }

    public static void main(String[] args) throws InterruptedException {
        ConcurrencyErrorTest test = new ConcurrencyErrorTest();
        Jogo jogo = test.jogo;
        test.setupTestData();
        Integer nThreads = 8;
        ArrayList<Thread> ths = new ArrayList<>();

        for(int i = 0; i < nThreads; i++){
            ths.add(new Thread(
                    () -> {
                        try {
                            test.executeConcurrentUpdate(jogo.getId());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            ));
        }

        for(int i = 0; i < nThreads; i++){
            ths.get(i).start();
        }

        for(int i = 0; i < nThreads; i++){
            ths.get(i).join();
        }


        test.close();
    }

    public void executeConcurrentUpdate(String idJogo) throws Exception {
        String nomeCracha = "TestCracha";
        blService.aumentarPontosOptimistic(nomeCracha, idJogo);
    }
}
