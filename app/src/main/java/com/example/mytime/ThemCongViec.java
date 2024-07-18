package com.example.mytime;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.EditText;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


import java.util.Calendar;


public class ThemCongViec extends DialogFragment {

    //Khởi tạo
    private EditText et_MoTaCV;
    private ImageView img_Ngay;

    private ImageView img_time;
    private ImageView img_next;
    private TextView tv_Ngay_Them;

    private TextView tv_Gio_Them;
    private DatePicker datePicker;

    private TimePicker timePicker;


    private DatabaseHelper databaseHelper;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.them_cong_viec, container, false);

        // Khai bao databasehelper
        databaseHelper = new DatabaseHelper(getContext());


        // Tham chiếu đến giao diện
        et_MoTaCV = view.findViewById(R.id.et_Mo_ta_cong_viec);
        img_Ngay = view.findViewById(R.id.img_today);
        img_time = view.findViewById(R.id.img_time);
        img_next = view.findViewById(R.id.img_next);
        tv_Ngay_Them = view.findViewById(R.id.tv_Ngay_Them);
        tv_Gio_Them = view.findViewById(R.id.tv_Gio_Them);
        datePicker = view.findViewById(R.id.datePicker);
        timePicker = view.findViewById(R.id.time_picker);

        // Khi imageview ngày được chọn thì hiện datePicker lên để chọn ngày
        img_Ngay.setOnClickListener(v -> {
            if (datePicker.getVisibility() == View.GONE) {
                datePicker.setVisibility(View.VISIBLE);
            } else {
                datePicker.setVisibility(View.GONE);
            }
        });

        // Khi imageview giờ được chọn thì hiện timePicker lên để chọn giờ
        img_time.setOnClickListener(v -> {
            if (timePicker.getVisibility() == View.GONE) {
                timePicker.setVisibility(View.VISIBLE);
            } else {
                timePicker.setVisibility(View.GONE);
            }
        });

        // Lấy giá trị ngày được chọn trong datePicker để gán vào TextView Ngày
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener((view1, year, monthOfYear, dayOfMonth) -> {
                String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                tv_Ngay_Them.setText(selectedDate);
            });
        }

        // Lấy giá trị giờ được chọn trong TimePicker để gán vào TextView Giờ
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
                String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                tv_Gio_Them.setText(selectedTime);
            });
        } else {
            timePicker.setOnTimeChangedListener((view1, hourOfDay, minute) -> {
                String selectedTime = hourOfDay + ":" + minute;
                tv_Gio_Them.setText(selectedTime);
            });
        }



        img_next.setOnClickListener(v -> {
            String taskDescription = et_MoTaCV.getText().toString().trim();
            String taskDate = tv_Ngay_Them.getText().toString();
            String taskTime = tv_Gio_Them.getText().toString();


            if (!taskDescription.isEmpty() && !taskDate.isEmpty() && !taskTime.isEmpty()) {
                boolean isInserted = databaseHelper.insertTask(taskDescription, taskDate, taskTime);
                if (isInserted) {
                    dismiss(); // Đóng dialog
                } else {
                    // Xử lý trường hợp lỗi
                }
            }
        });




        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            getDialog().getWindow().setDimAmount(0.5f); // Adjust the dim amount here
        }
    }
}
