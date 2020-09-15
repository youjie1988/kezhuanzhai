package com.huaheng.mobilewms.activity.shipment;


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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.activity.model.PDAListActivity;
import com.huaheng.mobilewms.activity.receipt.AddReceiptActivity;
import com.huaheng.mobilewms.activity.receipt.ReceiptActivity;
import com.huaheng.mobilewms.activity.receipt.ReceiptDetailActivity;
import com.huaheng.mobilewms.activity.shipment.pingku.PingkuShipmentActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.Receipt;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.bean.Shipment;
import com.huaheng.mobilewms.bean.ShipmentDetail;
import com.huaheng.mobilewms.bean.ShipmentHeader;
import com.huaheng.mobilewms.bean.ShipmentType;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.EditLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ShipmentActivity extends PDAListActivity {

    private DetailAdapter mAdapter;
    private List<DetailBean> detailBeanList;
    private List<ShipmentType> mShipmentTypeList;
    private MyHandler myHandler = new MyHandler();
    private PopupWindow popupWindow;
    private boolean isShowing = false;
    EditLayout codeEdit, shipmentTypeEdit,statusEdit,startTimeEdit, endTimeEdit;


    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.bulk_shipment));
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismiss();
        getShipmentType();
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

    private void initView() {
        ensureBtn.setText(mContext.getString(R.string.add_shipment));
        enableButton(true);
        ensureLayout.setVisibility(View.VISIBLE);
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
        inputEdit.setHint(mContext.getString(R.string.enter_shipment_number));
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
        codeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.shipment_code));
        contentLyaout.addView(codeEdit);
        shipmentTypeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.shipment_type));
        contentLyaout.addView(shipmentTypeEdit);
        statusEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.shipment_status));
        contentLyaout.addView(statusEdit);
        startTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.start_time));
        contentLyaout.addView(startTimeEdit);
        endTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.end_time));
        contentLyaout.addView(endTimeEdit);
        WMSUtils.setOnDatePick(mContext, startTimeEdit.getEditContent());
        WMSUtils.setOnDatePick(mContext, endTimeEdit.getEditContent());
    }

    private void addShipment() {
        WMSUtils.startActivity(mContext, AddShipmentActivity.class);
    }

    @OnClick(R.id.ensureBtn)
    public void onViewClicked() {
        addShipment();
    }


    private void search() {
        String code = codeEdit.getEditValue();
        String receiptType = shipmentTypeEdit.getEditValue();
        String lastStatus = statusEdit.getEditValue();
        String startTime = startTimeEdit.getEditValue();
        String endTime = endTimeEdit.getEditValue();

        searchShipmentInCondition(code, receiptType, lastStatus, startTime, endTime);
        dismiss();
    }

    private void reset() {
        WMSUtils.resetEdit(codeEdit.getEditContent());
        WMSUtils.resetEdit(shipmentTypeEdit.getEditContent());
        WMSUtils.resetEdit(statusEdit.getEditContent());
        WMSUtils.resetEdit(startTimeEdit.getEditContent());
        WMSUtils.resetEdit(endTimeEdit.getEditContent());
    }

    private void showContent(List<ShipmentHeader> shipmentHeaderList) {
        detailBeanList = new ArrayList<>();
        for(ShipmentHeader shipmentHeader : shipmentHeaderList) {
            int lastStatus = shipmentHeader.getLastStatus();
            String type = shipmentHeader.getShipmentType();
            for(ShipmentType shipmentType : mShipmentTypeList) {
                if(shipmentType.getCode().equals(type)) {
                    type = shipmentType.getName();
                }
            }
            if(lastStatus == 900) {
                detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.shipment_bill),
                        shipmentHeader.getCode(), type, shipmentHeader.getCreated(), true));
            } else {
                detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.shipment_bill),
                        shipmentHeader.getCode(), type, shipmentHeader.getCreated()));
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
            findShipment(receiptCode);
        }
    };

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<ShipmentHeader> shipmentHeaderList = (List<ShipmentHeader>)msg.obj;
                    showContent(shipmentHeaderList);
                    break;
            }
        }
    }

    private void sendReceipt(List<ShipmentHeader> shipmentHeaders) {
        Message message = new Message();
        message.obj = shipmentHeaders;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private void findShipment(String shipmentCode) {
        HttpInterface.getInsstance().findShipment(new ProgressSubscriber<Shipment>(mContext, shipmentListener), shipmentCode);
    }

    SubscriberOnNextListener shipmentListener = new SubscriberOnNextListener<Shipment>() {
        @Override
        public void onNext(Shipment shipment) {
            if(shipment != null) {
                ShipmentHeader shipmentHeader =  shipment.getShipmentHeader();
                List<ShipmentDetail> shipmentDetails = shipment.getShipmentDetails();
                String type = shipmentHeader.getShipmentType();
                for(ShipmentType shipmentType : mShipmentTypeList) {
                    if(shipmentType.getCode().equals(type)) {
                        type = shipmentType.getName();
                    }
                }
                Intent intent = new Intent();
                intent.setClass(mContext, ShipmentDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("shipmentCode", shipmentHeader.getCode());
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

//    private void searchShipment() {
//        HttpInterface.getInsstance().searchShipment(new ProgressSubscriber<List<ShipmentHeader>>(mContext, searchListener));
//    }
//
//    SubscriberOnNextListener searchListener = new SubscriberOnNextListener<List<ShipmentHeader>>() {
//        @Override
//        public void onNext(List<ShipmentHeader> shipmentHeaders) {
//            if (WMSUtils.isNotEmptyList(shipmentHeaders)) {
//                sendReceipt(shipmentHeaders);
//            }
//        }
//
//        @Override
//        public void onError(String str) {
//            WMSUtils.resetEdit(inputEdit);
//        }
//    };

    private void getShipmentType() {
        HttpInterface.getInsstance().getShipmentType(new ProgressSubscriber<List<ShipmentType>>(mContext, getShipmentTypeListener));
    }

    SubscriberOnNextListener getShipmentTypeListener = new SubscriberOnNextListener<List<ShipmentType>>() {
        @Override
        public void onNext(List<ShipmentType> shipmentTypeList) {
            mShipmentTypeList = shipmentTypeList;
            search();
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void searchShipmentInCondition(String code, String receiptType,
                                          String lastStatus, String startTime, String endTime) {
        HttpInterface.getInsstance().searchShipmentInCondition(new ProgressSubscriber <List <ShipmentHeader>>(mContext, searchInLocationListener),
                code, receiptType, lastStatus, startTime, endTime);
    }

    SubscriberOnNextListener searchInLocationListener = new SubscriberOnNextListener <List <ShipmentHeader>>() {
        @Override
        public void onNext(List<ShipmentHeader> shipmentHeaders) {
            if (WMSUtils.isNotEmptyList(shipmentHeaders)) {
                sendReceipt(shipmentHeaders);
            } else {
                WMSUtils.showShort(mContext.getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };
}
