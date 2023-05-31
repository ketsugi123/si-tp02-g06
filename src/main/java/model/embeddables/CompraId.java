package model.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

// Composite key for table Compra
@Embeddable
public class CompraId implements Serializable {
    int jogador;
    String jogo;
}
