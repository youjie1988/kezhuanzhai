package com.huaheng.mobilewms.activity.shipment;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.adapter.DetailAmountAdapter;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.Material;
import com.huaheng.mobilewms.bean.MaterialInfo;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.bean.ShipmentBill;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.SerachSelectDialog;
import com.huaheng.mobilewms.view.TypeSpinnerLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by youjie on 2020/9/9
 */
public class AddShipmentActivity extends PDAListCommonActivity {

    private ArrayList<MaterialInfo> materialInfos = new ArrayList<>();
    private ArrayList<ShipmentBill> shipmentBills = new ArrayList<>();
    private MaterialInfo materialInfo;
    private String shipmentCode;
    private List<ReceiptType> mReceiptTypeList;
    private List<DetailBean> detailBeanList;
    private DetailAmountAdapter mAdapter;
    private Activity activity;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setTitle(mContext.getString(R.string.add_shipment));
        initView();
        createShipmentCode();
        activity = this;
    }

    private void initView() {
        inputEdit.setHint(mContext.getString(R.string.enter_material_code));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    getMaterialInfo(number);
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
        View chooseView  = WMSUtils.newContent(mContext, mContext.getString(R.string.choose_material), "");
        chooseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findMaterial();
            }
        });
        contentLayout.addView(chooseView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        if(!WMSUtils.isNotEmptyList(materialInfos)) {
            return;
        }
        detailBeanList = new ArrayList<>();
        for(int i=0; i<materialInfos.size(); i++) {
            MaterialInfo material = materialInfos.get(i);
            DetailBean detailBean = new DetailBean(mContext.getResources().getDrawable(R.mipmap.kekoukele),
                    material.getMaterialCode(), material.getMaterialName(), "");
            detailBean.setAmount(Integer.parseInt(material.getQty()));
            detailBean.setMaxAmount(Integer.MAX_VALUE);
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
                MaterialInfo materialInfo = materialInfos.get(id);
                materialInfo.setQty(String.valueOf(amount));
                DetailBean detailBean = detailBeanList.get(id);
                detailBean.setAmount(amount);
                updateShipment();
            }
        });
        updateShipment();
        mAdapter.notifyDataSetChanged();
    }

    private void updateShipment() {
        shipmentBills.clear();
        boolean enable = false;
        for(int i=materialInfos.size() - 1; i>=0; i--) {
            final MaterialInfo materialInfo = materialInfos.get(i);
            ShipmentBill shipmentBill = new ShipmentBill();
            shipmentBill.setQty(new BigDecimal(materialInfo.getQty()));
            shipmentBill.setMaterialCode(materialInfo.getMaterialCode());
            final String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
            shipmentBill.setCompanyCode(companyCode);
            shipmentBill.setShipmentCode(shipmentCode);
            shipmentBills.add(shipmentBill);
            int qty = Integer.parseInt(materialInfo.getQty());
            if(qty > 0) {
                enable = true;
            }
        }
        enableButton(enable);
    }

    public void openSearchSelectDialog(final  List<Material> materialList) {
        final List<String> lists = new ArrayList<>();
        final List<String> lists2 = new ArrayList<>();
        for(Material material : materialList) {
            lists.add(material.getCode() + "  " + material.getName());
            lists2.add(String.valueOf(material.getCode()));
        }
        SerachSelectDialog.Builder alert = new SerachSelectDialog.Builder(mContext);
        alert.setListData(lists);
        alert.setTitle(mContext.getString(R.string.choose_material));
        alert.setSelectedListiner(new SerachSelectDialog.Builder.OnSelectedListiner() {
            @Override
            public void onSelected(String info, int which) {
                int index = 0;
                for(int i=0; i< lists.size(); i++) {
                    if(info.equals(lists.get(i))) {
                        index = i;
                        break;
                    }
                }
                getMaterialInfo(lists2.get(index));
            }
        });
        SerachSelectDialog mDialog = alert.show();
        //设置Dialog 尺寸
        mDialog.setDialogWindowAttr(0.9, 0.9, activity);
    }

    private List<String> getReceiptTypeList() {
        List<String> strs = new ArrayList <>();
        for(ReceiptType receiptType : mReceiptTypeList) {
            strs.add(receiptType.getName());
        }
        return  strs;
    }

    @OnClick(R.id.ensureBtn)
    public void onViewClicked() {
        createShipment();
    }

    private void getMaterialInfo(String number) {
        number = WMSUtils.getMaterialCode(number);
        HttpInterface.getInsstance().getMaterial(new ProgressSubscriber<MaterialInfo>(this, getMaterialListener), number);
    }

    SubscriberOnNextListener getMaterialListener = new SubscriberOnNextListener<MaterialInfo>() {
        @Override
        public void onNext(MaterialInfo material) {
            Collections.reverse(materialInfos);
            materialInfo = material;
            materialInfo.setQty("1");
            materialInfos.add(materialInfo);
            Collections.reverse(materialInfos);
            showContent();
            WMSUtils.resetEdit(inputEdit);
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void findMaterial() {
        HttpInterface.getInsstance().findMaterial(new ProgressSubscriber<List<Material>>(this, findMaterialListener));
    }

    SubscriberOnNextListener findMaterialListener = new SubscriberOnNextListener<List<Material>>() {
        @Override
        public void onNext(List<Material> materialList) {
            openSearchSelectDialog(materialList);
            WMSUtils.resetEdit(inputEdit);
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void createShipmentCode() {
        HttpInterface.getInsstance().createShipmentCode(new ProgressSubscriber<String>(mContext, createShipmentCodelistener));
    }

    SubscriberOnNextListener createShipmentCodelistener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            shipmentCode = result;
            showContent();
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void createShipment() {
        HttpInterface.getInsstance().createShipment(new ProgressSubscriber<String>(mContext, createShipmentListener), shipmentBills);
    }

    SubscriberOnNextListener createShipmentListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.create_shipment_success));
            finish();
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

}
