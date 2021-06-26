package com.oobiliuoo.parkingtong.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.database.CarInfo;
import com.oobiliuoo.parkingtong.database.OrderInfo;
import com.oobiliuoo.parkingtong.database.UsersInfo;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    private Button btn_user;
    private Button btn_order;
    private Button btn_car;
    private ListView list;

    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        data = new String[20];

        initView();

        testCarInfo();
        testOrderInfo();

    }

    private void initView() {
        btn_user = findViewById(R.id.manager_btn_user);
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<UsersInfo> order = LitePal.findAll(UsersInfo.class);
                if (order.size() > 0) {
                    int i = 0;
                    for (UsersInfo temp : order) {
                        data[i] = " tel:" + temp.getTel() + " Name:" + temp.getName() + " Pwd:" + temp.getPwd();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManagerActivity.this, android.R.layout.simple_list_item_1,data);
                    list.setAdapter(adapter);
                }
            }
        });
        btn_car = findViewById(R.id.manager_btn_car);
        btn_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_order = findViewById(R.id.manager_btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<OrderInfo> order = LitePal.findAll(OrderInfo.class);
                    if (order.size() > 0) {
                        int i = 0;
                        for (OrderInfo temp : order) {
                            Utils.mLog1("MA-testOrderInfo", temp.getOrderID() + " " + temp.getParkName()
                                    + "" + temp.getOrderTel() + " " + temp.getState());
                            data[i] =temp.getOrderID() + " " + temp.getParkName() + "" + temp.getOrderTel() + " " + temp.getState();
                            i++;
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManagerActivity.this, android.R.layout.simple_list_item_1,data);
                        list.setAdapter(adapter);
                    }

                }
        });

        list = findViewById(R.id.manager_list);

    }

    private void testCarInfo() {

        List<CarInfo> car = LitePal.findAll(CarInfo.class);
        if(car.size()>0){
            for(CarInfo temp : car){
                Utils.mLog1("MA-testCarInfo",temp.getCarNum()+" "+temp.getCarModel());
            }
        }
    }

    private void testOrderInfo() {

        List<OrderInfo> order = LitePal.findAll(OrderInfo.class);
        if (order.size() > 0) {
            for (OrderInfo temp : order) {
                Utils.mLog1("MA-testOrderInfo", temp.getOrderID() + " " + temp.getParkName()
                        + "" + temp.getOrderTel() + " " + temp.getState());
            }

        }
    }
}