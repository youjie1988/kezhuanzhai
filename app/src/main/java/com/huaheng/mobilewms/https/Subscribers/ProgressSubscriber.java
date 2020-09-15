package com.huaheng.mobilewms.https.Subscribers;

import android.content.Context;
import android.widget.Toast;

import com.huaheng.mobilewms.LoginActivity;
import com.huaheng.mobilewms.MainActivity;
import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.https.ProgressCancelListener;
import com.huaheng.mobilewms.https.ProgressDialogHandler;
import com.huaheng.mobilewms.util.SoundUtils;
import com.huaheng.mobilewms.util.WMSUtils;

import rx.Subscriber;


public class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SubscriberOnNextListener mListener;
    private ProgressDialogHandler progressHandler;

    private Context context;
    private boolean showError = true;
    private boolean playSound = true;
    private boolean showDialog = true;

    public ProgressSubscriber(Context context, SubscriberOnNextListener mListener) {
        this.context = context;
        this.mListener = mListener;

        progressHandler = new ProgressDialogHandler(context, this, false);
    }

    private void showProgressDialog() {
        if (progressHandler != null) {
            progressHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog() {
        if (progressHandler != null) {
            progressHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            progressHandler = null;
        }
    }


    @Override
    public void onStart() {
        WMSLog.d("onStart");
        if(showDialog) {
            showProgressDialog();
        }
    }

    @Override
    public void onCompleted() {
        WMSLog.d("onCompleted");
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        dismissProgressDialog();
        e.printStackTrace();
        if(isPlaySound()) {
            SoundUtils.getInstance(context).errorSound();
        }
        if(e.getMessage() != null) {
            WMSLog.d("onError:" + e.getMessage());
            if (e.getMessage().contains(context.getString(R.string.login_again))) {
                WMSUtils.startActivity(context, LoginActivity.class);
            }
            if(showError) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            if(e.toString() != null && e.toString().contains("SocketTimeoutException")) {
                Toast.makeText(context, context.getString(R.string.http_sockettime), Toast.LENGTH_SHORT).show();
            }
        }
        mListener.onError(e.getMessage());
    }

    @Override
    public void onNext(T t) {
        WMSLog.d("onNext t:" + t);
        mListener.onNext(t);
    }

    @Override
    public void onCancelProgress() {
        WMSLog.d("onCancelProgress");
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }

    public boolean isShowError() {
        return showError;
    }

    public void setShowError(boolean showError) {
        this.showError = showError;
    }

    public boolean isPlaySound() {
        return playSound;
    }

    public void setPlaySound(boolean playSound) {
        this.playSound = playSound;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }
}
