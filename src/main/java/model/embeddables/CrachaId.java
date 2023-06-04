package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

// Composite jey for table cracha
@Embeddable
public class CrachaId implements Serializable {
    private String nome;
    private String jogo;

    public CrachaId(){}
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getJogo() {
        return jogo;
    }

    public void setJogo(String jogo) {
        this.jogo = jogo;
    }

}
