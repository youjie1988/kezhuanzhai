package com.huaheng.mobilewms.activity.task;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.activity.shipment.ShipmentActivity;
import com.huaheng.mobilewms.activity.shipment.ShipmentDetailActivity;
import com.huaheng.mobilewms.activity.shipment.ShipmentTaskInfoActivity;
import com.huaheng.mobilewms.adapter.DetailAdapter;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DetailBean;
import com.huaheng.mobilewms.bean.DictData;
import com.huaheng.mobilewms.bean.InventoryDetail;
import com.huaheng.mobilewms.bean.MobileTask;
import com.huaheng.mobilewms.bean.TaskDetail;
import com.huaheng.mobilewms.bean.TaskHeader;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.WMSUtils;
import com.huaheng.mobilewms.view.EditLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.ListPopupWindow.MATCH_PARENT;

public class TaskActivity extends CommonActivity {


    @BindView(R.id.list)
    ListView listView;
    private List<DetailBean> detailBeanList;
    private DetailAdapter mAdapter;
    private MyHandler myHandler = new MyHandler();
    private List<DictData> mDictDataList;
    private List<TaskHeader> mTaskHeaders;
    private PopupWindow popupWindow;
    private boolean isShowing = false;
    EditLayout containerEdit, taskTypeEdit, statusEdit, fromLocationEdit, toLocationEdit, startTimeEdit, endTimeEdit;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);

        setContentView(R.layout.activity_task);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.task));
        getDictListData(Constant.DICT_TASK_TYPE);
        initView();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String str = bundle.getString("time");
            startTimeEdit.getEditContent().setText(str);
        }

    }

    private void initView() {
        chooseImage.setVisibility(View.VISIBLE);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show();
            }
        });
        View view = View.inflate(mContext, R.layout.choose_popup_window, null);
        popupWindow = new PopupWindow(view, MATCH_PARENT, 720);//参数为1.View 2.宽度 3.高度
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
        containerEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.container_number));
            contentLyaout.addView(containerEdit);
        taskTypeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.task_type));
            contentLyaout.addView(taskTypeEdit);
        statusEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.task_status));
            contentLyaout.addView(statusEdit);
        fromLocationEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.task_from_location));
            contentLyaout.addView(fromLocationEdit);
        toLocationEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.task_to_location));
        contentLyaout.addView(toLocationEdit);
        startTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.start_time));
            contentLyaout.addView(startTimeEdit);
        endTimeEdit = WMSUtils.newEdit(mContext, mContext.getString(R.string.end_time));
            contentLyaout.addView(endTimeEdit);
            WMSUtils.setOnDatePick(mContext, startTimeEdit.getEditContent());
            WMSUtils.setOnDatePick(mContext, endTimeEdit.getEditContent());
    }

    private void search() {
        String containerCode = containerEdit.getEditValue();
        String taskType = taskTypeEdit.getEditValue();
        String status = statusEdit.getEditValue();
        String fromLocation = fromLocationEdit.getEditValue();
        String toLocation = toLocationEdit.getEditValue();
        String startTime = startTimeEdit.getEditValue();
        String endTime = endTimeEdit.getEditValue();

        searchTaskInCondition(containerCode, taskType, status, fromLocation, toLocation, startTime, endTime);
        dismiss();
    }

    private void reset() {
        WMSUtils.resetEdit(containerEdit.getEditContent());
        WMSUtils.resetEdit(taskTypeEdit.getEditContent());
        WMSUtils.resetEdit(statusEdit.getEditContent());
        WMSUtils.resetEdit(fromLocationEdit.getEditContent());
        WMSUtils.resetEdit(toLocationEdit.getEditContent());
        WMSUtils.resetEdit(startTimeEdit.getEditContent());
        WMSUtils.resetEdit(endTimeEdit.getEditContent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        dismiss();
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

    private void showChooseDialog(DetailBean detailBean, final int position) {
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(mContext);
        final List<String> lists = new ArrayList<>();
        lists.add(mContext.getString(R.string.task_detail));
        lists.add(mContext.getString(R.string.force_complete));
        listDialog.setTitle("");
        String[] strs1= lists.toArray(new String[lists.size()]);
        listDialog.setItems(strs1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        Intent intent = new Intent();
                        intent.setClass(mContext, TaskInfoActivity.class);
                        Bundle bundle = new Bundle();
                        String taskId = String.valueOf(mTaskHeaders.get(position).getId());
                        bundle.putString("taskId", taskId);
                        intent.putExtras(bundle);// 发送数据
                        mContext.startActivity(intent);
                        break;
                    case 1:
                        String taskId2= String.valueOf(mTaskHeaders.get(position).getId());
                        completeTaskByWMS(taskId2);
                        break;
                }
            }
        });
        listDialog.show();
    }

    private void showContent(List<TaskHeader> taskHeaderList) {
        detailBeanList = new ArrayList<>();
        for(TaskHeader taskHeader : taskHeaderList) {
            int lastStatus = taskHeader.getStatus();
            int taskType = taskHeader.getTaskType();
            String type = String.valueOf(taskType);
            for(DictData dictData : mDictDataList) {
                if(Integer.parseInt(dictData.getDictValue()) == taskType) {
                    type = dictData.getDictLabel();
                }
            }
            String location = null;
            if(WMSUtils.isEmpty(taskHeader.getFromLocation())) {
                location = taskHeader.getToLocation();
            } else {
                location = taskHeader.getFromLocation();
            }
            if(lastStatus == 100) {
                detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.icon_task),
                        taskHeader.getContainerCode(), location, type, true));
            } else {
                detailBeanList.add(new DetailBean(mContext.getResources().getDrawable(R.mipmap.icon_task),
                        taskHeader.getContainerCode(), location, type));
            }
        }
        if(mAdapter == null) {
            mAdapter = new DetailAdapter(mContext);
            listView.setAdapter(mAdapter);
            listView.setOnItemClickListener(listener);
        }
        mAdapter.setList(detailBeanList);
        mAdapter.notifyDataSetChanged();
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {
            showChooseDialog(detailBeanList.get(position), position);
        }
    };

    private void sendTask(List<TaskHeader> taskHeaders) {
        Message message = new Message();
        message.obj = taskHeaders;
        message.what = 0;
        myHandler.sendMessage(message);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    List<TaskHeader> taskHeaderList = (List<TaskHeader>)msg.obj;
                    showContent(taskHeaderList);
                    break;
            }
        }
    }

    private void getDictListData(String dictType) {
        HttpInterface.getInsstance().getDictListData(new ProgressSubscriber<List<DictData>>(mContext, dictListener), dictType);
    }

    SubscriberOnNextListener dictListener = new SubscriberOnNextListener<List<DictData>>() {
        @Override
        public void onNext(List<DictData> dictDataList) {
            if(dictDataList != null) {
                mDictDataList = dictDataList;
//                searchTask();
                search();
            } else {
                WMSUtils.showShort(getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };

  /*   private void searchTask() {
        HttpInterface.getInsstance().searchTask(new ProgressSubscriber<List<TaskHeader>>(mContext, searchListener));
    }

    SubscriberOnNextListener searchListener = new SubscriberOnNextListener<List<TaskHeader>>() {
        @Override
        public void onNext(List<TaskHeader> taskHeaders) {
            if (WMSUtils.isNotEmptyList(taskHeaders)) {
                mTaskHeaders = taskHeaders;
                sendTask(taskHeaders);
            } else {
                WMSUtils.showShort(mContext.getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    }; */

    private void searchTaskInCondition(String containerCode, String taskType,
                                            String status, String fromLocation, String toLocation, String startTime, String endTime) {
        HttpInterface.getInsstance().searchTaskInCondition(new ProgressSubscriber <List <TaskHeader>>(mContext, searchInLocationListener),
                containerCode, taskType, status, fromLocation, toLocation, startTime, endTime);
    }

    SubscriberOnNextListener searchInLocationListener = new SubscriberOnNextListener <List <TaskHeader>>() {
        @Override
        public void onNext(List <TaskHeader> taskHeaders) {
            if (WMSUtils.isNotEmptyList(taskHeaders)) {
                mTaskHeaders = taskHeaders;
                sendTask(taskHeaders);
            } else {
                WMSUtils.showShort(mContext.getString(R.string.toast_error));
            }
        }

        @Override
        public void onError(String str) {

        }
    };

    private void completeTaskByWMS(String taskId) {
        HttpInterface.getInsstance().completeTaskByWMS(new ProgressSubscriber<String>(mContext, taskhListener), taskId);
    }

    SubscriberOnNextListener taskhListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String result) {
            WMSUtils.showShort(mContext.getString(R.string.force_complete_success));
            search();
        }

        @Override
        public void onError(String str) {
            search();
        }
    };
}
