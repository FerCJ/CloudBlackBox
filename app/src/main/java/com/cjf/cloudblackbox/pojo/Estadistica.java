package com.cjf.cloudblackbox.pojo;

public class Estadistica {

    private int icono;
    private String estadistica;

    public Estadistica (int icono, String estadistica)
    {
        this.icono = icono;
        this.estadistica = estadistica;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }

    public String getEstadistica() {
        return estadistica;
    }

    public void setEstadistica(String estadistica) {
        this.estadistica = estadistica;
    }
}
