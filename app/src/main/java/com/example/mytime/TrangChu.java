package com.example.mytime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class TrangChu extends AppCompatActivity implements TaskAdapter.OnTaskEditListener, TaskAdapter.OnTaskDeleteListener {
    TextView datetv;
    TextView tvtenUser;

    ImageView img_them;

    ImageView img_user;
    ImageView img_trangchu;

    ImageView img_thongbao;

    ImageView img_timkiem;

    ImageView img_danhsach;


    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private static final int REQUEST_CODE_EDIT_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.trang_chu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ///
        databaseHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = new ArrayList<>();
        //loadData();


        taskAdapter = new TaskAdapter(taskList, this, this);
        recyclerView.setAdapter(taskAdapter);
        //

        datetv = findViewById(R.id.dateTextView);
        tvtenUser = findViewById(R.id.tv_ten_user);
        img_user = findViewById(R.id.img_user);
        img_them = findViewById(R.id.img_them);
        img_trangchu = findViewById(R.id.img_trangchu);
        img_thongbao = findViewById(R.id.img_thongbao);
        img_timkiem = findViewById(R.id.img_timkiem);
        img_danhsach = findViewById(R.id.img_danhsach);

        // Lấy ngày tháng hiện tại
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        // Gán ngày tháng hiện tại lên TextView
        datetv.setText(currentDate);

        // Lấy tên người dùng từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String tenUser = sharedPref.getString("username", "Name");

        // Gán tên người dùng lên TextView
        tvtenUser.setText(tenUser);

        // Các icon img chuyển trang :

        // Sự kiệm img thong bao - chuyen trang thong bao
        img_thongbao.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChu.this, ThongBao.class);
            startActivity(intent);
        });

        // Sự kiệm img tim kiem - chuyen trang tim kiem
        img_timkiem.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChu.this, TimKiem.class);
            startActivity(intent);
        });

        // Sự kiệm img danh sach - chuyen trang danh sach
        img_danhsach.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChu.this, DanhSach.class);
            startActivity(intent);
        });

        // Chuyen sang giao dien trang ca nhan
        img_user.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChu.this, TrangCaNhan.class);
            startActivity(intent);
        });


        // Chuyen sang giao dien them cong viec
        img_them.setOnClickListener(v -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            ThemCongViec dialogFragment = new ThemCongViec();
            dialogFragment.show(fragmentManager, "AddTaskDialogFragment");
        });
        // Sự kiệm img trang chủ - quay về trang chủ
        img_trangchu.setOnClickListener(v -> {
            Intent intent = new Intent(TrangChu.this, TrangChu.class);
            startActivity(intent);
        });
        loadData();

    }

    // Nút xóa công việc
    @Override
    public void onTaskDelete(int taskId) {
        if (databaseHelper.deleteTask(taskId)) {
            Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
            loadData();
            taskAdapter.notifyDataSetChanged();
        }
    }


    // nút chỉnh sửa công việc
    @Override
    public void onTaskEdit(Task task) {
        Intent intent = new Intent(this, EditTaskActivity.class);
        intent.putExtra("task_id", task.getId());
        intent.putExtra("task_description", task.getDescription());
        intent.putExtra("task_date", task.getDate());
        intent.putExtra("task_time", task.getTime());
        startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
        loadData();
    }


    // Hàm hiện hết các công việc
    /*
    public void loadData() {
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
                    taskList.add(new Task(id, description, date, time));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }
    }

     */





    // Hàm hiện công việc trong ngày hôm nay

//    public void loadData() {
//        taskList.clear();
//        Cursor cursor = databaseHelper.getAllTasks();
//
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
//        String currentDate = dateFormat.format(calendar.getTime());
//
//        if (cursor != null && cursor.moveToFirst()) {
//            int indexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
//            int indexDescription = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
//            int indexDate = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);
//            int indexTime = cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME);
//
//            do {
//                if (indexId != -1 && indexDescription != -1 && indexDate != -1 && indexTime != -1) {
//                    int id = cursor.getInt(indexId);
//                    String description = cursor.getString(indexDescription);
//                    String date = cursor.getString(indexDate);
//                    String time = cursor.getString(indexTime);
//
//                    // Chỉ thêm công việc có ngày trùng với ngày hiện tại
//                    if (currentDate.equals(date)) {
//                        taskList.add(new Task(id, description, date, time));
//                    }
//                }
//            } while (cursor.moveToNext());
//
//            cursor.close();
//        }
//
//        taskAdapter.notifyDataSetChanged();
//    }


    public void loadData() {
        taskList.clear();
        Cursor cursor = databaseHelper.getAllTasks();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

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

                    // Chỉ thêm công việc có ngày trùng với ngày hiện tại
                    if (currentDate.equals(date)) {
                        taskList.add(new Task(id, description, date, time));
                    }
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Sắp xếp danh sách công việc theo thời gian
        Collections.sort(taskList, (task1, task2) -> {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                return timeFormat.parse(task1.getTime()).compareTo(timeFormat.parse(task2.getTime()));
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        });

       taskAdapter.notifyDataSetChanged();
    }

}
