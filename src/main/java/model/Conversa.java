package model;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="conversa")
public class Conversa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Conversa() { }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nome;

    @OneToMany(mappedBy = "id")
    private List<Jogador> jogadores = new ArrayList<>();


    public List<Jogador> getJogadores(){ return this.jogadores; }

    public void addJogador(Jogador jogador){
        this.jogadores.add(jogador);
    }

    @OneToMany(mappedBy = "mensagem", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Mensagem> mensagens = new ArrayList<>();

    public List<Mensagem> getMensagens() {
        return mensagens;
    }

    public void addMensagem(Mensagem mensagem) {
        this.mensagens.add(mensagem);
    }

    //getter and setters
    public int getId() { return id; }
    public void setNome(String nome){ this.nome = nome; }
    public String getNome(){ return this.nome; }
}
