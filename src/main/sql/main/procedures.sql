--associarCracha
create or replace procedure associarCracha(idJogador int, idJogo VARCHAR(10), nomeCracha varchar(20)) 
LANGUAGE plpgsql
as
$$
declare pontos bigint;
		limitePontos int;
begin
	ROLLBACK;
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
	begin
   --verificar se o id do jogo no parametro corresponde com o jogo do cracha
   if exists (select jogo from cracha where jogo = idJogo and nome = nomeCracha) then
		--selecionar o limite que pertence ao cracha em questao
		select limite into limitePontos from cracha where nome = nomeCracha;
		--verificar se o jogador tem o jogo
		if exists (select jogador from compra where jogo = idJogo and jogador = idJogador) then
			select pontuacaoTotal into pontos from PontosJogosPorJogador(idJogo)
			where jogadores = idJogador;
			-- verificar se o jogador atingiu o limite necessario de pontos
			if pontos >= limitePontos then
				--verificar se o jogador ja tem o cracha ou nao
				if idJogador not in (select jogador from cracha_jogador) then
					insert into cracha_jogador values (nomeCracha, idJogo, idJogador);
					return; -- em caso de sucesso
				end if;
			end if;
		end if;
	end if;
	raise exception 'Erro na validacao de dados'; -- em caso de falha
	end;
end;
$$;

CREATE OR REPLACE PROCEDURE iniciarConversa(idJogador int, nomeConversa  VARCHAR(50), idConversa OUT Int)
LANGUAGE plpgsql
as
$$
BEGIN
	ROLLBACK;
	SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
	BEGIN
		IF NOT EXISTS (SELECT id FROM JOGADOR WHERE id = idJogador) THEN
			RETURN;
		END IF;
		INSERT INTO conversa(nome) VALUES(nomeConversa) RETURNING id into idConversa;
		INSERT INTO jogador_conversa(jogador, conversa) VALUES(idJogador, idConversa);
	END;
END;
$$;


CREATE OR REPLACE PROCEDURE juntarConversa(idJogador int, idConversa int)
LANGUAGE plpgsql
as
$$
BEGIN
	INSERT INTO jogador_conversa(jogador, conversa) VALUES(idJogador, idConversa);
END;
$$;

CREATE OR REPLACE PROCEDURE enviarMensagem(idJogador int, idConversa int, content VARCHAR(200))
LANGUAGE plpgsql
as	
$$
BEGIN
	ROLLBACK;
	SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
	IF length(content) = 0  OR idJogador NOT IN 
		(SELECT jogador FROM jogador_conversa WHERE conversa = idConversa)
			THEN RETURN;
	END IF;
	INSERT INTO mensagem(data, conteúdo, conversa, jogador) 
		VALUES(NOW(), content, idConversa, idJogador);
END;
$$;

