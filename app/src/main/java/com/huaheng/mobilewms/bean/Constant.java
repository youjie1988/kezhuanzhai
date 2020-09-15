package com.huaheng.mobilewms.bean;

public class Constant {

    public static final String PROJECT_NAME = "xiangji";
    public static String LOGIN_NAME = "loginName";
    public static String CURREN_WAREHOUSE = "currentWarehouse";
    public static String CURREN_COMPANY_CODE = "currentCompanyCode";
    public static String CURREN_COMPANY_NAME = "currentCompanyName";
    public static String CURREN_COMPANY_ID = "currentCompanyID";
    public static String CURREN_STATION_CODE = "currentStationCode";
    public static String CURREN_STATION_ID = "currentStationID";
    public static String NETWORK = "network";


    public static final int USER_REQUEST_FOCUS = 0;
    public static final int PASSWORD_REQUEST_FOCUS = USER_REQUEST_FOCUS + 1;
    public static final int CLEAR_PASSOWRD = PASSWORD_REQUEST_FOCUS + 1;

    public static final int RET_OK = 200;
    public final static String  RECEIPT = "入库";
    public final static String  SHIPMENT = "出库";
    public final static String  TASK = "任务";
    public final static String  INVENTORY = "库存";
    public final static String  CONTAINER = "容器";
    public final static String  LOCATION = "库位";
    public final static String  MATERIAL = "物料";
    public final static String  SETTING_NETWORK = "设置网络";
    public final static String  LOGOUT = "退出登录";

    public static String DEFAULT_COMPANY_CODE = "cshuahengweld";
    public static String DEFAULT_COMPANY_ID = "1";
    public static String DEFAULT_COMPANY_NAME = "长沙华恒";
    public static final int BLUETOOTH_REQUEST_CODE = 0x001;
    public static final int abnormal_Disconnection=0x011;//异常断开
    public static final int MESSAGE_UPDATE_PARAMETER = 0x009;
    public static final int tip=0x010;

    public static String DEFAULT_STATION_CODE = "1号站台(内)";
    public static String DEFAULT_STATION_ID = "1";


    public final static String DICT_RECEIPT_TYPE = "receiptType";  //入库单 字典类型
    public final static String DICT_TASK_TYPE = "taskType";
    public final static String DICT_TASK_STATUS = "taskStatus";

    public final static String SEPARATOR_STRING = "/";  //出入库铭牌分隔符

}
