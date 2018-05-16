package com.sunteng.multichannelpackagefactory;

import com.sunteng.multichannelpackagefactory.uitl.Constants;
import com.sunteng.multichannelpackagefactory.uitl.FileUtil;
import com.sunteng.multichannelpackagefactory.uitl.Utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;

/**
 * Created by xiaozhonggao on 2017/5/10.
 */
public class Main {
    /**
     * 可执行jar文件的入口
     * @param args
     */

//    private HashMap<String, String> channelIdsMap;
    private static int outCount = 1;
    private static String channelName = "";
    private static String original_apk_path = Constants.ORIGINAL_APK_FILE;

    public static void main(String [] args){
        Options options = getOptions();
        CommandLine cl = getCommandLine(options, args);
        if (cl != null && cl.getOptions().length > 0){
            if (cl.hasOption(Constants.ARG_HELP)){
                HelpFormatter hf = new HelpFormatter();
                hf.printHelp("java [-options] -jar jarfile [args...] \t <执行 jar 文件>" +
                        "\n其中选项包括：", "", options,
                        "任务完成后，生产的渠道包会在当前目录下的 channel_package 文件夹中。");
            } else {
                if (cl.hasOption(Constants.ARG_CHANNEL_NAME)){
                    channelName = cl.getOptionValue(Constants.ARG_CHANNEL_NAME);
                } else {
                    System.err.print("任务执行失败：缺少渠道包名称参数。");
                    return;
                }
                if (cl.hasOption(Constants.ARG_COUNT)) {
                    String countStr = cl.getOptionValue(Constants.ARG_COUNT);
                    outCount = Integer.parseInt(countStr);
                }
                if (cl.hasOption(Constants.ARG_ORIGINAL_PATH)){
                    original_apk_path = cl.getOptionValue(Constants.ARG_ORIGINAL_PATH);
                }
                Main main = new Main();
                main.getChannelApk();
            }

        } else {
            System.out.print("缺少必要参数，任务无法执行，可用 -h 参数查看使用帮助。");
        }
    }

    /**
     * 定义 jar 包可以接受的参数
     * @return 参数封装结果
     */
    private static Options getOptions(){
        Options options = new Options();
        options.addOption("h", "help", false,"使用帮助");
        options.addOption("n", "channelName", true, "<必须> 定义渠道名称");
        options.addOption("c", "count", true, "定义渠道包的数量，默认值：1");
        options.addOption("o", "originalName", true, "定义原始包路径，默认值：original.apk");
        return options;
    }

    /**
     * 解析命令行中的参数
     * @param options
     * @param args
     * @return
     */
    private static CommandLine getCommandLine(Options options, String [] args){
        CommandLineParser clp = new DefaultParser();
        CommandLine cl;
        try {
            cl = clp.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            return null;
        }
        return cl;
    }

    /**
     * 开始多渠道打包
     */
    public void getChannelApk(){
//        if (!FileUtil.fileExit(Constants.CHANNEL_FILE_PATH)){
//            System.err.println("当前目录下没有渠道文件:channel.txt,请检查!");
//            return;
//        }
//        channelIdsMap = FileUtil.readChannelIds();
//        if (channelIdsMap.size() == 0){
//            System.err.println("channel.txt中内容为空!");
//            return;
//        }

        File file = new File(Constants.CHANNEL_APK_PATH);
        if (FileUtil.isFileExit(file)){
            FileUtil.deleteFile(file);
        }
        file.mkdir();

        if (!FileUtil.fileExit(original_apk_path)){
            System.err.println("任务失败，原始 APK 文件 " + original_apk_path + " 不存在!");
            return;
        }

//        Iterator iterator = channelIdsMap.entrySet().iterator();
        System.out.println("开始进行多渠道打包....");
        for(int count = 1; count <= outCount; count ++){
            try {
                String name = channelName+count;
                System.out.println("开始生成第" + count + "个渠道包，渠道名为：" + name);
                String apkFile = Constants.CHANNEL_APK_PATH + File.separator + name + Constants.APK_SUFFIX;
                FileUtil.copy(original_apk_path, apkFile);
                ZipFile zipFile = new ZipFile(apkFile);
                ZipParameters parameters = new ZipParameters();
                parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                parameters.setRootFolderInZip("META-INF/");
                String channelFilePath = "channel_" + name;
                File  channelFile = new File(channelFilePath);
                if (!FileUtil.isFileExit(channelFile)){
                    FileUtil.createNewFile(channelFilePath);
                }
//                IOUtil.writeString2File(link, channelFilePath);
                zipFile.addFile(channelFile, parameters);
                channelFile.delete();
            }catch (Exception e){
                Utils.printStackTrace(e);
            }
        }
        System.out.println("大功告成，告辞!");
        System.exit(0);
    }

}
