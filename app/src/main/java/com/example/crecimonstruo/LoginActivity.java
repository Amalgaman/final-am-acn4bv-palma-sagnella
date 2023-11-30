package com.example.crecimonstruo;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;


public class LoginActivity extends AppCompatActivity {
    private EditText emailLogin;
    private EditText passwordLogin;
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

        emailLogin = (EditText) findViewById(R.id.emailLogin);
        passwordLogin = (EditText) findViewById(R.id.passwordLogin);

        buttonLogin = (Button)findViewById(R.id.buttonLogin);
        buttonRegistro = (Button)findViewById(R.id.buttonRegistro);
        buttonRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( !emailLogin.getText().equals("") && !passwordLogin.getText().equals("")){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailLogin.getText().toString(), passwordLogin.getText().toString())
                            .addOnCompleteListener((OnCompleteListener<com.google.firebase.auth.AuthResult>) task -> {
                                if (task.isSuccessful()) {
                                    mostrarMain();
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
                                    mostrarMain();
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

    private void mostrarMain(){
        Intent intent = new Intent(this, MainActivity.class);
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