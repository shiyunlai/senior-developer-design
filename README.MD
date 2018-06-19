# BOS TIP项目组高级清单系统

## 开发目标

1、开发人员不再手工整理功能清单；
2、可追踪工作项投放状态；

## 本工程包括以下内容

>需求

	目录： docs/requirement

>数据库设计

	工具：eclipse ERMaster插件
	目录：docs/ermaster

>原型设计

	工具：（Axure 8）  
	目录：docs/axure

>数据库版本管理

	工具：Maven flyway
	目录：/src/main/resource/db/migration

>关键功能时序分析

	工具：eclipse AmaterasUML插件
	目录：docs/amaterasUML

>系统主要接口测试

	工具：（ editor-base rest client on IDEA）
	目录：/src/test/java

## 设计原则

>保持以下基于原则不变：

1、分支机制不变
2、保持原有工作规范（分支编号、代码注释、）
3、打包流程、程序、环境 不变
