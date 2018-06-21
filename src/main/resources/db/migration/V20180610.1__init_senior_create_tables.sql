SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Tables */

DROP TABLE IF EXISTS s_branch_mapping;
DROP TABLE IF EXISTS s_branch;
DROP TABLE IF EXISTS s_delivery_list;
DROP TABLE IF EXISTS s_merge_list;
DROP TABLE IF EXISTS s_patch;
DROP TABLE IF EXISTS s_delivery;
DROP TABLE IF EXISTS s_notice;
DROP TABLE IF EXISTS s_profiles;
DROP TABLE IF EXISTS s_project;
DROP TABLE IF EXISTS s_svn_account;
DROP TABLE IF EXISTS s_workitem;
-- 程序文件
DROP TABLE IF EXISTS s_program;
-- 代码提交历史
DROP TABLE IF EXISTS s_program_commit;




/* Create Tables */

-- 分支 : 开发分支
-- 1、某个工作项对应唯一的开发分支
-- 2、某个环境根据需要可以更换分支（如：SIT曾经使用过3个分支）
--
CREATE TABLE s_branch
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- F 特性分支，普通功能/项目对一个的分支
	-- H hot分支，修复生产bug，或开发紧急投产内容的分支
	-- R release分支
	branch_type char(1) DEFAULT 'F' NOT NULL COMMENT '分支类型 : F 特性分支，普通功能/项目对一个的分支
H hot分支，修复生产bug，或开发紧急投产内容的分支
R release分支',
	-- 冗余设计
	full_path varchar(1024) NOT NULL COMMENT '代码全路径 : 冗余设计',
	-- 分支创建人
	creater varchar(32) NOT NULL COMMENT '创建人 : 分支创建人',
	create_time timestamp NOT NULL COMMENT '创建时间',
	-- 创建这个分支的目的说明
	branch_for varchar(1024) NOT NULL COMMENT '分支作用说明 : 创建这个分支的目的说明',
	-- 分支的当前版本号
	curr_version int(8) NOT NULL COMMENT '分支当前版本 : 分支的当前版本号',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '分支 : 开发分支
1、某个工作项对应唯一的开发分支
2、某个环境根据需要可以更换分支（如：SIT曾经使用过3个分支）
';


-- 分支用途对照 : 记录了分支与开发目的之间的对应关系
-- 每次指定记录为一条记录
CREATE TABLE s_branch_mapping
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 唯一标示某条数据（自增长）
	guid_branch int(11) NOT NULL COMMENT '分支的GUID : 唯一标示某条数据（自增长）',
	-- W  为开发项（Workitem）
	-- R   为运行环境发版（Release）
	for_what char(1) NOT NULL COMMENT '为何而创建分支 : W  为开发项（Workitem）
R   为运行环境发版（Release）',
	-- 工作项/运行环境的GUID
	guid_of_whats int(11) NOT NULL COMMENT '何的guid : 工作项/运行环境的GUID',
	allot_time timestamp NOT NULL COMMENT '指配时间',
	-- 1 生效
	-- 0 不再生效
	status char(1) COMMENT '状态 : 1 生效
0 不再生效',
	PRIMARY KEY (guid),
	UNIQUE (guid),
	UNIQUE (guid_branch)
) COMMENT = '分支用途对照 : 记录了分支与开发目的之间的对应关系
每次指定记录为一条记录';


-- 投放申请 : 记录某个工作项的投放记录
-- 每次投放唯一对应一个投放申请（不对申请重复处理，但是可以提供 复制投放申请功能）
CREATE TABLE s_delivery
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 申请人为本次申请唯一设定的名称，用来自我识别
	apply_alias varchar(256) COMMENT '申请别名 : 申请人为本次申请唯一设定的名称，用来自我识别',
	-- 投放了那个功能
	guid_workitem int(11) NOT NULL COMMENT '工作项GUID : 投放了那个功能',
	-- 投放到了哪个环境
	guid_profiles int(11) NOT NULL COMMENT '运行环境GUID : 投放到了哪个环境',
	-- 提出投放申请的开发人员
	proposer varchar(32) NOT NULL COMMENT '投放申请人 : 提出投放申请的开发人员',
	apply_time timestamp NOT NULL COMMENT '提出申请时间',
	-- 对投放结果的说明，如：因为合并代码与申请中投放代码数量不符，RCT投放失败，此处说明该原因
	delivery_desc varchar(1024) NOT NULL COMMENT '投放说明 : 对投放结果的说明，如：因为合并代码与申请中投放代码数量不符，RCT投放失败，此处说明该原因',
	-- 谁处理了这个投放申请，一般记录RCT小组成员
	deliver varchar(32) COMMENT '投放处理人 : 谁处理了这个投放申请，一般记录RCT小组成员',
	-- 本环境对应的打包窗口时间，用业务字典来实现
	-- 如：SIT_PACK_TIMING，其中字典值为，09:00、12:30、16:00
	pack_timing varchar(64) NOT NULL COMMENT '打包窗口 : 本环境对应的打包窗口时间，用业务字典来实现
