package com.example.cma20;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.example.cma20.databinding.ActivityEmployeePageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ManagerHome extends AppCompatActivity{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;
    ActivityEmployeePageBinding binding;
    EmployeeAdapter adapter;
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
        binding=ActivityEmployeePageBinding.inflate(getLayoutInflater());
        ActionBar appbar=getSupportActionBar();
        appbar.setTitle("\t \t \tLoading...... ");
        appbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000cd")));
        getData();
        setContentView(binding.getRoot());
        drawerLayout=findViewById(R.id.drawerlayout);
        navigationView=(NavigationView) findViewById(R.id.NavigationView);
        navigationView.bringToFront();
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.menu_open,R.string.menu_close);
        //add toggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        appbar.setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.addTasks:
                        Intent taskpage=new Intent(getApplicationContext(), Tasks.class);
                        startActivity(taskpage);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        //log out
                        break;
                    case  R.id.EmployeeList:
                        //employee page
                        Intent employeepage=new Intent(getApplicationContext(), ManagerHome.class);
                        startActivity(employeepage);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.checkTasks:
                        //check the list of tasks
                        Intent checktaskpage=new Intent(getApplicationContext(),TaskList.class);
                        startActivity(checktaskpage);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }
                return true;
            }
        });
    }


    public void getData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        ArrayList<Employee> employeeList=new ArrayList<Employee>();
        //fetch data from the users collection
        database.collection("Employees")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ActionBar appbar=getSupportActionBar();
                            appbar.setTitle("\tList of employees ");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Employee employee=new Employee(document.getData().get("Name").toString(),document.getData().get("Role").toString(),document.getData().get("Monthly Salary").toString(),document.getData().get("Qualification").toString(),document.getData().get("Telephone").toString(),document.getData().get("Address").toString(),document.getId(),document.getData().get("status").toString());
                                employeeList.add(employee);
                            }
                            adapter= new EmployeeAdapter(
                                    ManagerHome.this, employeeList
                            );
                            binding.listview.setAdapter(adapter);
                            binding.listview.setClickable(true);
                            binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    //Pass data to the new page
                                    Intent detailspage=new Intent(getApplicationContext(),EmployeeDetails.class);
                                    detailspage.putExtra("EmployeeName", employeeList.get(position).name);
                                    detailspage.putExtra("EmployeeRole", employeeList.get(position).role);
                                    detailspage.putExtra("EmployeeStatus", employeeList.get(position).status);
                                    detailspage.putExtra("EmployeeSalary", employeeList.get(position).salary);
                                    detailspage.putExtra("EmployeeQualification", employeeList.get(position).qualification);
                                    detailspage.putExtra("EmployeeEmail", employeeList.get(position).email);
                                    detailspage.putExtra("EmployeeTelephone", employeeList.get(position).telephone);
                                    detailspage.putExtra("EmployeeAddress", employeeList.get(position).address);
                                    startActivity(detailspage);


                                }
                            });
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });


    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item=menu.findItem(R.id.search);
        SearchView searchView=(SearchView) item.getActionView();
        searchView.setQueryHint("Search an employee");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //when user presses enter
                    adapter.getFilter().filter(query);

                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                //when text changes
                //Running a test run by overriding the to string method
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }
}