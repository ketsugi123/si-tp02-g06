package model.tables;

import jakarta.persistence.*;
import model.embeddables.PartidaNId;

import java.io.Serial;
import java.io.Serializable;

@Entity
@DiscriminatorValue("MULTIJOGADOR")
@PrimaryKeyJoinColumns ({
        @PrimaryKeyJoinColumn(name = "partida", referencedColumnName = "partida"),
        @PrimaryKeyJoinColumn(name = "jogo", referencedColumnName = "jogo")
})
@Table(name="partida_normal")
public class Partida_Normal extends Partida implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Partida_Normal() { }

    @EmbeddedId
    private PartidaNId partida_normalId;

    private int pontuacao;


    @ManyToOne
    @MapsId
    @JoinColumn(name = "jogador", referencedColumnName = "jogador")
    private Jogador jogador;

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public PartidaNId getPartida_normalId() {
        return partida_normalId;
    }

    public void setPartida_normalId(PartidaNId partida) {
        this.partida_normalId = partida;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }


}
