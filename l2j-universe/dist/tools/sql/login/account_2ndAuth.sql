CREATE TABLE IF NOT EXISTS `account_2ndAuth` (
  `account_name` VARCHAR(45) NOT NULL DEFAULT '',
  `account_password` VARCHAR(45) NOT NULL DEFAULT '',
  `wrongAttempts` int(11) NOT NULL DEFAULT '0',
  `banTime` bigint(15) unsigned NOT NULL DEFAULT '0' ,
  PRIMARY KEY (`account_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
