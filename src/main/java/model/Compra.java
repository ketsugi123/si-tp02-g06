package model;

import jakarta.persistence.*;
import model.embeddables.CompraId;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name="compra")
public class Compra implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Compra() { }

    @EmbeddedId
    private CompraId id;
    private Date data;
    private Double preco;


    // N:N relation Compra between jogo and jogador
    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    private Jogo jogo;

    @ManyToOne
    @MapsId("id")
    @JoinColumn(name = "id")
    private Jogador jogador;

    //getter and setters
    public Jogador getJogador() { return jogador; }

    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public Jogo getJogo() { return jogo; }

    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public CompraId getId() { return this.id; }

    public Date getData() { return data; }

    public void setData(Date data) { this.data = data; }

    public void setPreco(Double preco) { this.preco = preco;}
    public Double getPreco() { return preco; }

}
