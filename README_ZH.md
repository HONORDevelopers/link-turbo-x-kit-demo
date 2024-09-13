#LINK Turbo X Kit示例代码

![Apache-2.0](https://img.shields.io/badge/license-Apache-blue)
![Open Source Love](https://img.shields.io/static/v1?label=Open%20Source&message=%E2%9D%A4%EF%B8%8F&color=green)
![Java Language](https://img.shields.io/badge/language-java-green.svg)

[English](README.md) | 中文

## 目录

 * [简介](#简介)
 * [环境要求](#环境要求)
 * [硬件要求](#硬件要求)
 * [开发准备](#开发准备)
 * [安装](#安装)
 * [技术支持](#技术支持)
 * [授权许可](#授权许可)

## 简介

本示例代码中，你将使用已创建的代码工程来调用荣耀网络加速服务（LINK Turbo X Kit）的接口。通过该工程，你将：
1.	学习如何在你的应用中使用增强的网络感知能力。
2.	学习如何在你的应用中使用增强的多网并发能力。

更多内容，请参见[业务简介](https://developer.honor.com/cn/docs/11004/guides/introduction)。

## 环境要求

推荐使用的安卓targetSdk版本为29及以上，JDK版本为1.8.211及以上。

## 硬件要求

安装有Windows 10/Windows 7操作系统的计算机（台式机或者笔记本）
带USB数据线的荣耀手机，用于业务调试。

## 开发准备

1.	注册荣耀账号，成为荣耀开发者。
2.	创建应用，启动接口。
3.	构建本示例代码，需要先把它导入安卓集成开发环境（Android Studio的版本为2021.2.1及以上）。然后从[荣耀开发者服务平台](https://developer.hihonor.com)下载应用的mcs-services.json文件，并添加到对应示例代码的app目录下（java语言为/javaapp/，kotlin语言为/KotlinApp/）。另外，需要生成签名证书指纹并将证书文件添加到项目中，然后将配置添加到build.gradle。详细信息，请参见[集成指南](https://developer.honor.com/cn/docs/11004/guides/app-registration)集成准备。


## 安装
* 方法1：在Android Studio中进行代码的编译构建。构建APK完成后，将APK安装到手机上，并调试APK。
* 方法2：在Android Studio中生成APK。使用ADB（Android Debug Bridge）工具通过adb install {YourPath/YourApp.apk} 命令将APK安装到手机，并调试APK。

## 技术支持

您可在[荣耀开发者社区](https://developer.hihonor.com/cn/forum/?navation=dh11614886576872095748%2F1)获取关于LINK Turbo X Kit的最新讯息，并与其他开发者交流见解。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/Honor-Developer/link-turbo-x-kit-demo/issues)，也欢迎您提交[Pull Request](https://github.com/Honor-Developer/link-turbo-x-kit-demo/pulls)。

开发过程遇到问题上[Stack Overflow](https://stackoverflow.com/questions/tagged/honor-developer-services?tab=Votes)，在`honor-developer-services`标签下提问，有荣耀研发专家在线一对一解决您的问题。

## 授权许可

该示例代码经过[Apache 2.0授权许可](http://www.apache.org/licenses/LICENSE-2.0)。
