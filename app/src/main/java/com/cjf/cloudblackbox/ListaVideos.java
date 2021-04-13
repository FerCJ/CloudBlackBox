package com.cjf.cloudblackbox;

public class ListaVideos {

    private String fecha;
    private int imagen;
    private String hora;

    public ListaVideos(String fecha, String hora, int imagen)
    {
        this.fecha = fecha;
        this.hora = hora;
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
