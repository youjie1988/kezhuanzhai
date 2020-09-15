package com.huaheng.mobilewms.activity.printer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.bean.Material;
import com.huaheng.mobilewms.bean.MaterialInfo;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.AmountView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by youjie
 * on 2020/2/7
 */
public class PrinterMaterialActivity extends PDACommonActivity {

    private ArrayList<MaterialInfo> materialInfos = new ArrayList<>();
    private MaterialInfo materialInfo;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(getString(R.string.printer_material));
        initViews();
        getLatestMaterial();
    }

    private void initViews() {
        ensureBtn.setText(mContext.getString(R.string.printer));
        inputEdit.setHint(mContext.getString(R.string.enter_material_code));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String content = textView.getText().toString();
                if(content == null || content.length() < 1) {
                    return false;
                }
                getMaterialInfo(content);
                return false;
            }
        });
    }

    private void showContentLayout() {
        contentLayout.removeAllViews();
        for(int i = materialInfos.size() - 1; i>=0; i--) {
            final MaterialInfo materialInfo = materialInfos.get(i);
            View conmodityView = WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_barcode), materialInfo.getMaterialCode());
            conmodityView.setTag(materialInfo);
            conmodityView.setOnClickListener(clickListener);
            contentLayout.addView(conmodityView);
            contentLayout.addView(WMSUtils.newDevider(mContext));
            View conmodityName = WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_name), materialInfo.getMaterialName());
            conmodityName.setTag(materialInfo);
            conmodityName.setOnClickListener(clickListener);
            contentLayout.addView(conmodityName);
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_type), materialInfo.getType()));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            View view2 = new View(mContext);
            view2.setMinimumHeight(15);
            view2.setMinimumWidth(-1);
            contentLayout.addView(view2);
        }
        enableButton(false);
    }

    private void showMaterialLayout() {
        contentLayout.removeAllViews();
        View conmodityView = WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_barcode), materialInfo.getMaterialCode());
        contentLayout.addView(conmodityView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        View conmodityName = WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_name), materialInfo.getMaterialName());
        contentLayout.addView(conmodityName);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_type), materialInfo.getType()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        AmountView quantityView = new AmountView(mContext);
        quantityView.setLineName(mContext.getString(R.string.commodity_number));
        quantityView.setAmount(Integer.parseInt(materialInfo.getQty()));
        quantityView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                materialInfo.setQty(String.valueOf(amount));
            }
        });
        contentLayout.addView(quantityView);
        enableButton(true);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            MaterialInfo materialInfo1 =  (MaterialInfo) view.getTag();
            getMaterialInfo(materialInfo1.getMaterialCode());
        }
    };


    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        if(materialInfo != null) {
            print();
        }
    }


    private void print() {
        String code = materialInfo.getMaterialCode();
        String name = materialInfo.getMaterialName();
        String spec = materialInfo.getType();
        String qty = materialInfo.getQty();
        WMSLog.d("print  code:" + code + "   name:" + name + "   spec:" + spec + "  qty:" + qty);
        PrinterManager.as(mContext).printerMaterial(code, name, spec, qty);
    }

    private void getLatestMaterial() {
        HttpInterface.getInsstance().getLatestMaterial(new ProgressSubscriber<List<Material>>(this, latestMaterialListener));
    }

    SubscriberOnNextListener latestMaterialListener = new SubscriberOnNextListener<List<Material>>() {

        @Override
        public void onNext(List<Material> materials) {
            if(materials != null) {
                Collections.reverse(materials);
                for(Material material : materials) {
                    MaterialInfo materialInfo1 = new MaterialInfo();
                    materialInfo1.setMaterialCode(material.getCode());
                    materialInfo1.setMaterialName(material.getName());
                    materialInfo1.setType(material.getSpec());
                    materialInfos.add(materialInfo1);
                }
                showContentLayout();
            } else {
                WMSUtils.showShort("没有获取物料信息");
            }
        }

        @Override
        public void onError(String str) {

        }
    };

    private void getMaterialInfo(String number) {
        HttpInterface.getInsstance().getMaterial(new ProgressSubscriber<MaterialInfo>(this, getMaterialListener), number);
    }

    SubscriberOnNextListener getMaterialListener = new SubscriberOnNextListener<MaterialInfo>() {
        @Override
        public void onNext(MaterialInfo material) {
            materialInfo = material;
            materialInfo.setQty("1");
            showMaterialLayout();
            WMSUtils.resetEdit(inputEdit);
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

}
