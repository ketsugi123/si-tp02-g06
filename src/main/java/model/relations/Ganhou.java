package model.relations;

import jakarta.persistence.*;
import model.tables.Cracha;
import model.tables.Jogador;
import model.embeddables.GanhouId;

import java.io.Serializable;

@Entity
@Table(name="crachá_jogador")
public class Ganhou implements Serializable {

    @EmbeddedId
    private GanhouId id;


    // N:N realtion ganhou
    @ManyToOne
    @MapsId("jogador")
    @JoinColumn(name="jogador")
    private Jogador jogador;

    @ManyToOne
    @MapsId("cracha")
    @JoinColumn(name="crachá")
    private Cracha cracha;


    public GanhouId getId() {
        return id;
    }

    public void setId(GanhouId id) {
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
