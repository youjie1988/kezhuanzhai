package com.huaheng.mobilewms.download;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.HashMap;

/**
 * Created by youjie on 2018/3/28.
 */

public class Downloader {

    private final static String TAG = "Downloader";
    private final static int MAX_RERTY_TIMES = 0;
    private DownloadCallback mCallback = null;
    private HashMap<String, Callback.Cancelable> mDownloads = null;

    public Downloader() {
        mDownloads = new HashMap<>();
    }

    public void download(final String url, String fileName, String path) {
        RequestParams requestParams = new RequestParams(url);
        requestParams.setSaveFilePath(path + fileName);
        requestParams.setAutoRename(true);
        requestParams.setMaxRetryCount(MAX_RERTY_TIMES);
        DownloadProgressCallback mDownloadProgressCallback = new DownloadProgressCallback(fileName);
        Callback.Cancelable cancelable =  x.http().get(requestParams, mDownloadProgressCallback);
        mDownloads.put(fileName, cancelable);
    }

    private class DownloadProgressCallback implements Callback.ProgressCallback<File> {

        private String fileName;
        private DownloadCallback callback;

        DownloadProgressCallback(String fileName) {
            this.fileName = fileName;
        }

        public void setProgressListener(DownloadCallback callback) {
            this.callback = callback;
        }

        public String getFileName() {
            return fileName;
        }

        @Override
        public void onWaiting() {

        }

        @Override
        public void onStarted() {
            if(mCallback != null) {
                mCallback.onStart(getFileName());
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            if(mCallback != null) {
                int progress = (int)(current * 100 / total);
                mCallback.onLoading(getFileName(), progress, isDownloading);
            }
        }

        @Override
        public void onSuccess(File result) {
            if(mCallback != null) {
                mCallback.onSuccess(getFileName(), result);
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            if(mCallback != null) {
                String msg = ex.getMessage();
                mCallback.onError(getFileName(), msg);
            }
        }

        @Override
        public void onCancelled(CancelledException cex) {
            if(mCallback != null) {
                mCallback.onCancelled(getFileName());
            }
        }

        @Override
        public void onFinished() {

        }
    }

    public void setDownloadCallback(DownloadCallback callback) {
        mCallback = callback;
    }

    public void removeDownloadWorker(String fileName) {
        if(fileName == null) {
            return;
        }
        Callback.Cancelable cancelable = mDownloads.get(fileName);
        if(cancelable != null) {
            cancelable.cancel();
        }
    }
}
