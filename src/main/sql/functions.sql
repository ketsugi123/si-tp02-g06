create or replace function totalPontosJogador(idJogador int)
returns table(totalPontos bigint)
as
$$
begin
	if not exists (select * from jogador where id = idJogador) THEN
		raise EXCEPTION 'Jogador nao existe';
	end if;
	return query select (
		(select COALESCE(sum(pontuaçao), 0) from partida_normal
		where jogador = idJogador) +
		(select COALESCE(sum(pontuaçao), 0) from partida_multijogador
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
returns table(jogadores int, pontuaçaoTotal numeric)
LANGUAGE plpgsql
as
$$
begin
	if not exists (select * from jogo where id = idJogo) THEN
		raise EXCEPTION 'Jogo nao existe';
	end if;
	return query (select partidas.jogador as jogadores, sum(total) as pontuacaoTotal from (
			select jogador, coalesce(sum(pontuaçao), 0) as total from partida_normal 
			where partida in (select id from partida where jogo = idJogo )
			group by jogador
			union select jogador, coalesce(sum(pontuaçao), 0) as total from partida_multijogador
			where partida in (select id from partida where jogo = idJogo )
			group by jogador) as partidas
		group by jogador);
end;
$$;
