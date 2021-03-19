# 数据库代理转发java版本
[![AUR](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg)](https://github.com/luohuihua/port-forwarding)
[![](https://img.shields.io/badge/version-0.0.1-brightgreen.svg)](https://github.com/luohuihua/port-forwarding)
## 简介
- 有些情况不能直接连库，这项目提供数据库代理转发相关功能，可进行映射配置和白名单配置，监控管理，为了轻量直接使用sqlite数据库
- 使用一个socket端口和线程池进行聚合转发，但是需要唯一service name，因为是通过service name进行映射转发到不同的ip端口中

## 功能介绍：
-----------------------------------
- 转发管理：进行映射配置，白名单配置，服务器信息展示
- 监控管理：进行服务情况监控

## 技术栈/版本介绍：
- 所涉及的相关的技术有：
    - SpringBoot
    - jdk版本：1.8
    - 项目构建：Maven
    - 模板引擎：thymeleaf
    - 数据库： sqlite
    - JSON: fastjson
    - 持久层：mybatis
    - 安全框架：SpringSecurity
    - 服务器信息：oshi-core
    - 工具：lombok
- 部署方面：
    - 服务器：windows/linux
    - 服务端部署: jar包启动方式 java -jar port-forwarding-0.0.1.jar
