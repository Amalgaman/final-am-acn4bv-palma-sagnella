package com.example.crecimonstruo.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.crecimonstruo.R;
import com.example.crecimonstruo.viewModels.LoginViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
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

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar el ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

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
        RadioButton rb4 = findViewById(R.id.radio_select4);
        RadioButton rb5 = findViewById(R.id.radio_select5);
        RadioButton rb6 = findViewById(R.id.radio_select6);
        RadioButton rb7 = findViewById(R.id.radio_select7);
        RadioButton rb8 = findViewById(R.id.radio_select8);
        RadioButton rb9 = findViewById(R.id.radio_select9);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonRegistro = (Button)findViewById(R.id.buttonRegistro);
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String seleccion;
                List<String> evos = new ArrayList<>();

                if (rb1.isChecked()) {
                    seleccion = "Agumon";
                    evos = Arrays.asList("Agumon", "Greymon", "Metal_Greymon", "War_Greymon");
                } else if (rb2.isChecked()) {
                    seleccion = "Gabumon";
                    evos = Arrays.asList("Gabumon", "Garurumon", "Were_Garurumon", "Metal_Garurumon");;
                } else if (rb3.isChecked()) {
                    seleccion = "Patamon";
                    evos = Arrays.asList("Patamon", "Angemon", "Holy_Angemon", "Seraphimon");
                } else if (rb4.isChecked()) {
                    seleccion = "Devimon";
                    evos = Arrays.asList("Pico_Devimon", "Devimon", "Vamdemon", "Belial_Vamdemon");
                } else if (rb5.isChecked()) {
                    seleccion = "Palmon";
                    evos = Arrays.asList("Palmon", "Togemon", "Lilimon", "Rosemon");
                } else if (rb6.isChecked()) {
                    seleccion = "Tentomon";
                    evos = Arrays.asList("Tentomon", "Kabuterimon", "Atlur_Kabuterimon_(Red)", "Herakle_Kabuterimon");
                } else if (rb7.isChecked()) {
                    seleccion = "Piyomon";
                    evos = Arrays.asList("Piyomon", "Birdramon", "Garudamon", "Hououmon");
                } else if (rb8.isChecked()) {
                    seleccion = "Gomamon";
                    evos = Arrays.asList("Gomamon", "Ikkakumon", "Zudomon", "Vikemon");
                } else if (rb9.isChecked()) {
                    seleccion = "Hagurumon";
                    evos = Arrays.asList("Hagurumon", "Guardromon", "Andromon", "Hi_Andromon");
                } else {
                    seleccion = "";
                }

                if( !seleccion.equals("") && !emailRegistro.getText().equals("") && !passwordRegistro.getText().equals("") && !nombreRegistro.getText().equals("")){

                    List<String> finalEvos1 = evos;
                    loginViewModel.registerUser(emailRegistro.getText().toString(), passwordRegistro.getText().toString(), nombreRegistro.getText().toString(), seleccion, evos,
                            new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registro exitoso
                                        loginViewModel.saveUserData(task.getResult().getUser().getEmail(), nombreRegistro.getText().toString(), seleccion, finalEvos1);
                                        sendEmailVerification(task.getResult().getUser());
                                        mostrarMain(task.getResult().getUser().getEmail());
                                    } else {
                                        // Mostrar error de registro
                                        Exception exception = task.getException();
                                        if (exception instanceof FirebaseAuthException) {
                                            FirebaseAuthException authException = (FirebaseAuthException) exception;
                                            String errorCode = authException.getErrorCode();
                                            String errorMessage = authException.getMessage();
                                            showErrorRegistro(errorCode, errorMessage);
                                        }
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

                    loginViewModel.loginUser(emailLogin.getText().toString(), passwordLogin.getText().toString(),
                            new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Inicio de sesión exitoso
                                        mostrarMain(task.getResult().getUser().getEmail());
                                    } else {
                                        // Mostrar error de inicio de sesión
                                        Exception exception = task.getException();
                                        if (exception instanceof FirebaseAuthException) {
                                            FirebaseAuthException authException = (FirebaseAuthException) exception;
                                            String errorCode = authException.getErrorCode();
                                            String errorMessage = authException.getMessage();
                                            showErrorRegistro(errorCode, errorMessage);
                                        }
                                    }
                                }
                            });
                }
            }
        });

    }

    private void mostrarMain(String email){
        Intent intent = new Intent(this, HomeActivity.class).putExtra("email", email);
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

    // Función para enviar la verificación por correo electrónico
    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Email de verificación enviado correctamente
                            Toast.makeText(LoginActivity.this, "Se ha enviado un correo de verificación", Toast.LENGTH_SHORT).show();
                            // Puedes redirigir a la actividad de inicio de sesión o realizar otras acciones
                        } else {
                            // Error al enviar el correo de verificación
                            Toast.makeText(LoginActivity.this, "Error al enviar el correo de verificación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}