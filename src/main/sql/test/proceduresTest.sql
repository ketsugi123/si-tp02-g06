do language plpgsql $$ 
DECLARE idJogador integer;
        idJogo VARCHAR(10);
        nomeCracha text;
        result text;
        n Integer;
		cpt text;
begin 
    raise notice '-----------------------------';
    raise notice 'associarCracha test'; 
    rollback;
    set transaction isolation level repeatable read;
    begin
		SELECT id into idJogo from jogo where nome = 'Valorant';
		perform createPlayer('dummyperson', 'dummyperson@email.com', 'EU');
		select id into idJogador from jogador where username = 'dummyperson';
		insert into compra values (idJogador, idJogo, '04-05-2023', 10);
		insert into partida_multijogador values (4, idJogo, idJogador, 100);
        n = 1;
        nomeCracha = 'Pro player';
        call associarCracha(idJogador, idJogo, nomeCracha);
        assert exists (
                select * from cracha_jogador 
                where jogador = idJogador and cracha = nomeCracha and jogo = idJogo
            ), 'Teste associarCracha(%) correu bem = FAIL', n;
        raise notice 'Teste associarCracha(%) correu bem = OK', n;
		rollback;
    end;
end;
$$;


do language plpgsql $$ 
DECLARE idJogador integer;
        result integer;
        n Integer;
        code char(5) default '00000';
	    msg text;
begin 
    raise notice '-----------------------------';
    raise notice 'iniciarConversa test'; 
    rollback;
    set transaction isolation level repeatable read;
    begin
        n = 1;
        idJogador = 1;
        call iniciarConversa(idJogador, 'dummy conversa', result);
		assert exists (SELECT * FROM jogador_conversa where jogador = idJogador AND idConversa = result),
			'Teste iniciarConversa(%) correu bem FAIL', n;
		raise notice 'Teste iniciarConversa(%) correu bem OK', n;
        n = 2;
        idJogador = 9999999;
        call iniciarConversa(idJogador, 'dummy conversa', result);
        assert result is NULL, 'Teste iniciarConversa(%) com jogador inexistente FAIL', n;
        raise notice 'Teste iniciarConversa(%) com jogador inexistente OK', n;
        exception
        when others then
                    GET stacked DIAGNOSTICS 
                                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
                    raise notice 'code=%, msg=%',code,msg;
                    raise notice 'teste de iniciarConversa(%) EXCEPTION',n;
        ROLLBACK;
    end;
end;
$$;


do language plpgsql $$ 
DECLARE idJogador integer;
        result integer;
        n Integer;
        code char(5) default '00000';
	    msg text;
begin 
    raise notice '-----------------------------';
    raise notice 'iniciarConversa test'; 
    rollback;
    set transaction isolation level repeatable read;
    begin
        n = 1;
        idJogador = 1;
        call iniciarConversa(idJogador, 'dummy conversa', result);
        assert exists (SELECT * FROM jogador_conversa where jogador = idJogador AND conversa = result),
			'Teste iniciarConversa(%) correu bem FAIL', n;
        raise notice 'Teste iniciarConversa(%) correu bem OK', n;
        DELETE FROM conversa WHERE id = result;
        n = 2;
        idJogador = 9999999;
        call iniciarConversa(idJogador, 'dummy conversa', result);
        assert result is NULL, 'Teste iniciarConversa(%) com jogador inexistente FAIL', n;
        raise notice 'Teste iniciarConversa(%) com jogador inexistente OK', n;
        ROLLBACK;
    end;
end;
$$;

do language plpgsql $$ 
DECLARE idJogador integer;
        result integer;
        n Integer;
        idConversa integer;
begin 
    raise notice '-----------------------------';
    raise notice 'juntarConversa test'; 
    rollback;
    set transaction isolation level repeatable read;
    begin
        n = 1;
        idJogador = 1;
        idConversa = 1;
        call juntarConversa(idJogador, idConversa);
        assert exists (SELECT * FROM jogador_conversa where jogador = idJogador AND conversa = idConversa),
			'Teste juntarConversa(%) correu bem FAIL', n;
        raise notice 'Teste juntarConversa(%) correu bem OK', n;
        ROLLBACK;
    end;
end;
$$;

do language plpgsql $$ 
DECLARE idJogador integer;
        msg text;
        idConversa integer;
        n Integer;
       
begin 
    raise notice '-----------------------------';
    raise notice 'enviarMensagem test'; 
    rollback;
    set transaction isolation level repeatable read;
    begin
        n = 1;
        idJogador = 2;
        idConversa = 1;
        msg = 'Ola a todos!';
        call enviarMensagem(idJogador, idConversa, msg);
        assert exists (SELECT * FROM mensagem where jogador = idJogador AND conversa = idConversa and conteúdo = msg),
        	'Teste juntarConversa(%) correu bem FAIL', n;
		raise notice 'Teste juntarConversa(%) correu bem OK', n;
        n = 2;
        call enviarMensagem(idJogador, idConversa, '');
        assert NOT exists (SELECT * FROM mensagem where jogador = idJogador AND conversa = idConversa and conteúdo = ''),
			'Teste juntarConversa(%) mensagem vazia FAIL', n;
        raise notice 'Teste juntarConversa(%) mensagem vazia OK', n;
        ROLLBACK;
    end;
end;
$$;
