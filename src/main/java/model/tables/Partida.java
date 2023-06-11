package model.tables;

import jakarta.persistence.*;
import model.embeddables.PartidaId;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name="partida")
public class Partida implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Partida() { }

    @EmbeddedId
    private PartidaId id;

    String estado;
    String nome_regiao;

    Date dtinicio;

    Date dtfim;

    // 1:N
    @ManyToOne
    @MapsId
    @JoinColumn(name = "id")
    private Jogo jogo;

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    public Jogo getJogo() {
        return jogo;
    }

    public PartidaId getId() {
        return id;
    }

    @OneToOne(mappedBy = "partida")
    private Partida_MultiJogador partida_multiJogador;

    public Partida_MultiJogador getPartida_multiJogador() {
        return partida_multiJogador;
    }

    public void setPartida_multiJogador(Partida_MultiJogador partida_multiJogador) {
        this.partida_multiJogador = partida_multiJogador;
    }

    @OneToOne(mappedBy = "partida")
    private Partida_Normal partida_normal;

    public Partida_Normal getPartida_normal() {
        return partida_normal;
    }

    public void setPartida_normal(Partida_Normal partida_normal) {
        this.partida_normal = partida_normal;
    }

    public void setId(PartidaId id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNome_regiao() {
        return nome_regiao;
    }

    public void setNome_regiao(String nome_regiao) {
        this.nome_regiao = nome_regiao;
    }

    public Date getDtinicio() {
        return dtinicio;
    }

    public void setDtinicio(Date dtinicio) {
        this.dtinicio = dtinicio;
    }

    public void setDtfim(Date dtfim) {
        this.dtfim = dtfim;
    }

    public Date getDtfim() {
        return dtfim;
    }
}

