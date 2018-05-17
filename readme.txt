美团多渠道打包的java实现。

clone 项目之后，首先需要使用跑gradle的 runnableJar任务生成Jar4MultiChannel.jar
(Gradle Projects -> MultiChannel -> other -> runnableJar)
(生成的jar文件路径：项目根目录/build/libs)

Jar4MultiChannel.jar使用方式: 命令行使用 -h 查看参数说明。

注：
1、jar 包使用对参数顺序并无要求；
2、不使用任何参数的情况下，需要保证和 jar 包相同的目录下存在 original.apk 和 channel.txt 文件；
3、存放渠道号的 txt 文件中的渠道号按行分割；
4、原始 apk 文件必须使用的是 v1 签名；
