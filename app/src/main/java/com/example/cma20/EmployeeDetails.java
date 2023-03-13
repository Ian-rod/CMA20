package com.example.cma20;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.example.cma20.databinding.ActivityEmployeeDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.Task;

import org.checkerframework.checker.units.qual.A;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmployeeDetails extends AppCompatActivity {
    ActivityEmployeeDetailsBinding binding;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar appbar=getSupportActionBar();
        appbar.setTitle("\t \tEmployee Details ");
        appbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000cd")));
        super.onCreate(savedInstanceState);
        binding= ActivityEmployeeDetailsBinding.inflate(getLayoutInflater());
        Intent details=getIntent();
        //render each detail to a view
        setContentView(binding.getRoot());
        TextView name=findViewById(R.id.EmployeeName);
        TextView role=findViewById(R.id.EmployeeRole);
        TextView salary=findViewById(R.id.EmployeeSalary);
        TextView qualification=findViewById(R.id.EmployeeQualification);
        TextView address=findViewById(R.id.EmployeeAddress);
        TextView telephone=findViewById(R.id.EmployeeTelephone);
        TextView email=findViewById(R.id.EmployeeEmail);
        //TextView status=findViewById(R.id.EmployeeName);

        //Assign passed values
        name.setText(details.getStringExtra("EmployeeName"));
        role.setText(details.getStringExtra("EmployeeRole"));
        salary.setText(details.getStringExtra("EmployeeSalary"));
        qualification.setText(details.getStringExtra("EmployeeQualification"));
        address.setText(details.getStringExtra("EmployeeAddress"));
        telephone.setText(details.getStringExtra("EmployeeTelephone"));
        email.setText(details.getStringExtra("EmployeeEmail"));
        createTask();

    }
    public void createTask()
    {
        //create the tasks table
        ArrayList<TaskClass> taskList=new ArrayList<TaskClass>();
        database.collection("Tasks")//remove Mr fury here
                .whereEqualTo("Assigned to", "bryanfury@gmail.com")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TaskClass assignedTasks=new TaskClass(document.getData().get("Task Name").toString(),document.getData().get("Task Description").toString(),document.getData().get("status").toString());
                                taskList.add(assignedTasks);
                            }
                            TaskAdapter adapter = new TaskAdapter(
                                    EmployeeDetails.this, taskList
                            );
                            binding.taskview.setAdapter(adapter);
                            binding.taskview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    AlertDialog alert=new AlertDialog.Builder(EmployeeDetails.this).create();
                                    alert.setTitle("Task Completion");
                                    alert.setMessage("Please confirm that this task has been completed?");
                                    alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
//                                                    HashMap<String,String> data=new HashMap<>();
//                                                    d
//                                                    database.collection("cities").document("LA")
//                                                            .set(data)
//                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                                @Override
//                                                                public void onSuccess(Void aVoid) {
//                                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
//                                                                }
//                                                            })
//                                                            .addOnFailureListener(new OnFailureListener() {
//                                                                @Override
//                                                                public void onFailure(@NonNull Exception e) {
//                                                                    Log.w(TAG, "Error writing document", e);
//                                                                }
//                                                            });
                                                    dialog.dismiss();
                                                }
                                            }
                                    );
                                    alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }
                                    );
                                    alert.show();
                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    public void FireEmployee(View view)
    {
        //Remove user from active user list
        //place them in a fired employees section
        AlertDialog alert=new AlertDialog.Builder(EmployeeDetails.this).create();
        alert.setTitle("ALERT");
        alert.setMessage("Are you sure you want to fire this employee?");
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //set status task to complete

                        dialog.dismiss();
                    }
                }
        );
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
        alert.show();
    }

}