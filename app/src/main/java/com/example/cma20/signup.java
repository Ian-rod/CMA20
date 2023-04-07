package com.example.cma20;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Signup extends Fragment {
    View myview;
    EditText name;
    EditText email;
    EditText password;
    EditText Cpassword;
    EditText Role;
    EditText Telephone;
    EditText qualification;
    EditText address;
    Spinner Banks;
    Button signup;
    ViewPager pager;
    EditText acc;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.signup,container,false);
        signup=myview.findViewById(R.id.signup);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        Banks=myview.findViewById(R.id.spinner1);
        String AvailableBanks[]={"Standard Chartered","Equity"};
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_dropdown_item,AvailableBanks);
        Banks.setAdapter(adapter);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get input fields ,validate them
                name=myview.findViewById(R.id.name_edit_text);
                email=myview.findViewById(R.id.email_edit_text);
                password=myview.findViewById(R.id.password_edit_text);
                Cpassword=myview.findViewById(R.id.confirm_password_edit_text);
                Role=myview.findViewById(R.id.role_edit_text);
                Telephone=myview.findViewById(R.id.telephone_edittext);
                qualification=myview.findViewById(R.id.qualification_edittext);
                address=myview.findViewById(R.id.address_edittext);
                acc=myview.findViewById(R.id.ACNO);
                //check empty
                if(name.getText().toString().isEmpty()||email.getText().toString().isEmpty()||password.getText().toString().isEmpty()||Cpassword.getText().toString().isEmpty()||address.getText().toString().isEmpty()||Role.getText().toString().isEmpty()||Telephone.getText().toString().isEmpty()||qualification.getText().toString().isEmpty()||acc.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Please fill out all the fields",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    if(!password.getText().toString().equals(Cpassword.getText().toString()))
                    {
                        Toast.makeText(getContext(), "Password and confirmed password don't match",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        //sign up a user'
                        System.out.println("Email is "+email.getText().toString());
                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Toast.makeText(getContext(), "Sign up success ",
                                                    Toast.LENGTH_LONG).show();
                                            FirebaseUser user = mAuth.getCurrentUser();
                                          //send user details to database
                                            Map<String,String> data=new HashMap();
                                            data.put("Address",address.getText().toString());
                                            data.put("Monthly Salary","Ksh 5");
                                            data.put("Name",name.getText().toString());
                                            data.put("Qualification",qualification.getText().toString());
                                            data.put("Role",Role.getText().toString());
                                            data.put("Telephone",Telephone.getText().toString());
                                            data.put("status","Free");
                                            data.put("Bank",Banks.getSelectedItem().toString());
                                            data.put("AccNo",acc.getText().toString());
                                            database.collection("Employees").document(email.getText().toString())
                                                    .set(data)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(getContext(), "Credentials Saved successfully. ",
                                                                    Toast.LENGTH_SHORT).show();
                                                            pager = myview.getRootView().findViewById(R.id.view_pager);
                                                            pager.setCurrentItem(1);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getContext(), "Error saving credentials ",
                                                                    Toast.LENGTH_SHORT).show();
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
            }
        });
        return myview;

    }
}
