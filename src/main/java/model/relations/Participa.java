package model.relations;

import jakarta.persistence.*;
import model.tables.Conversa;
import model.tables.Jogador;
import model.embeddables.ParticipaId;

import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "jogador_conversa")
public class Participa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Participa(){ }
    @EmbeddedId
    private ParticipaId id;

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
