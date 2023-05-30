package model;

import jakarta.persistence.*;
import model.embeddables.ParticipaId;

@Entity
@Table(name = "jogador_conversa")
public class Participa {
    @EmbeddedId
    ParticipaId id;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name="id")
    private Jogador jogador;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name="id")
    private Conversa conversa;


    //getters and setters
    public ParticipaId getId() {return id;}

    public void setId(ParticipaId id) { this.id = id; }

    public Jogador getJogador() { return jogador; }

    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public Conversa getConversa() { return conversa; }

    public void setConversa(Conversa conversa) { this.conversa = conversa; }
}
