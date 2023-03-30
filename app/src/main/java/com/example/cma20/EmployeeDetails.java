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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cma20.databinding.ActivityEmployeeDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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
    Intent details;
    ArrayList<HashMap<String,String>> taskdata= new ArrayList<>();
    ArrayList<String> updateID=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar appbar=getSupportActionBar();
        appbar.setTitle("\t \tEmployee Details ");
        appbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000cd")));
        super.onCreate(savedInstanceState);
        binding= ActivityEmployeeDetailsBinding.inflate(getLayoutInflater());
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
        details=getIntent();
        name.setText("Name: "+details.getStringExtra("EmployeeName"));
        role.setText("Role: "+details.getStringExtra("EmployeeRole"));
        salary.setText("Monthly Salary: "+details.getStringExtra("EmployeeSalary"));
        qualification.setText("Qualification: "+details.getStringExtra("EmployeeQualification"));
        address.setText("Physical Address: "+details.getStringExtra("EmployeeAddress"));
        telephone.setText(details.getStringExtra("EmployeeTelephone"));
        email.setText(details.getStringExtra("EmployeeEmail"));
        createTask();

    }
    public void createTask()
    {
        //create the tasks table
        ArrayList<TaskClass> taskList=new ArrayList<TaskClass>();
        database.collection("Tasks")//remove Mr fury here
                .whereEqualTo("Assigned to", details.getStringExtra("EmployeeEmail"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TaskClass assignedTasks=new TaskClass(document.getData().get("Task Name").toString(),document.getData().get("Task Description").toString(),document.getData().get("status").toString());
                                HashMap<String,String> data=new HashMap<>();
                                data.put("Task Description",document.getData().get("Task Description").toString());
                                data.put("Task Name",document.getData().get("Task Name").toString());
                                data.put("Assigned to",details.getStringExtra("EmployeeEmail"));
                                taskdata.add(data);
                                taskList.add(assignedTasks);
                                updateID.add(document.getId());
                            }
                            TaskAdapter adapter = new TaskAdapter(
                                    EmployeeDetails.this, taskList
                            );
                            binding.taskview.setAdapter(adapter);
                            binding.taskview.setClickable(true);
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
                                                taskdata.get(position).put("status","Complete");
                                                    database.collection("Tasks").document(updateID.get(position))
                                                            .set(taskdata.get(position))
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(getApplicationContext(), "Task status update successful",
                                                                            Toast.LENGTH_LONG).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(getApplicationContext(), "Task status update unsuccessful",
                                                                            Toast.LENGTH_LONG).show();
                                                                }
                                                            });
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
                        //Fire
                        //capture details move them to fired directory
                        Map<String,String> data=new HashMap<>();
                        data.put("Address",details.getStringExtra("EmployeeAddress"));
                        data.put("Monthly Salary",details.getStringExtra("EmployeeSalary"));
                        data.put("Name",details.getStringExtra("EmployeeName"));
                        data.put("Qualification",details.getStringExtra("EmployeeQualification"));
                        data.put("Role",details.getStringExtra("EmployeeRole"));
                        data.put("Telephone",details.getStringExtra("EmployeeTelephone"));
                        data.put("Status","Free");
                        database.collection("Fired").document(details.getStringExtra("EmployeeEmail"))
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //delete Employee details
                                        database.collection("Employees").document(details.getStringExtra("EmployeeEmail"))
                                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "EMPLOYEE FIRED SUCCESSFULLY",
                                                                Toast.LENGTH_LONG).show();

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Error Firing",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error Firing",
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
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