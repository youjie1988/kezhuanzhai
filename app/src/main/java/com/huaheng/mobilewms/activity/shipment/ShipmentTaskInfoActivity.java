package com.huaheng.mobilewms.activity.shipment;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.bean.MobileTask;
import com.huaheng.mobilewms.bean.TaskDetail;
import com.huaheng.mobilewms.bean.TaskHeader;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShipmentTaskInfoActivity extends CommonActivity {

    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);

        setContentView(R.layout.activity_shipment_task_info);
        ButterKnife.bind(this);
        setTitle("任务详情");
        initView();
    }

    private void initView() {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_id), "14828"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_priority), "优先级"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_type), "整盘出库"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_station), "出库口1"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_container), "M00100"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_from_location), "L01-01-02"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_to_location), "L01-01-02"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_status), "已经完成"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_create), "2020-03-30 14:56"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_craeted), "李成"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_create), "2020-03-30 15:01"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_craeted), "WCS"));
        contentLayout.addView(WMSUtils.newDevider(mContext));
    }

    private void findTask(String taskHeadId) {
        HttpInterface.getInsstance().findTask(new ProgressSubscriber<MobileTask>(mContext, findTaskListener), taskHeadId);
    }

    SubscriberOnNextListener findTaskListener = new SubscriberOnNextListener<MobileTask>() {
        @Override
        public void onNext(MobileTask mobileTask) {
            if(mobileTask != null) {
                TaskHeader taskHeader =  mobileTask.getTaskHeader();
                List<TaskDetail> taskDetailList = mobileTask.getTaskDetail();
            } else {
                WMSUtils.showShort(getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };
}
