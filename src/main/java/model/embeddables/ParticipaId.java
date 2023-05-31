package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

//Composite key for jogador_conversa rewlation participa
@Embeddable
public class ParticipaId implements Serializable {
    int jogador;
    int conversa;
}
