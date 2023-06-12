/*
 Walter Vieira (2022-04-22)
 Sistemas de Informa��o - projeto JPAAulas_ex3
 C�digo desenvolvido para iulustra��o dos conceitos sobre acesso a dados, concretizados com base na especifica��o JPA.
 Todos os exemplos foram desenvolvidos com EclipseLinlk (3.1.0-M1), usando o ambientre Eclipse IDE vers�o 2022-03 (4.23.0).
 
N�o existe a pretens�o de que o c�digo estaja completo.

Embora tenha sido colocado um esfor�o significativo na corre��o do c�digo, n�o h� garantias de que ele n�o contenha erros que possam 
acarretar problemas v�rios, em particular, no que respeita � consist�ncia dos dados.  
 
*/

package presentation;

import java.util.Scanner;


import businessLogic.*;


/**
 * Hello world!
 *
 */

public class App 
{
	protected interface ITest {
		void test();
	}

	public static void main( String[] args ) {
		BLService srv = new BLService();
		Scanner imp = new Scanner(System.in);
		ITest[] tests = new ITest[] {
				() -> {
					System.out.println("\n\n\n ------------------- CREATE PLAYER --------------------");
					System.out.println("Introduza um username");
					String playerName = imp.nextLine();
					System.out.println("Introduza um email");
					String email = imp.nextLine();
					try { System.out.println( srv.createPlayer(playerName, email, "EU") ); }
					catch(Exception e) { System.out.println(e); }},
				() -> {
					System.out.println("\n\n\n ------------------- BAN PLAYER --------------------");
					System.out.println("Introduza um email de um jogador que queira banir");
					String email = imp.nextLine();
					try { System.out.println(srv.setPlayerState(email, "Banido")); }
					catch(Exception e) {System.out.println(e);}},
				() -> {
					System.out.println("\n\n\n ------------------- PONTOS POR JOGADOR --------------------");
					System.out.println("Introduza um email de um jogador que queira saber o n�mero total de pontos");
					String email = imp.nextLine();
					try { System.out.println(srv.totalPontosJogador(email)); }
					catch(Exception e) {System.out.println(e);}
					},
				() -> {
					System.out.println("\n\n\n ------------------- TOTAL JOGOS JOGADOR --------------------");
					System.out.println("Introduza um email de um jogador que queira saber o n�mero total de jogos");
					String email = imp.nextLine();
					try { System.out.println(srv.totalJogosJogador(email)); }
					catch(Exception e) {System.out.println(e);}
				},
				() -> {
					System.out.println("\n\n\n ------------------- PONTOS JOGOS POR JOGADOR --------------------");
					try { System.out.println(srv.PontosJogosPorJogador("Valorant")); }
					catch (Exception e){ System.out.println(e);}
				},
				() -> {
					System.out.println("\n\n\n ------------------- ASSOCIAR CRACHA --------------------");
					try { srv.associarCracha(2, "Genshin Impact", "Master");}
					catch (Exception e){ System.out.println(e); }
				},
				() -> {
					System.out.println("\n\n\n ------------------- INICIAR CONVERSA --------------------");
					try { srv.iniciarConversa(1, "newConvo");}
					catch (Exception e){ System.out.println(e); }
				},
				() -> {
					System.out.println("\n\n\n ------------------- JUNTAR CONVERSA --------------------");
					try { srv.juntarConversa(2, 2);}
					catch (Exception e){ System.out.println(e); }
				},
				() -> {
					System.out.println("\n\n\n ------------------- ENVIAR MENSAGEM --------------------");
					try { srv.enviarMensagem(2, 1, "Hello");}
					catch (Exception e){ System.out.println(e); }
				},
				() -> {
					System.out.println("\n\n\n ------------------- JOGADOR TOTAL INFO --------------------");
					try { srv.jogadorTotalInfo(); }
					catch (Exception e) { System.out.println(e); }
				},
				() -> {
					System.out.println("\n\n\n ------------------- ASSOCIAR CRACHA MODEL --------------------");
					try { srv.associarCrachaModel(1, "Valorant", "Pro player");}
					catch (Exception e){ System.out.println(e); }
				},
		  };

		while(true){
			Scanner opt = new Scanner(System.in);
			System.out.printf("Choose a test (1-%d)? -1 to break ",tests.length);
			int option = opt.nextInt();
			if (option >= 1 && option <= tests.length)
				tests[--option].test();
			if(option == -1) break;
		}


   }
}
