package com.example.cma20;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EDetails extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar appbar=getSupportActionBar();
        appbar.setTitle("\t \t Your Details ");
        appbar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0000cd")));
        appbar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edetails);
        TextView name=findViewById(R.id.EName);
        TextView role=findViewById(R.id.ERole);
        TextView salary=findViewById(R.id.ESalary);
        TextView qualification=findViewById(R.id.EQualification);
        TextView address=findViewById(R.id.EAddress);
        TextView telephone=findViewById(R.id.ETelephone);
        TextView email=findViewById(R.id.EEmail);
        //get employee data from firebase
        DocumentReference docRef = database.collection("Employees").document(UserDetails.CurrentUser);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        //set data
                        name.setText(document.getData().get("Name").toString());
                        role.setText(document.getData().get("Role").toString());
                        salary.setText(document.getData().get("Monthly Salary").toString());
                        qualification.setText(document.getData().get("Qualification").toString());
                        address.setText(document.getData().get("Address").toString());
                        telephone.setText(document.getData().get("Telephone").toString());
                        email.setText(document.getId());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }
}