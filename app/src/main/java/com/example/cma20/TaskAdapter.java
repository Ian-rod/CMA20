package com.example.cma20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<TaskClass> {
    public TaskAdapter(Context context, ArrayList<TaskClass> taskClassArrayList)
    {
        super(context,R.layout.task_item, taskClassArrayList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TaskClass taskClass =getItem(position);
        if(convertView==null)
        {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.task_item,parent,false);
        }
        //reference individual list items available
        TextView name=convertView.findViewById(R.id.EmployeeTaskName);
        TextView description=convertView.findViewById(R.id.EmployeeTaskDescription);
        TextView status=convertView.findViewById(R.id.taskstatus);
        //set item to each element in the list view
        name.setText(taskClass.taskname);
        description.setText(taskClass.taskdescription);
        status.setText(taskClass.status);
        return convertView;
    }
}
