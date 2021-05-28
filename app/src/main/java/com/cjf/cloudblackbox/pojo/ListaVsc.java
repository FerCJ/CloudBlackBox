package com.cjf.cloudblackbox.pojo;

public class ListaVsc {
    private String NombreVid;
    private int Imagen;

    public ListaVsc(String nombreVid, int imagen) {
        this.NombreVid = nombreVid;
        this.Imagen = imagen;
    }

    public String getNombreVid() {
        return NombreVid;
    }

    public void setNombreVid(String nombreVid) {
        NombreVid = nombreVid;
    }

    public int getImagen() {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }
}
