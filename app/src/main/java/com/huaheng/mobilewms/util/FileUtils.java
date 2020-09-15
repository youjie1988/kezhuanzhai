package com.huaheng.mobilewms.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileUtils {
    public static String PATH_LOG = "/sdcard/wms/";

    /**
     * 取dir下指定扩展名的文件列表
     * @param dir
     * @param ext
     * @return
     */
    public static List<File> listDirFiles(String dir, String ext) {
        File[] fileNames = new File(dir).listFiles();

        List<File> listExt = new ArrayList<>();
        ext = ext.toLowerCase();

        for (int i = 0; i < fileNames.length; i++) {
            File f = fileNames[i];
            if (f.getName().toLowerCase().endsWith(ext)) {
                listExt.add(f);
            }
        }

        return listExt;
    }

    /**
     * 取dir下指定扩展名的文件名列表
     * @param dir
     * @param ext
     * @return
     */
    public static List<String> listDirFileNames(String dir, String ext) {
        List<File> fileList = listDirFiles(dir, ext);

        List<String> fileNameList = new ArrayList<>();

        for (File f : fileList) {
            fileNameList.add(f.getName());
        }
        return fileNameList;
    }

    /**取日志文件**/
    public static File[] getCrashLogFiles() {
        List<File> list = listDirFiles(PATH_LOG, ".txt");
        File[] files = new File[list.size()];

        //最新的日志放前面
        Collections.reverse(list);
        list.toArray(files);
        return files;
    }

    /**取日志文件名**/
    public static String[] getCrashLogNames(boolean needSeq) {
        List<String> list = listDirFileNames(PATH_LOG, ".txt");

        //最新的日志放前面
        Collections.reverse(list);

        String[] arr = new String[list.size()];
        list.toArray(arr);

        //加上序号
        if (needSeq) {
            for (int i = 0; i < arr.length; i++) {
                arr[i] = String.valueOf(arr.length - i) + ".  " + arr[i];
            }
        }

        return arr;
    }

    public static String readFileToString(File file){
        StringBuffer sbf = new StringBuffer();
        FileReader fr = null;
        BufferedReader br = null;

        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = null;
            while ((line = br.readLine()) != null) {
                sbf.append(line);
                sbf.append("\n");
            }
        } catch (FileNotFoundException e) {
            sbf.append("找不到文件：" + file.getAbsolutePath());
        } finally {
            try {
                if (br != null) br.close();
                if (fr != null) fr.close();
            } catch (IOException ioe) {

            }
            return sbf.toString();
        }
    }
}
