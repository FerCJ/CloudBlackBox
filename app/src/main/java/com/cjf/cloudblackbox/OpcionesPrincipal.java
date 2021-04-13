package com.cjf.cloudblackbox;

public class OpcionesPrincipal {

    private String opcion;
    private int icono;

    public OpcionesPrincipal(String opcion, int icono)
    {
        this.opcion = opcion;
        this.icono = icono;
    }

    public String getOpcion() {
        return opcion;
    }

    public void setOpcion(String opcion) {
        this.opcion = opcion;
    }

    public int getIcono() {
        return icono;
    }

    public void setIcono(int icono) {
        this.icono = icono;
    }



}
