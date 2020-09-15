package com.huaheng.mobilewms;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.https.HttpConstant;
import com.huaheng.mobilewms.util.WMSUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by youjie on 2018/9/19
 */
public class NetworkSettingActivity extends CommonActivity {


    @BindView(R.id.networkEdit)
    EditText networkEdit;
    @BindView(R.id.myNetwork)
    TextView myNetwork;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_network_setting);
        ButterKnife.bind(this);
        setTitle(getString(R.string.configure_network));
        freshText();
    }

    @OnClick({R.id.resetDefaultBtn, R.id.setBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.resetDefaultBtn:
                networkEdit.setText(HttpConstant.URL);
                break;
            case R.id.setBtn:
                setNetwork();
                break;
        }
    }

    private void freshText() {
        String network = WMSUtils.getData(Constant.NETWORK);
        network = this.getString(R.string.local ) + network;
        myNetwork.setText(network);
    }

    private void setNetwork() {
        String network = networkEdit.getText().toString();
        if (network != null && network.length() > 0) {
            WMSUtils.saveData(Constant.NETWORK, network);
            WMSUtils.showShort(getString(R.string.setting_success));
            freshText();
        } else {
            WMSUtils.showShort(getString(R.string.enter_error_network));
        }
    }

}
