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

import java.util.List;
import jakarta.persistence.*;

import model.*;

/**
 * Hello world!
 *
 */
public class BLService 
{
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("si-tp02-g06");
    EntityManager em = emf.createEntityManager();
    //@SuppressWarnings("unchecked")
	public void test1() throws Exception
    { }

    // These 2 functions constitute the exercise 2d, these do not require a transaction level above
    // read uncommitted since they only preform an update to the respective tables once
    public String createPlayer(String username, String email, String regiao) {
        String query = "SELECT createPlayer(:username, :email, :regiao)";
        Query functionQuery = em.createQuery(query);
        functionQuery.setParameter("username", username);
        functionQuery.setParameter("email", email);
        functionQuery.setParameter("regiao", regiao);
        return (String) functionQuery.getSingleResult();
    }

    public String setPlayerState(int idJogador, String newState) {
        String query = "SELECT setPlayerState(:idJogador, :newState)";
        Query functionQuery = em.createQuery(query);
        functionQuery.setParameter("idJogador", idJogador);
        functionQuery.setParameter("newState", newState);
        return (String) functionQuery.getSingleResult();
    }

    // Exercise 2e
}
