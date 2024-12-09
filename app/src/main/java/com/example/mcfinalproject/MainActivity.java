package com.example.mcfinalproject;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TaskAdapter.OnTaskEditListener {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    TaskDatabase taskDatabase;
    Button hideToggleBtn;
    boolean isHideToggled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_mainpage);

        recyclerView = findViewById(R.id.task_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hideToggleBtn = findViewById(R.id.toggle_button);
        taskDatabase = new TaskDatabase(this);
        taskList = new ArrayList<>();
        loadTaskFromDB();


        sortTasks();

        taskAdapter = new TaskAdapter(this, taskList, this);
        recyclerView.setAdapter(taskAdapter);
    }


    @Override
    public void onEditTask(Task task) {
        // Handle task edit action
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("TaskId", task.getId());
        intent.putExtra("taskName", task.getTitle());
        intent.putExtra("taskDesc", task.getDesc());
        intent.putExtra("taskPriority", task.getPriority());
        intent.putExtra("taskDueDate", task.getDueDate());
        startActivity(intent);

    }

    // Create Task Button Click Listener
    public void onCreateTaskClicked(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivity(intent);
    }

    private void loadTaskFromDB() {

        taskList.clear();


        Cursor cursor = taskDatabase.getAllTasks();
        if (cursor != null && cursor.moveToFirst()){
            do {
                int taskId = cursor.getInt(cursor.getColumnIndexOrThrow("task_id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                int priority = cursor.getInt(cursor.getColumnIndexOrThrow("priority"));
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow("due_date"));

                if (!isHideToggled || daysTillDueDate(dueDate) >= 0) {
                    taskList.add(new Task(taskId, title, desc, priority, dueDate));
                }

            } while (cursor.moveToNext());
            cursor.close();
        }
        if (taskAdapter != null) {
            taskAdapter.notifyDataSetChanged();
        }
    }

    public static long daysTillDueDate(String dueDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
            Date taskDueDate = sdf.parse(dueDate);

            Calendar today = Calendar.getInstance();
            Date currDate = today.getTime();

            long diffInMillis = taskDueDate.getTime() - currDate.getTime();
            return TimeUnit.MILLISECONDS.toDays(diffInMillis);
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void sortTasks() {

        Collections.sort(taskList, (task1, task2) -> {
            long daysTillDue1 = daysTillDueDate(task1.getDueDate());
            long daysTillDue2 = daysTillDueDate(task2.getDueDate());

            double score1 = daysTillDue1 / (double) task1.getPriority() + 1;
            double score2 = daysTillDue2 / (double) task2.getPriority() + 1;

            if ((daysTillDue1 == 0 && daysTillDue2 == 0) || (daysTillDue1 < 0 && daysTillDue2 < 0)) {
                return Integer.compare(task2.getPriority(), task1.getPriority());
            }

            return Double.compare(score1, score2);
        });
    }

    public void hideToggle(View view) {
        if (isHideToggled == false){
            hideToggleBtn.setText("Show Past Due Tasks");
            isHideToggled = true;
        } else {
            hideToggleBtn.setText("Hide Past Due Tasks");
            isHideToggled = false;
        }

        loadTaskFromDB();
        sortTasks();
        if (taskAdapter != null) {
            taskAdapter.notifyDataSetChanged();
        }
    }

}

