package com.nigapps.onibus.sjc.entities;

import com.nigapps.onibus.sjc.R;

public class BusItem {
    private int id;
    private String text, URL, numDaLinha, nomeDaLinha;
    private int favBgResId = R.drawable.ic_star_border_black_24dp;
    private String idDaLinha = "";
    private int indexDaLinha;

    public BusItem() {};

    public BusItem(String numDaLinha, int favBgResId) {
        this.numDaLinha = numDaLinha;
        this.favBgResId = favBgResId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "BusItem{" +
                "text='" + text + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }

    public String getNumLinha() {
        return numDaLinha;
    }

    public void setNumDaLinha(String numDaLinha) {
        this.numDaLinha = numDaLinha;
    }

    public String getNomeDaLinha() {
        return nomeDaLinha;
    }

    public void setNomeDaLinha(String nomeDaLinha) {
        this.nomeDaLinha = nomeDaLinha;
    }

    public int getFavBgResId() {
        return favBgResId;
    }

    public void setFavBgResId(int favBgResId) {
        this.favBgResId = favBgResId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdDaLinha(String idDaLinha) {
        this.idDaLinha = idDaLinha;
    }

    public String getIdDaLinha() {
        return idDaLinha;
    }

    public void setIndexDaLinha(int indexDaLinha) {
        this.indexDaLinha = indexDaLinha;
    }

    public int getIndexDaLinha() {
        return indexDaLinha;
    }
}