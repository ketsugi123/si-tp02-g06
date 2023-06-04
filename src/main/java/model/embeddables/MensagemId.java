package model.embeddables;

import jakarta.persistence.Embeddable;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.io.Serializable;

//Composite key for table mensagem
@Embeddable
public class MensagemId implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int conversa;

    public MensagemId(){ }
    public void setid(int id) {
        this.id = id;
    }

    public int getid() {
        return id;
    }

    public int getConversa() {
        return conversa;
    }
    public void setConversa(int conversa) {
        this.conversa = conversa;
    }

}
