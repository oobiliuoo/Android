package com.oobiliuoo.parkingtong.ui;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.adapter.OrderInfoAdapter;
import com.oobiliuoo.parkingtong.database.OrderInfo;
import com.oobiliuoo.parkingtong.database.UsersInfo;
import com.oobiliuoo.parkingtong.utils.Utils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView listView;

    private List<OrderInfo> orderInfoList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Utils.mLog1("OF:onCreate");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onStart() {
        Utils.mLog1("OF:onStart");
        super.onStart();
        if(listView == null){ initView();}

    }

    private void initView() {

        initOrderInfo();
        Utils.mLog1("OrderF:initView");
        OrderInfoAdapter adapter = new OrderInfoAdapter(getContext(),R.layout.layout_orderinfo,orderInfoList);

        listView = getView().findViewById(R.id.order_lv_1);

        listView.setAdapter(adapter);

    }
    private void initOrderInfo() {
        // 从本地数据库读取数据
        // 读取当前登录用户
        String tel =  Utils.readCurrentUser(getContext());
        if(tel!=""){
            // 查询数据库中数据
            SQLiteDatabase db = LitePal.getDatabase();
            orderInfoList = LitePal.where("orderTel = ?",tel).order("id desc").find(OrderInfo.class);
        }

        // 以下为自定义数据
        OrderInfo info1 = new OrderInfo("EE1900250145","新世纪超级无敌多的停车场出入口"
                ,"2021-05-25 13:51","预订");
        info1.setMoney("36.50");
        orderInfoList.add(info1);

        OrderInfo info3 = new OrderInfo("BB1250041145","北湖停车场"
                ,"2021-05-25 13:51","待支付");
        info3.setMoney("500");
        orderInfoList.add(info3);

        OrderInfo info4 = new OrderInfo("CC1900250145","苏仙停车场"
                ,"2021-05-25 13:51","已完成");
        info4.setMoney("200");
        orderInfoList.add(info4);


    }


    @Override
    public void onPause() {
        super.onPause();
        Utils.mLog1("OF:onPause");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
        Utils.mLog1("OF:onStop");
    }

}