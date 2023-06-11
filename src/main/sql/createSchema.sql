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

