package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {

    private TextInputLayout textInput1;
    private TextInputEditText Title;

    private TextInputLayout textInput2;
    private TextInputEditText Description;

    private TextInputLayout textInput3;
    private TextInputEditText DueDate;

    private TextInputLayout textInput4;
    private TextInputEditText TaskTime;


    DatePickerDialog.OnDateSetListener dateSetListener;
    Calendar calendar;

    private String[] item = new String[]{"HIGH", "LOW"};
    private AutoCompleteTextView Priority;
    ArrayAdapter<String> adapterItems;
    private Button addButton;

    private DBHandler taskDatabase;
//    private TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

//        t1.setText(taskDatabase.getDate());
        textInput1 = findViewById(R.id.TextInputLayout1);
        Title = findViewById(R.id.TitleInput);

        textInput2 = findViewById(R.id.TextInputLayout2);
        Description = findViewById(R.id.DescriptionInput);

        textInput3 = findViewById(R.id.TextInputLayout3);
        DueDate = findViewById(R.id.DueDateInput);

        textInput4 = findViewById(R.id.TextInputLayout4);
        TaskTime = findViewById(R.id.TimeInput);
        calendar = Calendar.getInstance();
        addButton=findViewById(R.id.addbutton);
        taskDatabase = new DBHandler(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTask();
            }
        });

        TaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get current time
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and show it
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Update the EditText with the selected time
                                String time = String.format("%02d:%02d", hourOfDay, minute);
                                TaskTime.setText(time);
                            }
                        },
                        hour,
                        minute,
                        true
                );
                timePickerDialog.show();

            }
        });

        DueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this,
                        dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                DueDate.clearFocus();
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String selectedDate;
                if(month<9 && dayOfMonth>9)
                {
                    selectedDate = year + "-0" + (month + 1) + "-" + dayOfMonth;
                }
                else if(month>=9 && dayOfMonth<10)
                {
                    selectedDate = year + "-" + (month + 1) + "-0" + dayOfMonth;
                }
                else if(month<9 && dayOfMonth<10)
                {
                    selectedDate = year + "-0" + (month + 1) + "-0" + dayOfMonth;
                }
                else{
                    selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                }
                DueDate.setText(selectedDate);
            }
        };
        //Dropdown for priority......................
        Priority = findViewById(R.id.taskPriority);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_view, item);

        Priority.setAdapter(adapterItems);
        Priority.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i).toString();
            }
        });
    }

    private void addTask() {
        String title="";
        String description ="";
        String dueDate = "";
        String tasktime = "";
        String status="PENDING";
        String priority = "";

        title = Title.getText().toString().trim();
        description = Description.getText().toString().trim();
        dueDate = DueDate.getText().toString().trim();
        tasktime = TaskTime.getText().toString().trim();
        priority = Priority.getText().toString().trim();

        // Validate input
        if (title.isEmpty() || dueDate.isEmpty() || tasktime.isEmpty() || status.isEmpty() || priority.isEmpty()) {
            Toast.makeText(this, "Please fill all out the form", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Task newTask = new Task(title, description, dueDate, tasktime, priority, status);

            // Add the task to the database
            taskDatabase.addTask(newTask);

            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


    }
    public void onBackPressed() {
        // Destroy the current activity and remove it from the stack
        Intent intent = new Intent(AddTaskActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}