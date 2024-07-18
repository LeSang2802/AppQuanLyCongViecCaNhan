package com.example.mytime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TrangCaNhan extends AppCompatActivity {
    TextView tv_ten_user_tcn1;

    TextView tv_ten_user_tcn2;

    TextView tv_email_tcn;

    ImageView img_trangchu;

    ImageView img_thongbao;

    ImageView img_timkiem;

    ImageView img_danhsach;

    ImageView imgback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.trang_ca_nhan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //
        tv_email_tcn = findViewById(R.id.tv_email_tcn);
        tv_ten_user_tcn2 = findViewById(R.id.tv_ten_user_tcn2);
        tv_ten_user_tcn1 = findViewById(R.id.tv_ten_user_tcn1);
        img_trangchu = findViewById(R.id.img_trangchu);
        img_thongbao = findViewById(R.id.img_thongbao);
        img_timkiem = findViewById(R.id.img_timkiem);
        img_danhsach = findViewById(R.id.img_danhsach);
        imgback = findViewById(R.id.img_back);


        // Lấy tên người dùng từ SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String tenUser = sharedPref.getString("username", "Name");
        String emailUser = sharedPref.getString("mail", "Email");

        // Gán tên người dùng lên TextView
        tv_ten_user_tcn1.setText(tenUser);
        tv_ten_user_tcn2.setText(tenUser);

        // Gán mail lên textview
        tv_email_tcn.setText(emailUser);



        // Các icon img chuyển trang :
        // Sự kiện cho nút back - quay về trang chủ
        imgback.setOnClickListener(v -> {
            Intent intent = new Intent(TrangCaNhan.this, TrangChu.class);
            startActivity(intent);
        });

        // Sự kiệm img trang chủ - quay về trang chủ
        img_trangchu.setOnClickListener(v -> {
            Intent intent = new Intent(TrangCaNhan.this, TrangChu.class);
            startActivity(intent);
        });

        // Sự kiệm img thong bao - chuyen trang thong bao
        img_thongbao.setOnClickListener(v -> {
            Intent intent = new Intent(TrangCaNhan.this, ThongBao.class);
            startActivity(intent);
        });

        // Sự kiệm img tim kiem - chuyen trang tim kiem
        img_timkiem.setOnClickListener(v -> {
            Intent intent = new Intent(TrangCaNhan.this, TimKiem.class);
            startActivity(intent);
        });

        // Sự kiệm img danh sach - chuyen trang danh sach
        img_danhsach.setOnClickListener(v -> {
            Intent intent = new Intent(TrangCaNhan.this, DanhSach.class);
            startActivity(intent);
        });
    }
}
