package com.oobiliuoo.parkingtong.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.database.CarInfo;
import com.oobiliuoo.parkingtong.database.CarModelTable;
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


        list = findViewById(R.id.manager_list);

        initView();

        //testCarInfo();
        //testOrderInfo();

    }

    private void initView() {
        btn_user = findViewById(R.id.manager_btn_user);
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<UsersInfo> order = LitePal.findAll(UsersInfo.class);
                data = new String[order.size()];
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
                //initDate();
                Utils.showToast(ManagerActivity.this,"test");
            }
        });

        btn_order = findViewById(R.id.manager_btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    List<OrderInfo> order = LitePal.findAll(OrderInfo.class);
                    data = new String[order.size()];
                    if (order.size() > 0) {
                        int i = 0;
                        for (OrderInfo temp : order) {
                            Utils.mLog1("MA-testOrderInfo", temp.getOrderID() + " " + temp.getParkName()
                                    + "" + temp.getOrderTel() + " " + temp.getState());
                            data[i] =temp.getOrderID() + " " + temp.getParkName() + "" + temp.getOrderTel() + " " + temp.getState();
                            i++;
                        }

                        Utils.showToast(ManagerActivity.this,data[0]);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManagerActivity.this, android.R.layout.simple_list_item_1,data);
                        list.setAdapter(adapter);
                    }

                }
        });


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

    private void initDate() {

        // 首次执行需要下面语句创建数据库
        SQLiteDatabase db = LitePal.getDatabase();

        String AODI_A8 = "奥迪A8";
        String[] AODI_A8_MODEL = {"2021款 A8L 50 TFSI quattro 舒适型"
                ,"2021款 A8L 50 TFSI quattro 豪华型"
                ,"2021款 A8L 55 TFSI quattro 尊贵型"};


        for(int i=0;i<3;i++) {
            CarModelTable carModel1 = new CarModelTable();
            carModel1.setCarName(AODI_A8);
            carModel1.setCarModel(AODI_A8_MODEL[i]);
            carModel1.save();
        }


        Utils.showToast(ManagerActivity.this,"initdata ok");
        Utils.mLog1("MF","initDate ok");

    }

}