package com.huaheng.mobilewms.activity.model;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.huaheng.mobilewms.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by youjie on 2018/6/14.
 */

public class CommonActivity extends Activity {

    @BindView(R.id.backImage)
    ImageView backImage;
    @BindView(R.id.commonTitle)
    TextView commonTitle;
    @BindView(R.id.chooseImage)
    public ImageView chooseImage;
    @BindView(R.id.commonLayout)
    public FrameLayout commonLayout;
    private ViewGroup viewGroup;
    public Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_common);
        mContext = this;
        viewGroup = (ViewGroup) this.findViewById(R.id.basePageContainer);
        initActivityOnCreate(savedInstanceState);
    }

    /**
     * 页面初始化
     */
    protected void initActivityOnCreate(Bundle savedInstanceState) {

    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater.from(this).inflate(layoutResID, viewGroup, true);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        viewGroup.addView(view, params);
    }

    public void setTitle(String message) {
        if (commonTitle != null) {
            commonTitle.setText(message);
        }
    }

    public void setBackImage(int visibility) {
        backImage.setVisibility(visibility);
    }


    @OnClick(R.id.backImage)
    public void onClick() {
        onBackPressed();
    }


}
