package com.sunteng.multichannelpackagefactory.uitl;

/**
 * Created by xiaozhonggao on 2017/5/10.
 */
public class Constants {
    /**
     * 存放一些常量
     */

    //存放渠道号的txt文件
    public static final String CHANNEL_FILE_PATH = "channel.txt";
    //原始apk文件
    public static final String ORIGINAL_APK_FILE = "original.apk";
    //新生成的带渠道号的路径
    public static final String CHANNEL_APK_PATH = "channel_package";
    //编码类型
    public static final String CHARSET_TYPE = "UTF-8";
    //apk 后缀
    public static final String APK_SUFFIX = ".apk";

    // 定义 jar 输入参数中使用到的一些常量
    public static final String ARG_COUNT = "n";
        public static final String ARG_CHANNEL_NAME = "cn";
    public static final String ARG_HELP = "h";
    public static final String ARG_ORIGINAL_PATH = "o";
    public static final String ARG_CHANNEL_FILE_PATH = "cf";

    public static final String SYNTAX = "java [-options] -jar jarfile [args...] \t <执行 jar 文件>\n其中选项包括：";
    public static final String HELP_FOOTER = "任务完成后，生产的渠道包会在当前目录下的 channel_package 文件夹中。"
            +"\n注意：\n┌ -n 和 -cn 搭配使用得到的渠道号为为：[渠道名称]_[1...n]"
            + "\n├ -n 和 -cf 定义的渠道号不能全为空\n└ -cf 暂时不支持和 -n 搭配使用 ";

}
