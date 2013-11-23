DROP TABLE IF EXISTS `tvt_teams`;
CREATE TABLE `tvt_teams` (
  `teamId` int(4) NOT NULL DEFAULT '0',
  `teamName` varchar(255) NOT NULL DEFAULT '',
  `teamX` int(11) NOT NULL DEFAULT '0',
  `teamY` int(11) NOT NULL DEFAULT '0',
  `teamZ` int(11) NOT NULL DEFAULT '0',
  `teamColor` varchar(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`teamId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `tvt_teams` VALUES ('0', 'Blue', '148695', '46725', '-3414', '0000FF');
INSERT INTO `tvt_teams` VALUES ('1', 'Red', '149999', '46728', '-3414', 'FF0000');