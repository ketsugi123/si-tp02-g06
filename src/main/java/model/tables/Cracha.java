package model.tables;


import jakarta.persistence.*;
import model.embeddables.CrachaId;
import model.relations.CrachasAdquiridos;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name="crach�")
@Entity
public class Cracha implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private CrachaId id;

    int limit;

    String url;

    @OneToMany(mappedBy = "crach�")
    private List<CrachasAdquiridos> crachasAdquiridosList;

    public List<CrachasAdquiridos> getGanhouList() {
        return crachasAdquiridosList;
    }

    public void addGanhou(CrachasAdquiridos crachasAdquiridos) {
        this.crachasAdquiridosList.add(crachasAdquiridos);
    }

    @OneToMany(mappedBy = "cracha", cascade = CascadeType.PERSIST)
    private List<Jogador> jogadores = new ArrayList<>();

    public List<Jogador> getJogadores() {
        return jogadores;
    }

    public void addJogadpr(Jogador jogador) {
        this.jogadores.add(jogador);
    }

    public CrachaId getId() {
        return this.id;
    }

    public void setId(CrachaId id) {
        this.id = id;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}