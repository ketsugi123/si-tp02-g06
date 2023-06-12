do $testes$
begin

do language plpgsql $$
declare result VARCHAR(30);
        code char(5) default '00000';
	    msg text;
        n integer;
    begin
        raise notice 'createPlayer test'; 
        rollback;
        set transaction isolation level serializable;
        begin
            n = 1;
            result = createPlayer('player2', 'player2@gmail.com', 'EU');
            assert result = 'Player created successfully', 'Teste de createPlayer(%) criar um player corretamente = FAIL', n; 
            raise notice 'teste de createPlayer(%) criar um player corretamente = OK', n;
            exception
		    when others then
		        GET stacked DIAGNOSTICS 
                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
		    raise notice 'code=%, msg=%',code,msg;
		    raise notice 'Teste createPlayer criar um player corretamente = EXCEPTION';
	    ROLLBACK;
        end;
        begin
            n = 2;
            result = createPlayer('player2', 'player2gmail.com', 'EU');
            assert result = 'Missing @ in email', 'Teste de createPlayer(%) sem @ no email = FAIL', n;
            raise notice 'Teste de createPlayer(%) sem @ no email = OK', n;
            exception
            when others then
                    GET stacked DIAGNOSTICS 
                                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
                    raise notice 'code=%, msg=%',code,msg;
                    raise notice 'teste de createPlayer(%) EXCEPTION',n;
        end;
    end;
$$;



do language plpgsql $$
declare result VARCHAR(30);
        code char(5) default '00000';
	    msg text;
        n integer;
        getState text;
        newState text;
        lastState text;
        idJogador integer;
    begin 
        raise notice '-----------------------------';
        raise notice 'setPlayerState test';
		rollback;
        set transaction isolation level serializable;
		begin
            n = 1;
            newState = 'Banido';
            INSERT INTO jogador(email, username, estado, regiao)
            VALUES('dummy@email.com', 'dummy', 'Ativo', 'EU') RETURNING id into idJogador;

            result = setPlayerState(idJogador, newState);
            SELECT estado FROM jogador WHERE id = idJogador INTO getState;
            assert result = 'Player state set' AND getState = newState, 'Teste de setPlayerState(%) em condicões normais =  FAIL', n; 
            raise notice 'Teste de setPlayerState(%) em condicões normais =  OK', n;
            exception
            when others then
                GET stacked DIAGNOSTICS 
                            code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
                raise notice 'code=%, msg=%',code,msg;
                raise notice 'teste de setPlayerState(%) EXCEPTION',n;
            DELETE FROM jogador WHERE id = idJogador;
            ROLLBACK;
        end;

        begin
            n = 2;
            idJogador = 10000;
            newState = 'Banido';
            result = setPlayerState(idJogador, newState);
            SELECT estado FROM jogador WHERE id = idJogador INTO getState;
            assert result = 'Something went wrong' AND getState IS NULL, 'Teste de setPlayerState(%) player inexistente = OK',n;
            raise notice 'Teste de setPlayerState(%) player inexistente = OK',n;
            exception
            when others then
                    GET stacked DIAGNOSTICS 
                                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
                    raise notice 'code=%, msg=%',code,msg;
                    raise notice 'teste de setPlayerState(%) EXCEPTION',n;
            ROLLBACK;
        END;
	end;
$$;


do language plpgsql $$
declare result int;
        code char(5) default '00000';
	    msg text;
        n integer;
    begin
        raise notice '-----------------------------';
        raise notice 'totalPontosJogador test';
        rollback;
        set transaction isolation level serializable;
        begin
            n = 1;
            SELECT totalPontos into result FROM totalPontosJogador(1);
			assert result = 300, 'teste de totalPontosJogadores(%) = % FAIL',n,result;
			raise notice 'teste de totalPontosJogadores(%) = % OK',n,result;
            n = 2; 
            SELECT * FROM totalPontosJogador(99999); --Jogador com este id nao existe
            exception
            when others then
                    GET stacked DIAGNOSTICS 
                                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
                    raise notice 'code=%, msg=%',code,msg;
                    raise notice 'teste de totalPontosJogadores(%) OK',n;
        end;
    end;
