package com.example.mytime;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;
    private OnTaskEditListener onTaskEditListener;
    private OnTaskDeleteListener onTaskDeleteListener;

    public TaskAdapter(List<Task> taskList, OnTaskEditListener onTaskEditListener, OnTaskDeleteListener onTaskDeleteListener) {
        this.taskList = taskList;
        this.onTaskEditListener = onTaskEditListener;
        this.onTaskDeleteListener = onTaskDeleteListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.tvDescription.setText(task.getDescription());
        holder.tvDate.setText(task.getDate());
        holder.tvTime.setText(task.getTime());
        holder.img_xoa.setOnClickListener(v -> onTaskDeleteListener.onTaskDelete(task.getId()));
        holder.img_sua.setOnClickListener(v -> onTaskEditListener.onTaskEdit(task));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView tvDescription, tvDate, tvTime;
        ImageView img_xoa, img_sua;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvTime = itemView.findViewById(R.id.tvTime);
            img_sua = itemView.findViewById(R.id.img_sua);
            img_xoa = itemView.findViewById(R.id.img_xoa);
        }
    }

    public interface OnTaskEditListener {
        void onTaskEdit(Task task);
    }

    public interface OnTaskDeleteListener {
        void onTaskDelete(int taskId);
    }
}

