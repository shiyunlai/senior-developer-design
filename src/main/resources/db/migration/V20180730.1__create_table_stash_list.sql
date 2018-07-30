-- 贮藏清单
CREATE TABLE s_stash_list
(
  -- 唯一标示某条数据（自增长）
  guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
  -- 唯一标示某条数据（自增长）
  guid_workitem int(11) NOT NULL COMMENT '工作项GUID : 唯一标示某条数据（自增长）',
  -- 冗余设计
  program_name varchar(256) NOT NULL COMMENT '程序名称 : 冗余设计',
  -- JAR 输出为jar包
  -- ECD 输出为ecd包
  -- EPD 输出为epd
  -- CFG 作为配置文件
  -- DBV 作为数据库脚本（SQL、DDL等数据库版本脚本）
  patch_type char(8) NOT NULL COMMENT '补丁类型 : JAR 输出为jar包
ECD 输出为ecd包
EPD 输出为epd
CFG 作为配置文件
DBV 作为数据库脚本（SQL、DDL等数据库版本脚本）',
  deploy_where varchar(256) NOT NULL COMMENT '部署到',
  full_path varchar(1024) NOT NULL COMMENT '代码全路径',
  -- 记录该代码所在的工程名称（s_project.project_name）
  -- 冗余设计
  part_of_project varchar(256) NOT NULL COMMENT '代码所在工程 : 记录该代码所在的工程名称（s_project.project_name）
冗余设计',
  -- 提交类型
  commit_type char NOT NULL COMMENT '提交类型 : 提交类型',
  PRIMARY KEY (guid),
  UNIQUE (guid)
) COMMENT = '贮藏清单';

ALTER TABLE s_stash_list
  ADD FOREIGN KEY (guid_workitem)
REFERENCES s_workitem (guid)
  ON UPDATE RESTRICT
  ON DELETE RESTRICT
;
