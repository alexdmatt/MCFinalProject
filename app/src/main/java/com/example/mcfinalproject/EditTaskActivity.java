package com.example.mcfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditTaskActivity extends AppCompatActivity {

    private EditText editTaskTitle, editTaskDescription;
    private CalendarView calendarView;
    private RadioGroup priorityRadioGroup;
    private Button deleteTaskButton, saveTaskButton, homeButton;

    private String selectedDueDate; // Stores the selected date from CalendarView
    private int selectedPriority = -1; // Default priority (-1 indicates no selection)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        // Initialize views
        editTaskTitle = findViewById(R.id.edit_task_title);
        editTaskDescription = findViewById(R.id.edit_task_description);
        calendarView = findViewById(R.id.edit_task_calendar);
        priorityRadioGroup = findViewById(R.id.priority_radio_group);
        deleteTaskButton = findViewById(R.id.delete_task_button);
        saveTaskButton = findViewById(R.id.save_task_button);
        homeButton = findViewById(R.id.home_button);

        // Prepopulate the fields with task data
        prepopulateFields();

        // Set CalendarView date change listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDueDate = (month + 1) + "/" + dayOfMonth + "/" + year; // Format MM/dd/yyyy
        });

        // Set Priority RadioGroup listener
        priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton selectedButton = findViewById(checkedId);
            if (selectedButton != null) {
                selectedPriority = Integer.parseInt(selectedButton.getText().toString());
            }
        });

        // Set Button click listeners
        homeButton.setOnClickListener(v -> finish());
         // Close activity and return home
    }

    private void prepopulateFields() {
        // Retrieve task details from Intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("taskName", "");
            String description = extras.getString("taskDesc", "");
            String dueDate = extras.getString("taskDueDate", "");
            int priority = extras.getInt("taskPriority", -1);

            // Populate the fields
            editTaskTitle.setText(title);
            editTaskDescription.setText(description);

            // Set the selected date on the CalendarView
            if (!dueDate.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(sdf.parse(dueDate));
                    calendarView.setDate(calendar.getTimeInMillis()); // Preselect the due date
                    selectedDueDate = dueDate; // Set initial due date
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            // Select the appropriate priority button
            if (priority != -1) {
                int buttonId = getPriorityButtonId(priority);
                if (buttonId != -1) {
                    RadioButton selectedButton = findViewById(buttonId);
                    if (selectedButton != null) {
                        selectedButton.setChecked(true);
                        selectedPriority = priority; // Set initial priority
                    }
                }
            }
        }
    }

    private int getPriorityButtonId(int priority) {
        // Map priority levels to RadioButton IDs
        switch (priority) {
            case 0: return R.id.priority_0_edit;
            case 1: return R.id.priority_1_edit;
            case 2: return R.id.priority_2_edit;
            case 3: return R.id.priority_3_edit;
            case 4: return R.id.priority_4_edit;
            case 5: return R.id.priority_5_edit;
            default: return -1;
        }
    }

    public void handleSaveTask(View v) {
        String title = editTaskTitle.getText().toString();
        String description = editTaskDescription.getText().toString();

        // Ensure the title and description are not empty
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please enter both title and description.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the user has selected a priority
        if (selectedPriority == -1) {
            Toast.makeText(this, "Please select a priority.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the user has selected a due date
        if (selectedDueDate == null || selectedDueDate.isEmpty()) {
            Toast.makeText(this, "Please select a due date.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Retrieve the task ID from the Intent
        int taskId = getIntent().getIntExtra("id", -1);
        if (taskId != -1) {
            TaskDatabase db = new TaskDatabase(this);
            db.updateTask(taskId, title, description, selectedPriority, selectedDueDate);
            Toast.makeText(this, "Task updated successfully.", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    public void handleDeleteTask(View v) {
        // Retrieve the task ID from the Intent
        int taskId = getIntent().getIntExtra("id", -1);
        Log.d("EditTaskActivity", "Task ID: " + taskId);

        if (taskId != -1) {
            TaskDatabase db = new TaskDatabase(this);
            int rowsDeleted = db.deleteTask(taskId);
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Task deleted successfully.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after deleting
            } else {
                Toast.makeText(this, "Failed to delete task.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void finish(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
