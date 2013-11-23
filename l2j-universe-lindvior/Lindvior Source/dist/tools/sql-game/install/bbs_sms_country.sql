DROP TABLE IF EXISTS `bbs_sms_country`;
CREATE TABLE `bbs_sms_country` (
  `countryId` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(255) NOT NULL,
  PRIMARY KEY (`countryId`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

INSERT INTO `bbs_sms_country` VALUES ('1', 'Россия');
INSERT INTO `bbs_sms_country` VALUES ('2', 'Украина');
INSERT INTO `bbs_sms_country` VALUES ('3', 'Беларусь');
INSERT INTO `bbs_sms_country` VALUES ('4', 'Казахстан');
INSERT INTO `bbs_sms_country` VALUES ('5', 'Таджикистан');
INSERT INTO `bbs_sms_country` VALUES ('6', 'Киргизия');
INSERT INTO `bbs_sms_country` VALUES ('7', 'Латвия');
INSERT INTO `bbs_sms_country` VALUES ('8', 'Литва');
INSERT INTO `bbs_sms_country` VALUES ('9', 'Эстония');
INSERT INTO `bbs_sms_country` VALUES ('10', 'Израиль');
INSERT INTO `bbs_sms_country` VALUES ('11', 'Польша');
INSERT INTO `bbs_sms_country` VALUES ('12', 'Чехия');
INSERT INTO `bbs_sms_country` VALUES ('13', 'Армения');
INSERT INTO `bbs_sms_country` VALUES ('14', 'Азербайджан');
INSERT INTO `bbs_sms_country` VALUES ('15', 'Грузия');
INSERT INTO `bbs_sms_country` VALUES ('16', 'Болгария');