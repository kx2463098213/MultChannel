美团多渠道打包的java实现。

clone 项目之后，首先需要使用跑gradle的 runnableJar任务生成Jar4MultChannel.jar
(Gradle Projects -> MultChannel -> other -> runnableJar)
(生成的jar文件路径：项目根目录/build/libs)

Jar4MultChannel.jar使用方式:

在jar文件同级目录下放置channel.txt和original.apk文件;

channel.txt文件中渠道号和游戏链接以汉字“分割”分开(SF0001分割http://api.49you.com/User/Connectlogin/loginGameAdv?platformid=9&newadv=1&advid=150681&subtype=0001);

original.apk是已经签名过的apk文件;

使用命令行先进入到jar所在的位置，然后运行命令 java -jar jar4MultChannel.jar 就可以坐等多渠道打包了。
