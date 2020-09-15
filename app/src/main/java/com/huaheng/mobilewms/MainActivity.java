package com.huaheng.mobilewms;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huaheng.mobilewms.activity.SettingsActivity;
import com.huaheng.mobilewms.activity.inventory.InventoryActivity;
import com.huaheng.mobilewms.activity.inventory.InventoryTransactionActivity;
import com.huaheng.mobilewms.activity.material.MaterialActivity;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.activity.printer.PrinterActivity;
import com.huaheng.mobilewms.activity.receipt.ReceiptListActivity;
import com.huaheng.mobilewms.activity.shipment.ShipmentListActivity;
import com.huaheng.mobilewms.activity.task.TaskActivity;
import com.huaheng.mobilewms.adapter.MenuAdapter;
import com.huaheng.mobilewms.adapter.RecyclerViewAdapter;
import com.huaheng.mobilewms.bean.MenuBean;
import com.huaheng.mobilewms.bean.TodayInfo;
import com.huaheng.mobilewms.https.HttpInterface;
import com.huaheng.mobilewms.https.Subscribers.ProgressSubscriber;
import com.huaheng.mobilewms.https.Subscribers.SubscriberOnNextListener;
import com.huaheng.mobilewms.util.MenuConstant;
import com.huaheng.mobilewms.util.WMSUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends CommonActivity {

    @BindView(R.id.commondity)
    TextView commondity;
    @BindView(R.id.receipt)
    TextView receipt;
    @BindView(R.id.shipment)
    TextView shipment;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    @BindView(R.id.inputEdit)
    EditText inputEdit;
    private MenuAdapter mAdapter;
    private GridLayoutManager layoutManager;
    private RecyclerViewAdapter adapter;
    private ArrayList <MenuBean> menuBeans;
    private int spanCount = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setTitle(mContext.getString(R.string.main_menu));
        initMenuList();
        setBackImage(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchTodayInfo();
    }

    private void showContent(TodayInfo todayInfo) {
        commondity.setText(String.valueOf(todayInfo.getTodayTAask()));
        receipt.setText(String.valueOf(todayInfo.getTodayReceipt()));
        shipment.setText(String.valueOf(todayInfo.getTodayShipment()));
    }

    private void initMenuList() {
        layoutManager = new GridLayoutManager(mContext, spanCount);
        adapter = new RecyclerViewAdapter(mContext);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(adapter);
        mAdapter = new MenuAdapter(mContext);
        menuBeans = buildMenuBeans();
        if (menuBeans != null) {
            adapter.setList(menuBeans);
        }
        adapter.setOnItemClickListener(listener);
        inputEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String number = textView.getText().toString();
                if (WMSUtils.isNotEmpty(number)) {
                    number = WMSUtils.getMaterialCode(number);
                    startInventoryInCode(number);
                    WMSUtils.resetEdit(inputEdit);
                }
                return false;
            }
        });
    }

    private ArrayList <MenuBean> buildMenuBeans() {
        ArrayList <MenuBean> menuBeans = new ArrayList <>();
        ArrayList <String> menuList = MenuConstant.getMenuList(mContext);
        ArrayList <Drawable> drawableList = MenuConstant.getMenuDrawable(mContext);
        MenuBean menuBean;
        for (int i = 0; i < menuList.size(); i++) {
            menuBean = new MenuBean(drawableList.get(i), menuList.get(i));
            menuBeans.add(menuBean);
        }
        return menuBeans;
    }

    private RecyclerViewAdapter.OnRecyclerViewItemClickListener listener = new RecyclerViewAdapter.OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(int position) {
            chooseItemListener(position);
        }
    };

    private void chooseItemListener(int positino) {
        MenuBean bean = menuBeans.get(positino);
        String name = bean.getName();
        if (name.equals(mContext.getString(R.string.receipt))) {
            WMSUtils.startActivity(mContext, ReceiptListActivity.class);
        } else if (name.equals(mContext.getString(R.string.shipment))) {
            WMSUtils.startActivity(mContext, ShipmentListActivity.class);
        } else if (name.equals(mContext.getString(R.string.task))) {
            startTask();
        } else if (name.equals(mContext.getString(R.string.menu_inventory))) {
            startInventory();
        } else if (name.equals(mContext.getString(R.string.printer))) {
            WMSUtils.startActivity(mContext, PrinterActivity.class);
        } else if (name.equals(mContext.getString(R.string.materialManager))) {
            WMSUtils.startActivity(mContext, MaterialActivity.class);
        } else if (name.equals(mContext.getString(R.string.setting))) {
            WMSUtils.startActivity(mContext, SettingsActivity.class);
        }
    }

    @OnClick({R.id.todayTask, R.id.todayReceipt, R.id.todayShipment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.todayTask:
                startTodayTask();
                break;
            case R.id.todayReceipt:
                startTodayReceipt();
                break;
            case R.id.todayShipment:
                startTodayShipment();
                break;
        }
    }

    private void startTodayTask() {
        Intent intent = new Intent();
        intent.setClass(mContext, TaskActivity.class);
        Bundle bundle = new Bundle();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(new Date());
        bundle.putString("time", time);
        intent.putExtras(bundle);// 发送数据
        startActivity(intent);
    }

    private void startTodayReceipt() {
        Intent intent = new Intent();
        intent.setClass(mContext, InventoryTransactionActivity.class);
        Bundle bundle = new Bundle();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(new Date());
        bundle.putString("time", time);
        bundle.putInt("type", 10);
        intent.putExtras(bundle);// 发送数据
        startActivity(intent);
    }

    private void startTodayShipment() {
        Intent intent = new Intent();
        intent.setClass(mContext, InventoryTransactionActivity.class);
        Bundle bundle = new Bundle();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String time = formatter.format(new Date());
        bundle.putString("time", time);
        bundle.putInt("type", 20);
        intent.putExtras(bundle);// 发送数据
        startActivity(intent);
    }

    private void startTask() {
        Intent intent = new Intent();
        intent.setClass(mContext, TaskActivity.class);
        Bundle bundle = new Bundle();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = calendar.getTime();
        String time = formatter.format(newDate);
        bundle.putString("time", time);
        intent.putExtras(bundle);// 发送数据
        startActivity(intent);
    }

    private void startInventory() {
        Intent intent = new Intent();
        intent.setClass(mContext, InventoryActivity.class);
        Bundle bundle = new Bundle();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = calendar.getTime();
        String time = formatter.format(newDate);
        bundle.putString("time", time);
        intent.putExtras(bundle);// 发送数据
        startActivity(intent);
    }

    private void startInventoryInCode(String code) {
        Intent intent = new Intent();
        intent.setClass(mContext, InventoryActivity.class);
        Bundle bundle = new Bundle();
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = calendar.getTime();
        String time = formatter.format(newDate);
        bundle.putString("code", code);
        intent.putExtras(bundle);// 发送数据
        startActivity(intent);
    }

    private void searchTodayInfo() {
        ProgressSubscriber progressSubscriber = new ProgressSubscriber <TodayInfo>(mContext, searchListener);
        progressSubscriber.setShowDialog(false);
        HttpInterface.getInsstance().searchTodayInfo(progressSubscriber);
    }

    SubscriberOnNextListener searchListener = new SubscriberOnNextListener <TodayInfo>() {
        @Override
        public void onNext(TodayInfo todayInfo) {
            showContent(todayInfo);
        }

        @Override
        public void onError(String str) {

        }
    };

}
