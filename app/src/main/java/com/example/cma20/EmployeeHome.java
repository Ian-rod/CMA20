package com.example.cma20;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.cma20.databinding.ActivityEmployeeHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class EmployeeHome extends AppCompatActivity {
    ActivityEmployeeHomeBinding binding;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    ArrayList<HashMap<String,String>> taskdata= new ArrayList<>();
    ArrayList<String> updateID=new ArrayList<>();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEmployeeHomeBinding.inflate(getLayoutInflater());
        ActionBar appbar=getSupportActionBar();
        appbar.setTitle("\t \t \tLoading...... ");
        appbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000cd")));
        setContentView(binding.getRoot());
        drawerLayout=findViewById(R.id.Employeedrawer);
        navigationView=(NavigationView) findViewById(R.id.EmployeeNavigationView);
        navigationView.bringToFront();
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        appbar.setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.EmployeeDetails:
                        Intent edetails=new Intent(getApplicationContext(), EDetails.class);
                        startActivity(edetails);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        //log out
                        break;
                    case  R.id.EmployeeTasks:
                        //employee page
                        Intent employeehome=new Intent(getApplicationContext(), EmployeeHome.class);
                        startActivity(employeehome);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
        ArrayList<TaskClass> taskList=new ArrayList<TaskClass>();
        database.collection("Tasks")//remove Mr mishima here
                .whereEqualTo("Assigned to", "jipanchimishima@gmail.com")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            appbar.setTitle("\t \tYour Tasks");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                TaskClass assignedTasks=new TaskClass(document.getData().get("Task Name").toString(),document.getData().get("Task Description").toString(),document.getData().get("status").toString());
                                HashMap<String,String> data=new HashMap<>();
                                data.put("Task Description",document.getData().get("Task Description").toString());
                                data.put("Task Name",document.getData().get("Task Name").toString());
                                data.put("Assigned to","jipanchimishima@gmail.com");
                                taskdata.add(data);
                                taskList.add(assignedTasks);
                                updateID.add(document.getId());
                            }
                            TaskAdapter adapter = new TaskAdapter(
                                    EmployeeHome.this, taskList
                            );
                            binding.EmployeeTaskview.setAdapter(adapter);
                            binding.EmployeeTaskview.setClickable(true);
                            binding.EmployeeTaskview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    AlertDialog alert=new AlertDialog.Builder(EmployeeHome.this).create();
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
}