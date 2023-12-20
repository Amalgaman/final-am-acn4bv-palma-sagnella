package com.example.crecimonstruo.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.crecimonstruo.models.Monster;
import com.example.crecimonstruo.models.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Monster> monsterLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Task>> tasksLiveData = new MutableLiveData<>();
    private String email;
    private MutableLiveData<String> resultadoGuardadoLiveData = new MutableLiveData<>();

    // Método para cargar los datos iniciales desde Firestore
    public void loadData(String email) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        this.setUserEmail(email);

        // Lógica para cargar datos de Monster
        db.collection("users").document(email).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Map<String, Object> userData = documentSnapshot.getData();
                if (userData != null && userData.containsKey("monster")) {
                    Map<String, Object> monsterData = (Map<String, Object>) userData.get("monster");
                    Monster monster = new Monster(
                            (String) monsterData.get("nombre"),
                            (int) ((long) monsterData.get("exp")),
                            (int) ((long) monsterData.get("lvl")),
                            (List<String>) monsterData.get("evos")
                    );
                    monsterLiveData.postValue(monster);
                }
                if (userData != null && userData.containsKey("task")) {
                    List<Object> taskData = (List<Object>) userData.get("task");
                    List<Task> tasks = new LinkedList<Task>();

                    for (Object task : taskData) {
                        Map<String, Object> aux = (Map<String, Object>) task;
                        Task aux2 = new Task((String) aux.get("titulo"), ((Long) aux.get("dificultad")).intValue(), (boolean) aux.get("lista"));
                        tasks.add(aux2);
                    }
                    tasksLiveData.postValue(tasks);
                    }

                }

        });


    }

    // Método para actualizar el nombre del Monster
    public void updateMonsterName(String newName) {
        Monster currentMonster = monsterLiveData.getValue();
        if (currentMonster != null) {
            currentMonster.setNombre(newName);
            monsterLiveData.postValue(currentMonster);


        }
    }

    // Método para agregar una nueva tarea
    public void addTask(Task newTask) {
        List<Task> currentTasks = tasksLiveData.getValue();
        if (currentTasks != null) {
            currentTasks.add(newTask);
            tasksLiveData.postValue(currentTasks);

            // Aquí puedes agregar lógica adicional según sea necesario

        }
    }

    // Método para completar una tarea
    public void completarTarea(int id) {
        Monster mA = monsterLiveData.getValue();
        List<Task> tasks = tasksLiveData.getValue();

        if (mA != null && tasks != null && id >= 1 && id <= tasks.size()) {
            Task task = tasks.get(id - 1);
            task.setLista(true);
            mA.subirNivel(task.getDificultad());

            // Actualizar LiveData para notificar cambios
            monsterLiveData.setValue(mA);
            tasksLiveData.setValue(tasks);

        }
    }

    // Método para manejar la lógica de la entrada de nombre
    public void handleInputNombre(String userInput) {
        Monster mA = monsterLiveData.getValue();

        if (mA != null && !userInput.isEmpty()) {
            mA.setNombre(userInput);

            // Actualizar LiveData para notificar cambios
            monsterLiveData.setValue(mA);
        }
    }

    // Método para manejar la lógica de añadir nueva tarea
    public void addNewTask(String titulo, int seleccion) {
        List<Task> tasks = tasksLiveData.getValue();

        if (tasks != null && seleccion > 0 && !titulo.isEmpty()) {
            Task newTask = new Task(titulo, seleccion, false);
            tasks.add(newTask);

            // Actualizar LiveData para notificar cambios
            tasksLiveData.setValue(tasks);
        }
    }

    public void guardarData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> userData = new HashMap<>();
        Map<String, Object> monsterData = new HashMap<>();
        List<Map<String, Object>> taskData = new ArrayList<>();

        for (Task tarea : tasksLiveData.getValue()) {
            Map<String, Object> tareaMap = new HashMap<>();
            tareaMap.put("titulo", tarea.getTitulo());
            tareaMap.put("dificultad", tarea.getDificultad());
            tareaMap.put("lista", tarea.isLista());
            taskData.add(tareaMap);
        }

        Monster mA = monsterLiveData.getValue();
        if (mA != null) {
            monsterData.put("nombre", mA.getNombre());
            monsterData.put("exp", mA.getExp());
            monsterData.put("evos", mA.getEvos());
            monsterData.put("lvl", mA.getNivel());

            userData.put("task", taskData);
            userData.put("monster", monsterData);

            db.collection("users").document(email).update(userData)
                    .addOnCompleteListener((OnCompleteListener<Void>) task -> {
                        if (task.isSuccessful()) {
                            resultadoGuardadoLiveData.postValue("OK");
                        } else {
                            // Error al agregar la lista de tareas
                            Exception exception = task.getException();
                            if (exception instanceof FirebaseAuthException) {
                                FirebaseAuthException authException = (FirebaseAuthException) exception;
                                resultadoGuardadoLiveData.postValue(authException.getErrorCode());
                            }
                        }

                    });
        }
    }

    // Métodos para obtener instancias de LiveData
    public LiveData<Monster> getMonsterLiveData() {
        return monsterLiveData;
    }

    public LiveData<List<Task>> getTasksLiveData() {
        return tasksLiveData;
    }

    public String getUserEmail() {
        return email;
    }

    public void setUserEmail(String userEmail) {
        this.email = userEmail;
    }
    public LiveData<String> getResultadoGuardadoLiveData() {
        return resultadoGuardadoLiveData;
    }
}
