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

    public PartidaId getId() {
        return id;
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

