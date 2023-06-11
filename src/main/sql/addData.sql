
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
        (4, Jogo1, 3, 100),
        (5, Jogo2, 2, 100);

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
