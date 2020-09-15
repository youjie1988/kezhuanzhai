package com.huaheng.mobilewms.download;

import android.net.Uri;
import android.util.Log;

/**
 * Created by youjie on 2018/3/30.
 */

public class FileInfo {

    public final static int FILE_TYPE_UNKNOWN = 0;
    public final static int FILE_TYPE_OTA = FILE_TYPE_UNKNOWN + 1;
    public final static int FILE_TYPE_APK = FILE_TYPE_OTA + 1;
    public final static int FILE_TYPE_NORMAL = FILE_TYPE_APK + 1;

    private final static String SUFFIX_APK = ".apk";
    private final static String SUFFIX_OTA = ".zip";

    private int fileType;
    private String url;
    private String fileName;

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    private String pkgName;
    private String path;
    private int version;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean check() {
        Log.i("WeiPos", "check  ");
        if (fileName == null || url == null) {
            return false;
        }
        Log.i("WeiPos", "check  @@");
        try {
            Uri.parse(url);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        Log.i("WeiPos", "check  @@");

        if (fileType == FILE_TYPE_APK) {
            if (!checkAPK()) {
                return false;
            }
        }

        if (fileType == FILE_TYPE_OTA) {
            if (!checkOTA()) {
                return false;
            }
        }
        return true;
    }

    public boolean checkAPK() {
        Log.i("WeiPos", "checkAPK  @@");
        if (fileName != null) {
            int length = fileName.length();
            String suffix = fileName.substring(length - 4, length);
            if (suffix != null && suffix.equals(SUFFIX_APK)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkOTA() {
        if (fileName != null) {
            int length = fileName.length();
            String suffix = fileName.substring(length - 4, length);
            Log.i("WeiPos", "suffix :" + suffix);
            if (suffix != null && suffix.equals(SUFFIX_OTA)) {
                Log.i("WeiPos", "checkOTA true");
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "fileType=" + fileType +
                ", url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", path='" + path + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
