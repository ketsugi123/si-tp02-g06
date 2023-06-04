package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

// Composite Primary key for Partida_Normal and Partida_MultiJogador
@Embeddable
public class PartidaNMId implements Serializable {
    int partida;
    String jogo;
    int jogador;

    public PartidaNMId(){}

    public void setPartida(int partidaId) {
        this.partida = partidaId;
    }

    public int getPartida() {
        return partida;
    }

    public String getJogo() {
        return jogo;
    }

    public int getJogador() {
        return jogador;
    }

    public void setJogador(int jogadorId) {
        this.jogador = jogadorId;
    }

    public void setJogo(String jogoId) {
        this.jogo = jogoId;
    }
}
