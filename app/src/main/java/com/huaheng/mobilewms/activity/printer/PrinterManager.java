package com.huaheng.mobilewms.activity.printer;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.util.WMSUtils;

/**
 * Created by youjie
 * on 2020/2/19
 */
public class PrinterManager {

    private ThreadPool threadPool;
    private Context mContext;

    private static final int CONN_MOST_DEVICES = 0x11;
    private static final int CONN_PRINTER = 0x12;
    private int id = 0;
    private static final int REQUEST_CODE = 0x004;
    /**
     * 连接状态断开
     */
    private static final int CONN_STATE_DISCONN = 0x007;
    /**
     * 使用打印机指令错误
     */
    private static final int PRINTER_COMMAND_ERROR = 0x008;

    public static PrinterManager instantce;

    public static PrinterManager as(Context context) {
        if(instantce == null) {
            instantce = new PrinterManager(context);
        }
        return instantce;
    }
    public PrinterManager(Context context) {
        mContext = context;
    }

    public void printerMaterial(final String code, final String name, final String spec, final String qty ) {
        threadPool = ThreadPool.getInstantiation();
        threadPool.addSerialTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                        !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                    mHandler.obtainMessage(CONN_PRINTER).sendToTarget();
                    return;
                }
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(PrintContent.printMaterial(code, name, spec, qty));
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }

    public void printerList(final String receipt, final String type, final String refer, final String createBy) {
        threadPool = ThreadPool.getInstantiation();
        threadPool.addSerialTask(new Runnable() {
            @Override
            public void run() {
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id] == null ||
                        !DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getConnState()) {
                    mHandler.obtainMessage(CONN_PRINTER).sendToTarget();
                    return;
                }
                if (DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].getCurrentPrinterCommand() == PrinterCommand.TSC) {
                    DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].sendDataImmediately(PrintContent.printList(receipt, type, refer, createBy));
                } else {
                    mHandler.obtainMessage(PRINTER_COMMAND_ERROR).sendToTarget();
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CONN_STATE_DISCONN://断开连接
                    DeviceConnFactoryManager deviceConnFactoryManager=DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id];
                    if (deviceConnFactoryManager!= null&&deviceConnFactoryManager.getConnState()) {
                        DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].closePort(id);
                        WMSUtils.showShort(mContext.getString(R.string.str_disconnect_success));
                    }
                    break;
                case PRINTER_COMMAND_ERROR://打印机指令错误
                    WMSUtils.showShort(mContext.getString(R.string.str_choice_printer_command));
                    break;
                case CONN_PRINTER://未连接打印机
                    WMSUtils.showShort(mContext.getString(R.string.str_cann_printer));
                    break;
                case Constant.MESSAGE_UPDATE_PARAMETER:
                    String strIp = msg.getData().getString("Ip");
                    String strPort = msg.getData().getString("Port");
                    //初始化端口信息
                    new DeviceConnFactoryManager.Build()
                            //设置端口连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                            //设置端口IP地址
                            .setIp(strIp)
                            //设置端口ID（主要用于连接多设备）
                            .setId(id)
                            //设置连接的热点端口号
                            .setPort(Integer.parseInt(strPort))
                            .build();
                    threadPool = ThreadPool.getInstantiation();
                    threadPool.addSerialTask(new Runnable() {
                        @Override
                        public void run() {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                        }
                    });
                    break;
                case Constant.tip:
                    String str=(String) msg.obj;;
                    WMSUtils.showShort(str);
                    break;
                default:
                    new DeviceConnFactoryManager.Build()
                            //设置端口连接方式
                            .setConnMethod(DeviceConnFactoryManager.CONN_METHOD.WIFI)
                            //设置端口IP地址
                            .setIp("192.168.2.227")
                            //设置端口ID（主要用于连接多设备）
                            .setId(id)
                            //设置连接的热点端口号
                            .setPort(9100)
                            .build();
                    threadPool.addSerialTask(new Runnable() {
                        @Override
                        public void run() {
                            DeviceConnFactoryManager.getDeviceConnFactoryManagers()[id].openPort();
                        }
                    });
                    break;
            }
        }
    };
}
