package com.huaheng.mobilewms.activity.receipt;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmptyInActivity extends PDACommonActivity {

    private boolean enterContainer = true;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.empty_container_in));
        initView();
    }

    private void initView() {
        inputEdit.setHint(mContext.getString(R.string.enter_task_number));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    if(enterContainer) {
                        isContainer(number);
                    } else {
                        number = WMSUtils.replaceLocation(number);
                        isLocation(number);
                    }
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
        showContentLayout();
    }

    private void showContentLayout() {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.container_number), mContainerCode));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.location), mLocation));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        if(mContainerCode != null) {
            enableButton(true);
        } else {
            enableButton(false);
        }
    }

    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        if (mContainerCode == null) {
            WMSUtils.showShort(mContext.getString(R.string.enter_correct_empty_container_in));
            return;
        }
        createEmptyIn(mContainerCode, mLocation);
    }

    private void createEmptyIn(String containerCode, String destinationLocation) {
        HttpInterface.getInsstance().createEmptyIn(new ProgressSubscriber<Integer>(this, createEmptyInListener), containerCode, destinationLocation);
    }

    SubscriberOnNextListener createEmptyInListener = new SubscriberOnNextListener<Integer>() {

        @Override
        public void onNext(Integer taskId) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.empty_container_in_success));
            finish();
            WMSUtils.startActivity(mContext, EmptyInActivity.class);
        }

        @Override
        public void onError(String str) {
            finish();
            WMSUtils.startActivity(mContext, EmptyInActivity.class);
        }
    };

    private void isContainer(String containerCode) {
        HttpInterface.getInsstance().isContainer(new ProgressSubscriber<String>(this, isContainerListener), containerCode);
    }

    SubscriberOnNextListener isContainerListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String str) {
            mContainerCode = str;
            enterContainer = false;
            showContentLayout();
        }

        @Override
        public void onError(String str) {

        }
    };

    private void isLocation(String locationCode) {
        HttpInterface.getInsstance().isLocation(new ProgressSubscriber<String>(this, isLocationListener), locationCode);
    }

    SubscriberOnNextListener isLocationListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String str) {
            mLocation = str;
            showContentLayout();
        }

        @Override
        public void onError(String str) {

        }
    };

}
