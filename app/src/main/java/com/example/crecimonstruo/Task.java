package com.example.crecimonstruo;

import java.util.Date;

public class Task {

    private int id;
    private String titulo;
    private String descripcion;
    private int dificultad;
    private boolean lista;
    private Date dia;
    //Truco para fingir un AutoIncremental
    private static int ai=1;

    public Task(String titulo, String descripcion, int dificultad, Date dia) {
        //"AutoIncremental"
        this.id = ai;
        ai++;

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.dificultad = dificultad;
        this.lista = false;
        this.dia = dia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public boolean isLista() {
        return lista;
    }

    public void setLista(boolean lista) {
        this.lista = lista;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }
}
