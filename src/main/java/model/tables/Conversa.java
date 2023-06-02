package model.tables;

import jakarta.persistence.*;
import model.relations.Participa;

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

    // N:N relation participa
    @OneToMany(mappedBy = "conversa")
    private List<Participa> participantes = new ArrayList<>();

    public List<Participa> getParticipantes(){ return this.participantes; }

    public void addJogador(Participa participante){
        this.participantes.add(participante);
    }

    // 1:N relation contem
    @OneToMany(mappedBy = "conversa", cascade = CascadeType.PERSIST, orphanRemoval = true)
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
