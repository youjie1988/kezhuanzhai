package com.huaheng.mobilewms.activity.shipment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.activity.receipt.EmptyInActivity;
import com.huaheng.mobilewms.activity.receipt.ReceiptActivity;
import com.huaheng.mobilewms.adapter.ChooseAdapter;
import com.huaheng.mobilewms.bean.ChooseBean;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by youjie on 2020/4/23
 */
public class ShipmentListActivity extends CommonActivity {

    @BindView(R.id.list_common)
    ListView listCommon;
    private ChooseAdapter mAdapter;
    private List<ChooseBean> chooseBeanList;
    private final int SHIPMENT = 0;
    private final int EMPTY_OUT = 1;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_list_common);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setTitle(getString(R.string.shipment));
        initView();
    }

    private void initView() {
        chooseBeanList = new ArrayList<>();
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_collect), this.getString(R.string.bulk_shipment)));
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_collect), this.getString(R.string.empty_container_out)));
        mAdapter = new ChooseAdapter(this);
        listCommon.setAdapter(mAdapter);
        listCommon.setOnItemClickListener(listener);
        mAdapter.setList(chooseBeanList);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            switch (position) {
                case SHIPMENT:
                    WMSUtils.startActivity(mContext, ShipmentActivity.class);
                    break;
                case EMPTY_OUT:
                    WMSUtils.startActivity(mContext, EmptyOutActivity.class);
                    break;
            }
        }
    };
}
