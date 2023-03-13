package com.example.cma20;



import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Tasks extends AppCompatActivity {
TextInputLayout taskN;
TextInputLayout taskD;
TextInputLayout taskA;
DrawerLayout drawerLayout;
NavigationView navigationView;

ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        ActionBar appbar=getSupportActionBar();
        appbar.setTitle("\t Task Page");
        appbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000cd")));

    }
    public void addTask(View view)
    {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        taskN= findViewById(R.id.TaskName);
        taskD= findViewById(R.id.TaskDescription);
        taskA= findViewById(R.id.TaskAssign);
        EditText taskName=taskN.getEditText();
        EditText taskDescription=taskD.getEditText();
        EditText taskAssigned=taskA.getEditText();
  if(taskDescription.getText().toString().isEmpty()||taskName.getText().toString().isEmpty()||taskAssigned.getText().toString().isEmpty())
  {
      Toast.makeText(getApplicationContext(), "Please fill out all the fields",
              Toast.LENGTH_LONG).show();
  }
  else{
        HashMap<String,String> data=new HashMap<>();
        data.put("Task Description",taskDescription.getText().toString());
        data.put("Task Name",taskName.getText().toString());
        data.put("status","incomplete");
        data.put("Assigned to",taskAssigned.getText().toString());
        //send task to firebase
        database.collection("Tasks")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "Task successfully added",
                                Toast.LENGTH_LONG).show();
                        taskName.setText("");
                        taskDescription.setText("");
                        taskAssigned.setText("");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error adding Task",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }}
}