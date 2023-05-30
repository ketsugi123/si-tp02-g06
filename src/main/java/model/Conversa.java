package model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="conversa")
public class Conversa implements Serializable {

    public Conversa() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;

    @OneToMany(mappedBy = "conversa")
    private List<Jogador> jogadores = new ArrayList<>();

    public List<Jogador> getJogadores(){ return this.jogadores; }

    public void addJogador(Jogador jogador){
        this.jogadores.add(jogador);
    }

    //getter and setters
    public int getId() { return id; }
    public void setNome(String nome){ this.nome = nome; }
    public String getNome(){ return this.nome; }
}
