package model.embeddables;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;

//Composite key for table mensagem
@Embeddable
public class MensagemId implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    int connversa;
}
