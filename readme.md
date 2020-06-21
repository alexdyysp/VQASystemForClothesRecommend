## 准备

1. 在com.audioTechAPI中填写APPID、APISecret、APIKey，可到讯飞云的控制台-我的应用-语音听写（流式版）页面获取

2. 安装node.js，打开cmd，进入demo目录，执行如下命令

 ```cmd
 npm install
 npm run dev
 ```
3. 在Maven中导入pom.xml文件，下载好各种package
4. 配置好application.yml中的neo4j和Mysql数据库地址和端口

## 运行

整个服务的运行入口是，com.web中的DyTest

## 访问

在浏览器中访问localhost:8080，即可进入页面