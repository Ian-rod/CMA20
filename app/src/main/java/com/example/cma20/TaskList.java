package com.example.cma20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import com.example.cma20.databinding.ActivityTaskListBinding;
import com.example.cma20.databinding.ActivityTasksBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class TaskList extends AppCompatActivity {
    ActivityTaskListBinding binding;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar appbar=getSupportActionBar();
        appbar.setTitle("\t \t \tLoading...... ");
        appbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000cd")));
        binding= ActivityTaskListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ArrayList<TaskClass> taskList=new ArrayList<TaskClass>();
        database.collection("Tasks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            appbar.setTitle("\t \tList of tasks ");
                            Toast.makeText(getApplicationContext(), "Tasks fetched successfully",
                                    Toast.LENGTH_LONG).show();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                TaskClass assignedTasks=new TaskClass(document.getData().get("Task Name").toString(),document.getData().get("Task Description").toString(),document.getData().get("status").toString());
                                taskList.add(assignedTasks);
                            }
                            TaskAdapter adapter = new TaskAdapter(
                                    TaskList.this, taskList
                            );
                            binding.tasklistview.setAdapter(adapter);
                            binding.tasklistview.setClickable(false);
                        } else {
                            Toast.makeText(getApplicationContext(), "Tasks fetched unsuccessfully",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}