package com.huaheng.mobilewms;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.huaheng.mobilewms.bean.ApkInfo;
import com.huaheng.mobilewms.bean.CompanyInfo;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.ModulesBean;
import com.huaheng.mobilewms.bean.TokenBean;
import com.huaheng.mobilewms.bean.UserBean;
import com.huaheng.mobilewms.download.DownloadUtils;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.SpeechUtil;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;


public class LoginActivity extends Activity{

    @BindView(R.id.userEdit)
    EditText userEdit;
    @BindView(R.id.passwordEdit)
    EditText passwordEdit;
    @BindView(R.id.loginBtn)
    Button loginBtn;
    @BindView(R.id.eyeImage)
    ImageView eyeImage;
    @BindView(R.id.logo)
    ImageView logoImage;
    private Context mContext;
    private boolean passwordType = false;
    private int count = 0;
    private int MAX_COUNT = 3;
    private String userName;
    private String password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        initView();
        WMSUtils.acquireWakeLock(WMSApplication.getContext());
        WMSUtils.requestAppPermission(LoginActivity.this);
        HttpInterface.getInsstance().reset();
        getUpdateApkInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView() {
        String userName = WMSUtils.getData(Constant.LOGIN_NAME);
        if (userName != null) {
            userEdit.setText(userName);
            WMSUtils.requestFocus(passwordEdit);
        }
        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                login();
                return false;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_MENU) {
            WMSUtils.startActivity(mContext, NetworkSettingActivity.class);
        } else if(keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.eyeImage, R.id.loginBtn, R.id.logo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.eyeImage:
                if (passwordType) {
                    passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    passwordType = false;
                    eyeImage.setImageResource(R.mipmap.eye);
                } else {
                    passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordType = true;
                    eyeImage.setImageResource(R.mipmap.eyeoff);
                 }
                break;
            case R.id.loginBtn:
                login();
                break;
            case R.id.logo:
                count++;
                if(count >= MAX_COUNT) {
                    count = 0;
                    WMSUtils.startActivity(mContext, NetworkSettingActivity.class);
                }
                break;
        }
    }

    public void login() {
         userName =  userEdit.getText().toString();
         password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            WMSUtils.showShort(mContext.getString(R.string.enter_username));
            WMSUtils.requestFocus(userEdit);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            WMSUtils.showShort(mContext.getString(R.string.enter_password));
            WMSUtils.requestFocus(passwordEdit);
            return;
        }
        HttpInterface.getInsstance().reset();

        HttpInterface.getInsstance().getToken(new ProgressSubscriber <String>(mContext, tokenListener), userName, password);
    }

    SubscriberOnNextListener tokenListener = new SubscriberOnNextListener<String>() {
        @Override
        public void onNext(String tokenBean) {
            WMSLog.d("tokenBean :" + tokenBean);
            WMSApplication.setToken(tokenBean);
            HttpInterface.getInsstance().login(new ProgressSubscriber<ArrayList<UserBean>>(mContext, loginListener), userName, password);
        }

        @Override
        public void onError(String str) {
            passwordEdit.setText("");
        }
    };



    SubscriberOnNextListener loginListener = new SubscriberOnNextListener<ArrayList<UserBean>>() {
        @Override
        public void onNext(ArrayList<UserBean> userBean) {
            SpeechUtil.getInstance(mContext).speech(mContext.getString(R.string.login_success));
            WMSUtils.saveData(Constant.LOGIN_NAME, userEdit.getText().toString());
            passwordEdit.setText("");
            chooseWareHouse(userBean);
            WMSUtils.startActivity(mContext, MainActivity.class);
        }

        @Override
        public void onError(String str) {
            passwordEdit.setText("");
        }
    };

    private void getCompanyInfo() {
        HttpInterface.getInsstance().getCompanyInfo(new Subscriber <ArrayList <CompanyInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArrayList <CompanyInfo> companyInfos) {
                CompanyInfo companyinfo = companyInfos.get(0);
                String companyName = companyinfo.getCompanyName();
                String companyCode = companyinfo.getCompanyCode();
                int companyId = companyinfo.getCompanyId();
                String currentCompanyName = WMSUtils.getData(Constant.CURREN_COMPANY_NAME);
                String currentCompanyCode = WMSUtils.getData(Constant.CURREN_COMPANY_NAME);
                String currentCompanyId = WMSUtils.getData(Constant.CURREN_COMPANY_NAME);
                if (WMSUtils.isEmpty(currentCompanyName)) {
                    WMSUtils.saveData(Constant.CURREN_COMPANY_NAME, companyName);
                }
                if (WMSUtils.isEmpty(currentCompanyCode)) {
                    WMSUtils.saveData(Constant.CURREN_COMPANY_CODE, companyCode);
                }
                if (WMSUtils.isEmpty(currentCompanyId)) {
                    WMSUtils.saveData(Constant.CURREN_COMPANY_ID, String.valueOf(companyId));
                }
            }
        });
    }

    private void chooseWareHouse(ArrayList<UserBean> mlist) {
        String warehouseCode = mlist.get(0).getCode();
        int warehouseId = mlist.get(0).getId();
        WMSUtils.saveData(Constant.CURREN_WAREHOUSE, mlist.get(0).getName());
        HttpInterface.getInsstance().getModules(new Subscriber <ArrayList <ModulesBean>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArrayList <ModulesBean> modulesBeans) {
                getCompanyInfo();
            }
        }, warehouseCode, warehouseId);
    }

    private void getUpdateApkInfo() {
        HttpInterface.getInsstance().getUpdateApkInfo(new Subscriber<ApkInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ApkInfo apkInfo) {
                String url = apkInfo.getUrl();
                String md5 = apkInfo.getMd5();
                int version  = apkInfo.getVersionCode();
                DownloadUtils.as(mContext).download(url, md5, version);
            }
        }, WMSUtils.getPackageName(), String.valueOf(WMSUtils.getVersionCode()));
    }
}
