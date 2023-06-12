package businessLogic.BLserviceUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import model.embeddables.CrachaId;
import model.embeddables.CrachasAdquiridosId;
import model.relations.Compra;
import model.tables.Jogador;
import model.tables.Jogo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class ModelManager {
    public ModelManager(EntityManager em){
        this.em = em;

    }
    private final EntityManager em;


    public Jogo getGameByName(String gameName, EntityManager em){
        TypedQuery<Jogo> query = em.createQuery("SELECT j FROM Jogo j WHERE j.nome = ?1", Jogo.class);
        query.setParameter(1, gameName);
        return query.getSingleResult();
    }

    public Jogador getPlayerByEmail(String email, EntityManager em){
        TypedQuery<Jogador> query = em.createQuery("SELECT j FROM Jogador j WHERE j.email = ?1", Jogador.class);
        query.setParameter(1, email);
        return query.getSingleResult();
    }

    public CrachasAdquiridosId setCrachaAdquiridoId(String idJogo, String nomeCracha, int idJogador){
        CrachasAdquiridosId crachasAdquiridosId = new CrachasAdquiridosId();
        crachasAdquiridosId.setJogo(idJogo);
        crachasAdquiridosId.setCracha(nomeCracha);
        crachasAdquiridosId.setJogador(idJogador);
        return  crachasAdquiridosId;
    }

    public CrachaId setCrachaId(String idJogo, String nomeCracha){
        CrachaId crachaId = new CrachaId();
        crachaId.setJogo(idJogo);
        crachaId.setNome(nomeCracha);
        return crachaId;
    }
    
    public BigDecimal getPlayerPoints(String idJogo, int idJogador){
        String jogadoresQuery = "SELECT pontuacaoTotal from PontosJogosPorJogador(?1) WHERE jogadores = ?2";
        Query pontuacaoQuery = em.createNativeQuery(jogadoresQuery);
        pontuacaoQuery.setParameter(1, idJogo);
        pontuacaoQuery.setParameter(2, idJogador);
        return (BigDecimal) pontuacaoQuery.getSingleResult();
    }
    
    public Boolean ownsGame(int idJogador, String idJogo){
        TypedQuery<Compra> getCompra = em.createQuery(
                "select c from Compra c "  +
                        "where c.jogador.id = :idJogador and c.jogo.id = :idJogo",
                Compra.class
        );
        getCompra.setParameter("idJogador", idJogador);
        getCompra.setParameter("idJogo", idJogo);
        return getCompra.getResultList().isEmpty();
    }

    public Boolean ownsBadge(int idJogador){
        Query hasCracha = em.createQuery("SELECT ca.id.jogo from CrachasAdquiridos ca WHERE ca.id.jogador = ?1");
        hasCracha.setParameter(1, idJogador);
        return !hasCracha.getResultList().isEmpty();
    }
}
