package com.example.mcfinalproject;

import static com.example.mcfinalproject.MainActivity.daysTillDueDate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private final Context context;
    private final List<Task> taskList;
    private final OnTaskEditListener editListener;


    public interface OnTaskEditListener {
        void onEditTask(Task task);

    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskEditListener editListener) {
        this.context = context;
        this.taskList = taskList;
        this.editListener = editListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.taskTitle.setText(task.getTitle());
        holder.taskDueDate.setText("Due: " + task.getDueDate());

        // Set priority color based on priority level
        int priorityColor = getPriorityColor(task.getPriority());
        holder.priorityDot.setBackgroundColor(priorityColor);


        if (daysTillDueDate(task.getDueDate()) < 0) {
            // If the task is overdue, set the background color to dark red
            holder.itemView.setBackgroundColor(Color.parseColor("#5C0000"));
            holder.taskTitle.setTextColor(Color.WHITE);
            holder.taskDueDate.setTextColor(Color.WHITE);// Dark red color
        } else {
            // Otherwise, set a default color (for example, white or another color)
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF")); // White background
        }

        // Handle edit button click
        holder.editButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTaskActivity.class);
            intent.putExtra("id", task.getId());
            intent.putExtra("taskName", task.getTitle());
            intent.putExtra("taskDesc", task.getDesc());
            intent.putExtra("taskDueDate", task.getDueDate());
            intent.putExtra("taskPriority", task.getPriority());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle, taskDueDate;
        View priorityDot;
        Button editButton;
        LinearLayout taskContainer;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.task_name);
            taskDueDate = itemView.findViewById(R.id.task_due_date);
            priorityDot = itemView.findViewById(R.id.priority_dot);
            editButton = itemView.findViewById(R.id.edit_task_button);
            taskContainer = itemView.findViewById(R.id.task_list_recycler);
        }
    }

    private int getPriorityColor(int priority) {
        switch (priority) {
            case 0:
                return ContextCompat.getColor(context, R.color.priority_0);
            case 1:
                return ContextCompat.getColor(context, R.color.priority_1);
            case 2:
                return ContextCompat.getColor(context, R.color.priority_2);
            case 3:
                return ContextCompat.getColor(context, R.color.priority_3);
            case 4:
                return ContextCompat.getColor(context, R.color.priority_4);
            case 5:
                return ContextCompat.getColor(context, R.color.priority_5);
            default:
                return Color.GRAY; // Default color for invalid priority
        }
    }
}