如：SIT_PACK_TIMING，其中字典值为，09:00、12:30、16:00',
	delivery_time timestamp COMMENT '投放时间',
	-- 0 申请中
	-- S 成功
	-- F 失败
	-- C 取消投放（功能没有投放）
	--
	delivery_result char(1) NOT NULL COMMENT '投放结果 : 0 申请中
S 成功
F 失败
C 取消投放（功能没有投放）
',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '投放申请 : 记录某个工作项的投放记录
每次投放唯一对应一个投放申请（不对申请重复处理，但是可以提供 复制投放申请功能）';


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
	-- 分支创建好时的版本
	start_version int(8) NOT NULL COMMENT '起始版本 : 分支创建好时的版本',
	-- 经过开发后的当前版本
	curr_version int(8) NOT NULL COMMENT '当前最新版本 : 经过开发后的当前版本',
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
	-- 冗余设计
	full_path varchar(1024) COMMENT '代码全路径 : 冗余设计',
	-- 记录该代码所在的工程名称（s_project.project_name）
	-- 冗余设计
	part_of_project varchar(256) COMMENT '代码所在工程 : 记录该代码所在的工程名称（s_project.project_name）
冗余设计',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '投产代码清单 : （开发人员提出）投放申请，其中包括哪些程序文件';


-- 合并代码清单 : （RCT人员合并开发分支）其中合并了哪些程序文件
CREATE TABLE s_merge_list
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 唯一标示某条数据（自增长）
	guid_delivery int(11) NOT NULL COMMENT '投放申请GUID : 唯一标示某条数据（自增长）',
	-- 来源分支
	guid_from_branch int(11) NOT NULL COMMENT '被合并分支 : 来源分支',
	-- 冗余字段，减少查询关联
	from_branch_path varchar(128) COMMENT '被合并分支路径 : 冗余字段，减少查询关联',
	-- 代码合并入分支
	guid_to_branch int(11) NOT NULL COMMENT '接收合并分支 : 代码合并入分支',
	-- 冗余字段，减少查询关联
	to_branch_path varchar(128) COMMENT '接受合并分支路径 : 冗余字段，减少查询关联',
	-- 冗余设计
	program_name varchar(256) NOT NULL COMMENT '程序名称 : 冗余设计',
	-- 记录开发人员的svn账号
	developer varchar(64) NOT NULL COMMENT '开发人员 : 记录开发人员的svn账号',
	merge_time timestamp NOT NULL COMMENT '合并时间',
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


-- 通知记录 : 记录开发过程中，各工作项流转过程中的各种通知记录
CREATE TABLE s_notice
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 事件描述，由事件的发起点记录
	-- 如：提交代码、投放申请、合并代码、功能项已投产
	event_desc varchar(128) NOT NULL COMMENT '事件信息 : 事件描述，由事件的发起点记录
如：提交代码、投放申请、合并代码、功能项已投产',
	sender varchar(128) COMMENT '发出人',
	-- 如果一个事件要通知多人，会生成多条记录
	to_who varchar(128) COMMENT '给谁的消息 : 如果一个事件要通知多人，会生成多条记录',
	-- 通过batch_no标记发送多个人的同一条消息，系统为每个接收人生成一条消息
	batch_no varchar(128) NOT NULL COMMENT '消息批次号 : 通过batch_no标记发送多个人的同一条消息，系统为每个接收人生成一条消息',
	-- 0 新生成
	-- S 已发送
	-- B 已查看（在本系统中点击后，置状态）
	status char(1) NOT NULL COMMENT '消息状态 : 0 新生成
