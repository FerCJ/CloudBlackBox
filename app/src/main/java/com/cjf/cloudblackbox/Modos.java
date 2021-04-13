package com.cjf.cloudblackbox;

public class Modos {

    private String modo;
    private int icono;

    public  Modos(String modo, int icono)
    {
        this.modo = modo;
        this.icono = icono;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }
}
