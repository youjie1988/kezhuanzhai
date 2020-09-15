package com.huaheng.mobilewms.activity.inventory;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.adapter.InventoryDetailAdapter;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.DictData;
import com.huaheng.mobilewms.bean.InventoryDetail;
import com.huaheng.mobilewms.bean.InventoryDetailBean;
import com.huaheng.mobilewms.bean.InventoryTransaction;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.EditLayout;
import com.huaheng.mobilewms.view.SpinnerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by youjie on 2020/6/1
 */
public class InventoryTransactionActivity extends CommonActivity {

    @BindView(R.id.list)
    ListView listView;
    private InventoryDetailAdapter mAdapter;
    private List <InventoryDetailBean> detailBeanList;
    private boolean isShowing = false;
    private PopupWindow popupWindow;
    private List <InventoryTransaction> mInventoryTransactions;
    private EditLayout containerEdit, materialNameEdit, materialCodeEdit, materialSpecEdit, startTimeEdit, endTimeEdit;
    private SpinnerLayout transactionTypeSpinner;
    private List<DictData> mDictDataList;
    private MyHandler myHandler = new MyHandler();
    private int type = 0;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.menu_inventory_transaction));
        initView();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String str = bundle.getString("time");
            type = bundle.getInt("type");
            startTimeEdit.getEditContent().setText(str);
        }
        getDictListData("inventoryTransactionType");
    }

    private void initView() {
        chooseImage.setVisibility(View.VISIBLE);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        transactionTypeSpinner = WMSUtils.newSpinner(mContext, mContext.getString(R.string.transactionType));
        startTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.start_time));
    }

    private void initPopupWindow() {
        View view = View.inflate(mContext, R.layout.choose_popup_window, null);
        popupWindow = new PopupWindow(view, MATCH_PARENT, 700);//参数为1.View 2.宽度 3.高度
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
        transactionTypeSpinner.setLineArray(mContext, getTrasactionTypeList());
        transactionTypeSpinner.getSpinner().setSelection(getTypeSelect(String.valueOf(type)));
        contentLyaout.addView(transactionTypeSpinner);
        materialCodeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialCode));
        contentLyaout.addView(materialCodeEdit);
        materialNameEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialName));
        contentLyaout.addView(materialNameEdit);
        materialSpecEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialSpec));
        contentLyaout.addView(materialSpecEdit);
        contentLyaout.addView(startTimeEdit);
        endTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.end_time));
        contentLyaout.addView(endTimeEdit);
        WMSUtils.setOnDatePick(mContext, startTimeEdit.getEditContent());
        WMSUtils.setOnDatePick(mContext, endTimeEdit.getEditContent());
    }

    private int getTypeSelect(String value) {
        for(int i=0; i < mDictDataList.size(); i++ ) {
            DictData dictData = mDictDataList.get(i);
            if(value.equals(dictData.getDictValue())) {
                return i+1;
            }
        }
        return 0;
    }

    private List<String> getTrasactionTypeList() {
        List<String> strs = new ArrayList <>();
        strs.add(mContext.getString(R.string.all));
        for(DictData dictData : mDictDataList) {
            strs.add(dictData.getDictLabel());
        }
        return strs;
    }

    private void search() {
        String containerCode = containerEdit.getEditValue();
        String transactionTypeValue = (String)transactionTypeSpinner.getSpinner().getSelectedItem();
        String materialCode = materialCodeEdit.getEditValue();
        String materialName =  materialNameEdit.getEditValue();
        String materialSpec = materialSpecEdit.getEditValue();
        String startTime = startTimeEdit.getEditValue();
        String endTime = endTimeEdit.getEditValue();

        transactionTypeValue = getTransactionType(transactionTypeValue);
        searchInventoryTransactionInCondition(containerCode, transactionTypeValue, materialCode, materialName, materialSpec, startTime, endTime);
        dismiss();
    }

    private String getTransactionType(String transactionTypeValue) {
        String value = "";
        for(DictData dictData : mDictDataList) {
            String label = dictData.getDictLabel();
            if(transactionTypeValue.equals(mContext.getString(R.string.all))) {
                break;
            }
            if(transactionTypeValue.equals(label)) {
               value = dictData.getDictValue();
               break;
            }
        }
        return value;
    }


    private void reset() {
        WMSUtils.resetEdit(containerEdit.getEditContent());
        transactionTypeSpinner.getSpinner().setSelection(0);
        WMSUtils.resetEdit(materialNameEdit.getEditContent());
        WMSUtils.resetEdit(materialCodeEdit.getEditContent());
        WMSUtils.resetEdit(materialSpecEdit.getEditContent());
        WMSUtils.resetEdit(startTimeEdit.getEditContent());
        WMSUtils.resetEdit(endTimeEdit.getEditContent());
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

    private void showContent(List <InventoryTransaction> inventoryTransactionList) {
        detailBeanList = new ArrayList <>();
        for (InventoryTransaction inventoryTransaction : inventoryTransactionList) {
            detailBeanList.add(new InventoryDetailBean(mContext.getResources().getDrawable(R.mipmap.icon_inventory),
                    inventoryTransaction.getContainerCode(), inventoryTransaction.getMaterialCode(), inventoryTransaction.getMaterialName(), String.valueOf(inventoryTransaction.getTaskQty())));
        }
        if (mAdapter == null) {
            mAdapter = new InventoryDetailAdapter(mContext);
            listView.setAdapter(mAdapter);
        }
        mAdapter.setList(detailBeanList);
        mAdapter.notifyDataSetChanged();
    }

    private void sendInventory(List <InventoryTransaction> inventoryTransactionList) {
        Message message = new Message();
        message.obj = inventoryTransactionList;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List <InventoryTransaction> inventoryTransactionList = (List <InventoryTransaction>) msg.obj;
                    showContent(inventoryTransactionList);
                    break;
            }
        }
    }

    private void searchInventoryTransactionInCondition(String containerCode, String transactionType, String materialCode,
                                            String materialName, String materialSpec, String startTime, String endTime) {
        HttpInterface.getInsstance().searchInventoryTransactionInCondition(new ProgressSubscriber<List<InventoryTransaction>>(mContext, searchInLocationListener),
                containerCode, transactionType, materialCode, materialName, materialSpec, startTime, endTime);
    }

    SubscriberOnNextListener searchInLocationListener = new SubscriberOnNextListener <List <InventoryTransaction>>() {
        @Override
        public void onNext(List <InventoryTransaction> inventoryTransactionList) {
            if (WMSUtils.isNotEmptyList(inventoryTransactionList)) {
                sendInventory(inventoryTransactionList);
            } else {
                WMSUtils.showShort(mContext.getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };

    private void getDictListData(String dictType) {
        HttpInterface.getInsstance().getDictListData(new ProgressSubscriber<List<DictData>>(mContext, dictListener), dictType);
    }

    SubscriberOnNextListener dictListener = new SubscriberOnNextListener<List<DictData>>() {
        @Override
        public void onNext(List<DictData> dictDataList) {
            if(dictDataList != null) {
                mDictDataList = dictDataList;
                initPopupWindow();
                search();
            } else {
                WMSUtils.showShort(getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };
}
