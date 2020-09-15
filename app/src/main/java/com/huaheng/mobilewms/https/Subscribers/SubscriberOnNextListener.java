package com.huaheng.mobilewms.https.Subscribers;


public interface SubscriberOnNextListener<T> {

        void onNext(T t);

        void onError(String str);
}
