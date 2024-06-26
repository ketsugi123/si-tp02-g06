package businessLogic.BLserviceUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import model.embeddables.CrachaId;
import model.embeddables.CrachasAdquiridosId;
import model.relations.Compra;
import model.tables.Jogador;
import model.tables.Jogo;
import model.views.JogadorTotalInfo;

import java.math.BigDecimal;
import java.util.List;

public class ModelManager {
    public ModelManager(EntityManager em) {
        this.em = em;

    }

    private final EntityManager em;

    public Jogo getGameByName(String gameName) {
        TypedQuery<Jogo> query = em.createQuery("SELECT j FROM Jogo j WHERE j.nome = ?1", Jogo.class);
        query.setParameter(1, gameName);
        return query.getSingleResult();
    }

    public Jogador getPlayerByEmail(String email) {
        TypedQuery<Jogador> query = em.createQuery("SELECT j FROM Jogador j WHERE j.email = ?1", Jogador.class);
        query.setParameter(1, email);
        return query.getSingleResult();
    }

    public CrachasAdquiridosId setCrachaAdquiridoId(String idJogo, String nomeCracha, int idJogador) {
        CrachasAdquiridosId crachasAdquiridosId = new CrachasAdquiridosId();
        crachasAdquiridosId.setJogo(idJogo);
        crachasAdquiridosId.setCracha(nomeCracha);
        crachasAdquiridosId.setJogador(idJogador);
        return crachasAdquiridosId;
    }

    public CrachaId setCrachaId(String idJogo, String nomeCracha) {
        CrachaId crachaId = new CrachaId();
        crachaId.setJogo(idJogo);
        crachaId.setNome(nomeCracha);
        return crachaId;
    }

    public BigDecimal getPlayerPoints(String idJogo, int idJogador) {
        String jogadoresQuery = "SELECT pontuacaoTotal from PontosJogosPorJogador(?1) WHERE jogadores = ?2";
        Query pontuacaoQuery = em.createNativeQuery(jogadoresQuery);
        pontuacaoQuery.setParameter(1, idJogo);
        pontuacaoQuery.setParameter(2, idJogador);
        return (BigDecimal) pontuacaoQuery.getSingleResult();
    }

    public List<JogadorTotalInfo> getPlayertotalInfo() {
        String query = "Select * from jogadorTotalInfo";
        Query getAllInfo = em.createNativeQuery(query, JogadorTotalInfo.class);
        return getAllInfo.getResultList();
    }

    public Boolean ownsGame(int idJogador, String idJogo) {
        TypedQuery<Compra> getCompra = em.createQuery(
                "select c from Compra c " +
                        "where c.jogador.id = :idJogador and c.jogo.id = :idJogo",
                Compra.class
        );
        getCompra.setParameter("idJogador", idJogador);
        getCompra.setParameter("idJogo", idJogo);
        return !getCompra.getResultList().isEmpty();
    }

    public Boolean ownsBadge(int idJogador, String idJogo) {
        Query hasCracha = em.createQuery("SELECT ca.id.jogo from CrachasAdquiridos ca WHERE ca.id.jogador = ?1 and ca.id.jogo = ?2");
        hasCracha.setParameter(1, idJogador);
        hasCracha.setParameter(2, idJogo);
        return !hasCracha.getResultList().isEmpty();
    }
}
