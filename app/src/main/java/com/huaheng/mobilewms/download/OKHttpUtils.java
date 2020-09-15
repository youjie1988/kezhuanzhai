package com.huaheng.mobilewms.download;


import com.huaheng.mobilewms.util.WMSLog;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by youjie on 2018/4/27.
 */

public class OKHttpUtils {

    private static final String TAG = "HttpUtils";
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static void commonPost(String url, String jsonString, final OKHttpInterface okHttpInterface) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, jsonString);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS).build();
        Request request= null;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                WMSLog.d( "onFailure  :" + e.toString());
                okHttpInterface.onFail(e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Headers headers = response.headers();
                List<String> cookies = headers.values("Set-Cookie");
                if (cookies.size() > 0) {
                    String session = cookies.get(0);
                    String result = session.substring(0, session.indexOf(";"));
                    System.out.println( "cookies:" + result);
                }
                okHttpInterface.onSuccess(str);
                WMSLog.d("onResponse:" + str);
            }
        });
    }

}
