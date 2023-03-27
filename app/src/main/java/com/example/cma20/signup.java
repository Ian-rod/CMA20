package com.example.cma20;


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
import androidx.viewpager.widget.ViewPager;

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
    Button signup;
    ViewPager pager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myview=inflater.inflate(R.layout.signup,container,false);
        signup=myview.findViewById(R.id.signup);
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
                //check empty
                if(name.getText().toString()==""||email.getText().toString()==""||password.getText().toString()==""||Cpassword.getText().toString()==""||address.getText().toString()==""||Role.getText().toString()==""||Telephone.getText().toString()==""||qualification.getText().toString()=="")
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
                        //sign up a user

                        //Take user to log in page
                        pager=myview.findViewById(R.id.view_pager);
                        pager.setCurrentItem(1);
                    }
                }
            }
        });
        return myview;

    }
}
