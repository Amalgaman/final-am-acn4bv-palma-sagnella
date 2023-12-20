package com.example.crecimonstruo.models;

import java.util.Date;

public class Task {

    private int id;
    private String titulo;
    private String descripcion;
    private int dificultad;
    private boolean lista;

    //Truco para fingir un AutoIncremental
    private static int ai=1;

    public Task(String titulo, String descripcion, int dificultad) {
        //"AutoIncremental"
        this.id = ai;
        ai++;

        this.titulo = titulo;
        this.descripcion = descripcion;
        this.dificultad = dificultad;
        this.lista = false;

    }

    public Task(String titulo, int dificultad, boolean lista) {
        //"AutoIncremental"
        this.id = ai;
        ai++;

        this.titulo = titulo;
        this.descripcion = "";
        this.dificultad = dificultad;
        this.lista = lista;

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

}
