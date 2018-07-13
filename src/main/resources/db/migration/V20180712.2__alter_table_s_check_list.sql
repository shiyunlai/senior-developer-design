ALTER TABLE s_check_list ADD guid_delivery_list int(11) NULL COMMENT '投产清单GUID';
ALTER TABLE s_check_list MODIFY guid_delivery int(11) COMMENT '投产申请GUID : 对应投产申请GUID';
ALTER TABLE s_check_list MODIFY guid_delivery_list int(11) NULL COMMENT '投产清单GUID' AFTER guid_delivery;