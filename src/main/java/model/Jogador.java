package model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="jogador")
public class Jogador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "jogador")
    List<Compra> compras = new ArrayList<>();

    public void addCompra(Compra compra){ this.compras.add(compra); }

    public List<Compra> getCompras() { return compras; }

    private String email;
    private String username;
    private String estado;
    private String regiao;


    public Jogador(){ }
    public int getId(){
        return this.id;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEstado(){
        return this.estado;
    }

    public void setEstado(String estado){
        this.estado = estado;
    }

    public String getRegiao(){
        return this.regiao;
    }

    public void setRegiao(String regiao){
        this.regiao = regiao;
    }

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "compra",
            joinColumns = @JoinColumn(name = "jogador"),
            inverseJoinColumns = @JoinColumn(name = "jogo")
    )
    private List<Jogo> jogos;

}
