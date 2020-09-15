package com.huaheng.mobilewms.activity.material;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.adapter.DetailAmountAdapter;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.Material;
import com.huaheng.mobilewms.bean.MaterialInfo;
import com.huaheng.mobilewms.bean.MaterialType;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.EditLayout;
import com.huaheng.mobilewms.view.MaterialEditLayout;
import com.huaheng.mobilewms.view.SerachSelectDialog;
import com.huaheng.mobilewms.view.TypeSpinnerLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by youjie on 2020/9/9
 */
public class AddMaterialActivity extends PDACommonActivity {

    private String materialCode;
    private Material material;
    private MaterialEditLayout codeEdit, nameEditLayout,specEditLayout, unitEditLayout;
    private TypeSpinnerLayout typeSpinnerLayout;
    private List<MaterialType> materialTypeList;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setTitle(mContext.getString(R.string.add_material));
        initView();
//        getMaterialType();
        createMaterialCode();
    }

    private void initView() {
        inputEdit.setHint(mContext.getString(R.string.enter_material_code));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {

                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
    }

    private void showContent() {
        contentLayout.removeAllViews();
        codeEdit = WMSUtils.newMaterialEdit(mContext, mContext.getString(R.string.materialCode));
        codeEdit.getEditContent().setText(materialCode);
        codeEdit.getEditContent().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                freshMaterial();
            }
        });
        contentLayout.addView(codeEdit);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        nameEditLayout = WMSUtils.newMaterialEdit(mContext, mContext.getString(R.string.materialName));
        nameEditLayout.getEditContent().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                freshMaterial();
            }
        });
        contentLayout.addView(nameEditLayout);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        specEditLayout = WMSUtils.newMaterialEdit(mContext, mContext.getString(R.string.materialSpec));
        specEditLayout.getEditContent().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                freshMaterial();
            }
        });
        contentLayout.addView(specEditLayout);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        unitEditLayout = WMSUtils.newMaterialEdit(mContext, mContext.getString(R.string.materialUnit));
        unitEditLayout.getEditContent().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                freshMaterial();
            }
        });
        contentLayout.addView(unitEditLayout);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        typeSpinnerLayout = WMSUtils.newTypeSpinner(mContext, mContext.getString(R.string.materialType));
//        typeSpinnerLayout.setLineArray(mContext, getReceiptTypeList());
        contentLayout.addView(typeSpinnerLayout);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        typeSpinnerLayout.getSpinner().setSelection(0);
        enableButton(true);
    }

    private void freshMaterial() {
        String code = codeEdit.getEditValue();
        String name = nameEditLayout.getEditValue();
        String spec = specEditLayout.getEditValue();
        String unit = unitEditLayout.getEditValue();
        material = new Material();
        material.setCode(code);
        material.setName(name);
        material.setSpec(spec);
        material.setUnit(unit);
        material.setCompanyCode(WMSUtils.getData(Constant.CURREN_COMPANY_CODE));
    }

    @OnClick(R.id.ensureBtn)
    public void onViewClicked() {
        if(WMSUtils.isEmpty(material.getCode())) {
            WMSUtils.showShort("请输入物料编码");
            return;
        }
        if (WMSUtils.isEmpty(material.getName())) {
            WMSUtils.showShort("请输入物料名称");
            return;
        }
        addMaterial();
    }

    private void createMaterialCode() {
        HttpInterface.getInsstance().createMaterialCode(new ProgressSubscriber<String>(this, createMaterialCodeListener));
    }

    SubscriberOnNextListener createMaterialCodeListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String code) {
            materialCode = code;
            showContent();
            WMSUtils.resetEdit(inputEdit);
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void getMaterialType() {
        HttpInterface.getInsstance().getMaterialType(new ProgressSubscriber<List<MaterialType>>(this, getMaterialTypeListener));
    }

    SubscriberOnNextListener getMaterialTypeListener = new SubscriberOnNextListener<List<MaterialType>>() {
        @Override
        public void onNext(List<MaterialType> materialTypes) {
            materialTypeList = materialTypes;
            createMaterialCode();
            WMSUtils.resetEdit(inputEdit);
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void addMaterial() {
        HttpInterface.getInsstance().addMaterial(new ProgressSubscriber<String>(mContext, addMaterialListener), material);
    }

    SubscriberOnNextListener addMaterialListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.add_material_success));
            finish();
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

}
