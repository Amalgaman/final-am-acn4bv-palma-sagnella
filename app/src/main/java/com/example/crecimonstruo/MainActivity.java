package com.example.crecimonstruo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvNombre;
    private TextView tvNivel;
    private TextView tvExp;

    private Monster mA;

    private LinkedList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Datos Placeholder para inicializar
        mA = new Monster("Cubsprout", new String[]{"cubsprout", "dandylion", "pawthorne", "bloomane"});
        tasks = new LinkedList<Task>();
        tvNombre = (TextView)findViewById(R.id.tvNombre);
        tvNivel = (TextView)findViewById(R.id.tvNivel);
        tvExp = (TextView)findViewById(R.id.tvExp);

        actualizarMonster();
    }

    public void actualizarMonster(){
        tvNombre.setText(mA.getNombre());
        tvNivel.setText("Nivel: "+mA.getNivel());
        tvExp.setText("Exp: "+mA.getExp()+"/"+5*mA.getNivel());
    }
}