package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CrachasAdquiridosId implements Serializable {
    private int jogador;
    private String jogo;
    private String cracha;

    public CrachasAdquiridosId(){}
    public void setJogo(String jogo) {
        this.jogo = jogo;
    }

    public String getJogo() {
        return jogo;
    }

    public void setJogador(int jogador) {
        this.jogador = jogador;
    }

    public int getJogador() {
        return this.jogador;
    }

    public void setCracha(String cracha) {
        this.cracha = cracha;
    }

    public String getCracha() {
        return cracha;
    }
}
