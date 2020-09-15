package com.huaheng.mobilewms.https;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.huaheng.mobilewms.WMSApplication;
import com.huaheng.mobilewms.bean.ApkInfo;
import com.huaheng.mobilewms.bean.BillsInfo;
import com.huaheng.mobilewms.bean.CompanyInfo;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.bean.DictData;
import com.huaheng.mobilewms.bean.GoodsInfo;
import com.huaheng.mobilewms.bean.InventoryDetail;
import com.huaheng.mobilewms.bean.InventoryDetails;
import com.huaheng.mobilewms.bean.InventoryTransaction;
import com.huaheng.mobilewms.bean.Location;
import com.huaheng.mobilewms.bean.Material;
import com.huaheng.mobilewms.bean.MaterialInfo;
import com.huaheng.mobilewms.bean.MaterialType;
import com.huaheng.mobilewms.bean.Materialforecast;
import com.huaheng.mobilewms.bean.MobileInventory;
import com.huaheng.mobilewms.bean.MobileTask;
import com.huaheng.mobilewms.bean.ModulesBean;
import com.huaheng.mobilewms.bean.PickingResult;
import com.huaheng.mobilewms.bean.Receipt;
import com.huaheng.mobilewms.bean.ReceiptBill;
import com.huaheng.mobilewms.bean.ReceiptHeader;
import com.huaheng.mobilewms.bean.ReceiptInfo;
import com.huaheng.mobilewms.bean.ReceiptType;
import com.huaheng.mobilewms.bean.Shipment;
import com.huaheng.mobilewms.bean.ShipmentBill;
import com.huaheng.mobilewms.bean.ShipmentHeader;
import com.huaheng.mobilewms.bean.ShipmentMaterial;
import com.huaheng.mobilewms.bean.ShipmentMaterialDetail;
import com.huaheng.mobilewms.bean.ShipmentTaskModel;
import com.huaheng.mobilewms.bean.ShipmentType;
import com.huaheng.mobilewms.bean.TaskDetailBean;
import com.huaheng.mobilewms.bean.TaskHeader;
import com.huaheng.mobilewms.bean.TodayInfo;
import com.huaheng.mobilewms.bean.TokenBean;
import com.huaheng.mobilewms.bean.UserBean;
import com.huaheng.mobilewms.https.convert.ResponseConverterFactory;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http2.ErrorCode;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpInterface {

    public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static HttpInterface insstance = null;
    private Retrofit retrofit;
    private Map<String, Object> paramMap;
    private OkHttpClient.Builder clientBuilder;
    private HttpService httpService;

    public static HttpInterface getInsstance() {
        WMSLog.d("HttpInterface insstance:" + insstance);
        if (insstance == null) {
            insstance = new HttpInterface();
        }
        return insstance;
    }

    public HttpInterface() {
        WMSLog.d("HttpInterface");
        TrustManager[] trustManager = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws
                            CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws
                            CertificateException {
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;    // 返回null
                    }
                }
        };

        clientBuilder = new OkHttpClient.Builder();
        try {
            clientBuilder
                    .connectTimeout(HttpConstant.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(HttpConstant.WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(HttpConstant.READ_TIMEOUT, TimeUnit.SECONDS)
                    .sslSocketFactory(getSSLSocketFactory())
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {

                            return true;
                        }
                    });
            clientBuilder.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    // 获取 Cookie
                    Response resp = chain.proceed(chain.request());
                    List<String> cookies = resp.headers("Set-Cookie");
                    String cookieStr = "";
                    if (cookies != null && cookies.size() > 0) {
                        for (int i = 0; i < cookies.size(); i++) {
                            cookieStr += cookies.get(i);
                        }
                        WMSApplication.setOkhttpCookie(cookieStr);
                    }
                    return resp;
                }
            });
            clientBuilder.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    // 设置 Cookie
                    String cookieStr = WMSApplication.getOkhttpCookie();
                    if (!TextUtils.isEmpty(cookieStr)) {
                        return chain.proceed(chain.request().newBuilder().header("Cookie", cookieStr).build());
                    }
                    return chain.proceed(chain.request());
                }
            });

            clientBuilder.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .addHeader("token",WMSApplication.getToken())
                            .addHeader("accept", "application/json").build();
                    return chain.proceed(request);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = WMSUtils.getData(Constant.NETWORK);
        if(url != null) {
            retrofit = new Retrofit.Builder()
                    .client(clientBuilder.build())
                    .addConverterFactory(ResponseConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(url)
                    .build();
        } else {
            retrofit = new Retrofit.Builder()
                    .client(clientBuilder.build())
                    .addConverterFactory(ResponseConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(HttpConstant.URL)
                    .build();
        }

        paramMap = new HashMap<>();
        WMSLog.d("initService");
        initService(retrofit);
    }

    public void  reset() {
        insstance = null;
    }

    private static SSLSocketFactory getSSLSocketFactory() throws Exception {
        //创建一个不验证证书链的证书信任管理器。
        final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        return sslContext.getSocketFactory();
    }

    public void setBaseUrl(String url) {
        retrofit = new Retrofit.Builder()
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(url)
                .build();
        initService(retrofit);
    }

    private void initService(Retrofit retrofit) {
        httpService = retrofit.create(HttpService.class);
    }

    public String getBaseUrl() {
        return retrofit.baseUrl().toString();
    }

    public void login(Subscriber<ArrayList<UserBean>> subscriber, String userName, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", userName);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("login :" + json.toString());
        WMSApplication.setOkhttpCookie(null);
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.login(formBody)
                .map(new ApiResponseFunc<ArrayList<UserBean>>());

        toSubscribe(observable, subscriber);
    }

    public void getToken(Subscriber<String> subscriber, String userName, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("getToken :" + json.toString());
        WMSApplication.setOkhttpCookie(null);
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.getToken(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void getModules(Subscriber<ArrayList<ModulesBean>> subscriber, String warehouseCode, int warehouseId) {
        JSONObject json = new JSONObject();
        try {
            json.put("warehouseId", warehouseId);
            json.put("warehouseCode", warehouseCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("getModules :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.getModules(formBody)
                .map(new ApiResponseFunc<ArrayList<ModulesBean>>());

        toSubscribe(observable, subscriber);
    }

    public void heartBeat(Subscriber<String> subscriber) {
        JSONObject json = new JSONObject();
        WMSLog.d("heartBeat :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.heartBeat(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void scanBill(Subscriber<ArrayList<ReceiptInfo>> subscriber, String code) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("scanBill :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.scanBill(formBody)
                .map(new ApiResponseFunc<ArrayList<ReceiptInfo>>());

        toSubscribe(observable, subscriber);
    }

    public void sanContainer(Subscriber<String> subscriber, String code) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("scanContainer :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.scanContainer(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void saveBillByPiece(Subscriber<GoodsInfo> subscriber, String receiptContainerCode, String receiptDetailId) {
        JSONObject json = new JSONObject();
        try {
            json.put("receiptContainerCode", receiptContainerCode);
            json.put("receiptDetailId", receiptDetailId);
            json.put("taskType", (short) 100);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("saveBillByPiece  array:" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.saveBillByPiece(formBody)
                .map(new ApiResponseFunc<GoodsInfo>());

        toSubscribe(observable, subscriber);
    }

    public void saveBillByBulk(Subscriber<List<GoodsInfo>> subscriber, List<BillsInfo> billsInfos) {
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < billsInfos.size(); i++) {
                JSONObject object = new JSONObject();
                object.put("receiptContainerCode", billsInfos.get(i).getReceiptContainerCode());
                object.put("locationCode", billsInfos.get(i).getLoation());
                object.put("receiptDetailId", billsInfos.get(i).getReceiptDetailId());
                object.put("qty", billsInfos.get(i).getQty());
                object.put("taskType", (short) 100);
                array.put(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("saveBillByBulk  array:" + array.toString());
        RequestBody formBody = RequestBody.create(JSON, array.toString());
        Observable observable = httpService.saveBillByBulk(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void collectByForm(Subscriber<String> subscriber, List<ReceiptBill> receiptBills, String location, String containerCode, int taskType) {
        JSONArray array = new JSONArray();
        try {
            for (int i = 0; i < receiptBills.size(); i++) {
                JSONObject object = new JSONObject();
                object.put("receiptContainerCode", containerCode);
                object.put("locationCode", location);
                object.put("receiptDetailId", receiptBills.get(i).getReceiptDetailId());
                object.put("qty", receiptBills.get(i).getQty());
                object.put("taskType", taskType);
                array.put(object);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("collectByForm  array:" + array.toString());
        RequestBody formBody = RequestBody.create(JSON, array.toString());
        Observable observable = httpService.saveBillByBulk(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void getMaterial(Subscriber<MaterialInfo> subscriber, String code) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("companyCode", companyCode);
            json.put("companyId", companyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        WMSLog.d("getMaterial :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.getMaterial(formBody)
                .map(new ApiResponseFunc<MaterialInfo>());

        toSubscribe(observable, subscriber);
    }

    public void checkLocation(Subscriber<String> subscriber, String code) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("checkLocation :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.checkLocation(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void quickSaveGoods(Subscriber<String> subscriber, ArrayList<ReceiptBill> receiptBills) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        JSONArray array = new JSONArray();
        for(ReceiptBill receiptBill : receiptBills) {
            JSONObject object = new JSONObject();
            try {
                object.put("receiptContainerCode", receiptBill.getReceiptContainerCode());
                object.put("materialCode", receiptBill.getMaterialCode());
                object.put("qty", receiptBill.getQty());
                object.put("locationCode", receiptBill.getLocationCode());
                object.put("batch", receiptBill.getBatch());
                object.put("project", receiptBill.getProject());
                object.put("companyCode", companyCode);
                object.put("companyId", companyId);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        WMSLog.d("quickSaveGoods  array:" + array.toString());
        RequestBody formBody = RequestBody.create(JSON, array.toString());
        Observable observable = httpService.quickSaveGoods(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void createReceiptTask(Subscriber<String> subscriber, String containerCode) {
        JSONObject object = new JSONObject();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        try {
            object.put("containerCode", containerCode);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("createReceiptTask  object:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.createReceiptTask(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void createQuickTask(Subscriber<Integer> subscriber, String containerCode) {
        JSONObject object = new JSONObject();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        try {
            object.put("containerCode", containerCode);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("createQuickTask  object:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.createQuickTask(formBody)
                .map(new ApiResponseFunc<Integer>());

        toSubscribe(observable, subscriber);
    }

    public void scanLocationCode(Subscriber<String> subscriber, String locationCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("locationCode", locationCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("scanLocationCode  locationCode:" + locationCode);
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.scanLocationCode(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void getTaskDetails(Subscriber<TaskDetailBean> subscriber, String code) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("getTaskDetails :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getTaskDetails(code)
                .map(new ApiResponseFunc<TaskDetailBean>());

        toSubscribe(observable, subscriber);
    }

    public void completeTaskDetails(Subscriber<List<PickingResult>> subscriber, List<Integer> list) {
        JSONArray array = new JSONArray();
        for(int i=0; i < list.size(); i++) {
            try {
                array.put(list.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WMSLog.d("completeTaskDetails :" + array.toString());
        RequestBody formBody = RequestBody.create(JSON, array.toString());
        Observable observable = httpService.completeTaskDetails(formBody)
                .map(new ApiResponseFunc<List<PickingResult>>());

        toSubscribe(observable, subscriber);
    }

    public void getInventoryForecast(Subscriber<ArrayList<String>> subscriber, String code, String companyCode, String companyId) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("getInventoryForecast :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getInventoryForecast(formBody)
                .map(new ApiResponseFunc<ArrayList<String>>());

        toSubscribe(observable, subscriber);
    }

    public void getInventoryInfo(Subscriber<ArrayList<MobileInventory>> subscriber, String code, String companyCode, String companyId) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("getInventoryInfo :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getInventoryInfo(formBody)
                .map(new ApiResponseFunc<ArrayList<MobileInventory>>());

        toSubscribe(observable, subscriber);
    }

    public void createCheckOutTask(Subscriber<List<Integer>> subscriber, String ids) {
        JSONObject object = new JSONObject();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        String station = WMSUtils.getData(Constant.CURREN_STATION_ID, Constant.DEFAULT_STATION_ID);
        try {
            object.put("ids", ids);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
            object.put("station", station);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("createCheckOutTask :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.createCheckOutTask(formBody)
                .map(new ApiResponseFunc<List<Integer>>());

        toSubscribe(observable, subscriber);
    }

    public void pickLocation(Subscriber<List<Location>> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("pickLocation :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.pickLocation(formBody)
                .map(new ApiResponseFunc<List<Location>>());

        toSubscribe(observable, subscriber);
    }

    public void getEmptyContainerInLocation(Subscriber<List<Location>> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("getEmptyContainerInLocation :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getEmptyContainerInLocation(formBody)
                .map(new ApiResponseFunc<List<Location>>());

        toSubscribe(observable, subscriber);
    }

    public void execute(Subscriber<String> subscriber, String taskId) {
        JSONObject object = new JSONObject();
        try {
            object.put("taskId", taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("execute :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.execute(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void executeList(Subscriber<String> subscriber, List<Integer> taskIdList) {
        JSONArray jsonArray = new JSONArray();
        for(Integer taskId : taskIdList) {
            JSONObject object = new JSONObject();
            try {
                object.put("taskId", taskId);
                jsonArray.put(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WMSLog.d("executeList  :" + jsonArray.toString());
        RequestBody formBody = RequestBody.create(JSON, jsonArray.toString());
        Observable observable = httpService.executeList(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void transfer(Subscriber<Integer> subscriber, String sourceLocation, String destinationLocation) {
        JSONObject object = new JSONObject();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        try {
            object.put("sourceLocation", sourceLocation);
            object.put("destinationLocation", destinationLocation);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("transfer :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.transfer(formBody)
                .map(new ApiResponseFunc<Integer>());

        toSubscribe(observable, subscriber);
    }

    public void isLocation(Subscriber<String> subscriber, String code) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("isLocation :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.isLocation(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void isContainer(Subscriber<String> subscriber, String code) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("isContainer :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.isContainer(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void getLocationForecast(Subscriber<ArrayList<String>> subscriber, String code, String type) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
            json.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("getLocationForecast :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.getLocation(formBody)
                .map(new ApiResponseFunc<ArrayList<String>>());

        toSubscribe(observable, subscriber);
    }

    public void replenishWarehouse(Subscriber<List<GoodsInfo>> subscriber, ArrayList<ReceiptBill> receiptBills) {
        JSONArray array = new JSONArray();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        for(ReceiptBill receiptBill : receiptBills) {
            JSONObject object = new JSONObject();
            try {
                object.put("materialCode", receiptBill.getMaterialCode());
                object.put("qty", receiptBill.getQty());
                object.put("locationCode", receiptBill.getLocationCode());
                object.put("batch", receiptBill.getBatch());
                object.put("project", receiptBill.getProject());
                object.put("companyCode", companyCode);
                object.put("companyId",companyId);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        WMSLog.d("replenishWarehouse  array:" + array.toString());
        RequestBody formBody = RequestBody.create(JSON, array.toString());
        Observable observable = httpService.replenishWarehouse(formBody)
                .map(new ApiResponseFunc<List<GoodsInfo>>());

        toSubscribe(observable, subscriber);
    }

    public void getMaterialForecast(Subscriber<List<Materialforecast>> subscriber, String code) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("getMaterialForecast  json:" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.getMaterialForecast(formBody)
                .map(new ApiResponseFunc<List<Materialforecast>>());

        toSubscribe(observable, subscriber);
    }

    public void autoShipment(Subscriber<List<Integer>> subscriber,  List<ShipmentBill> shipmentBills) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        JSONArray array = new JSONArray();
        for(ShipmentBill shipmentBill : shipmentBills) {
            JSONObject object = new JSONObject();
            try {
                object.put("materialCode", shipmentBill.getMaterialCode());
                object.put("qty", shipmentBill.getQty());
                if(shipmentBill.getLocation() != null && shipmentBill.getLocation().contains("DM")) {
                    object.put("taskType", 300);
                } else {
                    object.put("taskType", 400);
                }
                object.put("location", shipmentBill.getLocation());
                object.put("companyCode", companyCode);
                object.put("companyId", companyId);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        WMSLog.d("autoShipment :" + array.toString());
        RequestBody formBody = RequestBody.create(JSON, array.toString());
        Observable observable = httpService.autoShipment(formBody)
                .map(new ApiResponseFunc<List<Integer>>());

        toSubscribe(observable, subscriber);
    }

    public void getContainerCode(Subscriber<String> subscriber, String code) {
        JSONObject json = new JSONObject();
        try {
            json.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("getContainerCode:" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.getContainerCode(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void createShipTask(Subscriber<List<Integer>> subscriber, ShipmentTaskModel shipmentTaskCreateModel) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        for(int i = 0 ; i < shipmentTaskCreateModel.getShipmentContainerHeaderIds().length ;i++) {  //依次将数组元素添加进JSONArray对象中
            jsonArray.put(shipmentTaskCreateModel.getShipmentContainerHeaderIds()[i]);
        }
        try {
            json.put("shipmentContainerHeaderIds", jsonArray);
            json.put("taskType", shipmentTaskCreateModel.getTaskType());
            json.put("priority", shipmentTaskCreateModel.getPriority());
            json.put("companyCode", companyCode);
            json.put("companyId", companyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("createShipTask:" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.createShipTask(formBody)
                .map(new ApiResponseFunc<List<Integer>>());

        toSubscribe(observable, subscriber);
    }

    public void createSortShipTask(Subscriber<List<Integer>> subscriber, ShipmentTaskModel shipmentTaskCreateModel) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        for(int i = 0 ; i < shipmentTaskCreateModel.getShipmentContainerHeaderIds().length ;i++) {  //依次将数组元素添加进JSONArray对象中
            jsonArray.put(shipmentTaskCreateModel.getShipmentContainerHeaderIds()[i]);
        }
        try {
            json.put("shipmentContainerHeaderIds", jsonArray);
            json.put("taskType", shipmentTaskCreateModel.getTaskType());
            json.put("priority", shipmentTaskCreateModel.getPriority());
            json.put("companyCode", companyCode);
            json.put("companyId", companyId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        WMSLog.d("createSortShipTask:" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.createSortShipTask(formBody)
                .map(new ApiResponseFunc<List<Integer>>());

        toSubscribe(observable, subscriber);
    }

    public void completeTaskByWMS(Subscriber<String> subscriber, String taskId) {
        JSONObject object = new JSONObject();
        try {
            object.put("taskId", taskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("completeTaskByWMS:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.completeTaskByWMS(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void completeTaskListByWMS(Subscriber<String> subscriber, List<Integer> taskIdList) {
        JSONArray jsonArray = new JSONArray();
        for(Integer taskId : taskIdList) {
            JSONObject object = new JSONObject();
            try {
                object.put("taskId", taskId);
                jsonArray.put(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WMSLog.d("completeTaskListByWMS:" + jsonArray.toString());
        RequestBody formBody = RequestBody.create(JSON, jsonArray.toString());
        Observable observable = httpService.completeTaskListByWMS(formBody)
                .map(new ApiResponseFunc<String>());
        toSubscribe(observable, subscriber);
    }

    public void createReplenishTask(Subscriber<Integer> subscriber, String containerCode, String station) {
        JSONObject object = new JSONObject();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        try {
            object.put("containerCode", containerCode);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
            object.put("station", station);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("createReplenishTask:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.createReplenishTask(formBody)
                .map(new ApiResponseFunc<Integer>());

        toSubscribe(observable, subscriber);
    }

    public void createEmptyIn(Subscriber<Integer> subscriber, String containerCode, String destinationLocation) {
        JSONObject object = new JSONObject();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        try {
            object.put("containerCode", containerCode);
            object.put("destinationLocation", destinationLocation);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("createEmptyIn :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.createEmptyIn(formBody)
                .map(new ApiResponseFunc<Integer>());

        toSubscribe(observable, subscriber);
    }

    public void createEmptyOut(Subscriber<Integer> subscriber, String containerCode, String sourceLocation) {
        JSONObject object = new JSONObject();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        String companyId = WMSUtils.getData(Constant.CURREN_COMPANY_ID, Constant.DEFAULT_COMPANY_ID);
        String station = WMSUtils.getData(Constant.CURREN_STATION_ID, Constant.DEFAULT_STATION_ID);
        try {
            object.put("containerCode", containerCode);
            object.put("sourceLocation", sourceLocation);
            object.put("companyCode", companyCode);
            object.put("companyId", companyId);
            object.put("station", station);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("createEmptyOut :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.createEmptyOut(formBody)
                .map(new ApiResponseFunc<Integer>());

        toSubscribe(observable, subscriber);
    }

    public void getLocationFromContainer(Subscriber<String> subscriber, String containerCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("containerCode", containerCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("getLocationFromContainer :" + containerCode);
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getLocationFromContainer(formBody)
                .map(new ApiResponseFunc<String>());
        toSubscribe(observable, subscriber);
    }

    public void pickingReturn(Subscriber<String> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("pickingReturn:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.pickingReturn(formBody)
                .map(new ApiResponseFunc<String>());
        toSubscribe(observable, subscriber);
    }

    public void get7daysShipment(Subscriber<List<InventoryDetails>> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("get7daysShipment:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.get7daysShipment(formBody)
                .map(new ApiResponseFunc<List<InventoryDetails>>());
        toSubscribe(observable, subscriber);
    }

    public void getTodayShipmentDetail(Subscriber<List<ShipmentMaterialDetail>> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("getTodayShipmentDetail:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getTodayShipmentDetail(formBody)
                .map(new ApiResponseFunc<List<ShipmentMaterialDetail>>());
        toSubscribe(observable, subscriber);
    }

    public void getCompanyInfo(Subscriber<ArrayList<CompanyInfo>> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("getCompanyInfo:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getCompanyInfo(formBody)
                .map(new ApiResponseFunc<ArrayList<CompanyInfo>>());
        toSubscribe(observable, subscriber);
    }

    public void getUpdateApkInfo(Subscriber<ApkInfo> subscriber, String pkgName, String versionCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("pkgName", pkgName);
            object.put("versionCode", versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("getUpdateApkInfo:" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getUpdateApkInfo(formBody)
                .map(new ApiResponseFunc<ApkInfo>());

        toSubscribe(observable, subscriber);
    }

    public void scanBarcodeAdd(Subscriber<String> subscriber, int area, int flag, String barcode) {
        String loginName = WMSUtils.getData(Constant.LOGIN_NAME);
        JSONObject object = new JSONObject();
        try {
            object.put("area", area);
            object.put("barcode", barcode);
            object.put("flag", flag);
            object.put("createdBy", loginName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("scanBarcodeAdd :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.scanBarcodeAdd(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void findTrasnfer(Subscriber<List<MobileTask>> subscriber, String containerCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("containCode", containerCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("findTrasnfer :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.findTrasnfer(formBody)
                .map(new ApiResponseFunc<List<MobileTask>>());

        toSubscribe(observable, subscriber);
    }

    public void confirmGapQty(Subscriber<String> subscriber, int detailId, String qty) {
        JSONObject object = new JSONObject();
        try {
            object.put("detailId", detailId);
            object.put("qty", qty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("confirmGapQty :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.confirmGapQty(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void callBox(Subscriber<String> subscriber, String containerCode, String destinationLocation, int type) {
        JSONObject object = new JSONObject();
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        try {
            object.put("containerCode", containerCode);
            object.put("destinationLocation", destinationLocation);
            object.put("type", type);
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("callBox :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.callBox(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void findReceipt(Subscriber<Receipt> subscriber, String receiptCode) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("receiptCode", receiptCode);
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("findReceipt :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.findReceipt(formBody)
                .map(new ApiResponseFunc<Receipt>());

        toSubscribe(observable, subscriber);
    }

    public void quickReceipt(Subscriber<String> subscriber, List<ReceiptBill> receiptBills) {
        JSONObject json = new JSONObject();
        String post = new Gson().toJson(receiptBills);
        post = post.replace("\\", "");
        post = post.replace("}\"}", "}}");
        post = post.replace("\"{", "{");
        post = post.replace("\"}\"", "\"}");
        WMSLog.d("quickReceipt :" + post);
        RequestBody formBody = RequestBody.create(JSON, post);
        Observable observable = httpService.quickReceipt(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void listReceipt(Subscriber<String> subscriber, List<ReceiptBill> receiptBills) {
        JSONObject json = new JSONObject();
        String post = new Gson().toJson(receiptBills);
        post = post.replace("\\", "");
        post = post.replace("}\"}", "}}");
        post = post.replace("\"{", "{");
        post = post.replace("\"}\"", "\"}");
        WMSLog.d("listReceipt :" + post);
        RequestBody formBody = RequestBody.create(JSON, post);
        Observable observable = httpService.listReceipt(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void getLatestMaterial(Subscriber<List<Material>> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("getLatestMaterial :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getLatestMaterial(formBody)
                .map(new ApiResponseFunc<List<Material>>());

        toSubscribe(observable, subscriber);
    }

    public void getLatestReceipt(Subscriber<List<ReceiptHeader>> subscriber) {
        final String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("getLatestReceipt :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getLatestReceipt(formBody)
                .map(new ApiResponseFunc<List<ReceiptHeader>>());

        toSubscribe(observable, subscriber);
    }

    public void autoCombination(Subscriber<List<Integer>> subscriber, String shipmentCode) {
        JSONObject object = new JSONObject();
        try {
            object.put("shipmentCode", shipmentCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("autoCombination  :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.autoCombination(formBody)
                .map(new ApiResponseFunc<List<Integer>>());

        toSubscribe(observable, subscriber);
    }

    public void combination(Subscriber<List<Integer>> subscriber, List<ShipmentMaterial> shipmentMaterials) {
        String post = new Gson().toJson(shipmentMaterials);
        post = post.replace("\\", "");
        post = post.replace("}\"}", "}}");
        post = post.replace("\"{", "{");
        post = post.replace("\"}\"", "\"}");
        WMSLog.d("combination :" + post);
        RequestBody formBody = RequestBody.create(JSON, post);
        Observable observable = httpService.combination(formBody)
                .map(new ApiResponseFunc<List<Integer>>());

        toSubscribe(observable, subscriber);
    }

    public void createShipmentTask(Subscriber<List<Integer>> subscriber, List<Integer> taskIdList) {
        JSONArray jsonArray = new JSONArray();
        for(Integer taskId : taskIdList) {
            JSONObject object = new JSONObject();
            try {
                object.put("taskId", taskId);
                jsonArray.put(object);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        WMSLog.d("createShipmentTask  :" + jsonArray.toString());
        RequestBody formBody = RequestBody.create(JSON, jsonArray.toString());
        Observable observable = httpService.createShipmentTask(formBody)
                .map(new ApiResponseFunc<List<Integer>>());

        toSubscribe(observable, subscriber);
    }

    public void searchReceipt(Subscriber<List<ReceiptHeader>> subscriber) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchReceipt :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchReceipt(formBody)
                .map(new ApiResponseFunc<List<ReceiptHeader>>());

        toSubscribe(observable, subscriber);
    }

    public void searchReceiptInCondition(Subscriber<List<ReceiptHeader>> subscriber, String code,
                                         String receiptType, String lastStatus, String startTime, String endTime) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
            object.put("code", code);
            object.put("receiptType", receiptType);
            object.put("lastStatus", lastStatus);
            object.put("startTime", startTime);
            object.put("endTime", endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchReceiptInCondition :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchReceiptInCondition(formBody)
                .map(new ApiResponseFunc<List<ReceiptHeader>>());

        toSubscribe(observable, subscriber);
    }

    public void getDictListData(Subscriber<List<DictData>> subscriber, String dictType) {
        JSONObject object = new JSONObject();
        try {
            object.put("dictType", dictType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("getDictListData :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getDictListData(formBody)
                .map(new ApiResponseFunc<List<DictData>>());

        toSubscribe(observable, subscriber);
    }

    public void getReceiptType(Subscriber<List<ReceiptType>> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("getReceiptType :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getReceiptType(formBody)
                .map(new ApiResponseFunc<List<ReceiptType>>());

        toSubscribe(observable, subscriber);
    }

    public void getShipmentType(Subscriber<List<ShipmentType>> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("getShipmentType :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.getShipmentType(formBody)
                .map(new ApiResponseFunc<List<ShipmentType>>());

        toSubscribe(observable, subscriber);
    }

    public void findShipment(Subscriber<Shipment> subscriber, String shipmentCode) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("shipmentCode", shipmentCode);
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("findShipment :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.findShipment(formBody)
                .map(new ApiResponseFunc<Shipment>());

        toSubscribe(observable, subscriber);
    }

    public void searchShipment(Subscriber<List<ShipmentHeader>> subscriber) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchReceipt :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchShipment(formBody)
                .map(new ApiResponseFunc<List<ShipmentHeader>>());

        toSubscribe(observable, subscriber);
    }

    public void searchShipmentInCondition(Subscriber<List<ShipmentHeader>> subscriber, String code,
                                          String shipmentType, String lastStatus, String startTime, String endTime) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
            object.put("code", code);
            object.put("shipmentType", shipmentType);
            object.put("lastStatus", lastStatus);
            object.put("startTime", startTime);
            object.put("endTime", endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchShipmentInCondition :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchShipmentInCondition(formBody)
                .map(new ApiResponseFunc<List<ShipmentHeader>>());

        toSubscribe(observable, subscriber);
    }

    public void findTask(Subscriber<MobileTask> subscriber, String taskHeadId) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("taskHeadId", taskHeadId);
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("findTask :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.findTask(formBody)
                .map(new ApiResponseFunc<MobileTask>());

        toSubscribe(observable, subscriber);
    }

    public void searchTask(Subscriber<List<TaskHeader>> subscriber) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchTask :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchTask(formBody)
                .map(new ApiResponseFunc<List<TaskHeader>>());

        toSubscribe(observable, subscriber);
    }

    public void searchTaskInCondition(Subscriber<List<TaskHeader>> subscriber, String containerCode, String taskType, String status,
                                           String fromLocatrion, String toLocation, String startTime, String endTime) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
            object.put("containerCode", containerCode);
            object.put("taskType", taskType);
            object.put("status", status);
            object.put("fromLocatrion", fromLocatrion);
            object.put("toLocation", toLocation);
            object.put("startTime", startTime);
            object.put("endTime", endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchTaskInCondition :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchTaskInCondition(formBody)
                .map(new ApiResponseFunc<List<TaskHeader>>());

        toSubscribe(observable, subscriber);
    }

    public void findInventory(Subscriber<InventoryDetail> subscriber, String inventoryDetailId) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("inventoryDetailId", inventoryDetailId);
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("findInventory :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.findInvneotry(formBody)
                .map(new ApiResponseFunc<InventoryDetail>());

        toSubscribe(observable, subscriber);
    }

    public void searchInventory(Subscriber<List<InventoryDetail>> subscriber) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchInventory :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchInventory(formBody)
                .map(new ApiResponseFunc<List<InventoryDetail>>());

        toSubscribe(observable, subscriber);
    }

    public void searchInventoryInCondition(Subscriber<List<InventoryDetail>> subscriber, String containerCode, String materialCode,
                                            String materialName, String materialSpec, String startTime, String endTime) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
            object.put("containerCode", containerCode);
            object.put("materialCode", materialCode);
            object.put("materialName", materialName);
            object.put("materialSpec", materialSpec);
            object.put("startTime", startTime);
            object.put("endTime", endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchInventoryInCondition :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchInventoryInCondition(formBody)
                .map(new ApiResponseFunc<List<InventoryDetail>>());

        toSubscribe(observable, subscriber);
    }

    public void searchTodayInfo(Subscriber<TodayInfo> subscriber) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchTodayInfo :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchTodayInfo(formBody)
                .map(new ApiResponseFunc<TodayInfo>());

        toSubscribe(observable, subscriber);
    }

    public void searchInventoryTransactionInCondition(Subscriber<List<InventoryTransaction>> subscriber, String containerCode, String transactionType,
                                                      String materialCode, String materialName, String materialSpec, String startTime, String endTime) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
            object.put("containerCode", containerCode);
            object.put("transactionType", transactionType);
            object.put("materialCode", materialCode);
            object.put("materialName", materialName);
            object.put("materialSpec", materialSpec);
            object.put("startTime", startTime);
            object.put("endTime", endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchInventoryTransactionInCondition :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchInventoryTransactionInCondition(formBody)
                .map(new ApiResponseFunc<List<InventoryTransaction>>());

        toSubscribe(observable, subscriber);
    }

    public void createReceiptCode(Subscriber<String> subscriber) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("createReceiptCode :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.createReceiptCode(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void createReceipt(Subscriber<String> subscriber, List<ReceiptBill> receiptBills) {
        String post = new Gson().toJson(receiptBills);
        post = post.replace("\\", "");
        post = post.replace("}\"}", "}}");
        post = post.replace("\"{", "{");
        post = post.replace("\"}\"", "\"}");
        WMSLog.d("createReceipt :" + post);
        RequestBody formBody = RequestBody.create(JSON, post);
        Observable observable = httpService.createReceipt(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void findMaterial(Subscriber<List<Material>> subscriber) {
        JSONObject json = new JSONObject();
        WMSLog.d("findMaterial :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.findMaterial(formBody)
                .map(new ApiResponseFunc<List<Material>>());

        toSubscribe(observable, subscriber);
    }

    public void createShipmentCode(Subscriber<String> subscriber) {
        JSONObject object = new JSONObject();
        WMSLog.d("createShipmentCode :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.createShipmentCode(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void createShipment(Subscriber<String> subscriber, List<ShipmentBill> shipmentBills) {
        String post = new Gson().toJson(shipmentBills);
        post = post.replace("\\", "");
        post = post.replace("}\"}", "}}");
        post = post.replace("\"{", "{");
        post = post.replace("\"}\"", "\"}");
        WMSLog.d("createShipment :" + post);
        RequestBody formBody = RequestBody.create(JSON, post);
        Observable observable = httpService.createShipment(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void addMaterial(Subscriber<String> subscriber, Material material) {
        String post = new Gson().toJson(material);
        post = post.replace("\\", "");
        post = post.replace("}\"}", "}}");
        post = post.replace("\"{", "{");
        post = post.replace("\"}\"", "\"}");
        WMSLog.d("addMaterial :" + post);
        RequestBody formBody = RequestBody.create(JSON, post);
        Observable observable = httpService.addMaterial(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void searchMaterialInCondition(Subscriber<List<Material>> subscriber, String materialCode, String name,
                                          String spec, String startTime, String endTime) {
        String companyCode = WMSUtils.getData(Constant.CURREN_COMPANY_CODE, Constant.DEFAULT_COMPANY_CODE);
        JSONObject object = new JSONObject();
        try {
            object.put("companyCode", companyCode);
            object.put("code", materialCode);
            object.put("name", name);
            object.put("spec", spec);
            object.put("startTime", startTime);
            object.put("endTime", endTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        WMSLog.d("searchMaterialInCondition :" + object.toString());
        RequestBody formBody = RequestBody.create(JSON, object.toString());
        Observable observable = httpService.searchMaterialInCondition(formBody)
                .map(new ApiResponseFunc<List<Material>>());

        toSubscribe(observable, subscriber);
    }

    public void createMaterialCode(Subscriber<String> subscriber) {
        JSONObject json = new JSONObject();
        WMSLog.d("createMaterialCode :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.createMaterialCode(formBody)
                .map(new ApiResponseFunc<String>());

        toSubscribe(observable, subscriber);
    }

    public void getMaterialType(Subscriber<List<MaterialType>> subscriber) {
        JSONObject json = new JSONObject();
        WMSLog.d("getMaterialType :" + json.toString());
        RequestBody formBody = RequestBody.create(JSON, json.toString());
        Observable observable = httpService.getMaterialType(formBody)
                .map(new ApiResponseFunc<List<MaterialType>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class ApiResponseFunc<T> implements Func1<ApiResponse<T>, T> {

        @Override
        public T call(ApiResponse<T> apiResponse) {
            if (!apiResponse.getCode().equals("200")) {
                throw new ApiException(apiResponse.getMsg());
            }

            if (apiResponse.getData() != null) {
                return apiResponse.getData();
            } else if (apiResponse.getSession() != null) {
                return apiResponse.getSession();
            }

            return null;
        }
    }

    private void toSubscribe(Observable observable, Subscriber subscriber) {
        WMSUtils.startTime = System.currentTimeMillis();
        observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
