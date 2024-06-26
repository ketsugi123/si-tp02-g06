/*
 Walter Vieira (2022-04-22)
 Sistemas de Informa��o - projeto JPAAulas_ex1
 C�digo desenvolvido para iulustra��o dos conceitos sobre acesso a dados, concretizados com base na especifica��o JPA.
 Todos os exemplos foram desenvolvidos com EclipseLinlk (3.1.0-M1), usando o ambientre Eclipse IDE vers�o 2022-03 (4.23.0).
 
N�o existe a pretens�o de que o c�digo estaja completo.

Embora tenha sido colocado um esfor�o significativo na corre��o do c�digo, n�o h� garantias de que ele n�o contenha erros que possam 
acarretar problemas v�rios, em particular, no que respeita � consist�ncia dos dados.  
 
*/

package businessLogic;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.Map;


import businessLogic.BLserviceUtils.ModelManager;
import businessLogic.BLserviceUtils.TransactionManager;
import jakarta.persistence.*;
import model.embeddables.CrachaId;
import model.embeddables.CrachasAdquiridosId;
import model.relations.CrachasAdquiridos;
import model.tables.Cracha;
import model.tables.Jogador;
import model.views.JogadorTotalInfo;

/**
 * Hello world!
 *
 */
public class BLService 
{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAex");
    EntityManager em = emf.createEntityManager();

    private final ModelManager modelManager = new ModelManager(em);
    private final TransactionManager transactionManager = new TransactionManager(em);

    /**
     * 1. (a)
     * Access to the funcionalities 2d to 2l
     */
    // These 2 functions constitute the exercise 2d, these do not require a transaction level above
    // read uncommitted since they only preform an update to the respective tables once
    public String createPlayer(String username, String email, String regiao) {
        String query = "SELECT createPlayer(?1, ?2, ?3)";
        Query functionQuery = em.createNativeQuery(query)
                .setParameter(1, username)
                .setParameter(2, email)
                .setParameter(3, regiao);
        return (String) functionQuery.getSingleResult();

    }

    public String setPlayerState(String playerName, String newState) {
        int idJogador = modelManager.getPlayerByEmail(playerName).getId();
        String query = "SELECT setPlayerState(?1, ?2)";
        Query functionQuery = em.createNativeQuery(query)
                .setParameter(1, idJogador)
                .setParameter(2, newState);
        return (String) functionQuery.getSingleResult();
    }

    // Exercise 2e
    public Long totalPontosJogador(String email) {
        int idJogador = modelManager.getPlayerByEmail(email).getId();
        String query = "SELECT totalPontos from totalPontosJogador(?1)";
        Query functionQuery = em.createNativeQuery(query);
        functionQuery.setParameter(1, idJogador);
        return (Long) functionQuery.getSingleResult();
    }

    // Exercise 2f
    public Long totalJogosJogador(String email) {
        int idJogador = modelManager.getPlayerByEmail(email).getId();
        String query = "SELECT totalJogos from totalJogosJogador(?1)";
        Query functionQuery = em.createNativeQuery(query);
        functionQuery.setParameter(1, idJogador);
        return (Long) functionQuery.getSingleResult();
    }

    // Exercise 2g (Temporary implementation, not optimized)

    public Map<Integer, BigDecimal> PontosJogosPorJogador(String gameName) {
        String idJogo = modelManager.getGameByName(gameName).getId();
        String queryString = "SELECT jogadores, pontuacaoTotal from PontosJogosPorJogador(?1)";
        Query query = em.createNativeQuery(queryString);
        query.setParameter(1, idJogo);
        Map<Integer, BigDecimal> map = new java.util.HashMap<>(Map.of());
        List<Object[]> list  = query.getResultList();
        for (Object[] obj : list) {
            Integer idJogador = (Integer) obj[0];
            BigDecimal points = (BigDecimal) obj[1];
            map.put(idJogador,  points);
        }
        return map;
    }


    // Exercise 2h
    public void associarCracha(int idJogador, String gameName, String nomeCracha) {
        EntityTransaction transaction = transactionManager.startTransaction();
        Connection cn = em.unwrap(Connection.class);
        String idJogo = modelManager.getGameByName(gameName).getNome();
        try {
            transactionManager.setIsolationLevel(cn, Connection.TRANSACTION_REPEATABLE_READ, transaction);
            try (CallableStatement storedProcedure = cn.prepareCall("call associarCracha(?,?, ?)")) {
                storedProcedure.setInt(1, idJogador);
                storedProcedure.setString(2, idJogo);
                storedProcedure.setString(3, nomeCracha);
                storedProcedure.executeUpdate();
                transaction.commit();
            }
        } catch(Exception e){
            if(transaction.isActive()) transaction.rollback();
        }
    }

    // Exercise 2i
    public Integer iniciarConversa(int idJogador, String nomeConversa) {
        EntityTransaction transaction = transactionManager.startTransaction();
        Connection cn = em.unwrap(Connection.class);
        Integer idConversa = null;
        try {
            transactionManager.setIsolationLevel(cn, Connection.TRANSACTION_REPEATABLE_READ, transaction);
            try (CallableStatement storedProcedure = cn.prepareCall("call iniciarConversa(?,?, ?)")) {
                storedProcedure.setInt(1, idJogador);
                storedProcedure.setString(2, nomeConversa);
                storedProcedure.registerOutParameter(3, Types.INTEGER);
                storedProcedure.executeUpdate();
                idConversa = storedProcedure.getInt(3);
                transaction.commit();
            }
        } catch(Exception e){
            if(transaction.isActive()) transaction.rollback();
            return null;
        }
        return idConversa;
    }