S 已发送
B 已查看（在本系统中点击后，置状态）',
	-- 如：开发人员提出投放申请后，发送通知给RCT人员，则此字段记录工作项、投放申请 两个GUID。
	-- 假设以json的方式，记录清楚本次通知中需要告知的对象信息
	-- [
	--    {“guid”:”123124”, “obj”:”s_branch”},
	--    {“guid”:”123123”, “obj”:”s_branch”},
	-- ]
	notice_obj_guids varchar(1024) NOT NULL COMMENT '通知关联的对象guid : 如：开发人员提出投放申请后，发送通知给RCT人员，则此字段记录工作项、投放申请 两个GUID。
假设以json的方式，记录清楚本次通知中需要告知的对象信息
[
   {“guid”:”123124”, “obj”:”s_branch”},
   {“guid”:”123123”, “obj”:”s_branch”},
]',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '通知记录 : 记录开发过程中，各工作项流转过程中的各种通知记录';


-- 补丁 : 根据投放申请制作出来的补丁
CREATE TABLE s_patch
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 标示该补丁属于哪个投放申请
	guid_delivery int(11) NOT NULL COMMENT '投放申请GUID : 标示该补丁属于哪个投放申请',
	patch_time timestamp NOT NULL COMMENT '补丁制作时间',
	--
	--
	patcher varchar(64) NOT NULL COMMENT '补丁制作人 :
',
	patch_name varchar(256) NOT NULL COMMENT '补丁名称',
	-- 补丁被投放到对应环境的时间
	deploy_time timestamp COMMENT '部署时间 : 补丁被投放到对应环境的时间',
	-- 补丁部署到了哪个环境，则存储该环境的GUID
	deploy_to_profiles int(11) COMMENT '部署到什么环境 : 补丁部署到了哪个环境，则存储该环境的GUID',
	-- 0 init待部署（默认，补丁制作好后初始状态为0-init待部署）
	-- 1 部署成功
	-- 2 部署失败
	-- C 取消部署
	deploy_result char(1) NOT NULL COMMENT '部署结果 : 0 init待部署（默认，补丁制作好后初始状态为0-init待部署）
1 部署成功
2 部署失败
C 取消部署',
	-- 对部署状态的描述
	deploy_desc varchar(1024) COMMENT '部署说明 : 对部署状态的描述',
	deployer varchar(64) COMMENT '部署人',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '补丁 : 根据投放申请制作出来的补丁';


-- 运行环境 : 记录有哪些验证环境，如：SIT、SIT_DEV、UAT…. PP、生产
CREATE TABLE s_profiles
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 建议大写字母，如：:SIT、SIT_DEV、UAT
	profiles_code varchar(8) NOT NULL COMMENT '环境代码 : 建议大写字母，如：:SIT、SIT_DEV、UAT',
	profiles_name varchar(64) NOT NULL COMMENT '环境名称',
	-- 环境对应的服务器IP地址
	host_ip varchar(64) NOT NULL COMMENT '主机ip : 环境对应的服务器IP地址',
	-- 指系统所在的安装路径
	install_path varchar(256) COMMENT '安装路径 : 指系统所在的安装路径',
	-- 如：登录对应分支的svn账号
	csv_user varchar(128) NOT NULL COMMENT '版本控制用户 : 如：登录对应分支的svn账号',
	csv_pwd varchar(128) NOT NULL COMMENT '版本控制密码',
	-- 由RCT组人员控制，对开发人员开放投产窗口
	-- 1 允许向本环境提交投放申请
	-- 0 不允许
	is_allow_delivery char(1) NOT NULL COMMENT '是否允许投放 : 由RCT组人员控制，对开发人员开放投产窗口
1 允许向本环境提交投放申请
0 不允许',
	manager varchar(32) NOT NULL COMMENT '环境管理人员',
	-- 本环境对应的打包窗口时间，用业务字典来实现
	-- 如：SIT_PACK_TIMING，其中字典值为，09:00、12:30、16:00
	pack_timing varchar(64) NOT NULL COMMENT '打包窗口 : 本环境对应的打包窗口时间，用业务字典来实现
