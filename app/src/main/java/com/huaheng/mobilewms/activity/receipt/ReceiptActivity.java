package com.huaheng.mobilewms.activity.receipt;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDAListActivity;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.activity.receipt.pingku.PingkuReceiptActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.DictData;
import com.huaheng.mobilewms.bean.InventoryDetail;
import com.huaheng.mobilewms.bean.Receipt;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.EditLayout;
import com.huaheng.mobilewms.view.SpinnerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ReceiptActivity extends PDAListActivity {

    private DetailAdapter mAdapter;
    private List<DetailBean> detailBeanList;
    private MyHandler myHandler = new MyHandler();
    private int DELAY = 1000;
    private List<ReceiptType> mReceiptTypeList;
    private PopupWindow popupWindow;
    private boolean isShowing = false;
    private List<DictData> mDictDataList;
    EditLayout codeEdit,startTimeEdit, endTimeEdit;
    SpinnerLayout receiptTypeSpinner, statusSpinner;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.bulk_collection));
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismiss();
        getReceiptType();
    }

    @OnClick(R.id.ensureBtn)
    public void onViewClicked() {
        addReceipt();
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

    private void addReceipt() {
        WMSUtils.startActivity(mContext, AddReceiptActivity.class);
    }

    private void initView() {
        ensureBtn.setText(mContext.getString(R.string.add_receipt));
        enableButton(true);
        ensureLayout.setVisibility(View.VISIBLE);
        inputEdit.setHint(mContext.getString(R.string.enter_receipt_number));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    findReceipt(number);
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
        chooseImage.setVisibility(View.VISIBLE);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

    }

    private void initPopupWindow() {
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
        codeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.receipt_code));
        contentLyaout.addView(codeEdit);
        receiptTypeSpinner = WMSUtils.newSpinner(mContext, mContext.getString(R.string.receipt_type));
        receiptTypeSpinner.setLineArray(mContext, getReceiptTypeList());
        contentLyaout.addView(receiptTypeSpinner);
        statusSpinner = WMSUtils.newSpinner(mContext, mContext.getString(R.string.receipt_status));
        statusSpinner.setLineArray(mContext, getStatusArray());
        contentLyaout.addView(statusSpinner);
        startTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.start_time));
        contentLyaout.addView(startTimeEdit);
        endTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.end_time));
        contentLyaout.addView(endTimeEdit);
        WMSUtils.setOnDatePick(mContext, startTimeEdit.getEditContent());
        WMSUtils.setOnDatePick(mContext, endTimeEdit.getEditContent());
    }

    private List<String> getReceiptTypeList() {
        List<String> strs = new ArrayList <>();
        strs.add(mContext.getString(R.string.all));
        for(ReceiptType receiptType : mReceiptTypeList) {
            strs.add(receiptType.getName());
        }
        return  strs;
    }

    private List<String> getStatusArray() {
        List<String> strs = new ArrayList <>();
        strs.add(mContext.getString(R.string.all));
        for(DictData dictData : mDictDataList) {
            strs.add(dictData.getDictLabel());
        }
        return strs;
    }

    private void search() {
        String code = codeEdit.getEditValue();
        String receiptTypeValue = (String)receiptTypeSpinner.getSpinner().getSelectedItem();
        String receiptValue = "";
        String lastStatus =(String)statusSpinner.getSpinner().getSelectedItem();
        String status = "";
        String startTime = startTimeEdit.getEditValue();
        String endTime = endTimeEdit.getEditValue();
        for(ReceiptType receiptType : mReceiptTypeList) {
            String typeName = receiptType.getName();
            if(typeName.equals(receiptTypeValue)) {
                receiptValue = receiptType.getCode();
                break;
            } else {
                receiptValue = "";
            }
        }
        for(DictData dictData : mDictDataList) {
            if(dictData.getDictLabel().equals(lastStatus)) {
                status = dictData.getDictValue();
                break;
            } else {
                status = "";
            }
        }
        searchReceiptInCondition(code, receiptValue, status, startTime, endTime);
        dismiss();
    }

    private void reset() {
        WMSUtils.resetEdit(codeEdit.getEditContent());
        receiptTypeSpinner.getSpinner().setSelection(0);
        statusSpinner.getSpinner().setSelection(0);
        WMSUtils.resetEdit(startTimeEdit.getEditContent());
        WMSUtils.resetEdit(endTimeEdit.getEditContent());
    }

    private void showContent(List<ReceiptHeader> receiptHeaders) {
        detailBeanList = new ArrayList<>();
        WMSLog.d("showContent :" + receiptHeaders);
        for(ReceiptHeader receiptHeader : receiptHeaders) {
            int lastStatus = 0;
            if(receiptHeader.getLastStatus() != null) {
                lastStatus = receiptHeader.getLastStatus();
            }
            String type = receiptHeader.getReceiptType();
            for(ReceiptType receiptType : mReceiptTypeList) {
                if(receiptType.getCode().equals(type)) {
                    type = receiptType.getName();
                    break;
                }
            }
            if(lastStatus == 800) {
                detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.receipt_bill),
                        receiptHeader.getCode(), type, receiptHeader.getCreated(), true));
            } else {
                detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.receipt_bill),
                        receiptHeader.getCode(), type, receiptHeader.getCreated()));
            }
        }
        if(mAdapter == null) {
            mAdapter = new DetailAdapter(mContext);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(listener);
        }
        mAdapter.setList(detailBeanList);
        mAdapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {
            DetailBean detailBean = detailBeanList.get(position);
            String receiptCode = detailBean.getType();
            findReceipt(receiptCode);
        }
    };

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<ReceiptHeader> receiptHeaderList = (List<ReceiptHeader>)msg.obj;
                    showContent(receiptHeaderList);
                    break;
            }
        }
    }

    private void sendReceipt(List<ReceiptHeader> receiptHeaders) {
        Message message = new Message();
        message.obj = receiptHeaders;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private void findReceipt(String receiptCode) {
        HttpInterface.getInsstance().findReceipt(new ProgressSubscriber<Receipt>(mContext, receiptListener), receiptCode);
    }

    SubscriberOnNextListener receiptListener = new SubscriberOnNextListener<Receipt>() {
        @Override
        public void onNext(Receipt receipt) {
            if(receipt != null) {
                ReceiptHeader receiptHeader =  receipt.getReceiptHeader();
                List<ReceiptDetail> receiptDetails = receipt.getReceiptDetails();
                String type = receiptHeader.getReceiptType();
                for(ReceiptType receiptType : mReceiptTypeList) {
                    if(receiptType.getCode().equals(type)) {
                        type = receiptType.getName();
                    }
                }
                Intent intent = new Intent();
                intent.setClass(mContext, ReceiptDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("receiptCode", receiptHeader.getCode());
                bundle.putString("label", type);
                intent.putExtras(bundle);// 发送数据
                mContext.startActivity(intent);
            } else {
                WMSUtils.showShort(getString(R.string.toast_error));
                WMSUtils.resetEdit(inputEdit);
            }
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void searchReceipt() {
        HttpInterface.getInsstance().searchReceipt(new ProgressSubscriber<List<ReceiptHeader>>(mContext, searchListener));
    }

    SubscriberOnNextListener searchListener = new SubscriberOnNextListener<List<ReceiptHeader>>() {
        @Override
        public void onNext(List<ReceiptHeader> receiptHeaders) {
            if (WMSUtils.isNotEmptyList(receiptHeaders)) {
                sendReceipt(receiptHeaders);
            }
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };


    private void getReceiptType() {
        HttpInterface.getInsstance().getReceiptType(new ProgressSubscriber<List<ReceiptType>>(mContext, getReceiptTypeListener));
    }

    SubscriberOnNextListener getReceiptTypeListener = new SubscriberOnNextListener<List<ReceiptType>>() {
        @Override
        public void onNext(List<ReceiptType> receiptTypeList) {
            mReceiptTypeList = receiptTypeList;
            getDictListData("receiptHeaderStatus");
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
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

    private void searchReceiptInCondition(String code, String receiptType,
                                            String lastStatus, String startTime, String endTime) {
        HttpInterface.getInsstance().searchReceiptInCondition(new ProgressSubscriber <List <ReceiptHeader>>(mContext, searchInLocationListener),
                code, receiptType, lastStatus, startTime, endTime);
    }

    SubscriberOnNextListener searchInLocationListener = new SubscriberOnNextListener <List <ReceiptHeader>>() {
        @Override
        public void onNext(List<ReceiptHeader> receiptHeaders) {
            if (WMSUtils.isNotEmptyList(receiptHeaders)) {
                sendReceipt(receiptHeaders);
            } else {
                WMSUtils.showShort(mContext.getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };

}
