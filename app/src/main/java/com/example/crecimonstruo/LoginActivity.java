package com.example.crecimonstruo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {
    private EditText emailLogin;
    private EditText passwordLogin;
    private EditText emailRegistro;
    private EditText passwordRegistro;
    private EditText nombreRegistro;
    private Button buttonLogin;
    private Button buttonRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inicio
        inicio();
    }

    private void inicio(){

        FirebaseFirestore db =FirebaseFirestore.getInstance();

        emailLogin = (EditText) findViewById(R.id.emailLogin);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);
        emailRegistro = (EditText) findViewById(R.id.emailRegistro);
        passwordRegistro = (EditText) findViewById(R.id.passwordRegistro);
        nombreRegistro = (EditText) findViewById(R.id.nombreRegistro);

        RadioButton rb1 = findViewById(R.id.radio_select1);
        RadioButton rb2 = findViewById(R.id.radio_select2);
        RadioButton rb3 = findViewById(R.id.radio_select3);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonRegistro = (Button)findViewById(R.id.buttonRegistro);
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String seleccion;
                List<String> evos = new ArrayList<>();

                if (rb1.isChecked()) {
                    seleccion = "agumon";
                    evos = Arrays.asList("agumon", "greymon", "metalgreymon", "wargreymon");
                } else if (rb2.isChecked()) {
                    seleccion = "gabumon";
                    evos = Arrays.asList("gabumon", "garurumon", "weregarurumon", "metalgarurumon");;
                } else if (rb3.isChecked()) {
                    seleccion = "patamon";
                    evos = Arrays.asList("patamon", "angemon", "magnaangemon", "seraphimon");
                } else {
                    seleccion = "";
                }

                if( !seleccion.equals("") && !emailRegistro.getText().equals("") && !passwordRegistro.getText().equals("") && !nombreRegistro.getText().equals("")){

                    List<String> finalEvos = evos;
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailRegistro.getText().toString(), passwordRegistro.getText().toString())
                            .addOnCompleteListener((OnCompleteListener<com.google.firebase.auth.AuthResult>) task -> {
                                if (task.isSuccessful()) {

                                    Map<String, Object> userData = new HashMap<>();
                                    Map<String, Object> monsterData = new HashMap<>();

                                    monsterData.put("nombre", seleccion);
                                    monsterData.put("exp", 0);
                                    monsterData.put("lvl", 1);
                                    monsterData.put("evos", finalEvos);
                                    userData.put("nombre", nombreRegistro.getText().toString());
                                    userData.put("monster", monsterData);

                                    db.collection("users").document(task.getResult().getUser().getEmail()).set(userData);

                                    mostrarMain(task.getResult().getUser().getEmail());
                                }else{
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseAuthException) {
                                        FirebaseAuthException authException = (FirebaseAuthException) exception;
                                        String errorCode = authException.getErrorCode();
                                        String errorMessage = authException.getMessage();

                                        showErrorRegistro(errorCode, errorMessage);

                                    }
                                }
                            });
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( !emailLogin.getText().equals("") && !passwordLogin.getText().equals("")){

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(emailLogin.getText().toString(), passwordLogin.getText().toString())
                            .addOnCompleteListener((OnCompleteListener<com.google.firebase.auth.AuthResult>) task -> {
                                if (task.isSuccessful()) {
                                    mostrarMain(task.getResult().getUser().getEmail());
                                }else{
                                    Exception exception = task.getException();
                                    if (exception instanceof FirebaseAuthException) {
                                        FirebaseAuthException authException = (FirebaseAuthException) exception;
                                        String errorCode = authException.getErrorCode();
                                        String errorMessage = authException.getMessage();

                                            showErrorRegistro(errorCode, errorMessage);

                                    }
                                }
                            });
                }
            }
        });

    }

    private void mostrarMain(String email){
        Intent intent = new Intent(this, MainActivity.class).putExtra("email", email);
        startActivity(intent);
    }

    private void showErrorRegistro(String code, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(code)
                .setMessage(message)
                .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // START THE GAME!
                    }
                })
                .show();
    }
}