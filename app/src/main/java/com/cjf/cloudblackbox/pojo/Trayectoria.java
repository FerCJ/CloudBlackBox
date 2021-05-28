package com.cjf.cloudblackbox.pojo;

public class Trayectoria {

    private  int icon;
    private String fecha;

    public Trayectoria (int icon, String fecha)
    {
        this.fecha = fecha;
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
