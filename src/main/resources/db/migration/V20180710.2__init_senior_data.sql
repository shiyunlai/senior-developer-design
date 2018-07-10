/*
SQLyog Ultimate v8.32
MySQL - 5.6.29-log
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;
insert into `s_svn_account` (`guid`, `user_id`, `svn_user`, `svn_pwd`, `role`) values('1','admin','系统管理员','a44bd662ac500f01a1db48d9ea4bcf8a','admin');
insert into `s_svn_account` (`guid`, `user_id`, `svn_user`, `svn_pwd`, `role`) values('2','ljh','李俊华','93f8b473aebf6400f9b192085896be8c','rct');
insert into `s_svn_account` (`guid`, `user_id`, `svn_user`, `svn_pwd`, `role`) values('3','zch','赵春海','379a909e5133d45ef84ea374b8605758','dev');
insert into `s_svn_account` (`guid`, `user_id`, `svn_user`, `svn_pwd`, `role`) values('4','fjg','付佳鸽','1cc5eb39e1b0d6a7a70b65eb7a85738f','rct');

/*
SQLyog Ultimate v8.32
MySQL - 5.6.29-log
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('1','bos.tis.bosfx','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user\'},{{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('2','bos.tis.bs','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user\'},{{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('3','bos.tis.bs.other','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user\'},{{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('4','bos.tis.bs.pad','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user\'},{{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('5','bos.tis.bs.stdata','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user\'},{{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('6','bos.tis.bs.validation','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user\'},{{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('7','bos.tis.tws.bmtrans','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('8','bos.tis.tws.cpxtrans','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('9','bos.tis.tws.cpxtrans.demand','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('10','bos.tis.tws.cpxtrans.deposit','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('11','bos.tis.tws.cpxtrans.help','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('12','bos.tis.tws.cpxtrans.largedeposit','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('13','bos.tis.tws.cpxtrans.lftz','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('14','bos.tis.tws.cpxtrans.shoolcard','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('15','bos.tis.tws.cpxtrans.suzhoucity','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('16','bos.tis.tws.crossborder','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('17','bos.tis.tws.hostTest','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('18','bos.tis.tws.internationalSettlements','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('19','bos.tis.tws.invoice_management','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('20','bos.tis.tws.jnwb','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('21','bos.tis.tws.mgold','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('22','bos.tis.tws.signterrace','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('23','bos.tis.tws.sysservice','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('24','bos.tis.tws.trans','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('25','bos.tis.tws.trans.citybank','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('26','bos.tis.tws.trans.fourth','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('27','bos.tis.tws.trans.third','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('28','bos.tis.tws.trans.third.modify','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('29','bos.tis.tws.twshzysx','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('30','bos.tis.tws.twsjnwb','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('31','bos.tis.tws.twsnbtc','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('32','bos.tis.tws.twsswift','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('33','bos.tis.tws.validation','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('34','bos.tis.tws.validation.resultUtil','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('35','bos.tis.tws.version.tools','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('36','com.primeton.ibs.common.bean','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('37','com.primeton.ibs.common.bizlogic','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('38','com.primeton.ibs.common.config','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('39','com.primeton.ibs.common.data','C','[{\'exportType\':\'jar,epd\',\'deployType\':\'tws.lib,tws.work.user,bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('40','com.primeton.ibs.common.datadict','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('41','com.primeton.ibs.common.gtcp','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('42','com.primeton.ibs.common.log','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('43','com.primeton.ibs.common.lru','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('44','com.primeton.ibs.common.message','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('45','com.primeton.ibs.common.transaction','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('46','com.primeton.ibs.config','S','[{\'exportType\':\'配置文件,sql,jar\',\'deployType\':\'bs.数据库,tws.lib,bs.config,tws.config,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('47','com.primeton.ibs.desktop.auth','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('48','com.primeton.ibs.desktop.extservice','C','[{\'exportType\':\'plugins,jar\',\'deployType\':\'tws.plugins,tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('49','com.primeton.ibs.desktop.infra.context','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('50','com.primeton.ibs.desktop.infra.sso','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('51','com.primeton.ibs.desktop.ui','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('52','com.primeton.ibs.desktop.ui.advisor','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('53','com.primeton.ibs.desktop.ui.application','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('54','com.primeton.ibs.desktop.ui.themes','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('55','com.primeton.ibs.jsh','C','null');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('56','com.primeton.ibs.mbean','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('57','com.primeton.ibs.resource.server','C','[{\'exportType\':\'jar\',\'deployType\':\'bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('58','com.primeton.ibs.stdata','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('59','com.primeton.ibs.studio.tws.ui','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('60','com.primeton.ibs.tws.bizdict','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('61','com.primeton.ibs.tws.bos.t24browser','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('62','com.primeton.ibs.tws.bsa','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('63','com.primeton.ibs.tws.command','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('64','com.primeton.ibs.tws.dcs','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('65','com.primeton.ibs.tws.desktop','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('66','com.primeton.ibs.tws.desktop.extservice','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('67','com.primeton.ibs.tws.desktopstub','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('68','com.primeton.ibs.tws.desktopwidget','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('69','com.primeton.ibs.tws.document','C','[{\'exportType\':\'jar,可执行jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('70','com.primeton.ibs.tws.ds','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('71','com.primeton.ibs.tws.facade','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('72','com.primeton.ibs.tws.form','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('73','com.primeton.ibs.tws.lru','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('74','com.primeton.ibs.tws.ptp','C','[{\'exportType\':\'plugins\',\'deployType\':\'tws.plugins\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('75','com.primeton.ibs.tws.runtime','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('76','com.primeton.ibs.tws.sinoImage','C','[{\'exportType\':\'jar,可执行jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('77','com.primeton.ibs.tws.transaction','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('78','com.primeton.ibs.tws.ui','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('79','com.primeton.ibs.validation','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('80','com.primeton.ibs.validation.tws','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('81','com.primeton.ibs.vm','C','[{\'exportType\':\'jar\',\'deployType\':\'tws.lib,bs.lib\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('82','governor','C','default');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('83','resourceservice','C','[{\'exportType\':\'epd\',\'deployType\':\'bs.work.user,tws.work.user\'},{\'exportType\':\'ecd\',\'deployType\':\'bs.work.user,tws.work.user\'}]');
insert into `s_project` (`guid`, `project_name`, `project_type`, `deploy_config`) values('84','default','D','[{\'exportType\':\'jar,plugins,epd,ecd,sql,配置文件,可执行jar,war\',\'deployType\':\'tws.lib,bs.lib,bs.work.user,tws.work.user,bs.数据库,bs.config,governor,tws.plugins\'}]');


insert into `s_profiles` (`guid`, `profiles_code`, `profiles_name`, `host_ip`, `install_path`, `csv_user`, `csv_pwd`, `is_allow_delivery`, `manager`, `pack_timing`) values('1','R9_SIT_DEV','R9_SIT_DEV','10.240.94.116','/tis_sa1/tisclient/SIT_DEV','tis_sa1','abcd1234','1','hmh','12:00,17:00');
insert into `s_profiles` (`guid`, `profiles_code`, `profiles_name`, `host_ip`, `install_path`, `csv_user`, `csv_pwd`, `is_allow_delivery`, `manager`, `pack_timing`) values('2','R9_SIT','R9_SIT','10.240.94.117','/tis_sa1/tisclient/SIT','tis_sa2','abcd1234','1','hmh','12:00,17:00');
insert into `s_profiles` (`guid`, `profiles_code`, `profiles_name`, `host_ip`, `install_path`, `csv_user`, `csv_pwd`, `is_allow_delivery`, `manager`, `pack_timing`) values('3','R9_UAT_DEV','R9_UAT_DEV','10.240.94.179','/tis_sa1/tisclient/UAT_DEV','tis_u1a1','1qaz2wsx','1','hmh','12:00,17:00');
insert into `s_profiles` (`guid`, `profiles_code`, `profiles_name`, `host_ip`, `install_path`, `csv_user`, `csv_pwd`, `is_allow_delivery`, `manager`, `pack_timing`) values('4','R9_UAT','R9_UAT','10.240.94.35','/tis_sa1/tisclient/UAT','tis_u2a1','abcd1234','1','hmh','09:00,12:00,17:00');
insert into `s_profiles` (`guid`, `profiles_code`, `profiles_name`, `host_ip`, `install_path`, `csv_user`, `csv_pwd`, `is_allow_delivery`, `manager`, `pack_timing`) values('5','R9_PP','R9_PP','10.240.94.119','/tis_sa1/tisclient/PP','tisppa2','abcd1234','1','hmh','09:00,12:00,17:00');
insert into `s_profiles` (`guid`, `profiles_code`, `profiles_name`, `host_ip`, `install_path`, `csv_user`, `csv_pwd`, `is_allow_delivery`, `manager`, `pack_timing`) values('6','R9_UAT2','R9_UAT2','10.240.93.139','/tis_sa1/tisclient/UAT2','tis','abcd1234','1','hmh','09:00,12:00,17:00');
insert into `s_profiles` (`guid`, `profiles_code`, `profiles_name`, `host_ip`, `install_path`, `csv_user`, `csv_pwd`, `is_allow_delivery`, `manager`, `pack_timing`) values('7','R16_SIT','R16_SIT','10.240.94.238','/tis_sa1/tisclient/R16_SIT','tis','abcd1234','1','hmh','09:00,12:00,17:00');
insert into `s_profiles` (`guid`, `profiles_code`, `profiles_name`, `host_ip`, `install_path`, `csv_user`, `csv_pwd`, `is_allow_delivery`, `manager`, `pack_timing`) values('8','R16_UAT','R16_UAT','10.240.93.59','/tis_sa1/tisclient/R16_UAT','tis','abcd1234','1','hmh','09:00,12:00,17:00');

