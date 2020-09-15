package com.huaheng.mobilewms.activity.receipt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.adapter.DetailAmountAdapter;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.Material;
import com.huaheng.mobilewms.bean.MaterialInfo;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.AmountView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by youjie on 2020/5/26
 */
public class QuickReceiptActivity  extends PDAListCommonActivity {

    private ArrayList<ReceiptBill> receiptBills = new ArrayList<>();
    private List<DetailBean> detailBeanList;
    private DetailAmountAdapter mAdapter;
    private MaterialInfo materialInfo;
    private ArrayList<MaterialInfo> materialInfos = new ArrayList<>();

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.quick_collection));
        initView();
        showContent();
    }

    private void initView() {
        enableButton(false);
        if(mContainerCode == null) {
            inputEdit.setHint(mContext.getString(R.string.enter_container_number));
        }
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    if(mContainerCode == null) {
                        isContainer(number);
                    } else {
                        getMaterialInfo(number);
                    }
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
    }

    private void showContent() {
        if(mContainerCode == null) {
            inputEdit.setHint(mContext.getString(R.string.enter_container_number));
        } else {
            inputEdit.setHint(mContext.getString(R.string.enter_material_code));
        }
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.container_number), mContainerCode));
        contentLayout.addView(WMSUtils.newDevider(mContext));
//        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.location), mLocation));
//        contentLayout.addView(WMSUtils.newDevider(mContext));
        detailBeanList = new ArrayList<>();
        if(!WMSUtils.isNotEmptyList(materialInfos)) {
            return;
        }
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
        if(mContainerCode != null) {
            enableButton(true);
        }
        updateReceipt();
        mAdapter.notifyDataSetChanged();
    }

    private void changeMaterialNumber(MaterialInfo materialInfo, String number) {
        if(materialInfos != null && materialInfos.size() > 0) {
            materialInfo.setQty(number);
            materialInfos.remove(materialInfo);
            materialInfos.add(materialInfo);
        }
    }

    private View.OnLongClickListener replenishLongListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            MaterialInfo materialInfo = (MaterialInfo)view.getTag();
            showDeleteDialog(materialInfo);
            return false;
        }
    };

    private void showDeleteDialog(final MaterialInfo materialInfo) {
        AlertDialog mDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle(getString(R.string.delete))
                .setMessage(mContext.getString(R.string.delete_material))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        materialInfos.remove(materialInfo);
                        showContent();
                    }
                }).setNegativeButton(this.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        mDialog = builder.create();
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void updateReceipt() {
        receiptBills.clear();
        boolean enable = false;
        for(int i=materialInfos.size() - 1; i>=0; i--) {
            final MaterialInfo materialInfo = materialInfos.get(i);
            ReceiptBill receiptBill = new ReceiptBill();
            receiptBill.setReceiptContainerCode(mContainerCode);
            receiptBill.setLocationCode(mLocation);
            try {
                receiptBill.setQty(new BigDecimal(materialInfo.getQty()));
            } catch(Exception e) {
                e.printStackTrace();
            }
            receiptBill.setMaterialCode(materialInfo.getMaterialCode());
            receiptBill.setBatch(materialInfo.getBatch());
            receiptBill.setProject(materialInfo.getProject());
            receiptBill.setMaterialName(materialInfo.getMaterialName());
            final String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
            receiptBill.setCompanyCode(companyCode);
            receiptBills.add(receiptBill);
            enable = true;
        }
        if(mContainerCode == null) {
            enable = false;
        }
        enableButton(enable);
    }

    @OnClick(R.id.ensureBtn)
    public void onViewClicked() {
        quickReceipt();
    }

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

    private void quickReceipt() {
        receiptBills = WMSUtils.removeUnUseList(receiptBills);
        HttpInterface.getInsstance().quickReceipt(new ProgressSubscriber<String>(this, quickReceiptListener), receiptBills);
    }

    SubscriberOnNextListener quickReceiptListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String result) {
            createQuickTask();
        }

        @Override
        public void onError(String str) {

        }
    };

    private void createQuickTask() {
        HttpInterface.getInsstance().createQuickTask(new ProgressSubscriber<Integer>(this, createQuickTaskListener), mContainerCode);
    }

    SubscriberOnNextListener createQuickTaskListener = new SubscriberOnNextListener<Integer>() {

        @Override
        public void onNext(Integer result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.receipt_success));
            finish();
            WMSUtils.startActivity(mContext, QuickReceiptActivity.class);
        }

        @Override
        public void onError(String str) {

        }
    };

}
