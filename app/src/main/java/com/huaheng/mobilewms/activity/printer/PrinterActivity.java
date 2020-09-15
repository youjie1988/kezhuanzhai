package com.huaheng.mobilewms.activity.printer;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.WMSApplication;
import com.huaheng.mobilewms.activity.model.CommonActivity;
import com.huaheng.mobilewms.adapter.ChooseAdapter;
import com.huaheng.mobilewms.bean.ChooseBean;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_ATTACHED;
import static android.hardware.usb.UsbManager.ACTION_USB_DEVICE_DETACHED;

/**
 * Created by youjie
 * on 2020/2/7
 */
public class PrinterActivity extends CommonActivity {


    @BindView(R.id.collect_list)
    ListView collectList;
    @BindView(R.id.tv_state)
    TextView tvConnState;
    private ChooseAdapter mAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private List <ChooseBean> chooseBeanList;
    private ThreadPool threadPool;
    /**
     * 判断打印机所使用指令是否是ESC指令
     */
    private int id = 0;
    private final int PRINTER_LIST = 0;
    private final int PRINTER_MATERIAL = 1;
    private final int CONNECT_BLUETOOTH = 2;

    private String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.BLUETOOTH
    };
    ArrayList<String> per = new ArrayList<>();
    private static final int REQUEST_CODE = 0x004;

    @Override
    protected void initActivityOnCreate(Bundle savedInstanceState) {
        super.initActivityOnCreate(savedInstanceState);
        setContentView(R.layout.activity_printer);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setTitle(getString(R.string.printer));
        checkPermission();
        requestPermission();
        initView();
        initBroadcast();
        autoConnect();
    }

    private void checkPermission() {
        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, permission)) {
                per.add(permission);
            }
        }
    }

    private void requestPermission() {
        if (per.size() > 0) {
            String[] p = new String[per.size()];
            ActivityCompat.requestPermissions(this, per.toArray(p), REQUEST_CODE);
        }
    }


    private void initView() {
        chooseBeanList = new ArrayList <>();
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_printer), this.getString(R.string.printer_list)));
        chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_printer), this.getString(R.string.printer_material)));
     //   chooseBeanList.add(new ChooseBean(this.getResources().getDrawable(R.mipmap.menu_icon_printer), this.getString(R.string.connect_bluetooth)));
        mAdapter = new ChooseAdapter(this);
        collectList.setAdapter(mAdapter);
        collectList.setOnItemClickListener(listener);
        mAdapter.setList(chooseBeanList);
    }

    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {
            switch (position) {
                case PRINTER_LIST:
                    WMSUtils.startActivity(mContext, PrinterListActivity.class);
                    break;
                case PRINTER_MATERIAL:
                    WMSUtils.startActivity(mContext, PrinterMaterialActivity.class);
                    break;
                case CONNECT_BLUETOOTH:
                    startActivityForResult(new Intent(mContext, BluetoothDeviceList.class), Constant.BLUETOOTH_REQUEST_CODE);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                /*蓝牙连接*/
                case Constant.BLUETOOTH_REQUEST_CODE: {
                    /*获取蓝牙mac地址*/
                    String macAddress = data.getStringExtra(BluetoothDeviceList.EXTRA_DEVICE_ADDRESS);
                    connectBluetoothPrinter(macAddress);
                    break;
                }
            }
        }
    }

    private void autoConnect() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices != null && pairedDevices.size() > 0) {
            BluetoothDevice bluetoothDevice = pairedDevices.iterator().next();
            String address = bluetoothDevice.getAddress();
            if (!WMSApplication.isBluetoothConnect()) {
                connectBluetoothPrinter(address);
            }
        }
    }

    private void connectBluetoothPrinter(String macAddress) {
        closeport();
        //初始化话DeviceConnFactoryManager
        new DeviceConnFactoryManager.Build()
                .setId(id)
                //设置连接方式
                .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.BLUETOOTH)
                //设置连接的蓝牙mac地址
                .setMacAddress(macAddress)
                .build();
        //打开端口
        threadPool = ThreadPool.getInstantiation();
        threadPool.addSerialTask(new Runnable() {
            @Override
            public void run() {
                DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
            }
        });
    }

    private void closeport() {
        if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] != null && DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort != null) {
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].reader.cancel();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort.closePort();
            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].mPort = null;
        }
    }

    /**
     * 注册广播
     * Registration broadcast
     */
    private void initBroadcast() {
        IntentFilter filter = new IntentFilter(ACTION_USB_DEVICE_DETACHED);//USB访问权限广播
        filter.addAction(DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE);//查询打印机缓冲区状态广播，用于一票一控
        filter.addAction(DeviceConnFactoryManager.ACTION_CONN_STATE);//与打印机连接状态
        filter.addAction(ACTION_USB_DEVICE_ATTACHED);//USB线插入
        registerReceiver(receiver, filter);
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                //连接状态
                case DeviceConnFactoryManager.ACTION_CONN_STATE:
                    int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                    int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                    switch (state) {
                        case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                            if (id == deviceId) {
                                WMSLog.d("connection is lost");
                                tvConnState.setText(getString(R.string.str_conn_state_disconnect));
                            }
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                            tvConnState.setText(getString(R.string.str_conn_state_connecting));
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                            WMSApplication.setBluetoothConnect(true);
                            tvConnState.setText(getString(R.string.str_conn_state_connected) + "\n" + getConnDeviceInfo());
                            break;
                        case DeviceConnFactoryManager.CONN_STATE_FAILED:
//                            Utils.toast(MainActivity.this, getString(R.string.str_conn_fail));
                            //wificonn=false;
                            tvConnState.setText(getString(R.string.str_conn_state_disconnect));
                            break;
                        default:
                            break;
                    }
                    break;
                //连续打印，一票一控，防止打印机乱码
                case DeviceConnFactoryManager.ACTION_QUERY_PRINTER_STATE:
//                    if (counts >= 0) {
//                        if (continuityprint) {
//                            printcount++;
//                            Utils.toast(MainActivity.this, getString(R.string.str_continuityprinter) + " " + printcount);
//                        }
//                        if (counts != 0) {
//                            sendContinuityPrint();
//                        } else {
//                            continuityprint = false;
//                        }
//                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获取当前连接设备信息
     *
     * @return
     */
    private String getConnDeviceInfo() {
        String str = "";
        DeviceConnFactoryManager deviceConnFactoryManager = DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id];
        if (deviceConnFactoryManager != null
                && deviceConnFactoryManager.getConnState()) {
            if ("USB".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "USB\n";
                str += "USB Name: " + deviceConnFactoryManager.usbDevice().getDeviceName();
            } else if ("WIFI".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "WIFI\n";
                str += "IP: " + deviceConnFactoryManager.getIp() + "\t";
                str += "Port: " + deviceConnFactoryManager.getPort();
            } else if ("BLUETOOTH".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "BLUETOOTH\n";
                str += "MacAddress: " + deviceConnFactoryManager.getMacAddress();
            } else if ("SERIAL_PORT".equals(deviceConnFactoryManager.getConnMethod().toString())) {
                str += "SERIAL_PORT\n";
                str += "Path: " + deviceConnFactoryManager.getSerialPortPath() + "\t";
                str += "Baudrate: " + deviceConnFactoryManager.getBaudrate();
            }
        }
        return str;
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        WMSLog.d("onDestroy");
//        unregisterReceiver(receiver);
//        DeviceConnFactoryManager.closeAllPort();
//        if (threadPool != null) {
//            threadPool.stopThreadPool();
//        }
//    }


}
