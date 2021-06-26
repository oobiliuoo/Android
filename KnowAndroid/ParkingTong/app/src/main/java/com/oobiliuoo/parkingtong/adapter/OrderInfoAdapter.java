package com.oobiliuoo.parkingtong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.oobiliuoo.parkingtong.R;
import com.oobiliuoo.parkingtong.database.OrderInfo;
import com.oobiliuoo.parkingtong.utils.Utils;

import java.util.List;

public class OrderInfoAdapter extends ArrayAdapter<OrderInfo> {

    private int resourceId;

    public OrderInfoAdapter(@NonNull Context context, int resource, @NonNull List<OrderInfo> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        OrderInfo orderInfo = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.orderID = (TextView) view.findViewById(R.id.orderInfo_orderId);
            viewHolder.orderTime = view.findViewById(R.id.orderInfo_tvOrderTime);
            viewHolder.pName = view.findViewById(R.id.orderInfo_pName);
            viewHolder.money = view.findViewById(R.id.orderInfo_tvMoney);
            viewHolder.state = view.findViewById(R.id.orderInfo_tvState);
            viewHolder.inTime = view.findViewById(R.id.orderInfo_inTime);
            viewHolder.outTime = view.findViewById(R.id.orderInfo_outTime);
            viewHolder.iv1 = view.findViewById(R.id.orderInfo_iv1);
            viewHolder.btn1 = view.findViewById(R.id.orderInfo_btnCancel);
            viewHolder.tvCarNum = view.findViewById(R.id.orderInfo_carNum);

            view.setTag(viewHolder);

        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.orderID.setText(orderInfo.getOrderID());
        viewHolder.orderTime.setText(orderInfo.getOderTime());
        viewHolder.pName.setText(orderInfo.getParkName());
        viewHolder.state.setText(orderInfo.getState());
        viewHolder.money.setText(orderInfo.getMoney());
        viewHolder.tvCarNum.setText(orderInfo.getCarNum());
        viewHolder.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showToast(getContext(),"取消" + viewHolder.orderID.getText().toString());
                if( "预订成功".equals(orderInfo.getState())){
                    orderInfo.setState("已取消");
                    orderInfo.save();
                    viewHolder.state.setText("已取消");
                    viewHolder.btn1.setVisibility(View.INVISIBLE);
                }

            }
        });

        if("已完成".equals(orderInfo.getState())){
            viewHolder.btn1.setVisibility(View.INVISIBLE);
            viewHolder.state.setTextColor(0xffc4c4c4);
            Utils.mLog1("OrderInfoA","C "+ viewHolder.pName.getText() + " " + viewHolder.state.getText());
        }


        if("预订成功".equals(orderInfo.getState())){
            viewHolder.btn1.setVisibility(View.VISIBLE);
            viewHolder.btn1.setText("取消");
            viewHolder.state.setTextColor(0xfffda97e);
            Utils.mLog1("OrderInfoA","O "+ viewHolder.pName.getText()+ " " + viewHolder.state.getText());

        }

        if("已取消".equals(orderInfo.getState())){
            viewHolder.btn1.setVisibility(View.INVISIBLE);
            viewHolder.state.setTextColor(0xffcc3333);
            Utils.mLog1("OrderInfoA","O "+ viewHolder.pName.getText()+ " " + viewHolder.state.getText());

        }


        return view;
    }


    class ViewHolder {

        TextView orderID;
        TextView orderTime;
        TextView pName;
        TextView money;
        TextView state;
        TextView inTime;
        TextView outTime;
        ImageView iv1;
        TextView tvCarNum;
        Button btn1;

    }



}
