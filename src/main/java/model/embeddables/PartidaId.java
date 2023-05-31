package model.embeddables;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;

public class PartidaId implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int partida;
    String jogo;
}
