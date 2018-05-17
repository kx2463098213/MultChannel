package com.sunteng.multichannelpackagefactory;

import com.sunteng.multichannelpackagefactory.uitl.Constants;
import com.sunteng.multichannelpackagefactory.uitl.FileUtil;
import com.sunteng.multichannelpackagefactory.uitl.Utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 多渠道打包任务的实现
 * Created by xiaozhonggao on 2018/5/17.
 */

class MultiChannel {

    private ArrayList<String> channelList;
    private int outCount = 1;
    private String channelName = "";
    private String channelFilePath = Constants.CHANNEL_FILE_PATH;
    private String original_apk_path = Constants.ORIGINAL_APK_FILE;


    void start(String[] args){
        Options options = getOptions();
        CommandLine cl = getCommandLine(options, args);
        if (cl != null && cl.getOptions().length > 0){
            if (cl.hasOption(Constants.ARG_HELP)){
                HelpFormatter hf = new HelpFormatter();
                hf.setSyntaxPrefix("语法：");
                hf.printHelp(Constants.SYNTAX, "", options, Constants.HELP_FOOTER);
            } else {
                if (cl.hasOption(Constants.ARG_CHANNEL_NAME)){
                    channelName = cl.getOptionValue(Constants.ARG_CHANNEL_NAME);
                }
                if (cl.hasOption(Constants.ARG_CHANNEL_FILE_PATH)){
                    channelFilePath = cl.getOptionValue(Constants.ARG_CHANNEL_FILE_PATH);
                }
                channelList = getChannelFromFile();

                if (Utils.isEmpty(channelName) && (channelList == null || channelList.isEmpty())){
                    System.err.println("任务执行失败：缺少必须的渠道名称参数");
                    return;
                }
                if (cl.hasOption(Constants.ARG_COUNT)) {
                    String countStr = cl.getOptionValue(Constants.ARG_COUNT);
                    outCount = Integer.parseInt(countStr);
                }
                if (cl.hasOption(Constants.ARG_ORIGINAL_PATH)){
                    original_apk_path = cl.getOptionValue(Constants.ARG_ORIGINAL_PATH);
                }
                getChannelApk();
            }

        } else {
            if (FileUtil.fileExit(channelFilePath)){
                channelList = getChannelFromFile();
                if (channelList == null || channelList.isEmpty()){
                    System.err.println("任务执行失败，channel.txt 内容为空");
                    return;
                }
                getChannelApk();
            } else {
                System.err.println("任务执行失败：当前目录下不存在 channel.txt");
            }
        }
    }

    /**
     * 读取渠道文件中的渠道
     * @return
     */
    private ArrayList<String> getChannelFromFile(){
        if (!FileUtil.fileExit(channelFilePath)){
            return new ArrayList<>();
        }
        channelList = FileUtil.getChannels(channelFilePath);
        return channelList;
    }

    /**
     * 定义 jar 包可以接受的参数
     * @return 参数封装结果
     */
    private Options getOptions(){
        Options options = new Options();
        options.addOption(Constants.ARG_HELP, "help", false," - 输出帮助信息");
        options.addOption(Constants.ARG_CHANNEL_NAME, "channelName", true, " - 定义渠道名称");
        options.addOption(Constants.ARG_COUNT, "num", true, "┌ 定义渠道包的数量\n└ 默认值：1");
        options.addOption(Constants.ARG_ORIGINAL_PATH, "originalName", true,
                "┌ 定义原始包路径\n└ 默认值：original.apk");
        options.addOption(Constants.ARG_CHANNEL_FILE_PATH, "channelFileName", true,
                "┌ 定制多个不同渠道需求时使用\n├ 格式为每个渠道号占一行\n└ 默认值：channel.txt");
        return options;
    }

    /**
     * 解析命令行中的参数
     * @param options 可输入参数的封装类
     * @param args 命令行输入的参数
     * @return 定义的参数和输入的参数的格式化结果
     */
    private CommandLine getCommandLine(Options options, String [] args){
        CommandLineParser clp = new DefaultParser();
        CommandLine cl;
        try {
            cl = clp.parse(options, args);
        } catch (ParseException e) {
            Utils.printStackTrace(e);
            return null;
        }
        return cl;
    }

    /**
     * 进行多渠道打包
     */
    private void getChannelApk(){
        if (!FileUtil.fileExit(original_apk_path)){
            System.err.println("任务失败，原始 APK 文件 " + original_apk_path + " 不存在!");
            return;
        }
        cleanApk(original_apk_path);

        File file = new File(Constants.CHANNEL_APK_PATH);
        if (FileUtil.isFileExit(file)){
            FileUtil.deleteFile(file);
        }
        file.mkdir();

        System.out.println("开始进行多渠道打包....");
        int sum = 0;
        if (!Utils.isEmpty(channelName)){
            System.out.println("*********************************");
            for(int count = 1; count <= outCount; count ++) {
                String name = channelName  + "_" +  count;
                outputChannelApk(name);
                sum ++;
                System.out.println("-- 生成渠道名为：" + name + " 的渠道包");
            }
        }

        if (channelList != null && !channelList.isEmpty()){
            System.out.println("*********************************");
            for (String cn : channelList){
                outputChannelApk(cn);
                sum ++;
                System.out.println("-- 生成渠道名为：" + cn + " 的渠道包");
            }
        }

        System.out.println("*********************************");
        System.out.println("任务完成，一共生成：" + sum + " 个渠道包...");
        System.out.println("告辞!");
        System.exit(0);
    }

    private void outputChannelApk(String name){
        try {
            File originalFile = new File(original_apk_path);
            String originalName = originalFile.getName().replace(Constants.APK_SUFFIX, "");
            String targetApk = Constants.CHANNEL_APK_PATH + File.separator + originalName
                    + "_" + name + Constants.APK_SUFFIX;
            FileUtil.copy(originalFile, targetApk);
            ZipFile zipFile = new ZipFile(targetApk);
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
            parameters.setRootFolderInZip("META-INF/");
            String channelFilePath = "channel_" + name;
            File  channelFile = new File(channelFilePath);
            if (!FileUtil.isFileExit(channelFile)){
                FileUtil.createNewFile(channelFilePath);
            }
            zipFile.addFile(channelFile, parameters);
            channelFile.delete();
        } catch (Exception e){
            Utils.printStackTrace(e);
        }
    }

    /**
     * 某些情况下原生文件可能已经是渠道包，需要移除里面的渠道标识文件
     * @param path 文件名称（含路径）
     */
    private void cleanApk(String path){
        try {
            ZipFile file = new ZipFile(path);
            List<FileHeader> files = file.getFileHeaders();
            ArrayList<String> removeList = new ArrayList<>();
            for (int i = 0; i < files.size(); i++){
                String fileName = files.get(i).getFileName();
                if (fileName.startsWith("META-INF/channel_")) {
                    removeList.add(fileName);
                }
            }
            for (String name : removeList){
                file.removeFile(name);
            }
        } catch (ZipException e) {
            Utils.printStackTrace(e);
        }
    }

}
