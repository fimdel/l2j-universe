CREATE TABLE IF NOT EXISTS `bbs_teleport` (
	`TpId` int NOT NULL AUTO_INCREMENT ,
	`name` varchar(45) NOT NULL ,
	`charId` int(11) NOT NULL DEFAULT  '0',
	`xPos` int(9) NOT NULL DEFAULT  '0',
	`yPos` int(9) NOT NULL DEFAULT  '0',
	`zPos` int(9) NOT NULL DEFAULT  '0',
	PRIMARY KEY (`TpId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;  