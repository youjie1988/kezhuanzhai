package com.huaheng.mobilewms.download;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.FileProvider;

import com.huaheng.mobilewms.R;
import com.huaheng.mobilewms.bean.Constant;
import com.huaheng.mobilewms.https.HttpConstant;
import com.huaheng.mobilewms.util.WMSLog;
import com.huaheng.mobilewms.util.WMSUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by youjie on 2018/8/24
 */
public class DownloadUtils {

    private Context mContext;
    private static DownloadUtils downloadUtils;
    private String path = Environment.getExternalStorageDirectory().toString() + "/data/";
    private ProgressDialog progressDialog;
    ProgressHandlerThread handlerThread;
    Handler mHandler;

    public DownloadUtils(Context context) {
        mContext = context;
        handlerThread = new ProgressHandlerThread("ProgressHandlerThread");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper(), handlerThread);
    }

    public static DownloadUtils as(Context context) {
        if(downloadUtils == null) {
            downloadUtils = new DownloadUtils(context);
        }
        return downloadUtils;
    }

    public void startDownload() {
        String url = HttpConstant.GET_APKINFO_URL;
        String pkgName = WMSUtils.getPackageName();
        int versionCode = WMSUtils.getVersionCode();
        final JSONObject object = new JSONObject();
        try {
            object.put("pkgName", pkgName);
            object.put("versionCode", versionCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OKHttpUtils.commonPost(url, object.toString(), new OKHttpInterface() {

            @Override
            public void onSuccess(String info) {
                WMSLog.d("onSuccess :" + info);
                try {
                    JSONObject object1 = new JSONObject(info);
                    int code = (int)object1.get("code");
                    final String msg = WMSUtils.getJsonData(object1, "msg");
                    String data = WMSUtils.getJsonData(object1, "data");
                    if(code != Constant.RET_OK) {
                        Looper.prepare();
//                        WMSUtils.showShort(msg);
                        Looper.loop();
                    } else {
                        JSONObject jsonObject = new JSONObject(data);
                        String url = WMSUtils.getJsonData(jsonObject, "url");
                        String md5 = WMSUtils.getJsonData(jsonObject, "md5");
                        int version = Integer.parseInt(WMSUtils.getJsonData(jsonObject, "versionCode"));
                        download(url, md5, version);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Looper.prepare();
               //     WMSUtils.showShort(mContext.getString(R.string.download_fail));
                    Looper.loop();
                }
            }

            @Override
            public void onFail(String msg) {
                WMSLog.d("onFail :" + msg);
                Looper.prepare();
              //  WMSUtils.showShort(mContext.getString(R.string.network_exception));
                Looper.loop();
            }
        });
    }

    public void download(String url, final String md5, int version) {
        showProgress();
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFileName(WMSUtils.getPackageName() + ".apk");
        fileInfo.setFileType(FileInfo.FILE_TYPE_APK);
        fileInfo.setPath(path);
        fileInfo.setPkgName(WMSUtils.getPackageName());
        fileInfo.setUrl(url);
        fileInfo.setVersion(version);
        DownloadManager.getInstance(mContext).startDownload(fileInfo);
        DownloadManager.getInstance(mContext).setOnFileDownloadListener(new OnFileDownloadListener() {
            @Override
            public void onProgress(String fileName, int progress) {
                WMSLog.d("onProgress progress:" + progress);
                progressDialog.setProgress(progress);
            }

            @Override
            public void onError(String fileName, String msg) {
                WMSLog.d("onError msg:" + msg);
//                WMSUtils.showShort(msg);
                progressDialog.dismiss();
            }

            @Override
            public void onSuccess(String fileName) {
                WMSLog.d("onSuccess fileName:" + fileName);
                File apkFile = new File(path + fileName);
                String fileMD5 = WMSUtils.md5(apkFile);
                WMSLog.d("fileMD5:" + fileMD5  +  "    md5:" + md5);
                if(!fileMD5.equals(md5)) {
                    WMSUtils.showShort(mContext.getString(R.string.md5_fail));
                    progressDialog.dismiss();
                    return;
                }
                WMSUtils.showShort(mContext.getString(R.string.download_success));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.N) {
                    Uri contentUri = FileProvider.getUriForFile(mContext,"com.huaheng.mobilewms.fileProvider", apkFile);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
                }else{
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
                }
                mContext.startActivity(intent);
                progressDialog.dismiss();
            }

            @Override
            public void onCanceled(String fileName) {
                progressDialog.dismiss();
            }
        });
    }

    private void showProgress() {
        mHandler.sendEmptyMessage(0);
    }

    private void innerShowProgress() {
        progressDialog = new ProgressDialog(mContext);//实例化ProgressDialog
        progressDialog.setMax(100);//设置最大值
        progressDialog.setTitle(mContext.getString(R.string.progress_download));//设置标题
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置样式为横向显示进度的样式
        progressDialog.setIndeterminate(false);//是否精确显示对话框，flase为是，反之为否
        //是否可以通过返回按钮退出对话框
        progressDialog.setCancelable(false);
        progressDialog.show();//显示对话框
    }

    private class ProgressHandlerThread extends HandlerThread implements Handler.Callback {

        public ProgressHandlerThread(String name) {
            super(name);
        }

        @Override
        public boolean handleMessage(Message msg) {
            innerShowProgress();
            return true;
        }
    }
}
