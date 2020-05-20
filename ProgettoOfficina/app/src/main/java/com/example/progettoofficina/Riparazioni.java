package com.example.progettoofficina;

import java.util.Date;

public class Riparazioni {
    //Dichiaro le proprietà private della classe
    private int idRiparazione;
    private int idAutoRiparazione;
    private Date dataRiparazione;
    private String causaRiparazione;
    private int costoRiparazione;
    private int pagatoRiparazione;
    private String marcaAutoRiparazione;
    private String modelloAutoRiparazione;

    //Dichiaro il costruttore
    public Riparazioni(){

    }

    //Metodi Getter e Setter per accedere alle proprietà
    public int getIdRiparazione() {
        return idRiparazione;
    }

    public void setIdRiparazione(int idRiparazione) {
        this.idRiparazione = idRiparazione;
    }

    public int getIdAutoRiparazione() {
        return idAutoRiparazione;
    }

    public void setIdAutoRiparazione(int idAutoRiparazione) {
        this.idAutoRiparazione = idAutoRiparazione;
    }

    public Date getDataRiparazione() {
        return dataRiparazione;
    }

    public void setDataRiparazione(Date dataRiparazione) {
        this.dataRiparazione = dataRiparazione;
    }

    public String getCausaRiparazione() {
        return causaRiparazione;
    }

    public void setCausaRiparazione(String causaRiparazione) {
        this.causaRiparazione = causaRiparazione;
    }

    public int getCostoRiparazione() {
        return costoRiparazione;
    }

    public void setCostoRiparazione(int costoRiparazione) {
        this.costoRiparazione = costoRiparazione;
    }

    public int getPagatoRiparazione() {
        return pagatoRiparazione;
    }

    public void setPagatoRiparazione(int pagatoRiparazione) {
        this.pagatoRiparazione = pagatoRiparazione;
    }

    public String getMarcaAutoRiparazione() {
        return marcaAutoRiparazione;
    }

    public void setMarcaAutoRiparazione(String marcaAutoRiparazione) {
        this.marcaAutoRiparazione = marcaAutoRiparazione;
    }

    public String getModelloAutoRiparazione() {
        return modelloAutoRiparazione;
    }

    public void setModelloAutoRiparazione(String modelloAutoRiparazione) {
        this.modelloAutoRiparazione = modelloAutoRiparazione;
    }
}
