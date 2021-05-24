package com.baidu.mapsdkexample.routeplan;

import java.util.List;

import com.baidu.mapsdkexample.R;
import com.baidu.mapsdkexample.ui.FlowLayout;
import com.baidu.mapsdkexample.ui.TagAdapter;
import com.baidu.mapsdkexample.ui.TagInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 流式布局适配器
 */
public class TransitListItemFlowLayoutAdapter extends TagAdapter<TagInfo> {

    private Context mContext;

    private ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    public TransitListItemFlowLayoutAdapter(Context ctx, List<TagInfo> datas) {
        super(datas);

        lp.leftMargin = 0;
        lp.rightMargin = dip2px(3);
        lp.topMargin = dip2px(5);
        lp.bottomMargin = dip2px(5);

        mContext = ctx;
    }

    @Override
    public View getView(FlowLayout parent, int position, TagInfo tagInfo) {

        View view = View.inflate(this.mContext, R.layout.bus_flowlayout_item, null);
        ImageView arrow = view.findViewById(R.id.iv_transfer_icon);
        TextView stepDesc = view.findViewById(R.id.tv_step_desc);

        // 第一个item不展示箭头
        if (position == 0) {
            arrow.setVisibility(View.GONE);
        } else {
            arrow.setVisibility(View.VISIBLE);
        }
        view.setLayoutParams(lp);

        stepDesc.setText(tagInfo.getTagDesc());
        stepDesc.setPadding(dip2px(6), dip2px(1), dip2px(6), dip2px(1));

        return view;
    }

    private int dip2px(int dip) {
        if (mContext == null) {
            return 0;
        }
        return (int) (0.5F + mContext.getResources().getDisplayMetrics().density * dip);
    }
}
