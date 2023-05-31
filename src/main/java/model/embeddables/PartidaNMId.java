package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class PartidaNMId implements Serializable {
    int partida;
    String jogo;
    int jogador;
}
