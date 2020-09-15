package com.huaheng.mobilewms.activity.receipt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.activity.model.PDAListCommonActivity;
import com.huaheng.mobilewms.adapter.DetailAmountAdapter;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.Receipt;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptDetail;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.bean.ReceiptInfo;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.AmountView;
import com.huaheng.mobilewms.view.EditLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by youjie
 * on 2020/2/19
 */
public class ReceiptSupplementActivity extends PDAListCommonActivity {


    private boolean scanBill = false;
    private String mCode, receiptID, mCode2, mCode3;
    private ArrayList<ReceiptInfo> mReceiptInfos;
    private ArrayList<ReceiptBill> receiptBills = new ArrayList<>();
    private List<DetailBean> detailBeanList;
    private DetailAmountAdapter mAdapter;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        mReceiptInfos = new ArrayList <>();
        setTitle(mContext.getString(R.string.replenish_the_warehouse));
        initView();
    }

    private void initView() {
        inputEdit.setHint(mContext.getString(R.string.enter_single_number));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String content = textView.getText().toString();
                if(content == null || content.length() < 1) {
                    return false;
                }
                if(!scanBill) {
                    findReceipt(content);
                    return false;
                }
                String number = null;
                number = content;
                if(WMSUtils.isLocation(number)) {
                    isContainer(number);
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
    }

    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        if(receiptBills != null && receiptBills.size() > 0) {
            if(!WMSUtils.checkNumber(receiptBills)) {
                WMSUtils.showShort(mContext.getString(R.string.quantity_commodity_number));
                return;
            }
            quickReceipt();
        }
    }

    private void showContentLayout() {
        inputEdit.setHint(mContext.getString(R.string.enter_container_number));
        contentLayout.removeAllViews();
        View locationView = WMSUtils.newContent(mContext, mContext.getString(R.string.location), mLocation);
        contentLayout.addView(locationView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        View containerView = WMSUtils.newContent(mContext, mContext.getString(R.string.container_number), mContainerCode);
        contentLayout.addView(containerView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        if(!WMSUtils.isNotEmptyList(mReceiptInfos)) {
            return;
        }
        detailBeanList = new ArrayList<>();
        for(final ReceiptInfo receiptInfo : mReceiptInfos) {
            int tobeCollectQty = (int)receiptInfo.getTotalQty() - (int)receiptInfo.getOpenQty();
            DetailBean detailBean = new DetailBean(mContext.getResources().getDrawable(R.mipmap.kekoukele),
                    receiptInfo.getMaterialCode(), receiptInfo.getMaterialName(), String.valueOf(tobeCollectQty));
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
                ReceiptInfo receiptInfo = mReceiptInfos.get(id);
                receiptInfo.setQty(amount);
                DetailBean detailBean = detailBeanList.get(id);
                detailBean.setAmount(amount);
                updateReceipt();
            }
        });
        if(mContainerCode != null) {
            enableButton(true);
        }
        updateReceipt();
    }


    private void updateReceipt() {
        receiptBills.clear();
        boolean enable = false;
        for(int i = mReceiptInfos.size() - 1; i>=0; i--) {
            final ReceiptInfo receiptInfo = mReceiptInfos.get(i);
            ReceiptBill receiptBill = new ReceiptBill();
            receiptBill.setLocationCode(mLocation);
            receiptBill.setQty(new BigDecimal(receiptInfo.getQty()));
            receiptBill.setMaterialCode(receiptInfo.getMaterialCode());
            receiptBill.setProject(receiptInfo.getProject());
            receiptBill.setBatch(receiptInfo.getBatch());
            receiptBill.setReceiptDetailId(receiptInfo.getId());
            receiptBill.setReceiptContainerCode(mContainerCode);
            receiptBill.setMaterialName(receiptInfo.getMaterialName());
            receiptBills.add(receiptBill);
            int qty = (int)receiptInfo.getQty();
            if(qty > 0) {
                enable = true;
            }
        }
        if(mContainerCode == null) {
            enable = false;
        }
        enableButton(enable);
    }


    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

        @Override
        public boolean onLongClick(View view) {
            ReceiptInfo receiptInfo = (ReceiptInfo)view.getTag();
            showDeleteDialog(receiptInfo);
            return false;
        }
    };

    private void showDeleteDialog(final ReceiptInfo receiptInfo) {
        AlertDialog mDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setTitle(getString(R.string.delete))
                .setMessage(mContext.getString(R.string.delete_material))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        mReceiptInfos.remove(receiptInfo);
                        showContentLayout();
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

    private void findReceipt(String referCode) {
        HttpInterface.getInsstance().findReceipt(new ProgressSubscriber<Receipt>(mContext, receiptListener), referCode);
    }

    SubscriberOnNextListener receiptListener = new SubscriberOnNextListener<Receipt>() {
        @Override
        public void onNext(Receipt receipt) {
            if(receipt != null) {
                ReceiptHeader receiptHeader =  receipt.getReceiptHeader();
                receiptID = receiptHeader.getCode();
                List<ReceiptDetail> receiptDetails = receipt.getReceiptDetails();
                for(ReceiptDetail receiptDetail : receiptDetails) {
                    ReceiptInfo receiptInfo = new ReceiptInfo();
                    receiptInfo.setOpenQty(receiptDetail.getOpenQty());
                    receiptInfo.setId(String.valueOf(receiptDetail.getId()));
                    receiptInfo.setBatch(receiptDetail.getBatch());
                    receiptInfo.setMaterialCode(receiptDetail.getMaterialCode());
                    receiptInfo.setMaterialName(receiptDetail.getMaterialName());
                    receiptInfo.setProject(receiptDetail.getProjectNo());
                    receiptInfo.setReceiptCode(receiptDetail.getReceiptCode());
                    receiptInfo.setReceiptId(String.valueOf(receiptDetail.getReceiptId()));
                    receiptInfo.setTotalQty(receiptDetail.getTotalQty());
                    int maxValue = (int)receiptDetail.getTotalQty() - (int) receiptDetail.getOpenQty();
                    receiptInfo.setQty(maxValue);
                    mReceiptInfos.add(receiptInfo);
                }
                scanBill = true;
                WMSUtils.resetEdit(inputEdit);
                showContentLayout();
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

    private void scanContainer(String number) {
        mCode = number;
        ProgressSubscriber progressSubscriber = new ProgressSubscriber<String>(this, sanContainerListener);
        progressSubscriber.setShowError(false);
        HttpInterface.getInsstance().sanContainer(progressSubscriber, number);
    }

    SubscriberOnNextListener sanContainerListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String s) {
            mContainerCode = mCode;
            WMSUtils.resetEdit(inputEdit);
            getLocationFromContainer(mCode);
            showContentLayout();
        }

        @Override
        public void onError(String str) {
            WMSUtils.resetEdit(inputEdit);
//            getLocationFromContainer(mCode);
        }
    };

    private void isContainer(String containerCode) {
        HttpInterface.getInsstance().isContainer(new ProgressSubscriber<String>(this, isContainerListener), containerCode);
    }

    SubscriberOnNextListener isContainerListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String str) {
            mContainerCode = str;
            getLocationFromContainer(mContainerCode);
            WMSUtils.resetEdit(inputEdit);
        }

        @Override
        public void onError(String str) {
            WMSUtils.showShort(str);
            WMSUtils.resetEdit(inputEdit);
        }
    };

    private void getLocationFromContainer(String code) {
        mCode3 = code;
        HttpInterface.getInsstance().getLocationFromContainer(new ProgressSubscriber<String>(this, getLocationListener), code);
    }

    SubscriberOnNextListener getLocationListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String str) {
            mLocation = str;
            mContainerCode = mCode3;
            showContentLayout();
        }

        @Override
        public void onError(String str) {
            mContainerCode = null;
            showContentLayout();
        }
    };


    private void quickReceipt() {
        HttpInterface.getInsstance().quickReceipt(new ProgressSubscriber<String>(this, quickReceiptListener), receiptBills);
    }

    SubscriberOnNextListener quickReceiptListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String result) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.receipt_success));
            finish();
            WMSUtils.startActivity(mContext, ReceiptSupplementActivity.class);
        }

        @Override
        public void onError(String str) {

        }
    };

}
