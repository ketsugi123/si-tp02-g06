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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

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
        String pontuacaoQuery = "SELECT pontuaçãoTotal from PontosJogosPorJogador(?1)";
        Query pontuacaoFunctionQuery = em.createNativeQuery(pontuacaoQuery);
        pontuacaoFunctionQuery.setParameter(1, idJogo);
        List<Integer> idList = (List<Integer>) jogadoresFunctionQuery.getResultList();
        List<BigDecimal> pointsList = (List<BigDecimal>) pontuacaoFunctionQuery.getResultList();
        ArrayList<Map<Integer, BigDecimal>> resultList = new ArrayList<>();
        int numberOfPlayers = idList.size();
        for (int i = 0; i < numberOfPlayers; i++) {
            resultList.add(Map.of(idList.get(i), pointsList.get(i)));
        }
        return resultList;
    }

    // Exercise 2h
    public void associarCracha(int idJogador, String idJogo, String nomeCracha) throws SQLException {
        EntityTransaction transaction = startTransaction();
        Connection cn = em.unwrap(Connection.class);
        try {
            cn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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
            try (CallableStatement storedProcedure = cn.prepareCall("call iniciarConversa(?,?, ?)")) {
                cn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
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

    // Exercise 2l
    //TODO(Context: View jogadorTotalInfo)

    /**
     * 1. (b)
     * Functionality 2h without stored procedures or functions
     */
    //TODO

    /**
     * 1. (c)
     * Functionality 2h with the reuse of procedures and functions
     */
    //TODO
}
