package com.huaheng.mobilewms.activity.inventory;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.adapter.InventoryDetailAdapter;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.InventoryDetail;
import com.huaheng.mobilewms.bean.InventoryDetailBean;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.EditLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class InventoryActivity extends CommonActivity {

    @BindView(R.id.list)
    ListView listView;
    private InventoryDetailAdapter mAdapter;
    private List <InventoryDetailBean> detailBeanList;
    private List <InventoryDetail> mInventoryDetails;
    private MyHandler myHandler = new MyHandler();
    private PopupWindow popupWindow;
    private boolean isShowing = false;
    EditLayout containerEdit, materialNameEdit, materialCodeEdit, materialSpecEdit, startTimeEdit, endTimeEdit;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.menu_inventory));
        initView();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String time = bundle.containsKey("time") ? bundle.getString("time") : null;
            String code = bundle.containsKey("code") ? bundle.getString("code") : null;
            if(time != null) {
                startTimeEdit.getEditContent().setText(time);
            }
            if(code != null) {
                materialCodeEdit.getEditContent().setText(code);
            }
        }
        search();
    }

    private void initView() {
        chooseImage.setVisibility(View.VISIBLE);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        View view = View.inflate(mContext, R.layout.choose_popup_window, null);
        popupWindow = new PopupWindow(view, MATCH_PARENT, 650);//参数为1.View 2.宽度 3.高度
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isShowing = false;
            }
        });
        Button ensureBtn = view.findViewById(R.id.ensureBtn);
        ensureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        Button resetBtn = view.findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        LinearLayout contentLyaout = view.findViewById(R.id.contentLayout);
        containerEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.container_number));
        contentLyaout.addView(containerEdit);
        materialCodeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialCode));
        contentLyaout.addView(materialCodeEdit);
        materialNameEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialName));
        contentLyaout.addView(materialNameEdit);
        materialSpecEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialSpec));
        contentLyaout.addView(materialSpecEdit);
        startTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.start_time));
        contentLyaout.addView(startTimeEdit);
        endTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.end_time));
        contentLyaout.addView(endTimeEdit);
        WMSUtils.setOnDatePick(mContext, startTimeEdit.getEditContent());
        WMSUtils.setOnDatePick(mContext, endTimeEdit.getEditContent());
    }

    private void search() {
        String containerCode = containerEdit.getEditValue();
        String materialCode = materialCodeEdit.getEditValue();
        String materialName = materialNameEdit.getEditValue();
        String materialSpet = materialSpecEdit.getEditValue();
        String startTime = startTimeEdit.getEditValue();
        String endTime = endTimeEdit.getEditValue();

        searchInventoryInCondition(containerCode, materialCode, materialName, materialSpet, startTime, endTime);
        dismiss();
    }

    private void reset() {
        WMSUtils.resetEdit(containerEdit.getEditContent());
        WMSUtils.resetEdit(materialCodeEdit.getEditContent());
        WMSUtils.resetEdit(materialNameEdit.getEditContent());
        WMSUtils.resetEdit(materialSpecEdit.getEditContent());
        WMSUtils.resetEdit(startTimeEdit.getEditContent());
        WMSUtils.resetEdit(endTimeEdit.getEditContent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismiss();
    }

    private void show() {
        if (!isShowing) {
            popupWindow.showAtLocation(commonLayout, Gravity.TOP, 0,150);
            isShowing = true;
        } else {
            dismiss();
        }
    }

    private void dismiss() {
        if (popupWindow != null) {
            popupWindow.dismiss();
            isShowing = false;
        }
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {
            int inventoryDetailId = mInventoryDetails.get(position).getId();
            Intent intent = new Intent();
            intent.setClass(mContext, InventoryDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("inventoryDetailId", inventoryDetailId);
            intent.putExtras(bundle);// 发送数据
            mContext.startActivity(intent);
        }
    };

    private void showContent(List <InventoryDetail> inventoryDetailList) {
        detailBeanList = new ArrayList <>();
        for (InventoryDetail inventoryDetail : inventoryDetailList) {
            detailBeanList.add(new InventoryDetailBean(mContext.getResources().getDrawable(R.mipmap.icon_inventory),
                    inventoryDetail.getContainerCode(), inventoryDetail.getMaterialCode(), inventoryDetail.getMaterialName(), String.valueOf(inventoryDetail.getQty())));
        }
        if (mAdapter == null) {
            mAdapter = new InventoryDetailAdapter(mContext);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(listener);
        }
        mAdapter.setList(detailBeanList);
        mAdapter.notifyDataSetChanged();
    }

    private void sendInventory(List <InventoryDetail> inventoryDetailList) {
        Message message = new Message();
        message.obj = inventoryDetailList;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List <InventoryDetail> inventoryDetailList = (List <InventoryDetail>) msg.obj;
                    showContent(inventoryDetailList);
                    break;
            }
        }
    }

    private void searchInventoryInCondition(String containerCode, String materialCode,
                                           String materialName, String materialSpec, String startTime, String endTime) {
        HttpInterface.getInsstance().searchInventoryInCondition(new ProgressSubscriber <List <InventoryDetail>>(mContext, searchInLocationListener),
                containerCode, materialCode, materialName, materialSpec, startTime, endTime);
    }

    SubscriberOnNextListener searchInLocationListener = new SubscriberOnNextListener <List <InventoryDetail>>() {
        @Override
        public void onNext(List <InventoryDetail> inventoryDetailList) {
            if (WMSUtils.isNotEmptyList(inventoryDetailList)) {
                mInventoryDetails = inventoryDetailList;
                sendInventory(inventoryDetailList);
            } else {
                WMSUtils.showShort(mContext.getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };
}


