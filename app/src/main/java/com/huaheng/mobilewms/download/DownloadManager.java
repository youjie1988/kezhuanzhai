package com.huaheng.mobilewms.download;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.util.WMSUtils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

/**
 * Created by youjie on 2018/3/28.
 */

public class DownloadManager {

    private final static String TAG = "DownloadManager";
    private Downloader mDownloader = null;
    private static DownloadManager instance = null;
    private static Context mContext;
    private Thread mDownloadThread;
    private OnFileDownloadListener mOnFileDownloadListener;
    private HashMap<String, FileInfo> mDownloadCacheMap;
    private HashMap<String, FileInfo> mDownloadContinuesMap;
    private final int CHECK_TIME = 5 * 1000;
    private final int MAX_DOWNLOAD_SIZE = 3;
    private final int MSG_START_DOWNLOAD = 0;
    private final int MSG_RESUME_DOWNLOAD = 1;
    private Handler mHandler;
    private DownloadHandler handlerThread;
    private AtomicBoolean isDownloading, isTryContinue;
    private Queue<String> tasksContinue;
    private Object locker = new Object();

    public DownloadManager() {
        isDownloading = new AtomicBoolean(false);
        isTryContinue = new AtomicBoolean(false);
        mDownloader = new Downloader();
        handlerThread = new DownloadHandler("DownloadHandler");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper(), handlerThread);
        mDownloadContinuesMap = new HashMap<>();
        mDownloadCacheMap = new HashMap<>();
        tasksContinue = new LinkedList<String>();
        mDownloader.setDownloadCallback(new DownloadCallback() {
            @Override
            public void onStart(String fileName) {
                isDownloading.set(true);
            }

            @Override
            public void onLoading(String fileName, int progress, boolean isDownloading) {
                if (mOnFileDownloadListener != null) {
                    mOnFileDownloadListener.onProgress(fileName, progress);
                }
            }

            @Override
            public void onSuccess(String fileName, File result) {
                Log.i(TAG, "onSuccess ");
                if (result != null) {
                    mDownloadCacheMap.remove(result.getName());
                    mDownloadContinuesMap.remove(result.getName());
                }
                if (mOnFileDownloadListener != null) {
                    mOnFileDownloadListener.onSuccess(result.getName());
                }
                isDownloading.set(false);
            }

            @Override
            public void onError(String fileName, String msg) {
                if (mOnFileDownloadListener != null) {
                    mOnFileDownloadListener.onError(fileName, msg);
                }
                isDownloading.set(false);
                isTryContinue.set(true);
            }

            @Override
            public void onCancelled(String fileName) {
                if (mOnFileDownloadListener != null) {
                    mOnFileDownloadListener.onCanceled(fileName);
                }
                isDownloading.set(false);
            }
        });
    }

    public static synchronized DownloadManager getInstance(Context context) {
        if (instance == null) {
            mContext = context;
            instance = new DownloadManager();
        }
        return instance;
    }

    public void setOnFileDownloadListener(OnFileDownloadListener listener) {
        mOnFileDownloadListener = listener;
    }

    public void removeDownloadWorker(String fileName) {
        if (mDownloader != null) {
            mDownloader.removeDownloadWorker(fileName);
        }
    }

    public void startDownload(FileInfo fileinfo) {
        if (fileinfo == null) {
            onError(null, mContext.getString(R.string.file_info_error));
            return;
        }
        if (!fileinfo.check()) {
            onError(null, mContext.getString(R.string.file_info_error));
            return;
        }
        if (!NetworkUtils.isGoodNetworkConnected(mContext)) {
            onError(null, mContext.getString(R.string.network_error));
            return;
        }
        if (fileinfo != null && fileinfo.check()) {
            String fileName = fileinfo.getFileName();
            if (!mDownloadCacheMap.containsKey(fileName)) {
                mDownloadCacheMap.put(fileName, fileinfo);
            }
//            if(!mDownloadContinuesMap.containsKey(fileName)) {
//                mDownloadContinuesMap.put(fileName, fileinfo);
//            }
            doWorker();
            isTryContinue.set(false);
            if (!tasksContinue.contains(fileName)) {
                tasksContinue.add(fileName);
            }
        }
    }

    private void doWorker() {
        if (mDownloadThread != null) {
            if (mDownloadThread.isAlive()) {
                mDownloadThread.interrupt();
                return;
            }
        }
        mDownloadThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int flag = 1;
                Looper.prepare();
                while (true) {
                    flag++;
                    if (flag % 2 == 0) {
                        mHandler.sendEmptyMessage(MSG_START_DOWNLOAD);
                    } else {
                        mHandler.sendEmptyMessage(MSG_RESUME_DOWNLOAD);
                    }
                    if (flag == 2) {
                        flag = 0;
                    }
                    try {
                        sleep(CHECK_TIME);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        flag = 1;
                    }
                }
            }
        });
        mDownloadThread.start();
    }

    private void innerStartDownload() {
        for (int i = 0; i < MAX_DOWNLOAD_SIZE; i++) {
            FileInfo fileInfo = findDownloader();
            toDownload(fileInfo);
        }
    }

    private void innerContinueDownload() {
        if (!isTryContinue.get()) {
            return;
        }
        isTryContinue.set(false);
        if (mDownloadContinuesMap != null && mDownloadContinuesMap.size() > 0) {
            int size = mDownloadContinuesMap.size();
            for (int i = 0; i < size; i++) {
                FileInfo fileInfo = findContentDownloader();
                toDownload(fileInfo);
            }
        }
    }

    private FileInfo findDownloader() {
        String fileName = null;
        for (Map.Entry<String, FileInfo> ent : mDownloadCacheMap.entrySet()) {
            fileName = ent.getKey();
            break;
        }
        FileInfo fileInfo = mDownloadCacheMap.get(fileName);
        mDownloadCacheMap.remove(fileName);
        return fileInfo;
    }

    private FileInfo findContentDownloader() {
        String fileName = null;
        for (Map.Entry<String, FileInfo> ent : mDownloadContinuesMap.entrySet()) {
            fileName = ent.getKey();
            break;
        }
        FileInfo fileInfo = mDownloadContinuesMap.get(fileName);
        return fileInfo;
    }

    /*
     *如果已经下载过，就不再下载
     */
    private boolean checkFileExist(FileInfo fileInfo) {
        if (fileInfo != null) {
            Log.i(TAG, "checkFileExist  fileInfo:" + fileInfo.toString());
            String str = fileInfo.getPath() + fileInfo.getFileName();
            File file = new File(str);
            if (file.exists()) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean checkVersion(FileInfo fileInfo) {
        if (fileInfo != null) {
            Log.i(TAG, "checkVersion  fileInfo:" + fileInfo.toString());
            if (fileInfo.getFileType() == FileInfo.FILE_TYPE_OTA) {
//                int remoteOTAVersion = fileInfo.getVersion();
//                int localOTAVersion = WMSUtils..getLocalVersionNum();
//                if(localOTAVersion < remoteOTAVersion) {
//                    return true;
//                }
            } else if (fileInfo.getFileType() == FileInfo.FILE_TYPE_APK) {
                int localVersion = WMSUtils.getVersionCode();
                if (fileInfo.getVersion() > localVersion) {
                    return true;
                }
            } else if (fileInfo.getFileType() == FileInfo.FILE_TYPE_NORMAL) {
                return true;
            }
        }
        return false;
    }


    private void onError(String fileName, String msg) {
        if (mOnFileDownloadListener != null) {
            mOnFileDownloadListener.onError(fileName, msg);
        }
    }

    private void toDownload(FileInfo fileInfo) {
        if (fileInfo == null) {
            return;
        }
        if (!fileInfo.check()) {
            return;
        }

        if (!checkVersion(fileInfo)) {
            onError(fileInfo.getFileName(), mContext.getString(R.string.file_version_error));
            return;
        }
        String url = fileInfo.getUrl();
        String fileName = fileInfo.getFileName();
        String path = fileInfo.getPath();
        if (url != null && fileName != null && path != null) {
            mDownloader.download(url, fileName, path);
        } else {
            Log.i(TAG, "toDownload param error");
        }
    }

    private class DownloadHandler extends HandlerThread implements Handler.Callback {

        public DownloadHandler(String name) {
            super(name);
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_START_DOWNLOAD:
                    innerStartDownload();
                    break;
                case MSG_RESUME_DOWNLOAD:
                    innerContinueDownload();
                    break;
                default:
                    break;
            }
            return true;
        }
    }

}
