package com.sunteng.multichannelpackagefactory.uitl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by xiaozhonggao on 2017/5/10.
 */
public class IOUtil {

    /**
     * 把字符串写到文件中
     * @param str
     * @param fileName
     */
    public static void writeString2File(String str, String fileName){
        if (Utils.isEmpty(str)){
            return;
        }
        FileWriter fw = null;
        try {
            File file = FileUtil.createNewFile(fileName);
            fw = new FileWriter(file);
            fw.write(str);
            fw.flush();
        } catch (Exception e) {
            Utils.printStackTrace(e);
        }finally {
            try {
                if (fw != null){
                    fw.close();
                }
            } catch (IOException e) {
                Utils.printStackTrace(e);
            }
        }
    }

}
