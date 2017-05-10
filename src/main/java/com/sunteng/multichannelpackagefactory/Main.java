package com.sunteng.multichannelpackagefactory;

import com.sunteng.multichannelpackagefactory.uitl.Contants;
import com.sunteng.multichannelpackagefactory.uitl.FileUtil;
import com.sunteng.multichannelpackagefactory.uitl.IOUtil;
import com.sunteng.multichannelpackagefactory.uitl.Utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xiaozhonggao on 2017/5/10.
 */
public class Main {
    /**
     * 可执行jar文件的入口
     * @param args
     */

    private HashMap<String, String> channelIdsMap;

    public static void main(String [] args){
        Main main = new Main();
        main.getChannelApk();
    }

    /**
     * 开始多渠道打包
     */
    public void getChannelApk(){
        if (!FileUtil.fileExit(Contants.CHANNEL_FILE_PATH)){
            System.err.println("当前目录下没有渠道文件:channel.txt,请检查!");
            return;
        }
        channelIdsMap = FileUtil.readChannelIds();
        if (channelIdsMap.size() == 0){
            System.err.println("channel.txt中内容为空!");
            return;
        }

        if (!FileUtil.fileExit(Contants.CHANNEL_APK_PATH)){
            System.out.println("新建存放渠道APK的文件夹....");
            File file = new File(Contants.CHANNEL_APK_PATH);
            file.mkdir();
        }

        if (!FileUtil.fileExit(Contants.ORIGINAL_APK_FILE)){
            System.err.println("老铁，当前目录下找不到原始APK文件:original.apk!");
            return;
        }

        Iterator iterator = channelIdsMap.entrySet().iterator();
        System.out.println("开始进行多渠道打包....");
        int count = 0;
        while (iterator.hasNext()){
            try {
                count++;
                Map.Entry<String, String> entry = (Map.Entry) iterator.next();
                String name = entry.getKey();
                String link = entry.getValue();
                System.out.println("开始打包第"+count+"个渠道包，渠道名为："+name+"，链接为："+link);
                String apk = Contants.CHANNEL_APK_PATH + File.separator + name + Contants.APK_SUFFIX;
                FileUtil.copy(Contants.ORIGINAL_APK_FILE, apk);
                ZipFile zipFile = new ZipFile(apk);
                ZipParameters parameters = new ZipParameters();
                parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                parameters.setRootFolderInZip("META-INF/");
                String channelFilePath = Contants.CHANNEL_APK_PATH + File.separator + "channel";
                File  channelFile = new File(channelFilePath);
                if (!FileUtil.isFileExit(channelFile)){
                    FileUtil.createNewFile(channelFilePath);
                }
                IOUtil.writeString2File(link, channelFilePath);
                zipFile.addFile(channelFile, parameters);
                channelFile.delete();
            }catch (Exception e){
                Utils.printStackTrace(e);
            }
        }
    }

}
