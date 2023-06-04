package model.embeddables;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;

// Composite Primary key for Partida
@Embeddable
public class PartidaId implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int partida;
    private String jogo;

    public PartidaId(){}
    public void setPartidaId(int partida) {
        this.partida = partida;
    }

    public int getPartidaId() {
        return partida;
    }

    public void setJogoId(String jogo) {
        this.jogo = jogo;
    }

    public String getJogoId() {
        return jogo;
    }
}
