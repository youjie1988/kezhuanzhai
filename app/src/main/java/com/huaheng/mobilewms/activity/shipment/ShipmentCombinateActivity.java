package com.huaheng.mobilewms.activity.shipment;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.util.WMSUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipmentCombinateActivity extends CommonActivity {

    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);

        setContentView(R.layout.activity_shipment_combinate);
        ButterKnife.bind(this);
        setTitle("配盘详情");
        initView();
    }

    private void initView() {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.container_number), "M00100"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.location), "L01-01-02"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_type), "整盘出库"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.combinate_station), "出库口1"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialCode), "1234567890"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialName), "可口可乐"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.number), "20"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_create), "李成"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_craeted), "2020-03-30 16:14"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
    }


}
