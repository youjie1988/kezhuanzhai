package com.huaheng.mobilewms.activity.log;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonInfoActivity;
import com.huaheng.mobilewms.util.FileUtils;

import java.io.File;

import butterknife.ButterKnife;

public class LogDetailActivity extends CommonInfoActivity {
    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.list_log));
        initView();
    }

    private void initView(){
        TextView tx = new TextView(mContext);
        int index = getIntent().getIntExtra("index", 0);

        try {
            contentLayout.setBackgroundColor(Color.WHITE);
            File file = FileUtils.getCrashLogFiles()[index];
            String str = FileUtils.readFileToString(file);
            tx.setText(str);
//            tx.setTextSize(18);

        }catch (Exception e){
            tx.setText("日志读取失败: " + e.getMessage());
        }finally {
            contentLayout.addView(tx);
        }
    }
}
