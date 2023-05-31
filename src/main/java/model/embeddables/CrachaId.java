package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class CrachaId implements Serializable {
    String nome;
    String jogo;
}
