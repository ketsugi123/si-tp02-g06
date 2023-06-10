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
		ITest[] tests = new ITest[] {
				() -> {
					try { System.out.println( srv.createPlayer("player1", "player1@email.com", "EU") ); }
					catch(Exception e) { System.out.println(e); }},
				() -> {
					try { System.out.println(srv.setPlayerState(1, "Banido")); }
					catch(Exception e) {System.out.println(e);}},
				() -> {
					try { System.out.println(srv.totalPontosJogador(1)); }
					catch(Exception e) {System.out.println(e);}
					},
				() -> {
					try { System.out.println(srv.totalJogosJogador(1)); }
					catch(Exception e) {System.out.println(e);}
				},
				() -> {
					try { System.out.println(srv.PontosJogosPorJogador("U5qi4ZyKRX")); }
					catch (Exception e){ System.out.println(e);}
				},
				() -> {
					try { srv.associarCracha(2, "xqhfGVolbO", "Master");}
					catch (Exception e){ System.out.println(e); }
				},
				() -> {
					try { srv.iniciarConversa(1, "newConvo");}
					catch (Exception e){ System.out.println(e); }
				},
				() -> {
					try { srv.juntarConversa(2, 2);}
					catch (Exception e){ System.out.println(e); }
				},
				() -> {
					try { srv.enviarMensagem(2, 1, "Hello");}
					catch (Exception e){ System.out.println(e); }
				},
				() -> {
					try { srv.jogadorTotalInfo(); }
					catch (Exception e) { System.out.println(e); }
				}
		  };

		while(true){
			Scanner imp = new Scanner(System.in);
			System.out.printf("Choose a test (1-%d)? -1 to break ",tests.length);
			int option = imp.nextInt();
			if (option >= 1 && option <= tests.length)
				tests[--option].test();
			if(option == -1) break;
		}


   }
}
