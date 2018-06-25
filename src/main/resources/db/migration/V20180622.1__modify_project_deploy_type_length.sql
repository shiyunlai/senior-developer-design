ALTER TABLE s_project MODIFY deploy_type varchar(128) NOT NULL COMMENT '部署类型 : 该工程以什么样的形式部署到系统中
如： jar、war、ecd、epd';