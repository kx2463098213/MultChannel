package com.sunteng.multichannelpackagefactory.uitl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

/**
 * Created by xiaozhonggao on 2017/5/10.
 */


public class FileUtil {
    /**
     * 文件操作工具
     */


    /**
     * 判断文件是否存在
     * @param fileName 文件名
     * @return
     */
    public static boolean fileExit(String fileName){
        boolean isExit = false;
        if (!Utils.isEmpty(fileName)){
            File file = new File(fileName);
            isExit = isFileExit(file);
        }
        return isExit;
    }

    /**
     * 文件是否存在
     * @param file
     * @return
     */
    public static boolean isFileExit(File file){
        boolean isExit = false;
        if (file != null && file.exists()){
            isExit = true;
        }
        return isExit;
    }


    /**
     * 新建文件或者文件夹
     * @param fileName
     */
    public static File createNewFile(String fileName){
        File file = null;
        try {
            if(!Utils.isEmpty(fileName)){
                file = new File(fileName);
                if (file != null && !file.exists()){
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
            }
        } catch (IOException e) {
            Utils.printStackTrace(e);
        }
        return file;
    }

    /**
     * 文件复制
     * @param sourceName 原始文件名字（带路径）
     * @param targetName 目标文件（带路径）
     */
    public static void copy(String sourceName, String targetName){
        File sourceFile = new File(sourceName);
        File targetFile = new File(targetName);
        if (isFileExit(sourceFile)){
            FileInputStream fis = null;
            FileOutputStream fos = null;
            try {
                fis = new FileInputStream(sourceFile);
                fos = new FileOutputStream(targetFile);
                byte [] buffer = new byte[1024];
                int length = -1;
                while ((length = fis.read(buffer)) > 0){
                    fos.write(buffer, 0, length);
                }
            } catch (Exception e) {
                Utils.printStackTrace(e);
            }finally {
                try {
                    if (fis != null)
                        fis.close();
                    if (fos != null)
                        fos.close();
                } catch (IOException e) {
                    Utils.printStackTrace(e);
                }
            }
        }
    }

    /**
     * 读取channel.txt中的渠道号
     * key-渠道号
     * value - 链接
     * @return
     */
    public static HashMap<String, String> readChannelIds(){
        HashMap<String, String> map = new HashMap<>();
        FileInputStream fis = null;
        BufferedReader reader = null;
        try{
            fis = new FileInputStream(new File(Contants.CHANNEL_FILE_PATH));
            reader = new BufferedReader(new InputStreamReader(fis, Contants.CHARSET_TYPE));
            String result;
            while (null != (result = reader.readLine())){
                String [] strArray = result.split("分割");
                if (!Utils.isEmpty(strArray[0]) && !Utils.isEmpty(strArray[1])){
                    map.put(strArray[0].trim(), strArray[1].trim());
                }
            }
        }catch (Exception e){
            Utils.printStackTrace(e);
        }finally {
            try {
                if (fis != null){
                    fis.close();
                }
                if (reader != null){
                    reader.close();;
                }
            } catch (IOException e) {
                Utils.printStackTrace(e);
            }
        }
        return map;
    }
}
