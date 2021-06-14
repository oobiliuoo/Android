package com.oobiliuoo.parkingtong.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.database.CarInfo;

import org.litepal.LitePal;

import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        testCarInfo();

    }

    private void testCarInfo() {

        List<CarInfo> car = LitePal.findAll(CarInfo.class);
        if(car.size()>0){
            for(CarInfo temp : car){
                Utils.mLog1("MA-testCarInfo",temp.getCarNum()+" "+temp.getCarModel());
            }
        }
    }
}