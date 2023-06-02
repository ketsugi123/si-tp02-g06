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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import jakarta.persistence.*;

/**
 * Hello world!
 *
 */
public class BLService 
{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("JPAex");
    EntityManager em = emf.createEntityManager();
    //@SuppressWarnings("unchecked")
	public void test1() throws Exception
    { }

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

    public String setPlayerState(Integer idJogador, String newState) {
        String query = "SELECT setPlayerState(?1, ?2)";
        Query functionQuery = em.createNativeQuery(query)
                .setParameter(1, idJogador)
                .setParameter(2, newState);
        return (String) functionQuery.getSingleResult();
    }

    // Exercise 2e
    public Integer totalPontosJogador(Integer idJogador) {
        String query = "SELECT totalPontos from totalPontosJogador(?1)";
        Query functionQuery = em.createNativeQuery(query);
        functionQuery.setParameter(1, idJogador);
        return (Integer) functionQuery.getSingleResult();
    }

    // Exercise 2f
    public Integer totalJogosJogador(Integer idJogador) {
        String query = "SELECT totalJogos from totalJogosJogador(?1)";
        Query functionQuery = em.createNativeQuery(query);
        functionQuery.setParameter(1, idJogador);
        return (Integer) functionQuery.getSingleResult();
    }

    // Exercise 2g (Temporary implementation, not optimized)
    public ArrayList<Map<Integer, BigDecimal>> PontosJogosPorJogador(String idJogo) {
        String jogadoresQuery = "SELECT jogadores from PontosJogosPorJogador(?1)";
        Query jogadoresFunctionQuery = em.createNativeQuery(jogadoresQuery);
        jogadoresFunctionQuery.setParameter(1, idJogo);
        String pontuacaoQuery = "SELECT pontuaçãoTotal from PontosJogosPorJogador(?1)";
        Query pontuacaoFunctionQuery = em.createNativeQuery(pontuacaoQuery);
        jogadoresFunctionQuery.setParameter(1, idJogo);
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
    public void associarCracha(Integer idJogador, String idJogo, String nomeCracha) {
        String query = "associarCrachá(?1, ?2, ?3)";
        StoredProcedureQuery procedureQuery = em.createStoredProcedureQuery(query);
        procedureQuery.setParameter(1, idJogador);
        procedureQuery.setParameter(2, idJogo);
        procedureQuery.setParameter(3, nomeCracha);
        procedureQuery.executeUpdate();
    }

    // Exercise 2i
    public Integer iniciarConversa(Integer idJogador, String nomeConversa) {
        String query = "iniciarConversa(?1, ?2, ?3)";
        StoredProcedureQuery procedureQuery = em.createStoredProcedureQuery(query);
        procedureQuery.setParameter(1, idJogador);
        procedureQuery.setParameter(2, nomeConversa);
        procedureQuery
                .registerStoredProcedureParameter("idConversa", Integer.class, ParameterMode.OUT);
        procedureQuery.executeUpdate();
        return (Integer) procedureQuery.getOutputParameterValue("idConversa");
    }

    // Exercise 2j
    public void juntarConversa(Integer idJogador, Integer idConversa) {
        String query = "juntarConversa(?1, ?2)";
        StoredProcedureQuery procedureQuery = em.createStoredProcedureQuery(query);
        procedureQuery.setParameter(1, idJogador);
        procedureQuery.setParameter(2, idConversa);
        procedureQuery.executeUpdate();
    }

    // Exercise 2k
    public void enviarMensagem(Integer idJogador, Integer idConversa, String content) {
        String query = "enviarMensagem(?1, ?2)";
        StoredProcedureQuery procedureQuery = em.createStoredProcedureQuery(query);
        procedureQuery.setParameter(1, idJogador);
        procedureQuery.setParameter(2, idConversa);
        procedureQuery.setParameter(3, content);
        procedureQuery.executeUpdate();
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