如：SIT_PACK_TIMING，其中字典值为，09:00、12:30、16:00',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '运行环境 : 记录有哪些验证环境，如：SIT、SIT_DEV、UAT…. PP、生产';


-- 工程 : 记录了TIP中所有的工程，以及布丁形式，和可部署的位置信息
CREATE TABLE s_project
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	--
	--
	project_name varchar(256) NOT NULL COMMENT '工程名称 :
',
	-- 该工程以什么样的形式部署到系统中
	-- 如： jar、war、ecd、epd
	deploy_type char(8) NOT NULL COMMENT '部署类型 : 该工程以什么样的形式部署到系统中
如： jar、war、ecd、epd',
	-- 该工程可以部署到哪些子系统
	-- 用json的方式存储，前端解析后，提供多选
	deploy_where varchar(512) NOT NULL COMMENT '部署到哪里 : 该工程可以部署到哪些子系统
用json的方式存储，前端解析后，提供多选',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '工程 : 记录了TIP中所有的工程，以及布丁形式，和可部署的位置信息';


-- svn账号
CREATE TABLE s_svn_account
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	-- 登录用户
	user_id varchar(32) NOT NULL COMMENT '用户 : 登录用户',
	svn_user varchar(128) NOT NULL COMMENT 'svn账号',
	svn_pwd varchar(128) NOT NULL COMMENT 'svn密码',
	role varchar(12) COMMENT '角色',
	PRIMARY KEY (guid),
	UNIQUE (guid),
	UNIQUE (user_id)
) COMMENT = 'svn账号';


-- 工作项 : 某个需要开发的需求或项目
-- SALM管理流程：一个工作项对应一个唯一的开发分支（如果开发组内要多分枝，请自行g
CREATE TABLE s_workitem
(
	-- 唯一标示某条数据（自增长）
	guid int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id : 唯一标示某条数据（自增长）',
	item_name varchar(256) NOT NULL COMMENT '工作项名称',
	-- 开发内容对应的需求编号（有需求编号的才进入系统）
	seqno varchar(128) COMMENT '需求编号 : 开发内容对应的需求编号（有需求编号的才进入系统）',
	-- 一个工作项可有多个开发人员
	developers varchar(32) COMMENT '开发人员 : 一个工作项可有多个开发人员',
	-- 本工作项的负责人（对需求、测试、最终投产负责）
	owner varchar(32) COMMENT '工作项负责人 : 本工作项的负责人（对需求、测试、最终投产负责）',
	-- 工作项对应的需求简述
	requirement_desc varchar(1024) NOT NULL COMMENT '需求描述 : 工作项对应的需求简述',
	receive_time timestamp COMMENT '收到需求时间',
	develop_start_time timestamp COMMENT '启动开发时间',
	delivery_plan_time timestamp COMMENT '计划投产时间',
	delivery_time timestamp COMMENT '实际投产时间',
	-- 0 开发中
	-- 1 已投产 （ 不能再提交投放申请）
	-- 2 已取消 （新建后，不再使用）
	item_status char(1) NOT NULL COMMENT '工作项状态 : 0 开发中
1 已投产 （ 不能再提交投放申请）
2 已取消 （新建后，不再使用）',
	PRIMARY KEY (guid),
	UNIQUE (guid)
) COMMENT = '工作项 : 某个需要开发的需求或项目
SALM管理流程：一个工作项对应一个唯一的开发分支（如果开发组内要多分枝，请自行g';



/* Create Foreign Keys */

ALTER TABLE s_branch_mapping
	ADD FOREIGN KEY (guid_branch)
	REFERENCES s_branch (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE s_delivery_list
	ADD FOREIGN KEY (guid_delivery)
	REFERENCES s_delivery (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE s_merge_list
	ADD FOREIGN KEY (guid_delivery)
	REFERENCES s_delivery (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE s_patch
	ADD FOREIGN KEY (guid_delivery)
	REFERENCES s_delivery (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE s_delivery
	ADD FOREIGN KEY (guid_profiles)
	REFERENCES s_profiles (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;


ALTER TABLE s_delivery
	ADD FOREIGN KEY (guid_workitem)
	REFERENCES s_workitem (guid)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



