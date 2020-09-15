package com.huaheng.mobilewms.activity.receipt.pingku;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.activity.receipt.ReceiptDetailActivity;
import com.huaheng.mobilewms.activity.receipt.ReceiptDetailInfoActivity;
import com.huaheng.mobilewms.adapter.DetailAmountAdapter;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.Receipt;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PingkuReceiptActivity extends PDAListCommonActivity {

    private DetailAmountAdapter mAdapter;
    private List<DetailBean> detailBeanList;
    private String receiptCode, label;
    private MyHandler myHandler = new MyHandler();
    private ArrayList<ReceiptBill> receiptBills = new ArrayList<>();
    private List<ReceiptDetail> mReceiptDetailList;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        receiptCode = bundle.getString("receiptCode");
        label = bundle.getString("label");
        setTitle(label);
        initView();
        findReceipt(receiptCode);
        enableButton(false);
    }

    private void initView() {
        inputEdit.setHint(mContext.getString(R.string.enter_location_number));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    number = WMSUtils.replaceLocation(number);
                    isLocation(number);
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
    }

    private void showContent() {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.receipt_no), receiptCode));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.location), mLocation));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        detailBeanList = new ArrayList<>();
        if(mReceiptDetailList == null) {
            return;
        }
        for(ReceiptDetail receiptDetail : mReceiptDetailList) {
            int tobeCollectQty = (int)receiptDetail.getTotalQty() - (int)receiptDetail.getOpenQty();
            DetailBean detailBean = new DetailBean(mContext.getResources().getDrawable(R.mipmap.kekoukele),
                    receiptDetail.getMaterialCode(), receiptDetail.getMaterialName(), String.valueOf(tobeCollectQty));
            detailBean.setMaxAmount(tobeCollectQty);
            detailBeanList.add(detailBean);
        }

        if(mAdapter == null) {
            mAdapter = new DetailAmountAdapter(mContext);
            listView.setAdapter(mAdapter);
        }
        mAdapter.setmList(detailBeanList);
        mAdapter.setOnAmountListener(new DetailAmountAdapter.AmountChangeListener() {
            @Override
            public void onAmountChange(int id, int amount) {
                ReceiptDetail receiptDetail = mReceiptDetailList.get(id);
                receiptDetail.setQty(amount);
                DetailBean detailBean = detailBeanList.get(id);
                detailBean.setAmount(amount);
                updateReceipt();
            }
        });
        updateReceipt();
    }

    private void reStart() {
        Intent intent = new Intent();
        intent.setClass(mContext, ReceiptDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("receiptCode", receiptCode);
        bundle.putString("label", label);
        intent.putExtras(bundle);// 发送数据
        mContext.startActivity(intent);
    }


    @OnClick(R.id.ensureBtn)
    public void onViewClicked() {
        listReceipt();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    showContent();
                    break;
            }
        }
    }

    private void sendReceipt(List<ReceiptDetail> receiptDetails) {
        Message message = new Message();
        message.obj = receiptDetails;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private void updateReceipt() {
        receiptBills.clear();
        boolean enable = false;
        for(int i = mReceiptDetailList.size() - 1; i>=0; i--) {
            ReceiptDetail receiptDetail = mReceiptDetailList.get(i);
            ReceiptBill receiptBill = new ReceiptBill();
            receiptBill.setLocationCode(mLocation);
            receiptBill.setQty(new BigDecimal(receiptDetail.getQty()));
            receiptBill.setMaterialCode(receiptDetail.getMaterialCode());
            receiptBill.setProject(receiptDetail.getProjectNo());
            receiptBill.setBatch(receiptDetail.getBatch());
            receiptBill.setReceiptDetailId(String.valueOf(receiptDetail.getId()));
            receiptBill.setReceiptContainerCode(mContainerCode);
            receiptBill.setMaterialName(receiptDetail.getMaterialName());
            receiptBills.add(receiptBill);
            int qty = (int)receiptDetail.getQty();
            if(qty > 0) {
                enable = true;
            }
        }
        if(mLocation == null) {
            enable = false;
        }
        enableButton(enable);
    }


    private void findReceipt(String receiptCode) {
        HttpInterface.getInsstance().findReceipt(new ProgressSubscriber<Receipt>(mContext, receiptListener), receiptCode);
    }

    SubscriberOnNextListener receiptListener = new SubscriberOnNextListener<Receipt>() {
        @Override
        public void onNext(Receipt receipt) {
            if(receipt != null) {
                List<ReceiptDetail> receiptDetails = receipt.getReceiptDetails();
                mReceiptDetailList = receiptDetails;
                List<ReceiptDetail> removeReceiptList = new ArrayList <>();
                for(ReceiptDetail receiptDetail : mReceiptDetailList) {
                    int tobeCollectQty = (int)receiptDetail.getTotalQty() - (int)receiptDetail.getOpenQty();
                    if(tobeCollectQty <=0) {
                        removeReceiptList.add(receiptDetail);
                    }
                }
                mReceiptDetailList.removeAll(removeReceiptList);
                sendReceipt(mReceiptDetailList);
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

    private void isLocation(String code) {
        HttpInterface.getInsstance().isLocation(new ProgressSubscriber<String>(this, isLocationListener), code);
    }

    SubscriberOnNextListener isLocationListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String str) {
            mLocation = str;
            showContent();
        }

        @Override
        public void onError(String str) {

        }
    };

    private void listReceipt() {
        HttpInterface.getInsstance().listReceipt(new ProgressSubscriber<String>(this, quickReceiptListener), receiptBills);
    }

    SubscriberOnNextListener quickReceiptListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String result) {
            completeTaskByWMS(result);
        }

        @Override
        public void onError(String str) {

        }
    };

    private void completeTaskByWMS(String taskId) {
        HttpInterface.getInsstance().completeTaskByWMS(new ProgressSubscriber<String>(mContext, taskhListener), taskId);
    }

    SubscriberOnNextListener taskhListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.receipt_success));
            finish();
            reStart();
        }

        @Override
        public void onError(String str) {

        }
    };

}
