package com.huaheng.mobilewms.activity.shipment.pingku;

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
import com.huaheng.mobilewms.activity.receipt.pingku.PingkuReceiptActivity;
import com.huaheng.mobilewms.activity.shipment.ShipmentActivity;
import com.huaheng.mobilewms.activity.shipment.ShipmentDetailActivity;
import com.huaheng.mobilewms.activity.shipment.ShipmentDetailInfoActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.adapter.DetailAmountAdapter;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.Shipment;
import com.huaheng.mobilewms.bean.ShipmentDetail;
import com.huaheng.mobilewms.bean.ShipmentHeader;
import com.huaheng.mobilewms.bean.ShipmentMaterial;
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

public class PingkuShipmentActivity extends PDAListCommonActivity {

    private DetailAmountAdapter mAdapter;
    private List <DetailBean> detailBeanList;
    private String shipmentCode, label;
    private List<ShipmentDetail> mShipmentDetailList;
    private MyHandler myHandler = new MyHandler();
    private List<ShipmentMaterial> mShipmentMaterials = new ArrayList <>();

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
        enableButton(false);
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
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.shipment_code), shipmentCode));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.location), mLocation));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        detailBeanList = new ArrayList<>();
        if(mShipmentDetailList == null) {
            return;
        }
        for(ShipmentDetail shipmentDetail : mShipmentDetailList) {
            int tobeCollectQty = shipmentDetail.getShipQty().intValue() - shipmentDetail.getRequestQty().intValue();
            DetailBean detailBean = new DetailBean(mContext.getResources().getDrawable(R.mipmap.kekoukele),
                    shipmentDetail.getMaterialCode(), shipmentDetail.getMaterialName(), String.valueOf(tobeCollectQty));
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
                ShipmentDetail shipmentDetail = mShipmentDetailList.get(id);
                shipmentDetail.setQty(amount);
                DetailBean detailBean = detailBeanList.get(id);
                detailBean.setAmount(amount);
                updateShipment();
            }
        });
        updateShipment();
    }

    private void updateShipment() {
        mShipmentMaterials.clear();
        boolean enable = false;
        for(int i = mShipmentDetailList.size() - 1; i>=0; i--) {
            ShipmentDetail shipmentDetail = mShipmentDetailList.get(i);
            ShipmentMaterial shipmentMaterial = new ShipmentMaterial();
            shipmentMaterial.setLocaitonCode(mLocation);
            shipmentMaterial.setQty(shipmentDetail.getQty());
            shipmentMaterial.setCompanyCode(WMSUtils.getData(Constant.CURREN_COMPANY_CODE));
            shipmentMaterial.setShipmentDetailId(shipmentDetail.getId());
            shipmentMaterial.setMaterialCode(shipmentDetail.getMaterialCode());
            mShipmentMaterials.add(shipmentMaterial);
            int qty = (int)shipmentMaterial.getQty();
            if(qty > 0) {
                enable = true;
            }
        }
        if(mLocation == null) {
            enable = false;
        }
        enableButton(enable);
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

    private void reStart() {
        Intent intent = new Intent();
        intent.setClass(mContext, ShipmentDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("shipmentCode", shipmentCode);
        bundle.putString("label", label);
        intent.putExtras(bundle);// 发送数据
        mContext.startActivity(intent);
    }



    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        combination(mShipmentMaterials);
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
                List<ShipmentDetail> removeShipmentList = new ArrayList <>();
                for(ShipmentDetail shipmentDetail : shipmentDetails) {
                    int tobeCollectQty = shipmentDetail.getShipQty().intValue() - shipmentDetail.getRequestQty().intValue();
                    if(tobeCollectQty <=0) {
                        removeShipmentList.add(shipmentDetail);
                    }
                }
                mShipmentDetailList.removeAll(removeShipmentList);
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

    private void combination(List<ShipmentMaterial> shipmentMaterials) {
        HttpInterface.getInsstance().combination(new ProgressSubscriber<List<Integer>>(mContext, combineListener), shipmentMaterials);
    }

    SubscriberOnNextListener combineListener = new SubscriberOnNextListener<List<Integer>>() {
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
            completeTaskListByWMS(tasks);
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void completeTaskListByWMS(List<Integer> tasks) {
        HttpInterface.getInsstance().completeTaskListByWMS(new ProgressSubscriber<String>(mContext, taskhListener), tasks);
    }

    SubscriberOnNextListener taskhListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.shipment_success));
            finish();
            reStart();
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
}
