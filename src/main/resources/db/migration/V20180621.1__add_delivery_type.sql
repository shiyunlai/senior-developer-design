ALTER TABLE s_delivery ADD merge_list varchar(512) NULL COMMENT '合并投放申请的id集合，以，分割';
ALTER TABLE s_delivery ADD delivery_type char NOT NULL COMMENT 'G 普通申请
M 合并申请';
ALTER TABLE s_delivery
  MODIFY COLUMN delivery_type char NOT NULL COMMENT 'G 普通申请
M 合并申请' AFTER guid_profiles;