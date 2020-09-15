    package com.huaheng.mobilewms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.huaheng.mobilewms.LoginActivity;
import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.log.LogListActivity;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.bean.ApkInfo;
import com.huaheng.mobilewms.bean.CompanyInfo;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.download.DownloadUtils;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.FileUtils;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

    public class SettingsActivity extends CommonActivity {


    @BindView(R.id.contentLayout)
    LinearLayout contentLayout;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.menu_settings));

        initView();
    }

    private void initView() {
        String currentWareHouse = WMSUtils.getData(Constant.CURREN_WAREHOUSE);
        String currentCompanyName = WMSUtils.getData(Constant.CURREN_COMPANY_NAME);
        String loginName = WMSUtils.getData(Constant.LOGIN_NAME);
        contentLayout.removeAllViews();
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.current_warehouse), currentWareHouse));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        View companyView = WMSUtils.newContent(mContext, mContext.getString(R.string.company_code), currentCompanyName);
        companyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompanyInfo();
            }
        });
        contentLayout.addView(companyView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.current_user), loginName));
        contentLayout.addView(WMSUtils.newDevider(mContext));
        View logView = WMSUtils.newContent(mContext, mContext.getString(R.string.list_log),
                String.valueOf(FileUtils.getCrashLogFiles().length) + "条");
        contentLayout.addView(logView);
        logView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogs();
            }
        });
        contentLayout.addView(WMSUtils.newDevider(mContext));
//        contentLayout.addView(WMSUtils.newContent(mContext, mContext.getString(R.string.combinate_station), "站台1"));
//        contentLayout.addView(WMSUtils.newDevider(mContext));
        View versionView = WMSUtils.newContent(mContext, mContext.getString(R.string.current_version), WMSUtils.getVersionName());
        versionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUpdateApkInfo();
            }
        });
        contentLayout.addView(versionView);
        contentLayout.addView(WMSUtils.newDevider(mContext));
    }


    @OnClick(R.id.ensureLayout)
    public void onViewClicked() {
        logout();
    }

    private void logout() {
        WMSUtils.startActivity(mContext, LoginActivity.class);
    }

    private void getCompanyInfo() {
        HttpInterface.getInsstance().getCompanyInfo(new ProgressSubscriber <ArrayList <CompanyInfo>>(mContext, getCompanyInfoListener));
    }

    SubscriberOnNextListener getCompanyInfoListener = new SubscriberOnNextListener<ArrayList<CompanyInfo>>() {

        @Override
        public void onNext(ArrayList<CompanyInfo> companyInfos) {
            showListDialog(companyInfos);
        }

        @Override
        public void onError(String str) {

        }
    };

    private void showLogs(){
        Intent intent = new Intent();
        intent.setClass(mContext, LogListActivity.class);
        startActivity(intent);
    }

    private void showListDialog(final ArrayList<CompanyInfo> companyInfos) {
        if (companyInfos == null) {
            WMSUtils.showShort(mContext.getString(R.string.choose_current_company_error));
            return;
        }
        String[] companyNames = new String[companyInfos.size()];
        for(int i =0; i< companyInfos.size(); i++) {
            companyNames[i] = companyInfos.get(i).getCompanyName();
        }
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(mContext);
        listDialog.setTitle(mContext.getString(R.string.enter_current_company));
        listDialog.setItems(companyNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentCompanyCode = companyInfos.get(which).getCompanyCode();
                String currentCompanyName = companyInfos.get(which).getCompanyName();
                int currentId = companyInfos.get(which).getCompanyId();
                WMSUtils.saveData(Constant.CURREN_COMPANY_CODE, currentCompanyCode);
                WMSUtils.saveData(Constant.CURREN_COMPANY_NAME, currentCompanyName);
                WMSUtils.saveData(Constant.CURREN_COMPANY_ID, String.valueOf(currentId));
                initView();
            }
        });
        listDialog.show();
    }

    private void getUpdateApkInfo() {
        ProgressSubscriber progressSubscriber =  new ProgressSubscriber<ApkInfo>(mContext, getUpdateApkInfoListener);
        progressSubscriber.setPlaySound(false);
        HttpInterface.getInsstance().getUpdateApkInfo(progressSubscriber, WMSUtils.getPackageName(), String.valueOf(WMSUtils.getVersionCode()));
    }

    SubscriberOnNextListener getUpdateApkInfoListener = new SubscriberOnNextListener<ApkInfo>() {

        @Override
        public void onNext(ApkInfo apkInfo) {
            String url = apkInfo.getUrl();
            String md5 = apkInfo.getMd5();
            int version  = apkInfo.getVersionCode();
            DownloadUtils.as(mContext).download(url, md5, version);
        }

        @Override
        public void onError(String str) {

        }
    };
}
