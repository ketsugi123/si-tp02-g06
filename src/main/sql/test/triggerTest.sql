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