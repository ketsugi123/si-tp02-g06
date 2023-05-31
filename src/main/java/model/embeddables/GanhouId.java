package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class GanhouId implements Serializable {
    int jogo;
    String jogador;
    String cracha;
}
