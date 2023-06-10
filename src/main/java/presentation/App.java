/*
 Walter Vieira (2022-04-22)
 Sistemas de Informação - projeto JPAAulas_ex3
 Código desenvolvido para iulustração dos conceitos sobre acesso a dados, concretizados com base na especificação JPA.
 Todos os exemplos foram desenvolvidos com EclipseLinlk (3.1.0-M1), usando o ambientre Eclipse IDE versão 2022-03 (4.23.0).
 
Não existe a pretensão de que o código estaja completo.

Embora tenha sido colocado um esforço significativo na correção do código, não há garantias de que ele não contenha erros que possam 
acarretar problemas vários, em particular, no que respeita à consistência dos dados.  
 
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
