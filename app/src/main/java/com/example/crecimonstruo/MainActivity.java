package com.example.crecimonstruo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvNombre;
    private TextView tvNivel;
    private TextView tvExp;
    private Button bEditarNombre;
    private Button bAnadir;
    private ImageView imgMonster;
    private Monster mA;
    private LinkedList<Task> tasks = new LinkedList<Task>();;


    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db =FirebaseFirestore.getInstance();

        //setup
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        setup(db, email);
    }

    public void setup(FirebaseFirestore db, String email){

        tvNombre = (TextView)findViewById(R.id.tvNombre);
        tvNivel = (TextView)findViewById(R.id.tvNivel);
        tvExp = (TextView)findViewById(R.id.tvExp);
        imgMonster = (ImageView)findViewById(R.id.imageMonster);

        bEditarNombre = (Button)findViewById(R.id.b_editar_nombre);
        bEditarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialogNombre();
            }
        });

        bAnadir = (Button)findViewById(R.id.buttonAnadir);
        bAnadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFormDialogAnadir();
            }
        });

        db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // El documento existe, puedes acceder a los datos
                Map<String, Object> userData = documentSnapshot.getData();

                if (userData.containsKey("monster")) {
                    // Accede al subdocumento "monster"
                    Map<String, Object> monsterData = (Map<String, Object>) userData.get("monster");

                    mA = new Monster(
                            (String) monsterData.get("nombre"),
                            (int) ((long) monsterData.get("exp")),
                            (int) ((long) monsterData.get("lvl")),
                            (List<String>) monsterData.get("evos")
                    );

                    actualizarMonster();

                } else {
                    // El documento no tiene un subdocumento "monster"
                }

                if (userData.containsKey("task")) {
                    // Accede al subdocumento "task"
                    List<Object> taskData = (List<Object>) userData.get("task");

                    for (Object task : taskData) {
                        Map<String, Object> aux = (Map<String, Object>) task;
                        Task aux2 = new Task((String) aux.get("titulo"), ((Long) aux.get("dificultad")).intValue(), (boolean) aux.get("lista"));
                        tasks.add(aux2);
                    }

                    actualizarLista();

                } else {
                    // El documento no tiene un subdocumento "task"
                }
            } else {
                // El documento no existe

            }
        }).addOnFailureListener(e -> {
            // Manejar el caso de error si la operación no es exitosa
            e.printStackTrace();
        });

        //Datos Placeholder para inicializar


    }

    public void actualizarMonster(){
        tvNombre.setText(mA.getNombre());
        tvNivel.setText("Nivel: "+mA.getNivel());
        if (mA.getNivel() < 4){
            tvExp.setText("Exp: "+mA.getExp()+"/"+5*mA.getNivel());
        }else{
            tvExp.setText("Exp: "+mA.getExp());
        }

        int lvl = mA.getNivel()-1;//Para traer la imagen por id
        //imgMonster.setImageResource(getResources().getIdentifier(mA.getEvos(), "drawable",getPackageName()));
    }

    public void actualizarLista(){

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

    private void completarTarea(int id){
        Task task = tasks.get(id-1);
        task.setLista(true);
        mA.subirNivel(task.getDificultad());

        actualizarMonster();
        actualizarLista();
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
                if(!userInput.equals("")){
                    mA.setNombre(userInput);
                    actualizarMonster();
                }

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
        View formView = getLayoutInflater().inflate(R.layout.activity_editar_nombre, null);
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

                if(seleccion > 0 && !titulo.equals("")){
                    tasks.add(new Task(titulo, seleccion, false));

                    actualizarLista();
                }

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
}


