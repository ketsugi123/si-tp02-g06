/*
 Walter Vieira (2022-04-22)
 Sistemas de Informação - projeto JPAAulas_ex1
 Código desenvolvido para iulustração dos conceitos sobre acesso a dados, concretizados com base na especificação JPA.
 Todos os exemplos foram desenvolvidos com EclipseLinlk (3.1.0-M1), usando o ambientre Eclipse IDE versão 2022-03 (4.23.0).
 
Não existe a pretensão de que o código estaja completo.

Embora tenha sido colocado um esforço significativo na correção do código, não há garantias de que ele não contenha erros que possam 
acarretar problemas vários, em particular, no que respeita à consistência dos dados.  
 
*/

package businessLogic;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;


import jakarta.persistence.*;
import model.embeddables.CrachaId;
import model.embeddables.CrachasAdquiridosId;
import model.relations.CrachasAdquiridos;
import model.tables.Cracha;
import model.tables.Jogador;
import model.tables.Jogo;
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

    private Jogo getGameByName(String gameName){
        TypedQuery<Jogo> query = em.createQuery("SELECT j FROM Jogo j WHERE j.nome = ?1", Jogo.class);
        query.setParameter(1, gameName);
        return query.getSingleResult();
    }

    private Jogador getPlayerByEmail(String email){
        TypedQuery<Jogador> query = em.createQuery("SELECT j FROM Jogador j WHERE j.email = ?1", Jogador.class);
        query.setParameter(1, email);
        return query.getSingleResult();
    }

    private CrachasAdquiridosId setCid(String idJogo, String nomeCracha, int idJogador){
        CrachasAdquiridosId crachasAdquiridosId = new CrachasAdquiridosId();
        crachasAdquiridosId.setJogo(idJogo);
        crachasAdquiridosId.setCracha(nomeCracha);
        crachasAdquiridosId.setJogador(idJogador);
        return  crachasAdquiridosId;
    }

    private CrachaId setCrachaId(String idJogo, String nomeCracha){
        CrachaId crachaId = new CrachaId();
        crachaId.setJogo(idJogo);
        crachaId.setNome(nomeCracha);
        return crachaId;
    }

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
        int idJogador = getPlayerByEmail(playerName).getId();
        String query = "SELECT setPlayerState(?1, ?2)";
        Query functionQuery = em.createNativeQuery(query)
                .setParameter(1, idJogador)
                .setParameter(2, newState);
        return (String) functionQuery.getSingleResult();
    }

    // Exercise 2e
    public Long totalPontosJogador(String email) {
        int idJogador = getPlayerByEmail(email).getId();
        String query = "SELECT totalPontos from totalPontosJogador(?1)";
        Query functionQuery = em.createNativeQuery(query);
        functionQuery.setParameter(1, idJogador);
        return (Long) functionQuery.getSingleResult();
    }

    // Exercise 2f
    public Long totalJogosJogador(String email) {
        int idJogador = getPlayerByEmail(email).getId();
        String query = "SELECT totalJogos from totalJogosJogador(?1)";
        Query functionQuery = em.createNativeQuery(query);
        functionQuery.setParameter(1, idJogador);
        return (Long) functionQuery.getSingleResult();
    }

    // Exercise 2g (Temporary implementation, not optimized)

    public Map<Integer, BigDecimal> PontosJogosPorJogador(String gameName) {
        String idJogo = getGameByName(gameName).getId();
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
        EntityTransaction transaction = startTransaction();
        Connection cn = em.unwrap(Connection.class);
        String idJogo = getGameByName(gameName).getNome();
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


    public void associarCrachaModel(int idJogador, String gameName, String nomeCracha){
        String idJogo = getGameByName(gameName).getId();
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

            TypedQuery<Integer> getPlayerId = em.createQuery(
                    "select c.jogador.id from Compra c "  +
                    "where c.jogador.id = :idJogador and c.jogo.id = :idJogo",
                    Integer.class
            );
            getPlayerId.setParameter("idJogador", idJogador);
            getPlayerId.setParameter("idJogo", idJogo);
            if(idJogador == getPlayerId.getSingleResult()){
                String jogadoresQuery = "SELECT pontuacaoTotal from PontosJogosPorJogador(?1) WHERE jogadores = ?2";
                Query pontuacaoQuery = em.createNativeQuery(jogadoresQuery);
                pontuacaoQuery.setParameter(1, idJogo);
                pontuacaoQuery.setParameter(2, idJogador);
                BigDecimal totalPoints = (BigDecimal) pontuacaoQuery.getSingleResult();
                if(limit <= totalPoints.intValue() ){
                    Query q = em.createQuery("SELECT ca.id.jogo from CrachasAdquiridos ca WHERE ca.id.jogador = ?1");
                    q.setParameter(1, idJogador);
                    if(q.getResultList().isEmpty()){
                        CrachasAdquiridos crachaAdquirido = new CrachasAdquiridos();
                        CrachasAdquiridosId crachasAdquiridosId = setCid(idJogo, nomeCracha, idJogador);
                        crachaAdquirido.setId(crachasAdquiridosId);

                        Cracha cracha = em.find(Cracha.class, setCrachaId(idJogo, nomeCracha));
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
                    "UPDATE crachá c SET c.limit = c.limit * 1.2, c.version = c.version + 1 " +
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
            System.out.println("Error Message: " + e.getMessage());
        }
    }

    /**
     * 2.(a), 2.(b), 2.(c)
     */

    // Exercise 2 (a)
    public void aumentarPontosOptimistic(String nomeCracha, String idJogo) {
        aumentarPontosCracha20(nomeCracha, idJogo, LockModeType.OPTIMISTIC);
    }

    // Exercise 2 (b) -> Inside Test Folder

    // Exercise 2 (c)
    public void aumentarPontosPessimistic(String nomeCracha, String idJogo, LockModeType lockType) {
        aumentarPontosCracha20(nomeCracha, idJogo, LockModeType.PESSIMISTIC_READ);
    }
}
