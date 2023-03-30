package com.example.cma20;


import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EmployeeLogin extends Fragment {
    View myview;
    EditText password;
    EditText email;
    Button login;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.employeelogin,container,false);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        email=myview.findViewById(R.id.Eloginemail);
        password=myview.findViewById(R.id.Eloginpassword);
        login=myview.findViewById(R.id.Elogin);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()||password.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Please fill out all the fields",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    //log in as an employee
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserDetails.CurrentUser=user.getEmail();
                                        //check if user is in the fired list
                                        DocumentReference docRef = database.collection("Fired").document(user.getEmail());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                        user.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getContext(), "Sorry but you were Fired AMIGO ",
                                                                                    Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        Toast.makeText(getContext(), "Sign in success ",
                                                                Toast.LENGTH_LONG).show();
                                                        UserDetails.CurrentUser=user.getEmail();
                                                        UserDetails.UID=user.getUid();
                                                        //render ui based on current user status
//                                                       updateUI(user);
                                                        Intent EmployeePage=new Intent(getContext(), EmployeeHome.class);
                                                        startActivity(EmployeePage);
                                                    }
                                                } else {
                                                    Log.d(TAG, "get failed with ", task.getException());
                                                }
                                            }
                                        });

                                    } else {
                                        // If sign in fails, display a message to the user
                                        Toast.makeText(getContext(), "Authentication failed. "+task.getException(),
                                                Toast.LENGTH_LONG).show();
//                                            updateUI(null);
                                    }
                                }
                            });
                }
            }
        });

        return myview;
    }
}
