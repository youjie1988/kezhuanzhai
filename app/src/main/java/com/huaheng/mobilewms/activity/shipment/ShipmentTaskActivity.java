package com.huaheng.mobilewms.activity.shipment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShipmentTaskActivity extends CommonActivity {

    @BindView(R.id.list)
    ListView list;
    private DetailAdapter mAdapter;
    private List <DetailBean> detailBeanList;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);

        setContentView(R.layout.activity_shipment_task);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.shipment_task));
        initView();
    }

    private void initView() {
        detailBeanList = new ArrayList <>();
        detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.icon_task), "M00100", "l01-01-02", "整盘出库"));
        detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.icon_task), "M00101", "l01-01-03", "整盘出库"));
        detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.icon_task), "M00102", "l01-02-02", "整盘出库"));
        mAdapter = new DetailAdapter(mContext);
        list.setAdapter(mAdapter);
        list.setOnItemClickListener(listener);
        mAdapter.setList(detailBeanList);
    }


    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {
            DetailBean detailBean = detailBeanList.get(position);
            WMSUtils.startActivity(mContext, ShipmentCombinateActivity.class);
        }
    };

    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        finish();
        WMSUtils.startActivity(mContext, ShipmentActivity.class);
    }
}
