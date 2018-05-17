## 美团多渠道打包的 `java` 实现。

[`Jar` 包下载](https://github.com/kx2463098213/MultChannel/release/Jar4MultiChannel.jar)

### 下载代码并生成 `Jar` 包

`clone` 项目之后，首先需要运行 `runnableJar` 任务生成 `Jar4MultiChannel.jar`

（**Gradle Projects** -> **MultiChannel** -> **other** -> **runnableJar**）

（生成的 `jar` 文件位置：`rootProject/build/libs`）

`Jar4MultiChannel.jar `使用方式: 命令行使用 `-h` 查看参数说明。

> 注：
1、jar 包使用对参数顺序并无要求；  
2、不使用任何参数的情况下，需要保证和 jar 包相同的目录下存在 original.apk 和 channel.txt 文件；  
3、存放渠道号的 txt 文件中的渠道号按行分割；  
4、原始 apk 文件必须使用的是 v1 签名；  

### `Android` 项目中读取渠道号

代码如下：

```java
private static void getChannelDate(Context ctx){
    if (ctx == null){
        LogUtil.e("getChannelDate context is null.");
        return;
    }

    ApplicationInfo info = ctx.getApplicationInfo();
    String sourceDir = info.sourceDir;
    ZipFile zipFile = null;
    try {
        zipFile = new ZipFile(sourceDir);
        Enumeration entries = zipFile.entries();
        while (entries.hasMoreElements()){
            ZipEntry entry = (ZipEntry) entries.nextElement();
            String entryName = entry.getName();
            if (entryName.startsWith("META-INF/channel_")){
                LogUtil.d("getChannelDate entryName: " + entryName);
                channelName = entryName.replace("META-INF/channel_", "");
                break;
            }
        }
    }catch (Exception e){
        LogUtil.e(e);
    }finally {
        try {
            if (zipFile != null){
                zipFile.close();
            }
        } catch (IOException e) {
            LogUtil.e(e);
        }
    }
}
```