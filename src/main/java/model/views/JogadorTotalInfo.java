package model.views;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "jogadorTotalInfo")
public class JogadorTotalInfo implements Serializable {

    public JogadorTotalInfo(){}

    @Id
    private int id;

    private String estado;

    private String email;

    private String username;

    private int jogosParticipados;

    private int partidasParticipadas;

    private int pontuacaoTotal;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJogosParticipados() {
        return jogosParticipados;
    }

    public void setJogosParticipados(int numJogos) {
        this.jogosParticipados = numJogos;
    }

    public int getPartidasParticipadas() {
        return partidasParticipadas;
    }

    public void setPartidasParticipadas(int numPartidas) {
        this.partidasParticipadas = numPartidas;
    }

    public int getPontuacaoTotal() {
        return pontuacaoTotal;
    }

    public void setPontuacaoTotal(int pontos) {
        this.pontuacaoTotal = pontos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
