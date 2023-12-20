package com.example.crecimonstruo.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.crecimonstruo.R;
import com.example.crecimonstruo.models.Monster;
import com.example.crecimonstruo.models.Task;
import com.example.crecimonstruo.utils.ImageUtils;
import com.example.crecimonstruo.viewModels.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private TextView tvNombre;
    private TextView tvNivel;
    private TextView tvExp;
    private Button bEditarNombre;
    private Button bAnadir;
    private Button bGuardar;
    private ImageView imgMonster;
    private String email;
    private HomeViewModel viewModel;
    private ProgressBar progressBar;
    private View overlay;


    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        progressBar = findViewById(R.id.progressBar);
        overlay = findViewById(R.id.overlay);

        // Obtener el correo electrónico del intent
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            email = bundle.getString("email");
        }

        setup();

        // Configurar observadores para actualizar la interfaz de usuario cuando los datos cambian
        viewModel.getMonsterLiveData().observe(this, monster -> {
            if (monster != null) {
                actualizarMonster(monster);
            }
        });

        viewModel.getTasksLiveData().observe(this, tasks -> {
            if (tasks != null) {
                actualizarLista(tasks);
            }
        });

        viewModel.getResultadoGuardadoLiveData().observe(this, resultado -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            switch(resultado){
                case "OK":
                    // Éxito al agregar la lista de tareas
                    builder.setTitle("Datos Guardados con Exitos")
                            .setMessage("Se almacenaron los datos correctamente")
                            .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // START THE GAME!
                                }
                            })
                            .show();
                    break;
                default:
                    builder.setTitle(resultado)
                            .setMessage("Ocurrio un error inesperado, intentelo mas tarde")
                            .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // START THE GAME!
                                }
                            })
                            .show();
                    break;
            }
        });

        // Cargar datos iniciales
        viewModel.loadData(email);
    }

    public void setup(){

        //Se muestra la carga
        progressBar.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);

        tvNombre = (TextView)findViewById(R.id.tvNombre);
        tvNivel = (TextView)findViewById(R.id.tvNivel);
        tvExp = (TextView)findViewById(R.id.tvExp);
        imgMonster = (ImageView)findViewById(R.id.imageMonster);


        //Funcionalidad de Boton "Editar"
        bEditarNombre = (Button)findViewById(R.id.b_editar_nombre);
        bEditarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialogNombre();
            }
        });
        //Funcionalidad de Boton "Añadir"
        bAnadir = (Button)findViewById(R.id.buttonAnadir);
        bAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormDialogAnadir();
            }
        });
        //Funcionalidad de Boton "Guardar"
        bGuardar = (Button)findViewById(R.id.buttonGuardar);
        bGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { guardar(); }
        });

    }

    public void actualizarMonster(Monster monster) {
        //Se muestra la carga
        progressBar.setVisibility(View.VISIBLE);
        overlay.setVisibility(View.VISIBLE);
        tvNombre.setText(monster.getNombre());
        tvNivel.setText("Nivel: " + monster.getNivel());
        if (monster.getNivel() < 4) {
            tvExp.setText("Exp: " + monster.getExp() + "/" + 5 * monster.getNivel());
        } else {
            tvExp.setText("Exp: " + monster.getExp());
        }

        int aux = monster.getNivel()-1;
        String evo = monster.getEvos().get(aux);

        // Cargar imagen desde la API con Glide y eliminar fondo blanco
        String imageUrl = "https://digimon-api.com/images/digimon/w/"+ evo +".png";
        Glide.with(this)
                .asBitmap()
                .load(imageUrl)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Eliminar fondo blanco del bitmap
                        Bitmap noWhiteBackgroundBitmap = ImageUtils.removeWhiteBackground(resource);

                        // Asignar el bitmap resultante al ImageView
                        imgMonster.setImageBitmap(noWhiteBackgroundBitmap);

                        // Manipular los márgenes en dp
                        int leftMarginDp = 0; // Ajusta este valor en dp según sea necesario
                        int topMarginDp = 0; // Ajusta este valor en dp según sea necesario
                        int rightMarginDp = 0; // Ajusta este valor en dp según sea necesario
                        int bottomMarginDp = 70; // Ajusta este valor en dp según sea necesario

                        switch(monster.getNivel()){
                            case 1:
                                topMarginDp = 180;
                                break;
                            case 2:
                                topMarginDp = 140;
                                break;
                            case 3:
                                topMarginDp = 100;
                                break;
                            case 4:
                                topMarginDp = 80;
                                break;
                        }


                        // Convertir los márgenes de dp a píxeles
                        int leftMarginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftMarginDp, getResources().getDisplayMetrics());
                        int topMarginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, topMarginDp, getResources().getDisplayMetrics());
                        int rightMarginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightMarginDp, getResources().getDisplayMetrics());
                        int bottomMarginPx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottomMarginDp, getResources().getDisplayMetrics());

                        // Aplicar márgenes al ConstraintLayout
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) imgMonster.getLayoutParams();
                        params.setMargins(leftMarginPx, topMarginPx, rightMarginPx, bottomMarginPx);
                        imgMonster.setLayoutParams(params);
                    }
                });
        progressBar.setVisibility(View.GONE);
        overlay.setVisibility(View.GONE);

    }

    public void actualizarLista(List<Task> tasks){

        LinearLayout layout = (LinearLayout)findViewById(R.id.listaLayout);

        layout.removeAllViews();

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

    private void showConfirmDialogTarea(String titulo, int idTask) { //Crea un mensaje de confirmacion emergente

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle(titulo)
                .setMessage("¿Marcar esta tarea como completada?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewModel.completarTarea(idTask);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showInputDialogNombre() {
        // Crea el cuadro de diálogo de entrada
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ingrese nuevo Nombre");

        // Configura el campo de entrada
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT); // Cambiado a WRAP_CONTENT para la altura
        input.setLayoutParams(lp);
        builder.setView(input);

        // Configura los botones de aceptar y cancelar
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String userInput = input.getText().toString();
                viewModel.handleInputNombre(userInput);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Muestra el cuadro de diálogo
        builder.show();
    }

    private void showFormDialogAnadir() {
        // Crea el cuadro de diálogo de formulario
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflar el diseño del formulario dentro del cuadro de diálogo
        View formView = getLayoutInflater().inflate(R.layout.activity_new_task, null);
        builder.setView(formView);

        // Configurar los botones de aceptar y cancelar
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Aquí puedes obtener los valores de los campos del formulario
                EditText editText1 = formView.findViewById(R.id.editTitulo);
                RadioGroup radioGroup = formView.findViewById(R.id.radio_group);
                RadioButton rb1 = formView.findViewById(R.id.radio_select3);
                RadioButton rb2 = formView.findViewById(R.id.radio_select2);
                RadioButton rb3 = formView.findViewById(R.id.radio_select1);

                String titulo = editText1.getText().toString();
                int seleccion = 0;

                if (rb1.isChecked()) {
                    seleccion = 1;
                } else if (rb2.isChecked()) {
                    seleccion = 2;
                } else if (rb3.isChecked()) {
                    seleccion = 3;
                }

                viewModel.addNewTask(titulo, seleccion);

            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Mostrar el cuadro de diálogo del formulario
        builder.show();
    }

    private void guardar(){
        viewModel.guardarData();
    }
}


