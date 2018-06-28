DROP TABLE IF EXISTS s_merge_list;
DROP TABLE IF EXISTS s_delivery_list;

-- 投产代码清单 : （开发人员提出）投放申请，其中包括哪些程序文件
CREATE TABLE s_delivery_list
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 某次投产申请
	--
	guid_delivery int(11) NOT NULL COMMENT '投放申请GUID : 某次投产申请
',
	-- 记录程序名称
	program_name varchar(256) NOT NULL COMMENT '程序名称 : 记录程序名称',
	-- 投放时该代码的svn版本
	-- 可以投当前版本，也可以投开发过程中的某个版本
	delivery_version int(8) NOT NULL COMMENT '投放版本 : 投放时该代码的svn版本
可以投当前版本，也可以投开发过程中的某个版本',
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
	deploy_where varchar(32) NOT NULL COMMENT '部署到',
	-- 冗余设计
	full_path varchar(1024) NOT NULL COMMENT '代码全路径 : 冗余设计',
	-- 记录该代码所在的工程名称（s_project.project_name）
	-- 冗余设计
	part_of_project varchar(256) NOT NULL COMMENT '代码所在工程 : 记录该代码所在的工程名称（s_project.project_name）
冗余设计',
	-- svn提交人
	author varchar(12) NOT NULL COMMENT '提交人员 : svn提交人',
	-- 提交日期
	commit_date timestamp NOT NULL COMMENT '提交日期 : 提交日期',
	-- 提交类型
	commit_type char NOT NULL COMMENT '提交类型 : 提交类型',
	developer_confirm char NOT NULL COMMENT '开发确认',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '投产代码清单 : （开发人员提出）投放申请，其中包括哪些程序文件';

-- 合并代码清单 : （RCT人员合并开发分支）其中合并了哪些程序文件
CREATE TABLE s_merge_list
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 唯一标示某条数据（自增长）
	check_guid int(11) NOT NULL COMMENT '核对GUID : 唯一标示某条数据（自增长）',
	-- 冗余设计
	program_name varchar(256) NOT NULL COMMENT '程序名称 : 冗余设计',
	-- 合并成功后，代码的新版本号
	merge_version int(8) NOT NULL COMMENT '提交后新版本号 : 合并成功后，代码的新版本号',
	full_path varchar(1024) COMMENT '代码全路径',
	author varchar(12) COMMENT '合并人员',
	merge_date timestamp COMMENT '合并时间',
	-- A  新增 Add
	-- U  修改 Update
	-- D  删除 Delete
	--
	--
	merge_type char(1) NOT NULL COMMENT '合并操作类型 : A  新增 Add
U  修改 Update
D  删除 Delete

',
	-- 合并后的代码都需要开发人员进行确认
	-- 0 待确认
	-- 1 确认
	-- 2 有异议（代码合并有问题，需要线下手工处理）
	developer_confirm char(1) NOT NULL COMMENT '开发确认 : 合并后的代码都需要开发人员进行确认
0 待确认
1 确认
2 有异议（代码合并有问题，需要线下手工处理）',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '合并代码清单 : （RCT人员合并开发分支）其中合并了哪些程序文件';

ALTER TABLE s_merge_list
	ADD FOREIGN KEY (check_guid)
	REFERENCES s_check (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE s_delivery_list
	ADD FOREIGN KEY (guid_delivery)
	REFERENCES s_delivery (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;