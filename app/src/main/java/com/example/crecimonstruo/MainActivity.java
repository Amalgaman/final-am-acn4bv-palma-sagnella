package com.example.crecimonstruo;

import static com.example.crecimonstruo.R.color.taskB;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
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
        tasks.add(new Task("Terminar el TP", "Terminar el TP pendiente de Aplicaciones Moviles", 3, new Date()));
        tasks.add(new Task("Terminar el TP", "Terminar el TP pendiente de Aplicaciones Moviles", 3, new Date()));
        tasks.add(new Task("Terminar el TP", "Terminar el TP pendiente de Aplicaciones Moviles", 3, new Date()));
        tasks.add(new Task("Terminar el TP", "Terminar el TP pendiente de Aplicaciones Moviles", 3, new Date()));
        tasks.add(new Task("Terminar el TP", "Terminar el TP pendiente de Aplicaciones Moviles", 3, new Date()));
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
        actualizarLista(0);

    }

    public void actualizarMonster(){
        tvNombre.setText(mA.getNombre());
        tvNivel.setText("Nivel: "+mA.getNivel());
        tvExp.setText("Exp: "+mA.getExp()+"/"+5*mA.getNivel());
    }

    public void actualizarLista(int flag){

        LinearLayout layout = (LinearLayout)findViewById(R.id.listaLayout);

        if(flag == 1){
            layout.removeAllViews();
        }

        Button nuevo;

        int imgTilde = R.drawable.ic_complete;
        int img1 = R.drawable.baseline_filter_1_24;
        int img2 = R.drawable.baseline_filter_2_24;
        int img3 = R.drawable.baseline_filter_3_24;
        LinearLayout.LayoutParams parametros;
        int idTask;
        String titulo;

        for (Task task : tasks) {

            titulo = task.getTitulo();
            idTask = task.getId();

            nuevo = new Button(this);

            nuevo.setText(titulo);
            nuevo.setId(idTask);
            parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            parametros.setMargins(8,5,8,5);
            nuevo.setLayoutParams(parametros);
            nuevo.setBackgroundResource(R.drawable.boton_task);
            nuevo.setTextColor(ContextCompat.getColor(this, R.color.white));
            nuevo.setTextSize(20);


            if (task.isLista()){
                switch (task.getDificultad()){
                    case 1:
                        nuevo.setCompoundDrawablesWithIntrinsicBounds(img1, 0, imgTilde, 0);
                        break;
                    case 2:
                        nuevo.setCompoundDrawablesWithIntrinsicBounds(img2, 0, imgTilde, 0);
                        break;
                    case 3:
                        nuevo.setCompoundDrawablesWithIntrinsicBounds(img3, 0, imgTilde, 0);
                        break;
                }
                nuevo.setCompoundDrawablePadding(0);
            }else{
                switch (task.getDificultad()){
                    case 1:
                        nuevo.setCompoundDrawablesWithIntrinsicBounds(img1, 0, 0, 0);
                        break;
                    case 2:
                        nuevo.setCompoundDrawablesWithIntrinsicBounds(img2, 0, 0, 0);
                        break;
                    case 3:
                        nuevo.setCompoundDrawablesWithIntrinsicBounds(img3, 0, 0, 0);
                        break;
                }
                nuevo.setCompoundDrawablePadding(0);

                nuevo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmDialogTarea(((Button)v).getText().toString(),((Button)v).getId());
                    }
                });
            }

            layout.addView(nuevo);
        }

    }

    private void completarTarea(int id){
        tasks.get(id-1).setLista(true);

        actualizarLista(1);
    }

    private void showConfirmDialogTarea(String titulo, int idTask) { //Crea un mensaje de confirmacion emergente

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage("¿Marcar esta tarea como completada?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        completarTarea(idTask);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void editarNombre(View view){

    }
}