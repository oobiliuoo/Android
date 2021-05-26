package com.oobiliuoo.parkingtong.adapter;

import android.content.Context;
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
import com.oobiliuoo.parkingtong.object.OrderInfo;
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

            view.setTag(viewHolder);

        }else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.orderID.setText(orderInfo.getOrderID());
        viewHolder.orderTime.setText(orderInfo.getOderTime());
        viewHolder.pName.setText(orderInfo.getParkName());
        viewHolder.state.setText(orderInfo.getState());
        if("已预订".equals(orderInfo.getState())){
            viewHolder.btn1.setVisibility(View.VISIBLE);
            viewHolder.inTime.setVisibility(View.INVISIBLE);
            viewHolder.iv1.setVisibility(View.INVISIBLE);
            viewHolder.outTime.setVisibility(View.INVISIBLE);
            viewHolder.money.setVisibility(View.INVISIBLE);

        }else if("停车中".equals(orderInfo.getState())){
            viewHolder.outTime.setVisibility(View.INVISIBLE);
            viewHolder.money.setVisibility(View.INVISIBLE);
            viewHolder.btn1.setVisibility(View.INVISIBLE);

        }else if("已完成".equals(orderInfo.getState())){
            viewHolder.money.setText("￥"+orderInfo.getMoney());

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

        Button btn1;

    }



}
