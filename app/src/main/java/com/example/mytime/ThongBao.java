package com.example.mytime;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ThongBao extends AppCompatActivity {

    ImageView img_trangchu;

    ImageView img_timkiem;

    ImageView img_danhsach;

    ImageView imgback;

    // 3h  17/07/2024
    RecyclerView reListThongBao;
    TaskAdapter taskAdapter;
    List<Task> taskList;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.thong_bao);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img_trangchu = findViewById(R.id.img_trangchu);
        img_timkiem = findViewById(R.id.img_timkiem);
        img_danhsach = findViewById(R.id.img_danhsach);
        imgback = findViewById(R.id.img_back);

        // 3h  17/07/2024
        reListThongBao = findViewById(R.id.reListThongBao);

        databaseHelper = new DatabaseHelper(this);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, task -> {
            // Handle task click if needed
        }, taskId -> {
            databaseHelper.deleteTask(taskId);
            loadTasks();
        });

        reListThongBao.setLayoutManager(new LinearLayoutManager(this));
        reListThongBao.setAdapter(taskAdapter);

        // Các icon img chuyển trang :
        // Sự kiện cho nút back - quay về trang chủ
        imgback.setOnClickListener(v -> {
            Intent intent = new Intent(ThongBao.this, TrangChu.class);
            startActivity(intent);
        });

        // Sự kiệm img trang chủ - quay về trang chủ
        img_trangchu.setOnClickListener(v -> {
            Intent intent = new Intent(ThongBao.this, TrangChu.class);
            startActivity(intent);
        });

        // Sự kiệm img tim kiem - chuyen trang tim kiem
        img_timkiem.setOnClickListener(v -> {
            Intent intent = new Intent(ThongBao.this, TimKiem.class);
            startActivity(intent);
        });

        // Sự kiệm img danh sach - chuyen trang danh sach
        img_danhsach.setOnClickListener(v -> {
            Intent intent = new Intent(ThongBao.this, DanhSach.class);
            startActivity(intent);
        });

        loadTasks();
    }

    private void loadTasks() {
        taskList.clear();
        Cursor cursor = databaseHelper.getAllTasks();
        if (cursor != null && cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            int indexDescription = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
            int indexDate = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
            int indexTime = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME);

            do {
                if (indexId != -1 && indexDescription != -1 && indexDate != -1 && indexTime != -1) {
                    int id = cursor.getInt(indexId);
                    String description = cursor.getString(indexDescription);
                    String date = cursor.getString(indexDate);
                    String time = cursor.getString(indexTime);

                    if (isWithinNext8Hours(date, time)) {
                        taskList.add(new Task(id, description, date, time));
                        setNotification(id, description, date, time);
                    }
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        taskList.sort((task1, task2) -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            try {
                int dateComparison = dateFormat.parse(task1.getDate()).compareTo(dateFormat.parse(task2.getDate()));
                if (dateComparison == 0) {
                    return timeFormat.parse(task1.getTime()).compareTo(timeFormat.parse(task2.getTime()));
                }
                return dateComparison;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });
        taskAdapter.notifyDataSetChanged();
    }

    private void setNotification(int taskId, String description, String date, String time) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/M/yyyy HH:mm", Locale.getDefault());
        try {
            Calendar taskCalendar = Calendar.getInstance();
            taskCalendar.setTime(dateTimeFormat.parse(date + " " + time));
            taskCalendar.add(Calendar.MINUTE, -10); // Thông báo trước 10 phút

            Intent intent = new Intent(this, NotificationReceiver.class);
            intent.putExtra("taskId", taskId);
            intent.putExtra("description", description);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this,
                    taskId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, taskCalendar.getTimeInMillis(), pendingIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isWithinNext8Hours(String date, String time) {
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Calendar taskCalendar = Calendar.getInstance();
            taskCalendar.setTime(dateTimeFormat.parse(date + " " + time));

            Calendar now = Calendar.getInstance();
            Calendar eightHoursLater = (Calendar) now.clone();
            eightHoursLater.add(Calendar.HOUR, 8);

            return taskCalendar.after(now) && taskCalendar.before(eightHoursLater);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
