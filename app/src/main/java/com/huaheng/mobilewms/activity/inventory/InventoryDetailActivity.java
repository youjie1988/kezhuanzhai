package com.huaheng.mobilewms.activity.inventory;

import android.os.Bundle;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonInfoActivity;
import com.huaheng.mobilewms.bean.InventoryDetail;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;

import butterknife.ButterKnife;

public class InventoryDetailActivity extends CommonInfoActivity {

    private int inventoryDetailId;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.menu_inventory_detail));
        Bundle bundle = getIntent().getExtras();
        inventoryDetailId = bundle.getInt("inventoryDetailId");
        findInventory(inventoryDetailId);
    }

    private void showContent(InventoryDetail inventoryDetail) {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.location), inventoryDetail.getLocationCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.container_number), inventoryDetail.getContainerCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialCode), inventoryDetail.getMaterialCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialName), inventoryDetail.getMaterialName()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialSpec), inventoryDetail.getMaterialSpec()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialUnit), inventoryDetail.getMaterialUnit()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.inventory), String.valueOf(inventoryDetail.getQty())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.weight), inventoryDetail.getWeight()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.preview_lock_number), String.valueOf(inventoryDetail.getTaskQty())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.company_code), inventoryDetail.getCompanyCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.supplier_code), inventoryDetail.getSupplierCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.project_number), inventoryDetail.getProjectNo()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.batch_number), inventoryDetail.getBatch()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.warehouse_in_time),inventoryDetail.getCreated()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.update_user), inventoryDetail.getCreatedBy()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
    }

    private void findInventory(int inventoryDetailId) {
        HttpInterface.getInsstance().findInventory(new ProgressSubscriber<InventoryDetail>(mContext, findListener), String.valueOf(inventoryDetailId));
    }

    SubscriberOnNextListener findListener = new SubscriberOnNextListener<InventoryDetail>() {
        @Override
        public void onNext(InventoryDetail inventoryDetail) {
            if(inventoryDetail != null) {
                showContent(inventoryDetail);
            } else {
                WMSUtils.showShort(getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };

}
