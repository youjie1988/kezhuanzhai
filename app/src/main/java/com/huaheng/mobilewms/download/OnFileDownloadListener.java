package com.huaheng.mobilewms.download;

/**
 * Created by youjie on 2018/3/30.
 */

public interface OnFileDownloadListener {

    void onProgress(String fileName, int progress);

    void onError(String fileName, String msg);

    void onSuccess(String fileName);

    void onCanceled(String fileName);
}
