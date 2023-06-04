package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

// Composite Primary key for Partida_Normal and Partida_MultiJogador
@Embeddable
public class PartidaNMId implements Serializable {
    int partidaId;
    String jogoId;
    int jogadorId;

    public PartidaNMId(){}

    public void setPartidaId(int partidaId) {
        this.partidaId = partidaId;
    }

    public int getPartidaId() {
        return partidaId;
    }

    public String getJogoId() {
        return jogoId;
    }

    public int getJogadorId() {
        return jogadorId;
    }

    public void setJogadorId(int jogadorId) {
        this.jogadorId = jogadorId;
    }

    public void setJogoId(String jogoId) {
        this.jogoId = jogoId;
    }
}
