package com.huaheng.mobilewms.activity.model;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.huaheng.mobilewms.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PDAListCommonActivity extends CommonActivity {

    @BindView(R.id.inputEdit)
    public EditText inputEdit;
    @BindView(R.id.ensureBtn)
    public Button ensureBtn;
    @BindView(R.id.ensureLayout)
    public LinearLayout ensureLayout;
    public String mContainerCode, mLocation;
    @BindView(R.id.list)
    public ListView listView;
    @BindView(R.id.contentLayout)
    public LinearLayout contentLayout;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);

        setContentView(R.layout.activity_pda_list_common);
        ButterKnife.bind(this);
        enableButton(false);
    }


    public void enableButton(boolean enable) {
        if (enable) {
            ensureBtn.setBackgroundResource(R.drawable.blue_button_bg);
            ensureLayout.setClickable(true);
            ensureLayout.setVisibility(View.VISIBLE);
        } else {
            ensureBtn.setBackgroundResource(R.drawable.gray_button_bg);
            ensureLayout.setClickable(false);
            ensureLayout.setVisibility(View.GONE);
        }
    }
}
