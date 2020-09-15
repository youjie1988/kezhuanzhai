package com.huaheng.mobilewms.activity.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.activity.model.CommonInfoActivity;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.bean.Receipt;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceiptDetailInfoActivity extends CommonInfoActivity {

    private String receiptCode, receiptDetailCode;
    private MyHandler myHandler = new MyHandler();

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setTitle(mContext.getString(R.string.material_detail));
        Bundle bundle = getIntent().getExtras();
        receiptCode = bundle.getString("receiptCode");
        receiptDetailCode = bundle.getString("receiptDetailCode");
        findReceipt(receiptCode);
    }

    private void showContent(ReceiptDetail receiptDetail) {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialCode), receiptDetail.getMaterialCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialName), receiptDetail.getMaterialName()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialSpec), receiptDetail.getMaterialSpec()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialUnit), receiptDetail.getMaterialUnit()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.bill_number), String.valueOf((int)receiptDetail.getTotalQty())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.already_number), String.valueOf((int)receiptDetail.getOpenQty())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.batch_number), receiptDetail.getBatch()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.project_number), receiptDetail.getProjectNo()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
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
                for(ReceiptDetail receiptDetail : receiptDetails){
                    if(receiptDetail.getMaterialCode().equals(receiptDetailCode)) {
                        sendReceipt(receiptDetail);
                        break;
                    }
                }
            } else {
                WMSUtils.showShort(getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };

    private void sendReceipt(ReceiptDetail receiptDetail) {
        Message message = new Message();
        message.obj = receiptDetail;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ReceiptDetail receiptDetail = (ReceiptDetail)msg.obj;
                    showContent(receiptDetail);
                    break;
            }
        }
    }
}
