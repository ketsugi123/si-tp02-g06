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