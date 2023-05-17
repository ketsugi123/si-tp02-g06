package model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
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
    List<Compra> compras;

    public void addCompra(Compra compra){ this.compras.add(compra); }

    public List<Compra> getCompras() { return compras; }

    public String getId(){ return this.id; }
    public String getNome(){ return this.nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getUrl(){ return this.url; }
    public void setUrl(String url) { this.url = url; }

}
