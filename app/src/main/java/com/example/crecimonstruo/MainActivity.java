package com.example.crecimonstruo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvNombre;
    private TextView tvNivel;
    private TextView tvExp;
    private Button bEditarNombre;

    private Monster mA;

    private LinkedList<Task> tasks;


    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Datos Placeholder para inicializar
        mA = new Monster("Cubsprout", new String[]{"cubsprout", "dandylion", "pawthorne", "bloomane"});
        tasks = new LinkedList<Task>();
        tasks.add(new Task("Lavar la ropa", "Poner la ropa en el lavarropa y colgarla", 1, new Date()));
        tasks.add(new Task("Almorzar Sano", "Preparar ensalada de verduras para el almuerzo", 2, new Date()));
        tasks.add(new Task("Trotar 1 Hora", "Trotar durante 1 hora en la plaza", 2, new Date()));
        tasks.add(new Task("Terminar el TP", "Terminar el TP pendiente de Aplicaciones Moviles", 3, new Date()));

        tvNombre = (TextView)findViewById(R.id.tvNombre);
        tvNivel = (TextView)findViewById(R.id.tvNivel);
        tvExp = (TextView)findViewById(R.id.tvExp);

        bEditarNombre = (Button)findViewById(R.id.b_editar_nombre);
        bEditarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,EditarNombre.class);
                startActivity(intent);
            }
        });

        actualizarMonster();
        actualizarLista();
    }

    public void actualizarMonster(){
        tvNombre.setText(mA.getNombre());
        tvNivel.setText("Nivel: "+mA.getNivel());
        tvExp.setText("Exp: "+mA.getExp()+"/"+5*mA.getNivel());
    }

    public void actualizarLista(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.listaLayout);
        Button nuevo;

        int imgTilde = R.drawable.ic_complete;

        for (Task task : tasks) {

            nuevo = new Button(this);

            nuevo.setText(task.getTitulo());
            nuevo.setId(task.getId());

            if (task.isLista()){
                nuevo.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgTilde, 0);
                nuevo.setCompoundDrawablePadding(0);
            }



            layout.addView(nuevo);
        }
        tvNombre.setText(mA.getNombre());
        tvNivel.setText("Nivel: "+mA.getNivel());
        tvExp.setText("Exp: "+mA.getExp()+"/"+5*mA.getNivel());
    }

    public void editarNombre(View view){

    }
}