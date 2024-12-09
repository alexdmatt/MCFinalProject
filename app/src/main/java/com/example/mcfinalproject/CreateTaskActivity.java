package com.example.mcfinalproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class CreateTaskActivity extends AppCompatActivity {

    private Button dueDateButton;
    private int year, month, day;
    private EditText taskName, taskDesc, taskDueDate;
    private RadioGroup priorityGroup;
    private SQLiteDatabase db;
    private TaskDatabase taskDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_creation);

        taskDatabase = new TaskDatabase(this);
        db = taskDatabase.getWritableDatabase();

        taskName = findViewById(R.id.task_name);
        taskDesc = findViewById(R.id.task_description);

        dueDateButton = findViewById(R.id.due_date_button);

        // Initialize the date to the current date
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Initialize the RadioGroup for priority
        priorityGroup = findViewById(R.id.priority_group);

        // Set an onClickListener for the Due Date button
        dueDateButton.setOnClickListener(view -> {
            // Open DatePickerDialog when the button is clicked
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CreateTaskActivity.this,
                    (view1, selectedYear, selectedMonth, selectedDay) -> {
                        // Update the selected date and display it
                        year = selectedYear;
                        month = selectedMonth;
                        day = selectedDay;

                        // Display selected date in the button's text or use a separate TextView
                        dueDateButton.setText((month + 1) + "/" + day + "/" + year);
                    },
                    year, month, day);

            datePickerDialog.show();
        });
    }

    public void createTask(View v) {
        String title = taskName.getText().toString();
        String desc = taskDesc.getText().toString();
        String dueDateText = dueDateButton.getText().toString();

        // Get the selected priority from the RadioGroup
        int selectedPriorityId = priorityGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedPriorityId);
        String prioText = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "0";

        Integer prioInt = 0;
        try {
            prioInt = Integer.parseInt(prioText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please select a valid priority.", Toast.LENGTH_SHORT).show();
        }

        long result = taskDatabase.addTask(title, desc, prioInt, dueDateText);

        if (result != -1) {
            Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Couldn't add task", Toast.LENGTH_SHORT).show();
        }
    }

    public void homeBtn(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
