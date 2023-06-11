DROP VIEW IF EXISTS jogadortotalinfo;
DROP TABLE IF EXISTS jogador_conversa;
DROP TABLE IF EXISTS mensagem;
DROP TABLE IF EXISTS conversa;
DROP TABLE IF EXISTS cracha_jogador;
DROP TABLE IF EXISTS cracha;
DROP TABLE IF EXISTS partida_multijogador;
DROP TABLE IF EXISTS partida_normal;
DROP TABLE IF EXISTS partida;
DROP TABLE IF EXISTS compra;
DROP TABLE IF EXISTS jogo;
DROP TABLE IF EXISTS jogador;


CREATE OR REPLACE FUNCTION generate_uid()
RETURNS VARCHAR(10) AS $$
DECLARE
  chars VARCHAR(62) := 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
  result VARCHAR(10);
BEGIN
  SELECT string_agg(substr(chars, (random() * length(chars) + 1)::integer, 1), '')
  FROM generate_series(1, 10)
  INTO result;

  RETURN result;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE IF NOT EXISTS jogador(
    id SERIAL PRIMARY KEY,
    email VARCHAR(80) NOT NULL,
    username VARCHAR(20) NOT NULL,
    estado VARCHAR(7) NOT NULL CHECK (estado = 'Ativo' OR estado = 'Inativo' OR estado = 'Banido'),
    regiao VARCHAR(5)
);
CREATE TABLE IF NOT EXISTS jogo(
    id VARCHAR(10) PRIMARY KEY DEFAULT generate_uid(),
    nome VARCHAR(20) UNIQUE NOT NULL,
    url VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS compra(
    jogador INTEGER REFERENCES jogador(id) ON DELETE CASCADE,
    jogo VARCHAR(10) REFERENCES jogo(id) ON DELETE CASCADE,
    data DATE NOT NULL,
    preco NUMERIC NOT NULL CHECK (preco > 0),
	PRIMARY KEY (jogador, jogo)
);

CREATE TABLE IF NOT EXISTS partida(
    id SERIAL NOT NULL,
    jogo VARCHAR(10) REFERENCES jogo(id) ON DELETE CASCADE,
    dtinicio TIMESTAMP NOT NULL,
    dtfim TIMESTAMP,
    estado VARCHAR(20) NOT NULL CHECK (estado = 'Por iniciar' OR estado = 'A aguardar jogadores' OR estado = 'Em curso' or estado = 'Terminada'),
    regiao VARCHAR(10),
    CONSTRAINT chk_date CHECK (dtinicio < dtfim),
    PRIMARY KEY (id, jogo)
);

CREATE TABLE IF NOT EXISTS partida_normal(
    partida INTEGER NOT NULL,
    jogo VARCHAR(10) NOT NULL,
    jogador INTEGER REFERENCES jogador(id) ON DELETE CASCADE,
    grau INTEGER NOT NULL CHECK (grau BETWEEN 1 AND 5), 
    pontuacao INTEGER,
    PRIMARY KEY (partida, jogo, jogador),
	FOREIGN KEY (partida, jogo) REFERENCES partida(id, jogo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS partida_multijogador(
    partida INTEGER NOT NULL,
	jogo VARCHAR(10) NOT NULL,
    jogador INTEGER REFERENCES jogador(id) ON DELETE CASCADE,
    pontuacao INTEGER, 
    PRIMARY KEY (partida, jogo, jogador),
	FOREIGN KEY (partida, jogo) REFERENCES partida(id, jogo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS cracha(
    nome VARCHAR(20) NOT NULL,
    jogo VARCHAR(10) REFERENCES jogo(id) ON DELETE CASCADE, 
    limite INTEGER NOT NULL,
    url VARCHAR(500) NOT NULL,
    PRIMARY KEY (nome, jogo)
);

CREATE TABLE IF NOT EXISTS cracha_jogador(
    cracha VARCHAR(20) NOT NULL,
    jogo VARCHAR(10) NOT NULL, 
    jogador INTEGER REFERENCES jogador(id) ON DELETE CASCADE,
    PRIMARY KEY(cracha, jogo, jogador),
	FOREIGN KEY (cracha, jogo) REFERENCES cracha(nome, jogo) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS conversa(
    id SERIAL PRIMARY KEY, 
    nome VARCHAR(50) NOT NULL 
);

CREATE TABLE IF NOT EXISTS mensagem(
    id SERIAL NOT NULL,
    conversa INTEGER REFERENCES conversa(id)ON DELETE CASCADE,
    data TIMESTAMP NOT NULL, 
    conteúdo VARCHAR(200) NOT NULL,
    jogador INTEGER REFERENCES jogador(id) ON DELETE CASCADE,
    PRIMARY KEY (id, conversa)
);

CREATE TABLE IF NOT EXISTS jogador_conversa(
    jogador INTEGER REFERENCES jogador(id) ON DELETE CASCADE,
    conversa INTEGER REFERENCES conversa(id)ON DELETE CASCADE,
    PRIMARY KEY(jogador, conversa)
);



do language plpgsql $$
    DECLARE Jogo1 VARCHAR(10);
    DECLARE Jogo2 VARCHAR(10);
begin
    INSERT INTO jogador(email, username, estado, regiao)
    VALUES
        ('jogador1@email.com', 'player1', 'Ativo', 'EU'),
        ('jogador2@email.com', 'player2', 'Inativo', 'EU'),
        ('jogador3@email.com', 'player3', 'Ativo', 'EUA');

    INSERT INTO jogo(nome, url)
    VALUES
        ('Valorant', 'https://playvalorant.com/'),
        ('Genshin Impact', 'https://genshin.hoyoverse.com/pt/');

    SELECT id from jogo where nome = 'Valorant' into Jogo1;
    SELECT id from jogo where nome = 'Genshin Impact' into Jogo2;

    INSERT INTO compra(jogador, jogo, data, preco)
    VALUES
        (1, Jogo1 , '04-05-2023', 10),
        (2, Jogo2, '04-05-2023', 10);

   

    INSERT INTO partida(jogo, dtinicio, dtfim, estado, regiao)
    VALUES
        (Jogo1, '04-05-2023', '05-05-2023','Em curso', 'EU'),
        (Jogo2, '04-05-2023', '05-05-2023','Terminada', 'EU'),
        (Jogo2, '04-05-2023', '05-05-2023','Em curso', 'EU'),
        (Jogo1, '04-05-2023', '05-05-2023','Terminada', 'EU');


    INSERT INTO partida_normal(partida, jogo, jogador, grau, pontuacao)
    VALUES
        (1, Jogo1, 1, 4, 100),
        (2, Jogo2, 2, 3, 100),
        (3, Jogo2, 2, 3, 100);


    INSERT INTO partida_multijogador(partida, jogo, jogador, pontuacao)
    VALUES
        (4, Jogo1, 1, 200),
        (4, Jogo1, 3, 100);

    INSERT INTO cracha(nome, jogo, limite, url)
    VALUES
        ('Pro player', Jogo1, 10, 'https://www.google.pt/?gws_rd=ssl'),
        ('Master', Jogo2, 10, 'https://www.google.pt/?gws_rd=ssl');

    INSERT INTO cracha_jogador(cracha, jogador, jogo)
    VALUES
        ('Master', 2, Jogo2);

    INSERT INTO conversa(nome)
    VALUES
        ('Genshin Chat');

    INSERT INTO mensagem(data, conteúdo, conversa, jogador)
    VALUES
        ('04-05-2023', 'Ola', 1, 2);

    INSERT INTO jogador_conversa(jogador, conversa)
    VALUES
        (2, 1);
end;
$$;

create or replace function createPlayer(username VARCHAR(20), email VARCHAR(80), regiao VARCHAR(5))
RETURNS VARCHAR(30) 
LANGUAGE plpgsql
as 
$$ 
begin 
    IF email NOT LIKE '%@%' THEN
        RETURN 'Missing @ in email'; 
    END IF;
    INSERT INTO JOGADOR(email, username, estado, regiao)
    VALUES(email, username, 'Ativo', regiao);
    RETURN 'Player created successfully';
end;
$$;

CREATE OR REPLACE FUNCTION setPlayerState(idJogador int, newState VARCHAR(7))
RETURNS VARCHAR(20)
LANGUAGE plpgsql
AS
$$
DECLARE 
    row_count INTEGER;
BEGIN
     UPDATE jogador 
        SET estado = newState 
        WHERE id = idJogador;
     GET DIAGNOSTICS  row_count = ROW_COUNT;
     IF row_count > 0 THEN 
        RETURN 'Player state set';
     END IF;
     RETURN 'Something went wrong'; 
END;   
$$;

create or replace function totalPontosJogador(idJogador int)
returns table(totalPontos bigint)
as
$$
begin
	if not exists (select * from jogador where id = idJogador) THEN
		raise EXCEPTION 'Jogador nao existe';
	end if;
	return query select (
		(select COALESCE(sum(pontuacao), 0) from partida_normal
		where jogador = idJogador) +
		(select COALESCE(sum(pontuacao), 0) from partida_multijogador
		where jogador = idJogador)
	) as totalPontos;
end;
$$ LANGUAGE plpgsql;

create or replace function totalJogosJogador(idJogador int)
returns table(totalJogos bigint)
LANGUAGE plpgsql
as
$$
begin
	if not exists (select * from jogador where id = idJogador) THEN
		raise EXCEPTION 'Jogador nao existe';
	end if;
    return query (select (
		(select COALESCE(count(DISTINCT jogo), 0) from partida_normal
		where jogador = idJogador) +
		(select COALESCE(count(DISTINCT jogo), 0) from partida_multijogador
		where jogador = idJogador)
	) as totalJogos);
end;
$$;

create or replace function PontosJogosPorJogador(idJogo varchar(10))
returns table(jogadores int, pontuacaoTotal numeric)
LANGUAGE plpgsql
as
$$
begin
	if not exists (select * from jogo where id = idJogo) THEN
		raise EXCEPTION 'Jogo nao existe';
	end if;
	return query (select partidas.jogador as jogadores, sum(total) as pontuacaoTotal from (
			select jogador, coalesce(sum(pontuacao), 0) as total from partida_normal 
			where partida in (select id from partida where jogo = idJogo )
			group by jogador
			union select jogador, coalesce(sum(pontuacao), 0) as total from partida_multijogador
			where partida in (select id from partida where jogo = idJogo )
			group by jogador) as partidas
		group by jogador);
end;
$$;

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
        raise notice 'begin';
    --verificar se o id do jogo no parametro corresponde com o jogo do cracha
    if exists (select jogo from cracha where jogo = idJogo and nome = nomeCracha) then
            raise notice 'cracha existe';
            --selecionar o limite que pertence ao cracha em questao
            select limite into limitePontos from cracha where nome = nomeCracha;
            raise notice 'esta dentro do limite';
            --verificar se o jogador tem o jogo
            if exists (select jogador from compra where jogo = idJogo and jogador = idJogador) then
                select pontuacaoTotal into pontos from PontosJogosPorJogador(idJogo)
                where jogadores = idJogador;
                    raise notice '%', pontos;
                raise notice 'Jogador existe';
                -- verificar se o jogador atingiu o limite necessario de pontos
                if pontos >= limitePontos then
                    raise notice 'pontos certos';
                    --verificar se o jogador ja tem o cracha ou nao
                    if idJogador not in (select jogador from cracha_jogador) then
                        insert into cracha_jogador values (nomeCracha, idJogo, idJogador);
                        raise notice 'inseriu cracha';
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


CREATE OR REPLACE VIEW jogadorTotalInfo(id, estado, email, username, jogosParticipados, partidasParticipadas, pontuacaoTotal) AS
    SELECT jogador.id AS id,
           jogador.estado AS estado,
           jogador.email AS email,
           jogador.username AS username,
           statsPartidas.numJogos AS jogosParticipados,
           statsPartidas.numPartidas AS partidasParticipadas,
           statsPartidas.pontos AS pontuacaoTotal
    FROM jogador INNER JOIN (
        SELECT totalPartidas.jogador,
               COUNT(DISTINCT partida.jogo) AS numJogos,
               COUNT(partida.id) as numPartidas,
               SUM(totalPartidas.pontuacao) as pontos
        FROM partida INNER JOIN (
            SELECT partida_normal.partida, partida_normal.jogador, partida_normal.pontuacao
            FROM partida_normal
            UNION
            SELECT partida_multijogador.partida, partida_multijogador.jogador, partida_multijogador.pontuacao
            FROM partida_multijogador) totalPartidas
		ON partida.id = totalPartidas.partida
        GROUP BY jogador) statsPartidas
	ON jogador.id = statsPartidas.jogador
    WHERE jogador.estado <> 'Banido';

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

