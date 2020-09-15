package com.huaheng.mobilewms.https;

public interface HttpCallback {

    void onSuccess(String msg);

    void onError(String msg);
}
