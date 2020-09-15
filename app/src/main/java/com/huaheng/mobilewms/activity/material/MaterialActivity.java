package com.huaheng.mobilewms.activity.material;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDAListActivity;
import com.huaheng.mobilewms.activity.receipt.AddReceiptActivity;
import com.huaheng.mobilewms.activity.receipt.ReceiptActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.DictData;
import com.huaheng.mobilewms.bean.Material;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.EditLayout;
import com.huaheng.mobilewms.view.SpinnerLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by youjie on 2020/9/11
 */
public class MaterialActivity  extends PDAListActivity {

    private PopupWindow popupWindow;
    private boolean isShowing = false;
    EditLayout codeEdit, nameEdit, specEdit,startTimeEdit, endTimeEdit;
    SpinnerLayout receiptTypeSpinner, statusSpinner;
    private MyHandler myHandler = new MyHandler();
    private List<DetailBean> detailBeanList;
    private DetailAdapter mAdapter;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.materialManager));
        initView();
        initPopupWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
        search();
    }

    private void initView() {
        ensureBtn.setText(mContext.getString(R.string.add_material));
        enableButton(true);
        ensureLayout.setVisibility(View.VISIBLE);
        inputEdit.setHint(mContext.getString(R.string.enter_material_code));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    codeEdit.getEditContent().setText(number);
                    search();
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
        chooseImage.setVisibility(View.VISIBLE);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });

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

    private void initPopupWindow() {
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
        codeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialCode));
        contentLyaout.addView(codeEdit);
        nameEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialName));
        contentLyaout.addView(nameEdit);
        specEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.materialSpec));
        contentLyaout.addView(specEdit);
        startTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.start_time));
        contentLyaout.addView(startTimeEdit);
        endTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.end_time));
        contentLyaout.addView(endTimeEdit);
        WMSUtils.setOnDatePick(mContext, startTimeEdit.getEditContent());
        WMSUtils.setOnDatePick(mContext, endTimeEdit.getEditContent());
    }

    @OnClick(R.id.ensureBtn)
    public void onViewClicked() {
        addMaterial();
    }

    private void addMaterial() {
        WMSUtils.startActivity(mContext, AddMaterialActivity.class);
    }

    private void showContent(List<Material> materialList) {
        detailBeanList = new ArrayList<>();
        for(Material material : materialList) {
            detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.icon_material),
                    material.getCode(), material.getName(), material.getSpec()));
        }
        if(mAdapter == null) {
            mAdapter = new DetailAdapter(mContext);
            listView.setAdapter(mAdapter);
//            listView.setOnItemClickListener(listener);
        }
        mAdapter.setList(detailBeanList);
        mAdapter.notifyDataSetChanged();
    }

    private void search() {
        String code = codeEdit.getEditValue();
        String name = nameEdit.getEditValue();
        String spec = specEdit.getEditValue();
        String startTime = startTimeEdit.getEditValue();
        String endTime = endTimeEdit.getEditValue();
        searchMaterialInCondition(code, name, spec, startTime, endTime);
        dismiss();
    }

    private void reset() {
        WMSUtils.resetEdit(codeEdit.getEditContent());
        receiptTypeSpinner.getSpinner().setSelection(0);
        statusSpinner.getSpinner().setSelection(0);
        WMSUtils.resetEdit(startTimeEdit.getEditContent());
        WMSUtils.resetEdit(endTimeEdit.getEditContent());
    }

    private void sendMaterial(List<Material> materialList) {
        Message message = new Message();
        message.obj = materialList;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<Material> materialList = (List<Material>)msg.obj;
                    showContent(materialList);
                    break;
            }
        }
    }

    private void searchMaterialInCondition(String code, String name,
                                          String spec, String startTime, String endTime) {
        HttpInterface.getInsstance().searchMaterialInCondition(new ProgressSubscriber<List<Material>>(mContext, searchMaterialInConditionListener),
                code, name, spec, startTime, endTime);
    }

    SubscriberOnNextListener searchMaterialInConditionListener = new SubscriberOnNextListener <List <Material>>() {
        @Override
        public void onNext(List<Material> materialList) {
            if (WMSUtils.isNotEmptyList(materialList)) {
                sendMaterial(materialList);
            } else {
                WMSUtils.showShort(mContext.getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };
}
