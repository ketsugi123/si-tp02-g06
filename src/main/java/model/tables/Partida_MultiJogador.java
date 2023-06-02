package model.tables;

import jakarta.persistence.*;
import model.embeddables.PartidaNMId;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="partida_normal")
public class Partida_MultiJogador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Partida_MultiJogador() { }

    @Id
    private PartidaNMId partida_multiId;

    int pontuacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "partida", referencedColumnName = "partida"),
            @JoinColumn(name = "jogo", referencedColumnName = "jogo")
    })
    private Partida partida;

    @MapsId("jogador")
    @ManyToOne
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

    public void setPartida_multiId(PartidaNMId partida) {
        this.partida_multiId = partida;
    }

    public PartidaNMId getPartida_multiId() {
        return partida_multiId;
    }

    public int getPontuacao() {
        return pontuacao;
    }

}
