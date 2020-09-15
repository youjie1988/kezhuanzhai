package com.huaheng.mobilewms.activity.log;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.util.FileUtils;

import butterknife.ButterKnife;

    public class LogListActivity extends CommonActivity {

        @Override
        protected void initActivityOnCreate(Bundle savedInstanceState) {
            super.initActivityOnCreate(savedInstanceState);
            ButterKnife.bind(this);
            setTitle(mContext.getString(R.string.list_log));
            initView();
        }

        private void initView() {
            ListView listView = new ListView(this);
            listView.setBackgroundColor(Color.WHITE);
            listView.getMeasuredHeight();

            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            String[] logs = FileUtils.getCrashLogNames(true);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    logs);

            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, LogDetailActivity.class);
                    intent.putExtra("index", position);
                    startActivity(intent);
                }
            });

            addContentView(listView, params);
        }

    }
