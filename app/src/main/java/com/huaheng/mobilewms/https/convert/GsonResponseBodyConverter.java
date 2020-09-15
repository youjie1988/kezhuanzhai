package com.huaheng.mobilewms.https.convert;

import com.google.gson.Gson;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Converter;

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody,T> {
    private final Gson gson;
    private final Type type;


    public GsonResponseBodyConverter(Gson gson, Type type){
        this.gson = gson;
        this.type = type;
    }
    @Override
    public T convert(ResponseBody value) throws IOException {

        String response = value.string();
        //先将返回的json数据解析到Response中，如果code==200，则解析到我们的实体基类中，否则抛异常
        long waste = System.currentTimeMillis() - WMSUtils.startTime;
        WMSLog.d("convert :" + response + "\n waste:" + waste);
        Response httpResult = gson.fromJson(response, Response.class);
        return gson.fromJson(response, type);
    }
}
