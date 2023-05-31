package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

// Composite jey for table cracha
@Embeddable
public class CrachaId implements Serializable {
    String nome;
    String jogo;
}
