ALTER TABLE s_workitem ADD artf varchar(16) NULL COMMENT '提交标识';
ALTER TABLE s_workitem
  MODIFY COLUMN artf varchar(16) COMMENT '提交标识' AFTER seqno;

ALTER TABLE s_profiles ADD artf varchar(16) NULL COMMENT '提交标识';
ALTER TABLE s_profiles
  MODIFY COLUMN artf varchar(16) COMMENT '提交标识' AFTER profiles_name;