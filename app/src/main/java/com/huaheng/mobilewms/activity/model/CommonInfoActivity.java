package com.huaheng.mobilewms.activity.model;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.huaheng.mobilewms.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by youjie on 2020/4/9
 */
public class CommonInfoActivity  extends CommonActivity{

    @BindView(R.id.contentLayout)
    public LinearLayout contentLayout;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_common_info);
        ButterKnife.bind(this);
    }
}
