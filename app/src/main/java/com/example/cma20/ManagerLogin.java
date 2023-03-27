package com.example.cma20;


import android.content.Intent;
import android.os.Bundle;
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

public class ManagerLogin extends Fragment {
    View myview;
    EditText password;
    EditText email;
    Button login;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.managerlogin,container,false);
        login=myview.findViewById(R.id.Mlogin);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        email=myview.findViewById(R.id.Mloginemail);
        password=myview.findViewById(R.id.Mloginpassword);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()||password.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Please fill out all the fields",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    //log in as a manager
                    mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserDetails.CurrentUser=user.getEmail();
                                        UserDetails.UID=user.getUid();
                                        //navigate to manager home if the user is actually a manager else mrushe kwa employee
                                        //heihachi is the only manager
                                        DocumentReference docRef = database.collection("Managers").document(email.getText().toString());
                                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    if (document.exists()) {
                                                        Toast.makeText(getContext(), "Welcome Manager ",
                                                                Toast.LENGTH_LONG).show();
                                                        Intent ManagerPage=new Intent(getContext(), ManagerHome.class);
                                                        startActivity(ManagerPage);
                                                    } else {
                                                        Toast.makeText(getContext(), "Not a Manager.....Logging in as an Employee ",
                                                                Toast.LENGTH_LONG).show();
                                                        Intent EmployeePage=new Intent(getContext(), EmployeeHome.class);
                                                        startActivity(EmployeePage);
                                                    }
                                                } else {
                                                    Toast.makeText(getContext(), "Log in failed  "+task.getException(),
                                                            Toast.LENGTH_LONG).show();

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
