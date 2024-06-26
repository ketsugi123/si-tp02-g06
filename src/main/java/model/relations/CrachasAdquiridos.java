package model.relations;

import jakarta.persistence.*;
import model.tables.Cracha;
import model.tables.Jogador;
import model.embeddables.CrachasAdquiridosId;

import java.io.Serializable;

@Entity
@Table(name="cracha_jogador")
public class CrachasAdquiridos implements Serializable {

    @EmbeddedId
    private CrachasAdquiridosId id;


    // N:N realtion CrachasAdquiridos
    @ManyToOne
    @MapsId
    @JoinColumn(name="jogador",  referencedColumnName = "id")
    private Jogador jogador;


    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "nome", referencedColumnName = "nome",  insertable = false, updatable = false),
            @JoinColumn(name = "jogador", referencedColumnName = "id", insertable = false, updatable = false)
    })
    private Cracha cracha;


    public CrachasAdquiridosId getId() {
        return id;
    }

    public void setId(CrachasAdquiridosId id) {
        this.id = id;
    }

    public Cracha getCracha() {
        return cracha;
    }

    public void setCracha(Cracha cracha) {
        this.cracha = cracha;
    }

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }
}
