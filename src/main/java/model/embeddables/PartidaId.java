package model.embeddables;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;

// Composite Primary key for Partida
@Embeddable
public class PartidaId implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int partida;
    String jogo;
}
