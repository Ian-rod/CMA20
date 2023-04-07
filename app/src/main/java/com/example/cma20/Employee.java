package com.example.cma20;

import androidx.annotation.NonNull;

public class Employee {
    String name,role,salary,qualification,telephone,address,email,status,bank,acc;
//    int image;
    //configure Image
//email
    public Employee(String name, String role, String salary, String qualification, String telephone, String address, String email ,String status,String bank,String acc) {
        this.name = name;
        this.role = role;
        this.salary = salary;
        this.qualification = qualification;
        this.telephone = telephone;
        this.address = address;
        this.email = email;
        this.status=status;
        this.bank=bank;
        this.acc=acc;
    }

    @NonNull
    @Override
    public String toString() {
        return role;
    }
}