$$;

do language plpgsql $$
declare result int;
        code char(5) default '00000';
	    msg text;
        n integer;
    begin
        raise notice '-----------------------------';
        raise notice 'totalJogosJogador test';
        rollback;
        set transaction isolation level serializable;
        begin
            n = 1;
            SELECT totalJogos into result FROM totalJogosJogador(n);
			assert result = 2, 'teste de totalJogosJogadores(%) = % FAIL',n,result;
			raise notice 'teste de totalJogosJogadores(%) = % OK',n,result;
            n = 9999; --Jogador com este id nao existe
            SELECT * FROM totalPontosJogador(n);
            exception
            when others then
                    GET stacked DIAGNOSTICS 
                                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
                    raise notice 'code=%, msg=%',code,msg;
                    raise notice 'teste de totalJogosJogadores(%) OK',n;
        end;
    end;
$$;

do language plpgsql $$
declare result int;
		first_id varchar(10);
        code char(5) default '00000';
	    msg text;
        n integer;
    begin
        raise notice '-----------------------------';
        raise notice 'PontosJogosPorJogador test';
        rollback;
        set transaction isolation level serializable;
        begin
            n = 1;
			select id into first_id from jogo limit 1;
            SELECT count(jogadores) into result FROM PontosJogosPorJogador(first_id);
			--Existem pelo menos dois jogadores com partidas correspondentes ao jogo
			assert result >= 2, 'teste de PontosJogosPorJogador(%) = % FAIL',n,result;
			raise notice 'teste de PontosJogosPorJogador(%) = % OK',n,result;
            n = 2; 
            SELECT * FROM PontosJogosPorJogador('0000000000'); --Jogo com este id nao existe
            exception
            when others then
                    GET stacked DIAGNOSTICS 
                                code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
                    raise notice 'code=%, msg=%',code,msg;
                    raise notice 'teste de PontosJogosPorJogador(%) OK',n;
        end;
    end;
$$;

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
		select id into idJogador from jogador where email = 'jogador1@email.com';
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



do language plpgsql $$
declare n integer;
        code char(5) default '00000';
	    msg text;
        idPartida integer;
        lastState text;
    begin
        raise notice '-----------------------------';
        raise notice 'atribuirCrachas test';
        idPartida = 3;
        SELECT estado FROM partida WHERE id = idPartida into lastState;
        UPDATE partida SET estado = 'Terminada' where id = idPartida;
        n = 1;
        assert EXISTS (SELECT * FROM cracha_jogador WHERE jogador = 2 AND cracha = 'Master'),
        'atribuirCrachas(%) em condicões normais = FAIL', n;
        raise notice 'atribuirCrachas(%) corre bem = OK', n;
        UPDATE partida SET estado = lastState WHERE id = idPartida;
        exception
        when others then
            GET stacked DIAGNOSTICS 
                        code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
            raise notice 'code=%, msg=%',code,msg;
            raise notice 'teste de atribuirCrachas(%) EXCEPTION',n;
        ROLLBACK;
    end;
$$;

do language plpgsql $$
declare n integer;
        code char(5) default '00000';
	    msg text;
        idJogador INTEGER;
        lastState text;
    begin
        raise notice '-----------------------------';
        raise notice 'banirJogadores test';
        idJogador = 2
        DELETE FROM jogadorTotalInfo WHERE id = idJogador;
        SELECT estado FROM jogador WHERE id = idJogador INTO lastState; 
        n = 1;
        assert EXISTS (select * from jogadorTotalInfo where id = 2), 'banirJogadores(%) em condicões normais = FAIL';
        raise notice 'banirJogadores(%) em condicões normais = OK', n;
        exception
        when others then
            GET stacked DIAGNOSTICS 
                        code = RETURNED_SQLSTATE, msg = MESSAGE_TEXT;   
            raise notice 'code=%, msg=%',code,msg;
            raise notice 'teste de banirJogadores(%) EXCEPTION',n;
         
    UPDATE jogador SET estado = lastState WHERE id = idJogador;
    ROLLBACK;
    end;
$$;

end;
$testes$;