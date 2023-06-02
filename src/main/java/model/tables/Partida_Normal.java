package model.tables;

import jakarta.persistence.*;
import model.embeddables.PartidaNMId;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="partida_normal")
public class Partida_Normal implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Partida_Normal() { }

    @EmbeddedId
    private PartidaNMId partida_normalId;

    private int pontuacao;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumns({
            @JoinColumn(name = "partidaN_partida", referencedColumnName = "partida"),
            @JoinColumn(name = "partidaN_jogo", referencedColumnName = "jogo")
    })
    private Partida partida;

    public Partida getPartida() {
        return partida;
    }

    @MapsId("jogador")
    @ManyToOne
    @JoinColumn(name = "partidaN_jogador", referencedColumnName = "jogador")
    private Jogador jogador;

    public Jogador getJogador() {
        return jogador;
    }

    public void setJogador(Jogador jogador) {
        this.jogador = jogador;
    }

    public PartidaNMId getPartida_normalId() {
        return partida_normalId;
    }

    public void setPartida_normalId(PartidaNMId partida) {
        this.partida_normalId = partida;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }


}
