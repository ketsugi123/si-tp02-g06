package model.embeddables;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ParticipaId implements Serializable {
    int jogador;
    int conversa;
}
