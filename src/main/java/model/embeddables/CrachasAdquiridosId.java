package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CrachasAdquiridosId implements Serializable {
    private int jogo;
    private String jogador;
    private String cracha;

    public CrachasAdquiridosId(){}
    public void setJogo(int jogo) {
        this.jogo = jogo;
    }

    public int getJogo() {
        return jogo;
    }

    public void setJogador(String jogador) {
        this.jogador = jogador;
    }

    public String getJogador() {
        return jogador;
    }

    public void setCracha(String cracha) {
        this.cracha = cracha;
    }

    public String getCracha() {
        return cracha;
    }
}
