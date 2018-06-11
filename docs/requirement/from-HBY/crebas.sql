/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2018/6/5 9:30:22                             */
/*==============================================================*/


drop table if exists PACK_TASK;

drop table if exists PACK_TASK_DETAIL;

drop table if exists PACK_TASK_RECORD;

drop index PT_RD_INDEX_PRI on PT_RECORD_DETAIL;

drop table if exists PT_RECORD_DETAIL;

drop table if exists VERSION_LIST;

drop table if exists VERSION_LIST_DETAIL;

/*==============================================================*/
/* Table: PACK_TASK                                             */
/*==============================================================*/
create table PACK_TASK
(
   PACK_TASK_ID         varchar(16) not null,
   LIST_ID              varchar(16) comment '清单ID',
   SUBMIT_LIST_PERSON   varchar(32) comment '清单提交人',
   SUBMIT_TIME          timestamp,
   SUBMIT_BS            varchar(32) comment '需要上的BS',
   STATUS               char(1) comment '0-提交
            1-打包中
            2-打包完成
            3-已上环境
            4-失败',
   primary key (PACK_TASK_ID)
);

alter table PACK_TASK comment '打包任务表';

/*==============================================================*/
/* Table: PACK_TASK_DETAIL                                      */
/*==============================================================*/
create table PACK_TASK_DETAIL
(
   PT_DETAIL_ID         varchar(16) not null comment '打包任务详细ID',
   PACK_TASK_ID         varchar(16) comment '打包任务ID',
   DETAIL_ID            varchar(16) comment '清单详细项ID',
   CODE_SUB_PERSON      varchar(32) comment '代码提交人',
   CODE_SUB_TIME        timestamp comment '代码提交时间',
   CODE_BRANCH_VERSION  varchar(32) comment '分支的版本',
   CODE_MAIN_VERSION    varchar(16) comment '代码对应环境主干的版本',
   primary key (PT_DETAIL_ID)
);

alter table PACK_TASK_DETAIL comment '打包任务详细';

/*==============================================================*/
/* Table: PACK_TASK_RECORD                                      */
/*==============================================================*/
create table PACK_TASK_RECORD
(
   PACK_RECORD_ID       varchar(16) not null comment '打包记录ID',
   EXECUTANT_NAME       varchar(32) comment '执行人',
   EXECUTE_TIME         timestamp comment '执行时间',
   BS                   varchar(16) comment '上的BS',
   primary key (PACK_RECORD_ID)
);

alter table PACK_TASK_RECORD comment '打包记录(记录RCT打包的记录)';

/*==============================================================*/
/* Table: PT_RECORD_DETAIL                                      */
/*==============================================================*/
create table PT_RECORD_DETAIL
(
   PACK_RECORD_ID       varchar(16) comment '打包记录ID',
   PACK_TASK_ID         varchar(16) comment '打包任务ID'
);

alter table PT_RECORD_DETAIL comment '打包任务记录详细(记录RCT打包的详细)';

/*==============================================================*/
/* Index: PT_RD_INDEX_PRI                                       */
/*==============================================================*/
create index PT_RD_INDEX_PRI on PT_RECORD_DETAIL
(
   
);

/*==============================================================*/
/* Table: VERSION_LIST                                          */
/*==============================================================*/
create table VERSION_LIST
(
   LIST_ID              varchar(16) not null comment '清单ID',
   LIST_NAME            varchar(128) comment '清单名称',
   ASSIGNMENT_NO        varchar(32) comment '任务编号',
   DEVELOPMENT_NO       varchar(32) comment '开发编号',
   PLAN_ON_DATE         datetime comment '预计上线时间',
   primary key (LIST_ID)
);

alter table VERSION_LIST comment '清单列表';

/*==============================================================*/
/* Table: VERSION_LIST_DETAIL                                   */
/*==============================================================*/
create table VERSION_LIST_DETAIL
(
   DETAIL_ID            varchar(16) not null comment '详细清单ID',
   LIST_ID              varchar(16) comment '清单ID',
   MODIFY_PROJECT       varchar(64) comment '修改工程',
   EXPORT_TYPE          varchar(2) comment '导出类型',
   EXPORT_NAME          varchar(128) comment '导出what',
   EXPORT_LOCATION      varchar(32) comment '部署于where',
   CODE_LOCATION        varchar(512) comment '代码目录',
   BRANCH_EFFECT_VERSION varchar(32) comment '分支生效版本',
   BRANCH_OLD_VERSION   varchar(32) comment '修改前版本',
   CHECK_PERSON         varchar(32) comment '检查人',
   primary key (DETAIL_ID)
);

alter table VERSION_LIST_DETAIL comment '清单详细列表';

