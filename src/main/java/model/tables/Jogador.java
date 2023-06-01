package model.tables;

import jakarta.persistence.*;
import model.relations.Compra;
import model.relations.CrachasAdquiridos;
import model.relations.Participa;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="jogador")
public class Jogador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public Jogador(){ }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String username;
    private String estado;
    private String regiao;
    @OneToMany(mappedBy = "jogador")
    private List<Compra> compras = new ArrayList<>();
    public void addCompra(Compra compra){ this.compras.add(compra); }
    public List<Compra> getCompras() { return compras; }

    @OneToMany(mappedBy = "jogador")
    private List<CrachasAdquiridos> crachasAdquiridosList;

    public List<CrachasAdquiridos> getGanhouList() {
        return crachasAdquiridosList;
    }

    public void addGanhou(CrachasAdquiridos crachasAdquiridos) {
        this.crachasAdquiridosList.add(crachasAdquiridos);
    }

    @OneToMany(mappedBy = "jogador")
    private List<Participa> participaList;
    public List<Participa> getParticipaList() { return this.participaList; }
    public void addParticipacao(Participa participa){this.participaList.add(participa);}

    @OneToMany(mappedBy = "crachá")
    private List<Cracha> crachas = new ArrayList<>();

    public List<Cracha> getCrachas() {
        return crachas;
    }

    public void addCracha(Cracha cracha){
        this.crachas.add(cracha);
    }

    @OneToMany(mappedBy = "partida_normal", cascade = CascadeType.PERSIST)
    private List<Partida_Normal> partidasNormais = new ArrayList<>();

    public List<Partida_Normal> getPartidasNormais() {
        return partidasNormais;
    }

    public void addPartida_Normal(Partida_Normal partida_normal){
        this.partidasNormais.add(partida_normal);
    }

    @OneToMany(mappedBy = "partida_multijogador", cascade = CascadeType.PERSIST)
    private List<Partida_MultiJogador> partidasMultiJogador = new ArrayList<>();

    public List<Partida_MultiJogador> getPartidasMultiJogador() {
        return partidasMultiJogador;
    }

    public void setCompras(List<Compra> compras) {
        this.compras = compras;
    }


    //getter and setters
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

}
