package com.huaheng.mobilewms.activity.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
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
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.AmountView;
import com.huaheng.mobilewms.view.LineLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReceiptDetailActivity extends PDAListCommonActivity {

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
        enableButton(false);
        inputEdit.setHint(mContext.getString(R.string.enter_task_number));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    if(mContainerCode == null) {
                        isContainer(number);
                    } else {
                        number = WMSUtils.replaceLocation(number);
                        isLocation(number);
                    }
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
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.container_number), mContainerCode));
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
            detailBean.setAmount(receiptDetail.getQty());
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
        if(mContainerCode != null) {
            enableButton(true);
        }
        updateReceipt();
        mAdapter.notifyDataSetChanged();
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
                    List<ReceiptDetail> receiptDetails = (List<ReceiptDetail>)msg.obj;
                    showContent();
                    break;
            }
        }
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {
            DetailBean detailBean = detailBeanList.get(position);
            Intent intent = new Intent();
            intent.setClass(mContext, ReceiptDetailInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("receiptCode", receiptCode);
            bundle.putString("receiptDetailCode", detailBean.getType());
            intent.putExtras(bundle);// 发送数据
            mContext.startActivity(intent);
        }
    };

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
        if(mContainerCode == null) {
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
                ReceiptHeader receiptHeader =  receipt.getReceiptHeader();
                List<ReceiptDetail> receiptDetails = receipt.getReceiptDetails();
                mReceiptDetailList = receiptDetails;
                sendReceipt(receiptDetails);
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

    private void isContainer(String containerCode) {
        HttpInterface.getInsstance().isContainer(new ProgressSubscriber<String>(this, isContainerListener), containerCode);
    }

    SubscriberOnNextListener isContainerListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String str) {
            mContainerCode = str;
            showContent();
        }

        @Override
        public void onError(String str) {

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
        receiptBills = WMSUtils.removeUnUseList(receiptBills);
        HttpInterface.getInsstance().listReceipt(new ProgressSubscriber<String>(this, quickReceiptListener), receiptBills);
    }

    SubscriberOnNextListener quickReceiptListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.receipt_success));
            finish();
        }

        @Override
        public void onError(String str) {

        }
    };
}
