package model;

import jakarta.persistence.*;
import model.embeddables.CompraId;

import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name="compra")
public class Compra implements Serializable {

    public Compra() { }
    @EmbeddedId
    private CompraId id;
    private Date data;
    private Double preco;


    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jogo")
    private Jogo jogo;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("jogador")
    private Jogador jogador;

    public Jogador getJogador() { return jogador; }

    public void setJogador(Jogador jogador) { this.jogador = jogador; }

    public Jogo getJogo() { return jogo; }

    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public CompraId getId() { return this.id; }

    public void setId(CompraId id){ this.id = id; }

    public Date getData() { return data; }

    public void setData(Date data) { this.data = data; }

    public void setPreco(Double preco) { this.preco = preco;}
    public Double getPreco() { return preco; }

}
