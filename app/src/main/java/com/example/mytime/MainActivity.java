package com.example.mytime;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText et_Email;

    EditText et_MatKhau;

    Button bt_DangNhap;

    TextView tv_DangKy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // su kien dang ky - chuyen sang trang dang ky
        tv_DangKy = findViewById(R.id.tv_DangKy);
        tv_DangKy.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DangKy.class);
            startActivity(intent);
        });



        // su kien dang nhap (2003@gmail.com - 28022003)

        et_Email = findViewById(R.id.et_Email);
        et_MatKhau = findViewById(R.id.et_MatKhau);
        bt_DangNhap = findViewById(R.id.bt_DangNhap);

        bt_DangNhap.setOnClickListener(v -> {
            String email = et_Email.getText().toString();
            String matKhau = et_MatKhau.getText().toString();

            SharedPreferences sharedPreferences = getSharedPreferences("MyTimePrefs", MODE_PRIVATE);
            String registeredEmail = sharedPreferences.getString("email", "");
            String registeredPassword = sharedPreferences.getString("matKhau", "");

            if (email.equals(registeredEmail) && matKhau.equals(registeredPassword)) {
                // Đăng nhập thành công, chuyển đến TrangChu
                Intent intent = new Intent(MainActivity.this, TrangChu.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                finish(); // Đóng activity hiện tại
            } else {
                Toast.makeText(MainActivity.this, "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}