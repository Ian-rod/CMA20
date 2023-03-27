package com.example.cma20;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class EmployeeLogin extends Fragment {
    View myview;
    EditText password;
    EditText email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.employeelogin,container,false);
        email=myview.findViewById(R.id.Eloginemail);
        password=myview.findViewById(R.id.Eloginpassword);
        if(!(email.getText().toString()==""||password.getText().toString()==""))
        {
            Toast.makeText(getContext(), "Please fill out all the fields",
                    Toast.LENGTH_LONG).show();
        }
        else {
            //log in as an employee
        }
        return myview;
    }
}
