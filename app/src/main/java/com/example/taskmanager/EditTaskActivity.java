package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class EditTaskActivity extends AppCompatActivity {
    private DBHandler taskDatabase;
    private int taskId;

    private TextInputEditText TaskTime;

    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText dueDateEditText;

    private String[] item = new String[]{"HIGH", "LOW"};
    private String[] item2 = new String[]{"COMPLETED", "PENDING"};
    private AutoCompleteTextView Priority;
    private AutoCompleteTextView Status;

    DatePickerDialog.OnDateSetListener dateSetListener;
    ArrayAdapter<String> adapterItems;

    // update button
    Button updateButton;

    Calendar calendar;
    Task mytask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        taskDatabase = new DBHandler(this);

        taskId = getIntent().getIntExtra("taskId", -1);

        Log.d("edit task id ---------->", String.valueOf(taskId));

        titleEditText = findViewById(R.id.TitleInput);
        descriptionEditText = findViewById(R.id.DescriptionInput);
        dueDateEditText = findViewById(R.id.DueDateInput);
        Priority = findViewById(R.id.taskPriority);
        Status=findViewById(R.id.taskStatus);
        TaskTime = findViewById(R.id.TimeInput);
        calendar = Calendar.getInstance();

        //update button inflation
        updateButton = findViewById(R.id.updatebutton);


        Button saveButton = findViewById(R.id.updatebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                saveTask();
            }
        });

        mytask = taskDatabase.getTask(taskId);

        titleEditText.setText(mytask.getTitle());
        descriptionEditText.setText(mytask.getDescription());
        dueDateEditText.setText(mytask.getDueDate());
        Priority.setText(mytask.getPriority());
        TaskTime.setText(mytask.getTime());
        Status.setText(mytask.getStatus());



        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTask();

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
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditTaskActivity.this,
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

        dueDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar myCalendar = Calendar.getInstance();

                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EditTaskActivity.this,
                        dateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                dueDateEditText.clearFocus();
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
                dueDateEditText.setText(selectedDate);
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
        Status = findViewById(R.id.taskStatus);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_view, item2);

        Status.setAdapter(adapterItems);
        Status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = (String) adapterView.getItemAtPosition(i).toString();
            }
        });

    }


    private void updateTask() {
        String title="";
        String description ="";
        String dueDate = "";
        String tasktime = "";
        String status="";
        String priority = "";

        title = titleEditText.getText().toString().trim();
        description = descriptionEditText.getText().toString().trim();
        dueDate = dueDateEditText.getText().toString().trim();
        tasktime = TaskTime.getText().toString().trim();
        priority = Priority.getText().toString().trim();
        status = Status.getText().toString().trim();


        // Validate input
        if (title.isEmpty() || dueDate.isEmpty() || tasktime.isEmpty() || status.isEmpty() || priority.isEmpty()) {
            Toast.makeText(this, "Please fill all out the form", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Task newTask = new Task(title, description, dueDate, tasktime, priority, status);

            newTask.setId(taskId);

            taskDatabase.updateTask(newTask);
            // Notification

            int singleNotificationId = 1;

            // Create a notification channel
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("channel_id", "Notification", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }

            // Build the notification
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(EditTaskActivity.this, "channel_id")
                    .setContentTitle("Task Successfully Edited")
                    .setContentText("Content for the notification")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("The task " + mytask.getTitle() + " " +
                                    "has been successfully updated and saved"));

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(EditTaskActivity.this);
            if (ActivityCompat.checkSelfPermission(EditTaskActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManager.notify(singleNotificationId, mBuilder.build());
            // notification end
            Intent intent = new Intent(EditTaskActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
    public void onBackPressed() {
        finish();
    }
}