package businessLogic.BLserviceUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.embeddables.CrachaId;
import model.embeddables.CrachasAdquiridosId;
import model.tables.Jogador;
import model.tables.Jogo;

import java.sql.Connection;
import java.sql.SQLException;

public class ModelManager {
    public ModelManager(EntityManager em){
        this.em = em;

    }
    private final EntityManager em;

    public EntityTransaction startTransaction(){
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        return  transaction;
    }

    public void setIsolationLevel(Connection cn, Integer isolationLevel, EntityTransaction transaction) throws SQLException {
        transaction.rollback();
        cn.setTransactionIsolation(isolationLevel);
        transaction.begin();
    }
    //@SuppressWarnings("unchecked")

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
}
