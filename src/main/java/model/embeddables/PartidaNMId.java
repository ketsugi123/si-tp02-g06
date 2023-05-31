package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

// Composite Primary key for Partida_Normal and Partida_MultiJogador
@Embeddable
public class PartidaNMId implements Serializable {
    int partida;
    String jogo;
    int jogador;
}
