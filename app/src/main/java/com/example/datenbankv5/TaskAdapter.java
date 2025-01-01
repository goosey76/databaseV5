package com.example.datenbankv5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList; // List to hold task data

    // Constructor
    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        holder.textViewTask.setText(task.getTask());
        holder.textViewDescription.setText(task.getDescription());

        // Handle priority using enums
        switch (task.getPriority()) {
            case URGENT_IMPORTANT:
                holder.textViewPriority.setText(context.getString(R.string.priority_urgent_important));
                break;
            case NOT_URGENT_IMPORTANT:
                holder.textViewPriority.setText(context.getString(R.string.priority_not_urgent_important));
                break;
            case URGENT_NOT_IMPORTANT:
                holder.textViewPriority.setText(context.getString(R.string.priority_urgent_not_important));
                break;
            case NOT_URGENT_NOT_IMPORTANT:
                holder.textViewPriority.setText(context.getString(R.string.priority_not_urgent_not_important));
                break;
            default:
                holder.textViewPriority.setText(context.getString(R.string.priority_unknown));
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size(); // Number of items in the list
    }

    // ViewHolder class for individual items in RecyclerView
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTask, textViewDescription, textViewPriority;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTask = itemView.findViewById(R.id.textViewTask);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewPriority = itemView.findViewById(R.id.textViewPriority);
        }
    }
}
