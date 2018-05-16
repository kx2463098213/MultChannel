package com.sunteng.multichannelpackagefactory.uitl;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by xiaozhonggao on 2017/5/10.
 */
public class Utils {


    /**
     * 判断字符串是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        boolean isEmpty = false;
        if (str == null || "".equals(str)){
            isEmpty = true;
        }
        return isEmpty;
    }

    /**
     * 控制台打印异常信息
     * @param e
     */
    public static void printStackTrace(Exception e){
        StringWriter strWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(strWriter);
        e.printStackTrace(printWriter);
        System.out.println(strWriter.toString());
    }

}
