package com.example.crecimonstruo;

public class Monster {

    private String nombre;
    private int exp;
    private int nivel;
    private String[] evos;

    public Monster(String nombre, String[] evos) {
        this.nombre = nombre;
        this.exp = 0;
        this.nivel = 1;
        this.evos = evos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String[] getEvos() {
        return evos;
    }

    public void setEvos(String[] evos) {
        this.evos = evos;
    }

    public void subirNivel(int exp){
        this.exp += exp;

        if(this.exp < 5){
            this.nivel = 1;
        }else if(this.exp <10){
            this.nivel = 2;
        }else if(this.exp <15){
            this.nivel = 3;
        }else {
            this.nivel = 4;
        }
    }
}
