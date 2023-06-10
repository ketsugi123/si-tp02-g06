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
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import businessLogic.Utlis.PontosPorJogador;
import jakarta.persistence.*;
import model.tables.Cracha;
import model.views.JogadorTotalInfo;

/**
 * Hello world!
 *
 */
public class BLService 
{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAex");
    EntityManager em = emf.createEntityManager();

    private EntityTransaction startTransaction(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        return  transaction;
    }

    private void setIsolationLevel(Connection cn, Integer isolationLevel, EntityTransaction transaction) throws SQLException {
        transaction.rollback();
        cn.setTransactionIsolation(isolationLevel);
        transaction.begin();
    }
    //@SuppressWarnings("unchecked")

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

    public String setPlayerState(int idJogador, String newState) {
        String query = "SELECT setPlayerState(?1, ?2)";
        Query functionQuery = em.createNativeQuery(query)
                .setParameter(1, idJogador)
                .setParameter(2, newState);
        return (String) functionQuery.getSingleResult();
    }

    // Exercise 2e
    public Long totalPontosJogador(int idJogador) {
        String query = "SELECT totalPontos from totalPontosJogador(?1)";
        Query functionQuery = em.createNativeQuery(query);
        functionQuery.setParameter(1, idJogador);
        return (Long) functionQuery.getSingleResult();
    }

    // Exercise 2f
    public Long totalJogosJogador(int idJogador) {
        String query = "SELECT totalJogos from totalJogosJogador(?1)";
        Query functionQuery = em.createNativeQuery(query);
        functionQuery.setParameter(1, idJogador);
        return (Long) functionQuery.getSingleResult();
    }

    // Exercise 2g (Temporary implementation, not optimized)
    public ArrayList<Map<Integer, BigDecimal>> PontosJogosPorJogador(String idJogo) {
        String jogadoresQuery = "SELECT jogadores from PontosJogosPorJogador(?1)";
        Query jogadoresFunctionQuery = em.createNativeQuery(jogadoresQuery);
        jogadoresFunctionQuery.setParameter(1, idJogo);
        String pontuacaoQuery = "SELECT pontuacaoTotal from PontosJogosPorJogador(?1)";
        Query pontuacaoFunctionQuery = em.createNativeQuery(pontuacaoQuery);
        pontuacaoFunctionQuery.setParameter(1, idJogo);
        List<Integer> idList = jogadoresFunctionQuery.getResultList();
        List<BigDecimal> pointsList = pontuacaoFunctionQuery.getResultList();
        ArrayList<Map<Integer, BigDecimal>> resultList = new ArrayList<>();
        int numberOfPlayers = idList.size();
        for (int i = 0; i < numberOfPlayers; i++) {
            resultList.add(Map.of(idList.get(i), pointsList.get(i)));
        }
        return resultList;
    }

