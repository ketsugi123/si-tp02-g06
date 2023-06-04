package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CrachasAdquiridosId implements Serializable {
    private int jogoId;
    private String jogadorId;
    private String crachaId;

    public CrachasAdquiridosId(){}
    public void setJogoId(int jogo) {
        this.jogoId = jogo;
    }

    public int getJogoId() {
        return jogoId;
    }

    public void setJogadorId(String jogador) {
        this.jogadorId = jogador;
    }

    public String getJogadorId() {
        return jogadorId;
    }

    public void setCrachaId(String cracha) {
        this.crachaId = cracha;
    }

    public String getCrachaId() {
        return crachaId;
    }
}
