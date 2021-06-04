package com.oobiliuoo.parkingtong.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.utils.Utils;

public class ChangeInfoActivity extends AppCompatActivity {

    private Button btnChange;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        initView();
    }

    private void initView() {
        btnChange = findViewById(R.id.changeInfo_btn_changeInfo);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(ChangeInfoActivity.this,"修改成功");
            }
        });
    }
}