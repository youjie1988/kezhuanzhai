package com.huaheng.mobilewms.activity.printer;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.Receipt;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.bean.ReceiptInfo;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.AmountView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by youjie
 * on 2020/2/7
 */
public class PrinterListActivity extends PDACommonActivity {

    private ArrayList<ReceiptInfo> mReceiptInfos = new ArrayList <>();
    private String receiptID;
    private ThreadPool threadPool;
    private ReceiptHeader receiptHeader;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(getString(R.string.printer_list));
        initViews();
        getLatestReceipt();
    }

    private void initViews() {
        inputEdit.setHint(mContext.getString(R.string.enter_single_number));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String content = textView.getText().toString();
                if(content == null || content.length() < 1) {
                    return false;
                }
                findReceipt(content);
                return false;
            }
        });
        ensureBtn.setText(mContext.getString(R.string.printer));
    }

    private void showEntryLayout() {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.warehouse_number), receiptID));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        View view = new View(mContext);
        view.setMinimumHeight(15);
        view.setMinimumWidth(-1);
        contentLayout.addView(view);
        for(final ReceiptInfo receiptInfo : mReceiptInfos) {
            contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_barcode), receiptInfo.getMaterialCode()));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_name), receiptInfo.getMaterialName()));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_type), receiptInfo.getSpec()));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.commodity_total_number), String.valueOf((int)receiptInfo.getTotalQty())));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.already_received_number), String.valueOf((int)receiptInfo.getOpenQty())));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            AmountView quantityView = new AmountView(mContext);
            quantityView.setLineName(mContext.getString(R.string.commodity_number));
            int qty = (int)(receiptInfo.getTotalQty() - receiptInfo.getOpenQty());
            quantityView.setAmount(qty);
            quantityView.setMaxValue(qty);
            quantityView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {
                    changeReceiptInfo(receiptInfo, String.valueOf(amount));
                }
            });
            contentLayout.addView(quantityView);
            contentLayout.addView(WMSUtils.newDevider(mContext));
        }
    }

    private void changeReceiptInfo(ReceiptInfo receiptInfo, String number) {
        if(mReceiptInfos != null && mReceiptInfos.size() > 0) {
            receiptInfo.setQty(Float.valueOf(number));
            mReceiptInfos.remove(receiptInfo);
            mReceiptInfos.add(receiptInfo);
        }
    }


    private void showContentLayout(List<ReceiptHeader> receiptHeaders) {
        contentLayout.removeAllViews();
        for(ReceiptHeader receiptHeader : receiptHeaders) {
            View conmodityView = WMSUtils.newContent(mContext, mContext.getString(R.string.warehouse_number), receiptHeader.getCode());
            conmodityView.setTag(receiptHeader);
            conmodityView.setOnClickListener(clickListener);
            contentLayout.addView(conmodityView);
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, "入库类型", receiptHeader.getReceiptType()));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, "关联单号", receiptHeader.getReferCode()));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, "创建人", receiptHeader.getCreatedBy()));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            contentLayout.addView(WMSUtils.newContent(mContext, "创建时间", receiptHeader.getCreated()));
            contentLayout.addView(WMSUtils.newDevider(mContext));
            View view2 = new View(mContext);
            view2.setMinimumHeight(15);
            view2.setMinimumWidth(-1);
            contentLayout.addView(view2);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            ReceiptHeader receiptHeader1 =  (ReceiptHeader) view.getTag();
            findReceipt(receiptHeader1.getCode());
        }
    };

    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        if(mReceiptInfos != null && mReceiptInfos.size() > 0) {
            print();
        }
    }

    private void print() {
        String receiptCode = receiptHeader.getCode();
        String type = receiptHeader.getReceiptType();
        String refre = receiptHeader.getReferCode();
        String createBy = receiptHeader.getCreatedBy();
        PrinterManager.as(mContext).printerList(receiptCode, type, refre, createBy);
        for(final ReceiptInfo receiptInfo : mReceiptInfos) {
            PrinterManager.as(mContext).printerMaterial(receiptInfo.getMaterialCode(), receiptInfo.getMaterialName(),
                    receiptInfo.getSpec(), String.valueOf(receiptInfo.getQty()));
        }
    }

    private void getLatestReceipt() {
        HttpInterface.getInsstance().getLatestReceipt(new ProgressSubscriber<List<ReceiptHeader>>(this, latestReceiptListener));
    }

    SubscriberOnNextListener latestReceiptListener = new SubscriberOnNextListener<List<ReceiptHeader>>() {

        @Override
        public void onNext(List<ReceiptHeader> receiptHeaders) {
            if(receiptHeaders != null) {
                showContentLayout(receiptHeaders);
            } else {
                WMSUtils.showShort("没有获取物料信息");
            }
        }

        @Override
        public void onError(String str) {

        }
    };

    private void findReceipt(String receiptCode) {
        final String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        HttpInterface.getInsstance().findReceipt(new ProgressSubscriber<Receipt>(mContext, receiptListener), receiptCode);
    }

    SubscriberOnNextListener receiptListener = new SubscriberOnNextListener<Receipt>() {
        @Override
        public void onNext(Receipt receipt) {
            if(receipt != null) {
                receiptHeader =  receipt.getReceiptHeader();
                receiptID = receiptHeader.getCode();
                List<ReceiptDetail> receiptDetails = receipt.getReceiptDetails();
                for(ReceiptDetail receiptDetail : receiptDetails) {
                    ReceiptInfo receiptInfo = new ReceiptInfo();
                    receiptInfo.setTotalQty(receiptDetail.getTotalQty());
                    receiptInfo.setOpenQty(receiptDetail.getOpenQty());
                    receiptInfo.setQty(receiptDetail.getTotalQty() - receiptDetail.getOpenQty());
                    receiptInfo.setId(String.valueOf(receiptDetail.getId()));
                    receiptInfo.setBatch(receiptDetail.getBatch());
                    receiptInfo.setMaterialCode(receiptDetail.getMaterialCode());
                    receiptInfo.setMaterialName(receiptDetail.getMaterialName());
                    receiptInfo.setSpec(receiptDetail.getMaterialSpec());
                    receiptInfo.setProject(receiptDetail.getProjectNo());
                    receiptInfo.setReceiptCode(receiptDetail.getReceiptCode());
                    receiptInfo.setReceiptId(String.valueOf(receiptDetail.getReceiptId()));
                    mReceiptInfos.add(receiptInfo);
                }
//                WMSUtils.insertOrUpdateReceiptInfo(mReceiptInfos);
                WMSUtils.resetEdit(inputEdit);
                enableButton(true);
                showEntryLayout();
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
}
