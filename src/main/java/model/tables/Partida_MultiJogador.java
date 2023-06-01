package model.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private PartidaNMId partida;

    int pontuacao;

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void setPartida(PartidaNMId partida) {
        this.partida = partida;
    }

    public PartidaNMId getPartida() {
        return partida;
    }

    public int getPontuacao() {
        return pontuacao;
    }

}
