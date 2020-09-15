package com.huaheng.mobilewms.activity.shipment;

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
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.activity.receipt.ReceiptDetailInfoActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.adapter.DetailAmountAdapter;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.Shipment;
import com.huaheng.mobilewms.bean.ShipmentDetail;
import com.huaheng.mobilewms.bean.ShipmentHeader;
import com.huaheng.mobilewms.bean.ShipmentType;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.LineLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShipmentDetailActivity extends PDAListCommonActivity {

    private DetailAdapter mAdapter;
    private List <DetailBean> detailBeanList;
    private String shipmentCode, label;
    private List<ShipmentDetail> mShipmentDetailList;
    private MyHandler myHandler = new MyHandler();

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);

        setContentView(R.layout.activity_shipment_detail);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        shipmentCode = bundle.getString("shipmentCode");
        label = bundle.getString("label");
        setTitle(label);
        initView();
        findShipment(shipmentCode);
    }


    private void initView() {
        enableButton(true);
        ensureBtn.setText(mContext.getString(R.string.auto_shipment));
        inputEdit.setHint("");
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    findShipment(number);
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
    }

    private void showContent() {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.shipment_bill), shipmentCode));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        detailBeanList = new ArrayList<>();
        if(mShipmentDetailList == null) {
            return;
        }
        for(ShipmentDetail shipmentDetail : mShipmentDetailList) {
            int shipQty = shipmentDetail.getShipQty().intValue();
            int requestQty = shipmentDetail.getRequestQty().intValue();
            DetailBean detailBean = new DetailBean(mContext.getResources().getDrawable(R.mipmap.kekoukele),
                    shipmentDetail.getMaterialCode(), shipmentDetail.getMaterialName(), String.valueOf(shipQty - requestQty));
            detailBean.setMaxAmount(shipQty - requestQty);
            detailBeanList.add(detailBean);
        }

        if(mAdapter == null) {
            mAdapter = new DetailAdapter(mContext);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(listener);
        }
        mAdapter.setList(detailBeanList);
    }


    private void sendShipment(List<ShipmentDetail> shipmentDetails) {
        Message message = new Message();
        message.obj = shipmentDetails;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<ShipmentDetail> shipmentDetails = (List<ShipmentDetail>)msg.obj;
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
            intent.setClass(mContext, ShipmentDetailInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("shipmentCode", shipmentCode);
            bundle.putString("shipmentDetailCode", detailBean.getType());
            intent.putExtras(bundle);// 发送数据
            mContext.startActivity(intent);
        }
    };


    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        autoCombination(shipmentCode);
    }

    private void findShipment(String shipmentCode) {
        HttpInterface.getInsstance().findShipment(new ProgressSubscriber<Shipment>(mContext, shipmentListener), shipmentCode);
    }

    SubscriberOnNextListener shipmentListener = new SubscriberOnNextListener<Shipment>() {
        @Override
        public void onNext(Shipment shipment) {
            if(shipment != null) {
                ShipmentHeader shipmentHeader =  shipment.getShipmentHeader();
                shipmentCode = shipmentHeader.getCode();
                List<ShipmentDetail> shipmentDetails = shipment.getShipmentDetails();
                mShipmentDetailList = shipmentDetails;
                sendShipment(shipmentDetails);
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

    private void autoCombination(String shipmentCode) {
        HttpInterface.getInsstance().autoCombination(new ProgressSubscriber<List<Integer>>(mContext, autoListener), shipmentCode);
    }

    SubscriberOnNextListener autoListener = new SubscriberOnNextListener<List<Integer>>() {
        @Override
        public void onNext(List<Integer> tasks) {
            createShipmentTask(tasks);
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };


    private void createShipmentTask(List<Integer> tasks) {
        HttpInterface.getInsstance().createShipmentTask(new ProgressSubscriber<List<Integer>>(mContext, shipmentTaskListener), tasks);
    }

    SubscriberOnNextListener shipmentTaskListener = new SubscriberOnNextListener<List<Integer>>() {
        @Override
        public void onNext(List<Integer> tasks) {
            executeList(tasks);
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void executeList(List<Integer> taskIdS) {
        HttpInterface.getInsstance().executeList(new ProgressSubscriber<String>(this, executeListener), taskIdS);
    }

    SubscriberOnNextListener executeListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.shipment_success));
            finish();
            WMSUtils.startActivity(mContext, ShipmentActivity.class);
        }

        @Override
        public void onError(String str) {

        }
    };
}
