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