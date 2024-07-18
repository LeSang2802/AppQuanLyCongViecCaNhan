package com.example.mytime;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DangKy extends AppCompatActivity {

    ImageView iconback;
    EditText etTen;
    EditText etEmailDangKy;
    EditText etMatKhauDangKy;
    EditText etNhapLaiMatKhauDangKy;
    Button btDangKy;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.dang_ky);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Sự kiện cho nút back - quay về màn hình đăng nhập
        iconback = findViewById(R.id.icon_back);
        iconback.setOnClickListener(v -> {
            Intent intent = new Intent(DangKy.this, MainActivity.class);
            startActivity(intent);
        });

        // lấy giá trị các et và button khi đăng ký
        etTen = findViewById(R.id.et_Ten);
        etEmailDangKy = findViewById(R.id.et_Email_Dang_Ky);
        etMatKhauDangKy = findViewById(R.id.et_Mat_Khau_Dang_ky);
        etNhapLaiMatKhauDangKy = findViewById(R.id.et_Nhap_Lai_Mat_Khau_Dang_ky);
        btDangKy = findViewById(R.id.bt_Dang_ky);

        btDangKy.setOnClickListener(v -> {
            String ten = etTen.getText().toString();
            String email = etEmailDangKy.getText().toString();
            String matKhau = etMatKhauDangKy.getText().toString();
            String nhapLaiMatKhau = etNhapLaiMatKhauDangKy.getText().toString();

            if (ten.isEmpty() || email.isEmpty() || matKhau.isEmpty() || nhapLaiMatKhau.isEmpty()) {
                Toast.makeText(DangKy.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else if (!matKhau.equals(nhapLaiMatKhau)) {
                Toast.makeText(DangKy.this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
            } else {
                // Lưu thông tin tài khoản vào SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyTimePrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("ten", ten);
                editor.putString("email", email);
                editor.putString("matKhau", matKhau);
                editor.apply();

                // lưu tên user
                saveUserName(ten);
                // lưu mail
                saveMail(email);

                // Đăng ký thành công, chuyển về MainActivity
                Intent intent = new Intent(DangKy.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(DangKy.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                finish(); // Đóng activity hiện tại
            }
        });
    }

    private void saveUserName(String userName) {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", userName);
        editor.apply();
    }

    private void saveMail(String mail) {
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("mail", mail); // Đổi "username" thành "mail"
        editor.apply();
    }
}
