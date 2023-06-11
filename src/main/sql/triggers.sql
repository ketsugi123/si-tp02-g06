CREATE OR REPLACE FUNCTION atribuirCracha() RETURNS TRIGGER AS
$$
DECLARE
	score record;
BEGIN
FOR score IN
    SELECT * FROM (SELECT partida_normal.partida, partida_normal.jogo, partida_normal.jogador, partida_normal.pontuacao
	FROM partida_normal WHERE NEW.id = partida_normal.partida AND NEW.jogo = partida_normal.jogo
	UNION
	SELECT partida_multijogador.partida, partida_multijogador.jogo, partida_multijogador.jogador, partida_multijogador.pontuacao
	FROM partida_multijogador WHERE NEW.id = partida_multijogador.partida AND NEW.jogo = partida_multijogador.jogo) as totalPartidas
	INNER JOIN cracha ON cracha.jogo = totalPartidas.jogo
LOOP
	IF score.pontuacao >= score.limite THEN
		IF score.jogador NOT IN (SELECT jogador FROM cracha_jogador) THEN
			insert into cracha_jogador values (score.nome, score.jogo, score.jogador);
		END IF;
	END IF;
END LOOP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER atribuirCrachas
AFTER UPDATE OF estado ON partida
FOR EACH ROW
WHEN (NEW.estado = 'Terminada')
EXECUTE FUNCTION atribuirCracha();

CREATE OR REPLACE FUNCTION banirJogador() RETURNS TRIGGER AS
$$
BEGIN
UPDATE jogador SET estado = 'Banido' WHERE jogador.id = OLD.id;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER banirJogadores
INSTEAD OF DELETE ON jogadorTotalInfo
FOR EACH ROW
EXECUTE FUNCTION banirJogador();