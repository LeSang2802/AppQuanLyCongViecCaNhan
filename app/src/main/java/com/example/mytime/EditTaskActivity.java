package com.example.mytime;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    private EditText etDescription, etDate, etTime;
    private Button btnSave;
    private DatabaseHelper databaseHelper;
    private int taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        btnSave = findViewById(R.id.btnSave);

        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        taskId = intent.getIntExtra("task_id", -1);
        etDescription.setText(intent.getStringExtra("task_description"));
        etDate.setText(intent.getStringExtra("task_date"));
        etTime.setText(intent.getStringExtra("task_time"));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etDescription.getText().toString();
                String date = etDate.getText().toString();
                String time = etTime.getText().toString();

                if (databaseHelper.updateTask(taskId, description, date, time)) {
                    Toast.makeText(EditTaskActivity.this, "Sửa thành công !", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("task_id", taskId);
                    resultIntent.putExtra("task_description", description);
                    resultIntent.putExtra("task_date", date);
                    resultIntent.putExtra("task_time", time);
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditTaskActivity.this, "Sửa thất bại !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
