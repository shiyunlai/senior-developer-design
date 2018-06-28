ALTER TABLE s_merge_list ADD part_of_project varchar(256) NOT NULL COMMENT '记录该代码所在的工程名称（s_project.project_name）
冗余设计';
ALTER TABLE s_merge_list
  MODIFY COLUMN part_of_project varchar(256) NOT NULL COMMENT '记录该代码所在的工程名称（s_project.project_name）
冗余设计' AFTER full_path;