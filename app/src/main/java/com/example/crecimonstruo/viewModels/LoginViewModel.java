package com.example.crecimonstruo.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginViewModel extends ViewModel {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Métodos relacionados con la lógica de registro y inicio de sesión

    public void registerUser(String email, String password, String nombre, String seleccion, List<String> evos, OnCompleteListener<AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    public void loginUser(String email, String password, OnCompleteListener<com.google.firebase.auth.AuthResult> onCompleteListener) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(onCompleteListener);
    }

    public void saveUserData(String userEmail, String nombre, String seleccion, List<String> evos) {
        Map<String, Object> userData = new HashMap<>();
        Map<String, Object> monsterData = new HashMap<>();

        monsterData.put("nombre", seleccion);
        monsterData.put("exp", 0);
        monsterData.put("lvl", 1);
        monsterData.put("evos", evos);
        userData.put("nombre", nombre);
        userData.put("monster", monsterData);

        db.collection("users").document(userEmail).set(userData);
    }

    // Otros métodos y variables necesarios para la lógica de la vista


}
