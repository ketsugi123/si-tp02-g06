package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

//Composite key for jogador_conversa rewlation participa
@Embeddable
public class ParticipaId implements Serializable {
    private int jogador;
    private int conversa;

    public ParticipaId(){}

    public void setJogadorId(int jogador) {
        this.jogador = jogador;
    }

    public int getJogadorId() {
        return jogador;
    }

    public void setConversaId(int conversa) {
        this.conversa = conversa;
    }

    public int getConversaId() {
        return conversa;
    }
}
