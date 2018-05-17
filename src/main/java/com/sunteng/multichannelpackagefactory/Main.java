package com.sunteng.multichannelpackagefactory;

/**
 * 可执行jar文件的入口
 * Created by xiaozhonggao on 2017/5/10.
 */
public class Main {

    public static void main(String [] args){
        MultiChannel main = new MultiChannel();
        main.start(args);
    }

}
