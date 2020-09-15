package com.huaheng.mobilewms.download;

import java.io.File;

/**
 * Created by youjie on 2018/3/30.
 */

public interface DownloadCallback  {

    void onStart(String fileName);

    void onLoading(String fileName, int progress, boolean isDownloading);

    void onSuccess(String fileName, File result);

    void onError(String fileName, String msg);

    void onCancelled(String fileName);
}
