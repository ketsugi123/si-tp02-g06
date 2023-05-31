package model.relations;

import jakarta.persistence.*;
import model.Cracha;
import model.Jogador;
import model.embeddables.GanhouId;

import java.io.Serializable;

@Entity
@Table(name="crach�_jogador")
public class Ganhou implements Serializable {

    @EmbeddedId
    private GanhouId id;


    // N:N realtion ganhou
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name="id")
    private Jogador jogador;

    @ManyToOne
    @MapsId("nome")
    @JoinColumn(name="nome")
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
