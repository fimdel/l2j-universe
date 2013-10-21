CREATE TABLE loginserv_log (
act_time int unsigned NOT NULL default '0',
log_id SMALLINT unsigned NOT NULL default '0',
etc_str1 varchar(50),
etc_str2 varchar(50),
etc_str3 varchar(100),
etc_num1 int NOT NULL default '0',
etc_num2 int NOT NULL default '0'
) ENGINE=ARCHIVE;