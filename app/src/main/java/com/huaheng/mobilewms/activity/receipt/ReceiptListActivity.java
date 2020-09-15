package com.huaheng.mobilewms.activity.receipt;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.adapter.ChooseAdapter;
import com.huaheng.mobilewms.bean.ChooseBean;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * * 入库
 */
public class ReceiptListActivity extends CommonActivity {

    @BindView(R.id.list_common)
    ListView listCommon;
    private ChooseAdapter mAdapter;
    private List<ChooseBean> chooseBeanList;
    private final int COLLECTION_BULK = 0;
    private final int QUICK_COLLECTION = 1;
    private final int EMPTY_IN = 2;
    private final int CALL_BOX = 3;
    private final int SUPPLE_RECEIPT = 4;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_list_common);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setTitle(getString(R.string.collect_goods));
        initView();
    }

    private void initView() {
        chooseBeanList = new ArrayList<>();
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_collect), this.getString(R.string.bulk_collection)));
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_collect), this.getString(R.string.quick_collection)));
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_collect), this.getString(R.string.empty_container_in)));
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_collect), this.getString(R.string.call_box_in)));
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_collect), this.getString(R.string.replenish_the_warehouse)));
        mAdapter = new ChooseAdapter(this);
        listCommon.setAdapter(mAdapter);
        listCommon.setOnItemClickListener(listener);
        mAdapter.setList(chooseBeanList);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            switch (position) {
                case COLLECTION_BULK:
                    WMSUtils.startActivity(mContext, ReceiptActivity.class);
                    break;
                case QUICK_COLLECTION:
                    WMSUtils.startActivity(mContext, QuickReceiptActivity.class);
                    break;
                case EMPTY_IN:
                    WMSUtils.startActivity(mContext, EmptyInActivity.class);
                    break;
                case CALL_BOX:
                    WMSUtils.startActivity(mContext, CallBoxActivity.class);
                    break;
                case SUPPLE_RECEIPT:
                    WMSUtils.startActivity(mContext, ReceiptSupplementActivity.class);
                    break;
            }
        }
    };
}
