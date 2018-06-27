DROP TABLE IF EXISTS s_merge_list;
DROP TABLE IF EXISTS s_check;
DROP TABLE IF EXISTS s_project;

-- 合并代码清单 : （RCT人员合并开发分支）其中合并了哪些程序文件
CREATE TABLE s_merge_list
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 来源分支
	guid_from_branch int(11) NOT NULL COMMENT '被合并分支 : 来源分支',
	-- 冗余字段，减少查询关联
	from_branch_path varchar(128) COMMENT '被合并分支路径 : 冗余字段，减少查询关联',
	-- 冗余字段，减少查询关联
	to_branch_path varchar(128) COMMENT '接受合并分支路径 : 冗余字段，减少查询关联',
	-- 冗余设计
	program_name varchar(256) NOT NULL COMMENT '程序名称 : 冗余设计',
	-- 记录开发人员的svn账号
	developer varchar(64) NOT NULL COMMENT '开发人员 : 记录开发人员的svn账号',
	-- A  新增 Add
	-- U  修改 Update
	-- D  删除 Delete
	--
	--
	merge_operator char(1) NOT NULL COMMENT '合并操作类型 : A  新增 Add
U  修改 Update
D  删除 Delete

',
	-- 记录代码在合并前的历史版本号（svn版本号）
	old_version int(8) NOT NULL COMMENT '提交前代码版本 : 记录代码在合并前的历史版本号（svn版本号）',
	-- 合并成功后，代码的新版本号
	new_version int(8) NOT NULL COMMENT '提交后新版本号 : 合并成功后，代码的新版本号',
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

CREATE TABLE s_check
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 每次核对后由服务端生成，如201806270900第一次核对，日期+窗口+第N次核对
	check_alias varchar(256) NOT NULL COMMENT '核对别名 : 每次核对后由服务端生成，如201806270900第一次核对，日期+窗口+第N次核对',
	-- 唯一标示某条数据（自增长）
	guid_profiles int(11) NOT NULL COMMENT '运行环境GUID : 唯一标示某条数据（自增长）',
	pack_timing varchar(64) NOT NULL COMMENT '打包窗口',
	check_date timestamp NOT NULL COMMENT '核对时间',
	-- F 核对错误
	-- S 核对成功
	check_status char NOT NULL COMMENT '核对状态 : F 核对错误
S 核对成功',
	check_user varchar(32) NOT NULL COMMENT '核对人员',
	PRIMARY KEY (guid)
) COMMENT = '投产核对';

-- 工程 : 记录了TIP中所有的工程，以及布丁形式，和可部署的位置信息
CREATE TABLE s_project
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	--
	--
	project_name varchar(256) NOT NULL COMMENT '工程名称 :
',
	project_type char NOT NULL COMMENT '工程类型',
	-- 该工程可以部署到哪些子系统
	-- 用json的方式存储，前端解析后，提供多选
	deploy_config varchar(1024) NOT NULL COMMENT '部署配置 : 该工程可以部署到哪些子系统
用json的方式存储，前端解析后，提供多选',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '工程 : 记录了TIP中所有的工程，以及布丁形式，和可部署的位置信息';

ALTER TABLE s_check
	ADD FOREIGN KEY (guid_profiles)
	REFERENCES s_profiles (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;