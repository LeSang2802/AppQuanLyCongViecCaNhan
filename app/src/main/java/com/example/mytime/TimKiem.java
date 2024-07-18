package com.example.mytime;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TimKiem extends AppCompatActivity {

    ImageView img_trangchu;

    ImageView img_thongbao;

    ImageView img_danhsach;

    ImageView imgback;

    //10h 16/07
    SearchView searchView;
    RecyclerView recyclerViewTK;
    TaskAdapter taskAdapter;
    List<Task> taskList;
    DatabaseHelper databaseHelper;

    private static final int REQUEST_CODE_EDIT_TASK = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.tim_kiem);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        img_trangchu = findViewById(R.id.img_trangchu);
        img_thongbao = findViewById(R.id.img_thongbao);
        img_danhsach = findViewById(R.id.img_danhsach);
        imgback = findViewById(R.id.img_back);


        //10h 16/07
        searchView = findViewById(R.id.search_view);
        recyclerViewTK = findViewById(R.id.recyclerViewTK);

        databaseHelper = new DatabaseHelper(this);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, task -> {
            Intent intent = new Intent(this, EditTaskActivity.class);
            intent.putExtra("task_id", task.getId());
            intent.putExtra("task_description", task.getDescription());
            intent.putExtra("task_date", task.getDate());
            intent.putExtra("task_time", task.getTime());
            startActivityForResult(intent, REQUEST_CODE_EDIT_TASK);
            searchTasks(searchView.getQuery().toString());
        }, taskId -> {
            databaseHelper.deleteTask(taskId);
            searchTasks(searchView.getQuery().toString());
        });
        recyclerViewTK.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewTK.setAdapter(taskAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTasks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchTasks(newText);
                return true;
            }
        });

        // Các icon img chuyển trang :
        // Sự kiện cho nút back - quay về trang chủ
        imgback.setOnClickListener(v -> {
            Intent intent = new Intent(TimKiem.this, TrangChu.class);
            startActivity(intent);
        });

        // Sự kiệm img trang chủ - quay về trang chủ
        img_trangchu.setOnClickListener(v -> {
            Intent intent = new Intent(TimKiem.this, TrangChu.class);
            startActivity(intent);
        });

        // Sự kiệm img thong bao - chuyen trang thong bao
        img_thongbao.setOnClickListener(v -> {
            Intent intent = new Intent(TimKiem.this, ThongBao.class);
            startActivity(intent);
        });

        // Sự kiệm img danh sach - chuyen trang danh sach
        img_danhsach.setOnClickListener(v -> {
            Intent intent = new Intent(TimKiem.this, DanhSach.class);
            startActivity(intent);
        });
    }



    private void searchTasks(String query) {
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

                    if (description.toLowerCase().contains(query.toLowerCase())) {
                        taskList.add(new Task(id, description, date, time));
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
}
