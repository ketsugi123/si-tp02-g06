--Notes:
--Testes estao implementados a assumir que foi executado o script 'addData' com a base de dados inicialmente vazia

do $testes_functions$
begin

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
                    raise notice 'teste de totalPontosJogadores(%) FAIL',n;
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
                    raise notice 'teste de totalJogosJogadores(%) FAIL',n;
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
                    raise notice 'teste de PontosJogosPorJogador(%) EXCEPTION',n;
        end;
    end;
$$;

end;
$testes_functions$;