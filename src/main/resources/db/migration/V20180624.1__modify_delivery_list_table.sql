ALTER TABLE s_delivery_list ADD author varchar(12) NOT NULL COMMENT 'snv提交人';
ALTER TABLE s_delivery_list ADD commit_date timestamp NOT NULL COMMENT '提交日期';
ALTER TABLE s_delivery_list ADD commit_type char NULL COMMENT '提交类型';