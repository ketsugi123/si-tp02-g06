package model;

import jakarta.persistence.*;
import model.relations.Compra;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="jogo")
public class Jogo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String nome;
    private String url;

    @OneToMany(mappedBy = "jogo")
    private List<Compra> compras = new ArrayList<>();

    public void addCompra(Compra compra){ this.compras.add(compra); }

    public List<Compra> getCompras() { return compras; }

    @OneToMany(mappedBy = "crachá" ,cascade = CascadeType.PERSIST)
    private List<Cracha> crachas = new ArrayList<>();

    public List<Cracha> getCrachas() {
        return crachas;
    }
    public void addCracha(Cracha cracha){
        this.crachas.add(cracha);
    }

    @OneToMany(mappedBy = "partida", cascade = CascadeType.PERSIST)
    private List<Partida> partidas = new ArrayList<>();

    public List<Partida> getPartidas() {
        return partidas;
    }

    public void addPartida(Partida partida){
        partidas.add(partida);
    }

    public String getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUrl(){ return this.url; }
    public void setUrl(String url) { this.url = url; }

}
