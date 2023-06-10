package model.tables;


import jakarta.persistence.*;
import model.embeddables.CrachaId;
import model.relations.CrachasAdquiridos;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name="cracha")
@Entity
public class Cracha implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @EmbeddedId
    private CrachaId id;

    int limite;

    String url;

    @MapsId("jogo")
    @ManyToOne
    @JoinColumn(name="jogo")
    private Jogo jogo;

    public Jogo getJogo() {
        return jogo;
    }

    public void setJogo(Jogo jogo) {
        this.jogo = jogo;
    }

    @OneToMany(mappedBy = "cracha")
    private List<CrachasAdquiridos> crachasAdquiridosList = new ArrayList<>();

    public List<CrachasAdquiridos> getCrachasList() {
        return crachasAdquiridosList;
    }

    public void addCrachas(CrachasAdquiridos crachasAdquiridos) {
        this.crachasAdquiridosList.add(crachasAdquiridos);
    }


    public CrachaId getId() {
        return this.id;
    }

    public void setId(CrachaId id) {
        this.id = id;
    }

    public int getLimite() {
        return limite;
    }

    public void setLimite(int limit) {
        this.limite = limit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
