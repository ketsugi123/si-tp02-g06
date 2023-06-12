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
