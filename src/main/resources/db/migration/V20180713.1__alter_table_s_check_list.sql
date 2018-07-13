ALTER TABLE s_check_list DROP FOREIGN KEY s_check_list_ibfk_1;
ALTER TABLE s_check MODIFY guid int(11) NOT NULL auto_increment COMMENT '数据id : 唯一标示某条数据（自增长）';
ALTER TABLE s_check_list
  ADD CONSTRAINT s_check_list_ibfk_1
FOREIGN KEY (guid_check) REFERENCES s_check (guid);