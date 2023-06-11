package model.tables;

import jakarta.persistence.*;
import model.embeddables.MensagemId;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "mensagem")
public class Mensagem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    public Mensagem() { }

    @EmbeddedId
    private MensagemId mensagemId;

    String conteudo;

    Date data;


    @ManyToOne
    @MapsId
    @JoinColumn(name="conversa")
    private Conversa conversa;

    public MensagemId getMensagemId() {
        return mensagemId;
    }

    public void setMensagemId(MensagemId mensagemId) {
        this.mensagemId = mensagemId;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
