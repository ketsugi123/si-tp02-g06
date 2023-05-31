package model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private PartidaNMId partida;

    private int pontuacao;
    public PartidaNMId getPartida() {
        return partida;
    }

    public void setPartida(PartidaNMId partida) {
        this.partida = partida;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }


}
