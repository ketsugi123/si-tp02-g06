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
import jakarta.persistence.*;
import model.views.JogadorTotalInfo;
import org.postgresql.shaded.com.ongres.scram.common.util.UsAsciiUtils;

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
        String pontuacaoQuery = "SELECT pontua��oTotal from PontosJogosPorJogador(?1)";
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

    public void associarCrachaModel(int idJogador, String idJogo, String nomeCracha){

    }

    /*
     * 1. (b)
     * Functionality 2h without stored procedures or functions
     */
    //

}
