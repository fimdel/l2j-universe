CREATE TABLE IF NOT EXISTS `lock` (
	`login` varchar(45) NOT NULL,
	`type` ENUM('HWID','IP') NOT NULL,
	`string` varchar(32)  NOT NULL,
	PRIMARY KEY  (`login`,`string`)
) ENGINE=MyISAM;