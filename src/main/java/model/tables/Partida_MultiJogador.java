package model.tables;

import jakarta.persistence.*;
import model.embeddables.PartidaMId;
import model.embeddables.PartidaNId;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="partida_multijogador")
public class Partida_MultiJogador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Partida_MultiJogador() { }

    @EmbeddedId
    private PartidaMId partida_multiId;

    int pontuacao;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "partida", referencedColumnName = "partida", insertable = false, updatable = false),
            @JoinColumn(name = "jogo", referencedColumnName = "jogo", insertable = false, updatable = false)
    })
    private Partida partida;


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

    public Partida getPartida() {
        return partida;
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
