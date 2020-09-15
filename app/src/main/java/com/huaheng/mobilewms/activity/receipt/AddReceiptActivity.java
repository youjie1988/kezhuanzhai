package com.huaheng.mobilewms.activity.receipt;

import android.app.Activity;
import android.os.Bundle;
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
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.SerachSelectDialog;
import com.huaheng.mobilewms.view.SpinnerLayout;
import com.huaheng.mobilewms.view.TypeSpinnerLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by youjie on 2020/9/9
 */
public class AddReceiptActivity extends PDAListCommonActivity {

    private ArrayList<MaterialInfo> materialInfos = new ArrayList<>();
    private ArrayList<ReceiptBill> receiptBills = new ArrayList<>();
    private MaterialInfo materialInfo;
    private List<DetailBean> detailBeanList;
    private DetailAmountAdapter mAdapter;
    private Activity activity;
    private String receiptNo;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setTitle(mContext.getString(R.string.add_receipt));
        initView();
        createReceiptCode();
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
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.receipt_code), receiptNo));
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
                updateReceipt();
            }
        });
        updateReceipt();
        mAdapter.notifyDataSetChanged();
    }

    private void updateReceipt() {
        receiptBills.clear();
        boolean enable = false;
        for(int i=materialInfos.size() - 1; i>=0; i--) {
            final MaterialInfo materialInfo = materialInfos.get(i);
            ReceiptBill receiptBill = new ReceiptBill();
            receiptBill.setReceiptContainerCode(mContainerCode);
            receiptBill.setLocationCode(mLocation);
            receiptBill.setQty(new BigDecimal(materialInfo.getQty()));
            receiptBill.setMaterialCode(materialInfo.getMaterialCode());
            receiptBill.setBatch(materialInfo.getBatch());
            receiptBill.setProject(materialInfo.getProject());
            receiptBill.setMaterialName(materialInfo.getMaterialName());
            final String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
            receiptBill.setCompanyCode(companyCode);
            receiptBill.setReceiptCode(receiptNo);
            receiptBills.add(receiptBill);
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


    @OnClick(R.id.ensureBtn)
    public void onViewClicked() {
        createReceipt();
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



    private void createReceiptCode() {
        HttpInterface.getInsstance().createReceiptCode(new ProgressSubscriber<String>(mContext, createReceiptCodeListener));
    }

    SubscriberOnNextListener createReceiptCodeListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            receiptNo = result;
            showContent();
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void createReceipt() {
        receiptBills = WMSUtils.removeUnUseList(receiptBills);
        HttpInterface.getInsstance().createReceipt(new ProgressSubscriber<String>(mContext, createReceiptListener), receiptBills);
    }

    SubscriberOnNextListener createReceiptListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.create_receipt_success));
            finish();
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
        }
    };

}
