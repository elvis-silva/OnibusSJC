package com.nigapps.onibus.sjc.entities;

import java.util.ArrayList;

public class SiteResponse extends BaseObject {

    private ArrayList<BusItem> posts, diasUteisList, sabadosList, domingosList, sabDomFeriadosList, segDomFeriadosList, obsList;

    public ArrayList<BusItem> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<BusItem> posts) {
        this.posts = posts;
    }

    public ArrayList<BusItem> getDiasUteisList() {
        return diasUteisList;
    }

    public void setDiasUteisList(ArrayList<BusItem> diasUteisList) {
        this.diasUteisList = diasUteisList;
    }

    public ArrayList<BusItem> getSabadosList() {
        return sabadosList;
    }

    public void setSabadosList(ArrayList<BusItem> sabadosList) {
        this.sabadosList = sabadosList;
    }

    public ArrayList<BusItem> getDomingosList() {
        return domingosList;
    }

    public void setDomingosList(ArrayList<BusItem> domingosList) {
        this.domingosList = domingosList;
    }

    public ArrayList<BusItem> getSabDomFeriadosList() {
        return sabDomFeriadosList;
    }

    public void setSabDomFeriadosList(ArrayList<BusItem> sabDomFeriadosList) {
        this.sabDomFeriadosList = sabDomFeriadosList;
    }

    public void setSegDomFeriadosList(ArrayList<BusItem> segDomFeriadosList) {
        this.segDomFeriadosList = segDomFeriadosList;
    }

    public ArrayList<BusItem> getSegDomFeriadosList() {
        return segDomFeriadosList;
    }

    public ArrayList<BusItem> getObsList() {
        return obsList;
    }

    public void setObsList(ArrayList<BusItem> obsList) {
        this.obsList = obsList;
    }

    @Override
    public String toString() {
        return "SiteResponse{" +
                "posts=" + posts +
                '}';
    }
}