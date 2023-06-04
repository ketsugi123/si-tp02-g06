package model.embeddables;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

// Composite key for table Compra
@Embeddable
public class CompraId implements Serializable {
    private int jogador;
    private String jogo;

    public CompraId(){}
    public void setJogoId(String jogo) {
        this.jogo = jogo;
    }

    public String getJogoId() {
        return jogo;
    }

    public void setJogadorId(int jogador) {
        this.jogador = jogador;
    }

    public int getJogadorId() {
        return jogador;
    }
}

