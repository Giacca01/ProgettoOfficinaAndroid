package com.example.progettoofficina;

public class Auto {
    //Dichiaro le proprietà private della classe
    private int idAuto;
    private String marcaAuto;
    private String modelloAuto;

    //Dichiaro il costruttore
    public Auto(){

    }

    //Metodi Getter e Setter per accedere alle proprietà
    public int getIdAuto() {
        return idAuto;
    }

    public void setIdAuto(int idAuto) {
        this.idAuto = idAuto;
    }

    public String getMarcaAuto() {
        return marcaAuto;
    }

    public void setMarcaAuto(String marcaAuto) {
        this.marcaAuto = marcaAuto;
    }

    public String getModelloAuto() {
        return modelloAuto;
    }

    public void setModelloAuto(String modelloAuto) {
        this.modelloAuto = modelloAuto;
    }
}
