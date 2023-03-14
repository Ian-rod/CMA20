package com.example.cma20;

import androidx.annotation.NonNull;

public class Employee {
    String name,role,salary,qualification,telephone,address,email,status;
//    int image;
    //configure Image
//email
    public Employee(String name, String role, String salary, String qualification, String telephone, String address, String email ,String status ) {
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.qualification = qualification;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        this.status=status;
    }

    @NonNull
    @Override
    public String toString() {
        return role;
    }
}
