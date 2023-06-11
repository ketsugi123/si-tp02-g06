package model.tables;

import jakarta.persistence.*;
import model.embeddables.PartidaMId;
import model.embeddables.PartidaNId;

import java.io.Serial;
import java.io.Serializable;

@Entity
@DiscriminatorValue("MULTIJOGADOR")
@PrimaryKeyJoinColumns ({
        @PrimaryKeyJoinColumn(name = "partida", referencedColumnName = "partida"),
        @PrimaryKeyJoinColumn(name = "jogo", referencedColumnName = "jogo")
})
@Table(name="partida_multijogador")
public class Partida_MultiJogador extends Partida implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Partida_MultiJogador() { }

    @EmbeddedId
    private PartidaMId partida_multiId;

    int pontuacao;


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


    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void setPartida_multiId(PartidaMId partida) {
        this.partida_multiId = partida;
    }

    public PartidaMId getPartida_multiId() {
        return partida_multiId;
    }

    public int getPontuacao() {
        return pontuacao;
    }

}
