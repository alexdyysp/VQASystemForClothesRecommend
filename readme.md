# Voice Question Answering System For Clothes Recommendation

## 环境准备

1. 在com.audioTechAPI中填写APPID、APISecret、APIKey，可到讯飞云的控制台-我的应用-语音听写（流式版）页面获取

2. 安装node.js，打开cmd，进入demo目录，执行如下命令

 ```cmd
 npm install
 npm run dev
 ```
3. 在Maven中导入pom.xml文件，下载好各种package
4. 配置好application.yml中的neo4j和Mysql数据库地址和端口

## 项目运行

整个服务的运行入口是，com.web中的DyTest

在浏览器中访问localhost:8080，即可进入Web页面

## 项目架构

![1593068334586](https://github.com/alexdyysp/VQASystemForClothesRecommend/blob/master/img/archt.JPG)



## 技术整合

整合了讯飞云语音技术Neo4j知识图谱，实现了以服装推荐为主要目的的语音问答流程

- [x]  流式语音识别
- [x] 语音合成
- [x] 自动问答
- [x] 服装推荐
- [x] 知识图谱
