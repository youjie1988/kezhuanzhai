package com.huaheng.mobilewms.activity.shipment;

import android.os.Bundle;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonInfoActivity;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.Shipment;
import com.huaheng.mobilewms.bean.ShipmentDetail;
import com.huaheng.mobilewms.bean.ShipmentHeader;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.List;

/**
 * Created by youjie on 2020/4/27
 */
public class ShipmentDetailInfoActivity extends CommonInfoActivity {

    private String shipmentCode, shipmentDetailCode;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setTitle(mContext.getString(R.string.material_detail));
        Bundle bundle = getIntent().getExtras();
        shipmentCode = bundle.getString("shipmentCode");
        shipmentDetailCode = bundle.getString("shipmentDetailCode");
        findShipment(shipmentCode);
    }

    private void showContent(ShipmentDetail shipmentDetail) {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialCode), shipmentDetail.getMaterialCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialName), shipmentDetail.getMaterialName()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.materialUnit), shipmentDetail.getMaterialUnit()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.bill_number), String.valueOf(shipmentDetail.getShipQty())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.already_number), String.valueOf(shipmentDetail.getRequestQty())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.batch_number), shipmentDetail.getBatch()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
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
                for(ShipmentDetail shipmentDetail : shipmentDetails){
                    if(shipmentDetail.getMaterialCode().equals(shipmentDetailCode)) {
                        showContent(shipmentDetail);
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
}