    // Exercise 2j
    public void juntarConversa(int idJogador, int idConversa) {
        EntityTransaction transaction = transactionManager.startTransaction();
        Connection cn = em.unwrap(Connection.class);
        try {
            try (CallableStatement storedProcedure = cn.prepareCall("call juntarConversa(?,?)")) {
                storedProcedure.setInt(1, idJogador);
                storedProcedure.setInt(2, idConversa);
                transaction.commit();
            }
        } catch(Exception e){
            if(transaction.isActive()) transaction.rollback();
        }
    }

    // Exercise 2k
    public void enviarMensagem(int idJogador, int idConversa, String content) {

        EntityTransaction transaction = transactionManager.startTransaction();
        Connection cn = em.unwrap(Connection.class);
        try {
            transactionManager.setIsolationLevel(cn, Connection.TRANSACTION_READ_COMMITTED, transaction);
            try (CallableStatement storedProcedure = cn.prepareCall("call enviarMensagem(?,?, ?)")) {
                cn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
                storedProcedure.setInt(1, idJogador);
                storedProcedure.setInt(2, idConversa);
                storedProcedure.setString(3, content);
                storedProcedure.executeUpdate();
                transaction.commit();
            }
        } catch(Exception e){
            if(transaction.isActive()) transaction.rollback();
        }
    }

    public void jogadorTotalInfo(){
        for(JogadorTotalInfo jogador: modelManager.getPlayertotalInfo()){
            System.out.println(
                    jogador.getEstado() + " " +
                    jogador.getEmail() + " " +
                    jogador.getUsername() + " " +
                    jogador.getJogosParticipados() + " " +
                    jogador.getPartidasParticipadas() + " " +
                    jogador.getPontuacaoTotal() + " "
            );
        }
    }


    public void associarCrachaModel(int idJogador, String gameName, String nomeCracha){
        String idJogo = modelManager.getGameByName(gameName).getId();
        TypedQuery<String> crachaExistsQuery =
                em.createQuery(
                        "select c.id.jogo from Cracha c where c.id.jogo = :idJogo and c.id.nome = :nomeCracha",
                        String.class
                );

        crachaExistsQuery.setParameter("idJogo", idJogo);
        crachaExistsQuery.setParameter("nomeCracha", nomeCracha);
        String foundIdJogo = crachaExistsQuery.getSingleResult();

        if(foundIdJogo.equals(idJogo)){
            TypedQuery<Integer> getLimitPoints = em.createQuery(
                    "select c.limite from Cracha c where c.id.nome = :nomeCracha",
                    Integer.class
            );
            getLimitPoints.setParameter("nomeCracha", nomeCracha);
            Integer limit = getLimitPoints.getSingleResult();

            if(modelManager.ownsGame(idJogador, idJogo)){
                BigDecimal totalPoints = modelManager.getPlayerPoints(idJogo, idJogador);
                if(limit <= totalPoints.intValue() ){
                   
                    if(!modelManager.ownsBadge(idJogador, idJogo)){
                        CrachasAdquiridos crachaAdquirido = new CrachasAdquiridos();
                        CrachasAdquiridosId crachasAdquiridosId = modelManager.setCrachaAdquiridoId(idJogo, nomeCracha, idJogador);
                        crachaAdquirido.setId(crachasAdquiridosId);

                        Cracha cracha = em.find(Cracha.class, modelManager.setCrachaId(idJogo, nomeCracha));
                        crachaAdquirido.setCracha(cracha);

                        Jogador jogador = em.find(Jogador.class, idJogador); // Fetch the Jogador entity by ID
                        crachaAdquirido.setJogador(jogador);
                        em.getTransaction().begin();
                        em.persist(crachaAdquirido);
                        em.getTransaction().commit();
                    }

                }

            }

        }
    }

    private void aumentarPontosCracha20(String nomeCracha, String idJogo, LockModeType lockType) throws Exception {
        EntityTransaction transaction = transactionManager.startTransaction();
        Connection cn = em.unwrap(Connection.class);
        try {
            transactionManager.setIsolationLevel(cn, Connection.TRANSACTION_REPEATABLE_READ, transaction);
            String selectQuery = "SELECT c.id FROM Cracha c WHERE c.id.nome = ?1 AND c.id.jogo = ?2";
            TypedQuery<CrachaId> selectTypedQuery = em.createQuery(selectQuery, CrachaId.class);
            selectTypedQuery.setParameter(1, nomeCracha);
            selectTypedQuery.setParameter(2, idJogo);
            selectTypedQuery.setLockMode(lockType);
            CrachaId cracha = selectTypedQuery.getSingleResult();
            String query =
                    "UPDATE Cracha c SET c.limite = c.limite * 1.2 WHERE c.id = :crachaId";
            Query updateQuery = em.createQuery(query);
            updateQuery.setParameter("crachaId", cracha);
            updateQuery.executeUpdate();
            transaction.commit();
        }
        catch (Exception e){
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
    }

    /**
     * 2.(a), 2.(b), 2.(c)
     */

    // Exercise 2 (a)
    public void aumentarPontosOptimistic(String nomeCracha, String gameName) throws Exception {
        String idJogo = modelManager.getGameByName(gameName).getId();
        aumentarPontosCracha20(nomeCracha, idJogo, LockModeType.OPTIMISTIC);
    }

    // Exercise 2 (b) -> Inside test/java/ConcurrencyErrorTest.java

    // Exercise 2 (c)
    public void aumentarPontosPessimistic(String nomeCracha, String gameName) throws Exception {
        String idJogo = modelManager.getGameByName(gameName).getId();
        aumentarPontosCracha20(nomeCracha, idJogo, LockModeType.PESSIMISTIC_READ);
    }
}