    // Exercise 2h
    public void associarCracha(int idJogador, String idJogo, String nomeCracha) {
        EntityTransaction transaction = startTransaction();
        Connection cn = em.unwrap(Connection.class);
        try {
            setIsolationLevel(cn, Connection.TRANSACTION_REPEATABLE_READ, transaction);
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
        EntityTransaction transaction = startTransaction();
        Connection cn = em.unwrap(Connection.class);
        Integer idConversa = null;
        try {
            setIsolationLevel(cn, Connection.TRANSACTION_REPEATABLE_READ, transaction);
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
        }
        return idConversa;
    }

    // Exercise 2j
    public void juntarConversa(int idJogador, int idConversa) {
        EntityTransaction transaction = startTransaction();
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

        EntityTransaction transaction = startTransaction();
        Connection cn = em.unwrap(Connection.class);
        try {
            setIsolationLevel(cn, Connection.TRANSACTION_READ_COMMITTED, transaction);
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
        String query = "Select * from jogadorTotalInfo";
        Query q = em.createNativeQuery(query, JogadorTotalInfo.class);
        List<JogadorTotalInfo> allInfo =  q.getResultList();
        for(JogadorTotalInfo jogador: allInfo){
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

    public void pontosJogosPorJogadorModel(String idJogo)
    {
        Query q = em.createQuery("SELECT pn.partida_normalId.jogador, COALESCE(SUM(pn.pontuacao), 0) FROM Partida_Normal pn " +
                "WHERE pn.partida_normalId.partida IN (SELECT pn.partida_normalId.partida FROM Partida p WHERE p.id.jogo = ?1) " +
                "GROUP BY pn.partida_normalId.jogador " +
                "UNION " +
                "SELECT pm.partida_multiId.jogador, COALESCE(SUM(pm.pontuacao), 0) FROM Partida_MultiJogador pm " +
                "WHERE pm.partida_multiId.partida IN (SELECT pm.partida_multiId.partida FROM Partida p1 WHERE p1.id.jogo = ?1) " +
                "GROUP BY pm.partida_multiId.jogador");


        List<PontosPorJogador> lst = q.getResultList();
        for(PontosPorJogador p : lst){

        }
    }

    public void associarCrachaModel(int idJogador, String idJogo, String nomeCracha){
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
                    "select c.limit from Cracha c where c.id.nome = :nomeCracha",
                    Integer.class
            );
            Integer limit = getLimitPoints.getSingleResult();

            TypedQuery<Integer> getPlayerId = em.createQuery(
                    "select c.jogador.id from Compra c "  +
                    "where c.jogador.id = :idJogador and c.jogo.id = :idJogo",
                    Integer.class
            );
            if(idJogador == getPlayerId.getSingleResult()){

            }

        }
    }

    private void aumentarPontosCracha20(String nomeCracha, String idJogo, LockModeType lockType) {
        EntityTransaction transaction = startTransaction();
        Connection cn = em.unwrap(Connection.class);
        try {
            setIsolationLevel(cn, Connection.TRANSACTION_READ_COMMITTED, transaction);
            String selectQuery = "SELECT c FROM Cracha c WHERE c.id.nome = ?1 AND c.id.jogo = ?2";
            TypedQuery<Cracha> selectTypedQuery = em.createQuery(selectQuery, Cracha.class);
            selectTypedQuery.setParameter(1, nomeCracha);
            selectTypedQuery.setParameter(2, idJogo);
            selectTypedQuery.setLockMode(lockType);
            Cracha cracha = selectTypedQuery.getSingleResult();
            setIsolationLevel(cn, Connection.TRANSACTION_REPEATABLE_READ, transaction);
            String query =
                    "UPDATE crach� c SET c.limit = c.limit * 1.2, c.version = c.version + 1 " +
                            "WHERE c.id = :crachaId AND c.version = :crachaVersion";
            Query updateQuery = em.createNativeQuery(query);
            updateQuery.setParameter("crachaId", cracha.getId());
            updateQuery.setParameter("crachaVersion", Cracha.getSerialVersionUID());
            int updatedCount = updateQuery.executeUpdate();
            if (updatedCount == 0) {
                switch (lockType) {
                    case OPTIMISTIC -> throw new OptimisticLockException("Concurrent update detected for Cracha");
                    case PESSIMISTIC_READ -> throw new PessimisticLockException("Concurrent update detected for Cracha");
                    default -> throw new Exception("Concurrent update detected for Cracha");
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
        }
    }

    //2 (a)
    public void aumentarPontosOptimistic(String nomeCracha, String idJogo) {
        aumentarPontosCracha20(nomeCracha, idJogo, LockModeType.OPTIMISTIC);
    }

    //2 (c)
    public void aumentarPontosPessimistic(String nomeCracha, String idJogo, LockModeType lockType) {
        aumentarPontosCracha20(nomeCracha, idJogo, LockModeType.PESSIMISTIC_READ);
    }
}
