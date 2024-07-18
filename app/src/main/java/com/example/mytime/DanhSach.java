package com.example.mytime;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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

public class DanhSach extends AppCompatActivity {

    ImageView img_trangchu;
    ImageView img_thongbao;
    ImageView img_timkiem;
    ImageView imgback;

    RecyclerView recyclerViewDS;
    TaskAdapter taskAdapter;
    List<Task> taskList;
    DatabaseHelper databaseHelper;

    Spinner spinner;
    int selectedSpinnerPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.danh_sach);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img_trangchu = findViewById(R.id.img_trangchu);
        img_thongbao = findViewById(R.id.img_thongbao);
        img_timkiem = findViewById(R.id.img_timkiem);
        imgback = findViewById(R.id.img_back);

        recyclerViewDS = findViewById(R.id.recyclerViewDS);
        recyclerViewDS.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, task -> editTask(task), taskId -> deleteTask(taskId));
        recyclerViewDS.setAdapter(taskAdapter);

        databaseHelper = new DatabaseHelper(this);

        spinner = findViewById(R.id.spinner);
        String[] items = {"3 Ngày qua", "7 ngày tới", "Trong tháng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSpinnerPosition = position;
                loadFilteredData(selectedSpinnerPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không cần xử lý
            }
        });

        imgback.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSach.this, TrangChu.class);
            startActivity(intent);
        });

        img_trangchu.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSach.this, TrangChu.class);
            startActivity(intent);
        });

        img_thongbao.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSach.this, ThongBao.class);
            startActivity(intent);
        });

        img_timkiem.setOnClickListener(v -> {
            Intent intent = new Intent(DanhSach.this, TimKiem.class);
            startActivity(intent);
        });
    }

    private void loadFilteredData(int filterType) {
        taskList.clear();
        Cursor cursor = databaseHelper.getAllTasks();
        if (cursor != null && cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
            int indexDescription = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
            int indexDate = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
            int indexTime = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME);

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());

            do {
                if (indexId != -1 && indexDescription != -1 && indexDate != -1 && indexTime != -1) {
                    int id = cursor.getInt(indexId);
                    String description = cursor.getString(indexDescription);
                    String date = cursor.getString(indexDate);
                    String time = cursor.getString(indexTime);

                    switch (filterType) {
                        case 0: // 3 Ngày qua
                            // Kiểm tra nếu task nằm trong 3 ngày qua
                            if (isWithinLastThreeDays(date, calendar, dateFormat)) {
                                taskList.add(new Task(id, description, date, time));
                            }
                            break;
                        case 1: // 7 ngày tới
                            // Kiểm tra nếu task nằm trong 7 ngày tới
                            if (isWithinCurrentWeek(date, calendar, dateFormat)) {
                                taskList.add(new Task(id, description, date, time));
                            }
                            break;
                        case 2: // Trong tháng
                            // Kiểm tra nếu task nằm trong cùng tháng
                            if (isWithinCurrentMonth(date, calendar, dateFormat)) {
                                taskList.add(new Task(id, description, date, time));
                            }
                            break;
                        default:
                            break;
                    }
                }
            } while (cursor.moveToNext());

            cursor.close();
            taskAdapter.notifyDataSetChanged();
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
    }

    private boolean isWithinLastThreeDays(String date, Calendar calendar, SimpleDateFormat dateFormat) {

        // Trừ đi 3 ngày
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        String threeDaysBefore = dateFormat.format(calendar.getTime());

        // Reset lại calendar về ngày hiện tại sau khi tính toán
        calendar.add(Calendar.DAY_OF_MONTH, 3); // Phục hồi lại để không làm ảnh hưởng đến các lần kiểm tra sau
        calendar.setTimeInMillis(System.currentTimeMillis());

        // So sánh ngày của task với khoảng thời gian từ 3 ngày trước đến ngay trước hiện tại
        return date.compareTo(threeDaysBefore) >= 0 && date.compareTo(dateFormat.format(calendar.getTime())) < 0;
    }

    private boolean isWithinNextThreeDays(String date, Calendar calendar, SimpleDateFormat dateFormat) {
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        String threeDaysAfter = dateFormat.format(calendar.getTime());
        return date.compareTo(threeDaysAfter) <= 0;
    }

    private boolean isWithinCurrentWeek(String date, Calendar calendar, SimpleDateFormat dateFormat) {
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTimeInMillis(System.currentTimeMillis());
        String currentWeekFirstDay = dateFormat.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 7);
        String nextWeekFirstDay = dateFormat.format(calendar.getTime());
        return date.compareTo(currentWeekFirstDay) >= 0 && date.compareTo(nextWeekFirstDay) < 0;
    }

    private boolean isWithinCurrentMonth(String date, Calendar calendar, SimpleDateFormat dateFormat) {
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        String[] dateParts = date.split("/");
        int taskMonth = Integer.parseInt(dateParts[1]);
        return currentMonth == taskMonth;
    }

    private void editTask(Task task) {
        Intent intent = new Intent(DanhSach.this, EditTaskActivity.class);
        intent.putExtra("task_id", task.getId());
        intent.putExtra("task_description", task.getDescription());
        intent.putExtra("task_date", task.getDate());
        intent.putExtra("task_time", task.getTime());
        startActivityForResult(intent, 1);
    }

    private void deleteTask(int taskId) {
        if (databaseHelper.deleteTask(taskId)) {
            loadFilteredData(selectedSpinnerPosition);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            loadFilteredData(selectedSpinnerPosition);
        }
    }
}

