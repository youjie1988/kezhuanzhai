package com.huaheng.mobilewms.activity.task;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.activity.model.CommonInfoActivity;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DictData;
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
import rx.Subscriber;

public class TaskInfoActivity extends CommonInfoActivity {

    private String taskHeadId;
    private List<DictData> mDictDataList_taskType;
    private List<DictData> mDictDataList_taskStatus;
    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);

        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.task_detail));
        Bundle bundle = getIntent().getExtras();
        taskHeadId = bundle.getString("taskId");

        getDictListData_TaskStatus();
    }

    private String getDictLabel(List<DictData> dict, int type){
        String typeStr = String.valueOf(type);
        for(DictData dictData : dict) {
            if(Integer.parseInt(dictData.getDictValue()) == type) {
                typeStr = dictData.getDictLabel();
            }
        }
        return typeStr;
    }

    private void showContent(TaskHeader taskHeader) {
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_id), String.valueOf(taskHeader.getId())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_type), getDictLabel(mDictDataList_taskType,taskHeader.getTaskType())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_container), taskHeader.getContainerCode()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_from_location), taskHeader.getFromLocation()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_to_location), taskHeader.getToLocation()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_status), getDictLabel(mDictDataList_taskStatus,taskHeader.getStatus())));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_create), taskHeader.getCreated()));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.task_craeted), taskHeader.getCreatedBy()));
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
                showContent(taskHeader);
            } else {
                WMSUtils.showShort(getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };

    /**取任务类型字典信息**/
    private void getDictListData_TaskType() {
        HttpInterface.getInsstance().getDictListData(new Subscriber<List<DictData>>(){
            @Override
            public void onNext(List<DictData> dictDataList) {
                if(dictDataList != null) {
                    mDictDataList_taskType = dictDataList;
                    findTask(taskHeadId);
                } else {
                    WMSUtils.showShort(getString(R.string.toast_error));
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onCompleted() {
            }
        }, Constant.DICT_TASK_TYPE);
    }



    /**取任务状态字典信息**/
    private void getDictListData_TaskStatus() {
        HttpInterface.getInsstance().getDictListData(new Subscriber<List<DictData>>(){
            @Override
            public void onNext(List<DictData> dictDataList) {
                if(dictDataList != null) {
                    mDictDataList_taskStatus = dictDataList;
                    getDictListData_TaskType();
                } else {
                    WMSUtils.showShort(getString(R.string.toast_error));
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onCompleted() {
            }
        }, Constant.DICT_TASK_STATUS);
    }
}
