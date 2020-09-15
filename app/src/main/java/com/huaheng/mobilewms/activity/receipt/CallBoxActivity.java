package com.huaheng.mobilewms.activity.receipt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.adapter.InventoryDetailAdapter;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.InventoryDetail;
import com.huaheng.mobilewms.bean.InventoryDetailBean;
import com.huaheng.mobilewms.bean.Location;
import com.huaheng.mobilewms.bean.MobileInventory;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by youjie
 * 呼叫料盒
 * on 2020/1/17
 */
public class CallBoxActivity extends PDAListCommonActivity {

    private String mIds;
    private int type = 200;
    private List <InventoryDetailBean> detailBeanList = new ArrayList <>();
    private InventoryDetailAdapter mAdapter;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(getString(R.string.call_box_in));
        initView();
        enableButton(false);
    }

    private void initView() {
        inputEdit.setHint(mContext.getString(R.string.enter_task_number));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if (WMSUtils.isNotEmpty(number)) {
                    getInventoryInfo(number);
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });

        View replenishView = WMSUtils.newContent(mContext, mContext.getString(R.string.container_number), mContainerCode);
        replenishView.setOnClickListener(pickClickListener);
        contentLayout.addView(replenishView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
    }

    @OnClick({R.id.ensureLayout})
    public void onViewClicked() {
        if (mContainerCode == null && mLocation == null) {
            WMSUtils.showShort(mContext.getString(R.string.enter_correct_empty_container_in));
            return;
        }
        callBox();
    }

    private View.OnClickListener pickClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pickLocation();
        }
    };

    private void addLocationLayout(final ArrayList<MobileInventory> mobileInventories) {
        contentLayout.removeAllViews();
        View locationView = WMSUtils.newContent(mContext, mContext.getString(R.string.location),  mobileInventories.get(0).getLocationCode());
        contentLayout.addView(locationView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        View containerView = WMSUtils.newContent(mContext, mContext.getString(R.string.container_number),  mobileInventories.get(0).getContainerCode());
        containerView.setOnClickListener(pickClickListener);
        contentLayout.addView(containerView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        for(final MobileInventory mobileInventory : mobileInventories) {
                detailBeanList.add(new InventoryDetailBean(mContext.getResources().getDrawable(R.mipmap.icon_inventory),
                        mobileInventory.getContainerCode(), mobileInventory.getMaterialCode(), mobileInventory.getMaterialName(), String.valueOf(mobileInventory.getQty())));
        }
        if (mAdapter == null) {
            mAdapter = new InventoryDetailAdapter(mContext);
            listView.setAdapter(mAdapter);
        }
        mAdapter.setList(detailBeanList);
        mAdapter.notifyDataSetChanged();
        mIds = String.valueOf(mobileInventories.get(0).getId());
        enableButton(true);
    }

    private void addEmptyLayout(ArrayList<MobileInventory> mobileInventories) {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.location), mobileInventories.get(0).getLocationCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        View containerView = WMSUtils.newContent(mContext, mContext.getString(R.string.container_number),  mobileInventories.get(0).getContainerCode());
        containerView.setOnClickListener(pickClickListener);
        contentLayout.addView(containerView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        enableButton(true);
        mIds = mobileInventories.get(0).getLocationCode();
    }

    private void showListDialog(final List<Location> locationList) {
        if(locationList == null) {
            WMSUtils.showShort("没有找到适合的托盘");
            return;
        }
        List<String> locations = new ArrayList<>();
        for(Location location : locationList) {
            if(location.getStatus().equals("empty")) {
                locations.add(location.getContainerCode() + "                 空");
            } else {
                locations.add(location.getContainerCode() + "                 有");
            }
        }

        final String[] list = locations.toArray(new String[locations.size()]);
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(mContext);
        listDialog.setTitle("选择托盘出库");
        listDialog.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContainerCode = locationList.get(which).getContainerCode();
                mLocation =  locationList.get(which).getCode();
                getInventoryInfo(mContainerCode);
            }
        });
        listDialog.show();
    }

    private void getInventoryInfo(String code) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        HttpInterface.getInsstance().getInventoryInfo(new ProgressSubscriber<ArrayList<MobileInventory>>(this, inventoryInfoListener), code, companyCode, companyId);
    }

    SubscriberOnNextListener inventoryInfoListener = new SubscriberOnNextListener<ArrayList<MobileInventory>>() {

        @Override
        public void onNext(ArrayList<MobileInventory> mobileInventories) {
            int resultType = mobileInventories.get(0).getResultType();
            if(resultType == 0) {

            } else if(resultType == 1){

            } else if(resultType == 2){
                addEmptyLayout(mobileInventories);
                return;
            }
            BigDecimal total = new BigDecimal(0);
            for(MobileInventory mobileInventory : mobileInventories) {
                total = total.add(mobileInventory.getQty());
            }
            addLocationLayout(mobileInventories);
        }

        @Override
        public void onError(String str) {

        }
    };

    private void pickLocation() {
        HttpInterface.getInsstance().pickLocation(new ProgressSubscriber<List<Location>>(mContext, pickListener));
    }

    SubscriberOnNextListener pickListener = new SubscriberOnNextListener<List<Location>>() {

        @Override
        public void onNext(List<Location> locationList) {
            showListDialog(locationList);
        }

        @Override
        public void onError(String str) {
            WMSLog.d("onError:" + str);
        }
    };

    private void callBox() {
        HttpInterface.getInsstance().callBox(new ProgressSubscriber<String>(mContext, callBoxListener), mContainerCode, mLocation, type);
    }

    SubscriberOnNextListener callBoxListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String taskId) {
            execute(taskId);
        }

        @Override
        public void onError(String str) {
            WMSLog.d("onError:" + str);
        }
    };

    private void execute(String taskId) {
        HttpInterface.getInsstance().execute(new ProgressSubscriber<String>(this, executeListener), taskId);
    }

    SubscriberOnNextListener executeListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.call_box_success));
            finish();
            WMSUtils.startActivity(mContext, CallBoxActivity.class);
        }

        @Override
        public void onError(String str) {

        }
    };
}
