package com.huaheng.mobilewms.activity.shipment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.PDACommonActivity;
import com.huaheng.mobilewms.activity.receipt.EmptyInActivity;
import com.huaheng.mobilewms.bean.Location;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmptyOutActivity extends PDACommonActivity {

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.empty_container_out));
        initView();
    }

    private void initView() {
        inputEdit.setHint(mContext.getString(R.string.enter_task_number));
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if(WMSUtils.isNotEmpty(number)) {
                    isContainer(number);
                }
                WMSUtils.resetEdit(inputEdit);
                return false;
            }
        });
        showContentLayout();
    }

    private void showContentLayout() {
        contentLayout.removeAllViews();
        View replenishView = WMSUtils.newContent(mContext, mContext.getString(R.string.container_number), mContainerCode);
        replenishView.setOnClickListener(pickClickListener);
        contentLayout.addView(replenishView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.location), mLocation));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        if(mContainerCode != null) {
            enableButton(true);
        } else {
            enableButton(false);
        }
    }

    private View.OnClickListener pickClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getEmptyContainerInLocation();
        }
    };


    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        if (mContainerCode == null) {
            WMSUtils.showShort(mContext.getString(R.string.enter_correct_empty_container_in));
            return;
        }
        createEmptyOut(mContainerCode, mLocation);
    }

    private void createEmptyOut(String containerCode, String destinationLocation) {
        HttpInterface.getInsstance().createEmptyOut(new ProgressSubscriber<Integer>(this, createEmptyOutListener), containerCode, destinationLocation);
    }

    SubscriberOnNextListener createEmptyOutListener = new SubscriberOnNextListener<Integer>() {

        @Override
        public void onNext(Integer taskId) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.empty_container_out_success));
            finish();
            WMSUtils.startActivity(mContext, EmptyOutActivity.class);
        }

        @Override
        public void onError(String str) {
            finish();
            WMSUtils.startActivity(mContext, EmptyOutActivity.class);
        }
    };

    private void getEmptyContainerInLocation() {
        HttpInterface.getInsstance().getEmptyContainerInLocation(new ProgressSubscriber<List<Location>>(this, pickListener));
    }

    SubscriberOnNextListener pickListener = new SubscriberOnNextListener<List<Location>>() {

        @Override
        public void onNext(List<Location> locationList) {
            showListDialog(locationList);
        }

        @Override
        public void onError(String str) {
            WMSLog.d("onError:" + str);
        }
    };

    private void showListDialog(final List<Location> locationList) {
        if(locationList == null) {
            WMSUtils.showShort(mContext.getString(R.string.no_choose_empty_out));
            return;
        }
        List<String> locations = new ArrayList<>();
        for(Location location : locationList) {
            locations.add(location.getContainerCode());
        }

        final String[] list = locations.toArray(new String[locations.size()]);
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(mContext);
        listDialog.setTitle(mContext.getString(R.string.choose_empty_out));
        listDialog.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContainerCode = locationList.get(which).getContainerCode();
                getLocationFromContainer(mContainerCode);
            }
        });
        listDialog.show();
    }

    private void getLocationFromContainer(String code) {
        HttpInterface.getInsstance().getLocationFromContainer(new ProgressSubscriber<String>(this, getLocationListener), code);
    }

    SubscriberOnNextListener getLocationListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String str) {
            mLocation = str;
            WMSUtils.resetEdit(inputEdit);
            showContentLayout();
        }

        @Override
        public void onError(String str) {

        }
    };

    private void isContainer(String containerCode) {
        HttpInterface.getInsstance().isContainer(new ProgressSubscriber<String>(this, isContainerListener), containerCode);
    }

    SubscriberOnNextListener isContainerListener = new SubscriberOnNextListener<String>() {

        @Override
        public void onNext(String str) {
            mContainerCode = str;
            showContentLayout();
            getLocationFromContainer(mContainerCode);
        }

        @Override
        public void onError(String str) {

        }
    };

}
