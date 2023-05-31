package model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name="partida_normal")
public class Partida_MultiJogador implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    public Partida_MultiJogador() { }

    @Id
    int partida;

    int pontuacao;

    public int getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(int pontuacao) {
        this.pontuacao = pontuacao;
    }

    public void setPartida(int partida) {
        this.partida = partida;
    }

    public int getPartida() {
        return partida;
    }


}
