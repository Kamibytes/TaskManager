package com.example.taskmanager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DueDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DueDateFragment extends Fragment {

    private ListView taskListView;
    private TaskAdapter taskAdapter;
    private List<Task> allTasks;
    private DBHandler taskDatabase;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_due_date, container, false);

        taskListView = rootView.findViewById(R.id.DuetaskListView);
        taskDatabase = new DBHandler(getActivity());
        allTasks = taskDatabase.getDueDateTasks();
        taskAdapter = new TaskAdapter(getActivity(), allTasks);
        taskListView.setAdapter(taskAdapter);

        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = (Task) parent.getItemAtPosition(position);
                openEditTaskActivity(task.getId());
            }
        });

        // Set up item long click listener for deleting task
        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(
                        getActivity());
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete?");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Task task = (Task) parent.getItemAtPosition(position);
                        deleteTask(task);
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

                return true;
            }
        });

        return rootView;
    }
    private void openEditTaskActivity(int taskId) {
        Intent intent = new Intent(getActivity(), EditTaskActivity.class);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
    }

    private void deleteTask(Task task) {

        taskDatabase.deleteTask(task.getId());
        taskAdapter.updateTasks(taskDatabase.getAllTasks());
        Toast.makeText(getActivity(), "Task deleted", Toast.LENGTH_SHORT).show();
    }
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_filter, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByDueDate:
                List<Task> dueDateTasks = taskDatabase.getDueDateTasks();
                allTasks.clear();
                for (Task task : dueDateTasks) {
                    allTasks.add(task);
                }
                taskAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}