package com.example.cma20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class EmployeeAdapter extends ArrayAdapter<Employee> {
    public EmployeeAdapter(Context context, ArrayList<Employee> employeeArrayList)
    {
        super(context,R.layout.list_item,employeeArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Employee employee=getItem(position);
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        //reference individual list items available
        ImageView image=convertView.findViewById(R.id.image);
        TextView name=convertView.findViewById(R.id.name);
        TextView role=convertView.findViewById(R.id.role);
        TextView status=convertView.findViewById(R.id.status);
        //set item to each element in the list view
        name.setText(employee.name);
        role.setText(employee.role);
        status.setText(employee.status);
        return convertView;
    }
}
