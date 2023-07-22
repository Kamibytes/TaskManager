package com.example.taskmanager;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskAdapter extends ArrayAdapter<Task> {
    private Context context;
    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
        this.context = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false);
        }

        ImageView priorityIcon = convertView.findViewById(R.id.priorityIcon);
        TextView titleTextView = convertView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        TextView timeTextView = convertView.findViewById(R.id.dateTextView);

        Task task = getItem(position);

        // Set the priority icon based on the task's priority
        int priorityIconRes = getPriorityIconResource(task.getPriority());

        Log.d(task.getTitle(), "task.getTitle() is : ");
        Log.d(task.getPriority(), "task.getPriority() is : ");
        Log.d("priorityIconRes is :",priorityIconRes+"");

        priorityIcon.setImageResource(priorityIconRes);

        titleTextView.setText(task.getTitle());
        descriptionTextView.setText(task.getDescription());
        timeTextView.setText(task.getDueDate());

        return convertView;
    }

    private int getPriorityIconResource(String priority) {
        Log.d(priority, "priority is : ");
        String uppercasePriority = priority.toUpperCase();
        Log.d(uppercasePriority, "uppercasePriority is : ");
        Log.d("getPriority method", "getPriorityIconResource: ");
        if (priority.equals("HIGH")) {
            Log.d("Inside if priority.equals(\"HIGH\")", "getPriorityIconResource: ");
            return R.drawable.priority_high;
        }
        else {
            Log.d("Inside else priority.equals(\"low\")", "getPriorityIconResource: ");
            return R.drawable.priority_low;
        }
    }


    public void updateTasks(List<Task> updatedTasks) {
        clear();
        addAll(updatedTasks);
        notifyDataSetChanged();
    }
}
