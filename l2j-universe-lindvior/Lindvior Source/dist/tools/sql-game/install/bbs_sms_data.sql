DROP TABLE IF EXISTS `bbs_sms_data`;
CREATE TABLE `bbs_sms_data` (
  `dataId` int(11) NOT NULL AUTO_INCREMENT,
  `country` varchar(255) NOT NULL,
  `operator` varchar(255) NOT NULL,
  `phone` int(6) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `price_nds` decimal(10,2) NOT NULL,
  PRIMARY KEY (`dataId`)
) ENGINE=MyISAM AUTO_INCREMENT=419 DEFAULT CHARSET=utf8;

INSERT INTO `bbs_sms_data` VALUES ('1', 'Россия', 'Оренбург GSM', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('2', 'Россия', 'Оренбург GSM', '1131', '16.00', '18.88');
INSERT INTO `bbs_sms_data` VALUES ('3', 'Россия', 'Оренбург GSM', '1141', '27.00', '31.86');
INSERT INTO `bbs_sms_data` VALUES ('4', 'Россия', 'Оренбург GSM', '5013', '38.14', '45.00');
INSERT INTO `bbs_sms_data` VALUES ('5', 'Россия', 'Оренбург GSM', '1151', '39.22', '46.27');
INSERT INTO `bbs_sms_data` VALUES ('6', 'Россия', 'Оренбург GSM', '1161', '81.36', '96.00');
INSERT INTO `bbs_sms_data` VALUES ('7', 'Россия', 'Оренбург GSM', '5014', '120.34', '142.00');
INSERT INTO `bbs_sms_data` VALUES ('8', 'Россия', 'Оренбург GSM', '7781', '173.73', '205.00');
INSERT INTO `bbs_sms_data` VALUES ('9', 'Россия', 'Оренбург GSM', '5537', '173.73', '205.00');
INSERT INTO `bbs_sms_data` VALUES ('10', 'Россия', 'Оренбург GSM', '4124', '228.81', '270.00');
INSERT INTO `bbs_sms_data` VALUES ('11', 'Россия', 'Оренбург GSM', '4481', '228.81', '270.00');
INSERT INTO `bbs_sms_data` VALUES ('12', 'Россия', 'Оренбург GSM', '3747', '228.81', '270.00');

INSERT INTO `bbs_sms_data` VALUES ('13', 'Россия', 'МТС', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('14', 'Россия', 'МТС', '1121', '2.87', '3.38');
INSERT INTO `bbs_sms_data` VALUES ('15', 'Россия', 'МТС', '1131', '14.35', '16.93');
INSERT INTO `bbs_sms_data` VALUES ('16', 'Россия', 'МТС', '1141', '22.96', '27.09');
INSERT INTO `bbs_sms_data` VALUES ('17', 'Россия', 'МТС', '1151', '35.88', '42.33');
INSERT INTO `bbs_sms_data` VALUES ('18', 'Россия', 'МТС', '5013', '37.31', '44.02');
INSERT INTO `bbs_sms_data` VALUES ('19', 'Россия', 'МТС', '1899', '63.14', '74.50');
INSERT INTO `bbs_sms_data` VALUES ('20', 'Россия', 'МТС', '1161', '94.71', '111.75');
INSERT INTO `bbs_sms_data` VALUES ('21', 'Россия', 'МТС', '5014', '114.80', '135.46');
INSERT INTO `bbs_sms_data` VALUES ('22', 'Россия', 'МТС', '5537', '172.20', '203.19');
INSERT INTO `bbs_sms_data` VALUES ('23', 'Россия', 'МТС', '7781', '172.20', '203.19');
INSERT INTO `bbs_sms_data` VALUES ('24', 'Россия', 'МТС', '4124', '258.30', '304.79');
INSERT INTO `bbs_sms_data` VALUES ('25', 'Россия', 'МТС', '4481', '258.30', '304.79');
INSERT INTO `bbs_sms_data` VALUES ('26', 'Россия', 'МТС', '3747', '258.30', '304.79');

INSERT INTO `bbs_sms_data` VALUES ('27', 'Россия', 'UTEL', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('28', 'Россия', 'UTEL', '1121', '6.36', '7.50');
INSERT INTO `bbs_sms_data` VALUES ('29', 'Россия', 'UTEL', '1131', '9.32', '11.00');
INSERT INTO `bbs_sms_data` VALUES ('30', 'Россия', 'UTEL', '1141', '18.64', '22.00');
INSERT INTO `bbs_sms_data` VALUES ('31', 'Россия', 'UTEL', '1151', '30.51', '36.00');
INSERT INTO `bbs_sms_data` VALUES ('32', 'Россия', 'UTEL', '5013', '38.14', '45.00');
INSERT INTO `bbs_sms_data` VALUES ('33', 'Россия', 'UTEL', '1899', '60.17', '71.00');
INSERT INTO `bbs_sms_data` VALUES ('34', 'Россия', 'UTEL', '1161', '90.68', '107.00');
INSERT INTO `bbs_sms_data` VALUES ('35', 'Россия', 'UTEL', '5014', '122.88', '145.00');
INSERT INTO `bbs_sms_data` VALUES ('36', 'Россия', 'UTEL', '7781', '152.54', '180.00');
INSERT INTO `bbs_sms_data` VALUES ('37', 'Россия', 'UTEL', '3747', '152.54', '180.00');
INSERT INTO `bbs_sms_data` VALUES ('38', 'Россия', 'UTEL', '5537', '152.54', '180.00');
INSERT INTO `bbs_sms_data` VALUES ('39', 'Россия', 'UTEL', '4124', '260.17', '307.00');
INSERT INTO `bbs_sms_data` VALUES ('40', 'Россия', 'UTEL', '4481', '260.17', '307.00');

INSERT INTO `bbs_sms_data` VALUES ('41', 'Россия', 'TELE2', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('42', 'Россия', 'TELE2', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('43', 'Россия', 'TELE2', '1131', '14.00', '16.52');
INSERT INTO `bbs_sms_data` VALUES ('44', 'Россия', 'TELE2', '1141', '23.00', '27.14');
INSERT INTO `bbs_sms_data` VALUES ('45', 'Россия', 'TELE2', '1151', '34.70', '40.94');
INSERT INTO `bbs_sms_data` VALUES ('46', 'Россия', 'TELE2', '5013', '38.00', '44.84');
INSERT INTO `bbs_sms_data` VALUES ('47', 'Россия', 'TELE2', '1899', '76.00', '89.68');
INSERT INTO `bbs_sms_data` VALUES ('48', 'Россия', 'TELE2', '1161', '94.70', '111.74');
INSERT INTO `bbs_sms_data` VALUES ('49', 'Россия', 'TELE2', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('50', 'Россия', 'TELE2', '7781', '173.00', '204.14');
INSERT INTO `bbs_sms_data` VALUES ('51', 'Россия', 'TELE2', '5537', '173.00', '204.14');
INSERT INTO `bbs_sms_data` VALUES ('52', 'Россия', 'TELE2', '3747', '211.86', '250.00');
INSERT INTO `bbs_sms_data` VALUES ('53', 'Россия', 'TELE2', '4481', '211.86', '250.00');
INSERT INTO `bbs_sms_data` VALUES ('54', 'Россия', 'TELE2', '4124', '212.00', '250.16');

INSERT INTO `bbs_sms_data` VALUES ('55', 'Россия', 'Мегафон', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('56', 'Россия', 'Мегафон', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('57', 'Россия', 'Мегафон', '1131', '15.00', '17.70');
INSERT INTO `bbs_sms_data` VALUES ('58', 'Россия', 'Мегафон', '1141', '24.00', '28.32');
INSERT INTO `bbs_sms_data` VALUES ('59', 'Россия', 'Мегафон', '1151', '34.75', '41.00');
INSERT INTO `bbs_sms_data` VALUES ('60', 'Россия', 'Мегафон', '5013', '38.14', '45.00');
INSERT INTO `bbs_sms_data` VALUES ('61', 'Россия', 'Мегафон', '1899', '94.00', '110.92');
INSERT INTO `bbs_sms_data` VALUES ('62', 'Россия', 'Мегафон', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('63', 'Россия', 'Мегафон', '1161', '125.00', '147.50');
INSERT INTO `bbs_sms_data` VALUES ('64', 'Россия', 'Мегафон', '5537', '150.00', '177.00');
INSERT INTO `bbs_sms_data` VALUES ('65', 'Россия', 'Мегафон', '7781', '150.00', '177.00');
INSERT INTO `bbs_sms_data` VALUES ('66', 'Россия', 'Мегафон', '4481', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('67', 'Россия', 'Мегафон', '4124', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('68', 'Россия', 'Мегафон', '3747', '300.00', '354.00');

INSERT INTO `bbs_sms_data` VALUES ('69', 'Россия', 'МОТИВ', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('70', 'Россия', 'МОТИВ', '1121', '3.05', '3.59');
INSERT INTO `bbs_sms_data` VALUES ('71', 'Россия', 'МОТИВ', '1131', '16.95', '20.00');
INSERT INTO `bbs_sms_data` VALUES ('72', 'Россия', 'МОТИВ', '1141', '27.12', '32.00');
INSERT INTO `bbs_sms_data` VALUES ('73', 'Россия', 'МОТИВ', '5013', '38.00', '44.84');
INSERT INTO `bbs_sms_data` VALUES ('74', 'Россия', 'МОТИВ', '1151', '38.98', '46.00');
INSERT INTO `bbs_sms_data` VALUES ('75', 'Россия', 'МОТИВ', '1899', '74.58', '88.00');
INSERT INTO `bbs_sms_data` VALUES ('76', 'Россия', 'МОТИВ', '1161', '110.00', '129.80');
INSERT INTO `bbs_sms_data` VALUES ('77', 'Россия', 'МОТИВ', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('78', 'Россия', 'МОТИВ', '7781', '173.73', '205.00');
INSERT INTO `bbs_sms_data` VALUES ('79', 'Россия', 'МОТИВ', '5537', '173.73', '205.00');
INSERT INTO `bbs_sms_data` VALUES ('80', 'Россия', 'МОТИВ', '4481', '228.81', '270.00');
INSERT INTO `bbs_sms_data` VALUES ('81', 'Россия', 'МОТИВ', '4124', '228.81', '270.00');
INSERT INTO `bbs_sms_data` VALUES ('82', 'Россия', 'МОТИВ', '3747', '228.81', '270.00');

INSERT INTO `bbs_sms_data` VALUES ('83', 'Россия', 'Билайн', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('84', 'Россия', 'Билайн', '1121', '2.97', '3.50');
INSERT INTO `bbs_sms_data` VALUES ('85', 'Россия', 'Билайн', '1131', '18.64', '22.00');
INSERT INTO `bbs_sms_data` VALUES ('86', 'Россия', 'Билайн', '1141', '28.81', '34.00');
INSERT INTO `bbs_sms_data` VALUES ('87', 'Россия', 'Билайн', '5013', '37.29', '44.00');
INSERT INTO `bbs_sms_data` VALUES ('88', 'Россия', 'Билайн', '1151', '37.29', '44.00');
INSERT INTO `bbs_sms_data` VALUES ('89', 'Россия', 'Билайн', '1899', '65.91', '77.77');
INSERT INTO `bbs_sms_data` VALUES ('90', 'Россия', 'Билайн', '1161', '110.17', '130.00');
INSERT INTO `bbs_sms_data` VALUES ('91', 'Россия', 'Билайн', '5014', '118.64', '140.00');
INSERT INTO `bbs_sms_data` VALUES ('92', 'Россия', 'Билайн', '7781', '144.07', '170.00');
INSERT INTO `bbs_sms_data` VALUES ('93', 'Россия', 'Билайн', '5537', '144.07', '170.00');
INSERT INTO `bbs_sms_data` VALUES ('94', 'Россия', 'Билайн', '4481', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('95', 'Россия', 'Билайн', '4124', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('96', 'Россия', 'Билайн', '3747', '254.24', '300.00');

INSERT INTO `bbs_sms_data` VALUES ('97', 'Россия', 'БайкалВестКом', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('98', 'Россия', 'БайкалВестКом', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('99', 'Россия', 'БайкалВестКом', '1131', '16.95', '20.00');
INSERT INTO `bbs_sms_data` VALUES ('100', 'Россия', 'БайкалВестКом', '1141', '27.54', '32.49');
INSERT INTO `bbs_sms_data` VALUES ('101', 'Россия', 'БайкалВестКом', '5013', '38.00', '44.84');
INSERT INTO `bbs_sms_data` VALUES ('102', 'Россия', 'БайкалВестКом', '1151', '38.98', '46.00');
INSERT INTO `bbs_sms_data` VALUES ('103', 'Россия', 'БайкалВестКом', '1899', '65.00', '76.70');
INSERT INTO `bbs_sms_data` VALUES ('104', 'Россия', 'БайкалВестКом', '1161', '110.00', '129.80');
INSERT INTO `bbs_sms_data` VALUES ('105', 'Россия', 'БайкалВестКом', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('106', 'Россия', 'БайкалВестКом', '4124', '169.49', '200.00');
INSERT INTO `bbs_sms_data` VALUES ('107', 'Россия', 'БайкалВестКом', '3747', '172.88', '204.00');
INSERT INTO `bbs_sms_data` VALUES ('108', 'Россия', 'БайкалВестКом', '7781', '172.88', '204.00');
INSERT INTO `bbs_sms_data` VALUES ('109', 'Россия', 'БайкалВестКом', '4481', '172.88', '204.00');
INSERT INTO `bbs_sms_data` VALUES ('110', 'Россия', 'БайкалВестКом', '5537', '172.88', '204.00');

INSERT INTO `bbs_sms_data` VALUES ('111', 'Россия', 'НТК', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('112', 'Россия', 'НТК', '1121', '2.90', '3.42');
INSERT INTO `bbs_sms_data` VALUES ('113', 'Россия', 'НТК', '1131', '14.50', '17.11');
INSERT INTO `bbs_sms_data` VALUES ('114', 'Россия', 'НТК', '1141', '21.75', '25.66');
INSERT INTO `bbs_sms_data` VALUES ('115', 'Россия', 'НТК', '5013', '37.30', '44.01');
INSERT INTO `bbs_sms_data` VALUES ('116', 'Россия', 'НТК', '1151', '37.70', '44.48');
INSERT INTO `bbs_sms_data` VALUES ('117', 'Россия', 'НТК', '1899', '72.71', '85.79');
INSERT INTO `bbs_sms_data` VALUES ('118', 'Россия', 'НТК', '1161', '101.50', '119.77');
INSERT INTO `bbs_sms_data` VALUES ('119', 'Россия', 'НТК', '5014', '116.00', '136.88');
INSERT INTO `bbs_sms_data` VALUES ('120', 'Россия', 'НТК', '7781', '145.00', '171.10');
INSERT INTO `bbs_sms_data` VALUES ('121', 'Россия', 'НТК', '5537', '145.00', '171.10');
INSERT INTO `bbs_sms_data` VALUES ('122', 'Россия', 'НТК', '3747', '260.00', '306.80');
INSERT INTO `bbs_sms_data` VALUES ('123', 'Россия', 'НТК', '4124', '260.00', '306.80');
INSERT INTO `bbs_sms_data` VALUES ('124', 'Россия', 'НТК', '4481', '260.00', '306.80');

INSERT INTO `bbs_sms_data` VALUES ('125', 'Россия', 'СМАРТС', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('126', 'Россия', 'СМАРТС', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('127', 'Россия', 'СМАРТС', '1131', '16.95', '20.00');
INSERT INTO `bbs_sms_data` VALUES ('128', 'Россия', 'СМАРТС', '1141', '22.00', '25.96');
INSERT INTO `bbs_sms_data` VALUES ('129', 'Россия', 'СМАРТС', '5013', '38.14', '45.00');
INSERT INTO `bbs_sms_data` VALUES ('130', 'Россия', 'СМАРТС', '1151', '38.14', '45.00');
INSERT INTO `bbs_sms_data` VALUES ('131', 'Россия', 'СМАРТС', '1899', '74.70', '88.14');
INSERT INTO `bbs_sms_data` VALUES ('132', 'Россия', 'СМАРТС', '5014', '120.34', '142.00');
INSERT INTO `bbs_sms_data` VALUES ('133', 'Россия', 'СМАРТС', '1161', '129.00', '152.22');
INSERT INTO `bbs_sms_data` VALUES ('134', 'Россия', 'СМАРТС', '5537', '200.00', '236.00');
INSERT INTO `bbs_sms_data` VALUES ('135', 'Россия', 'СМАРТС', '7781', '200.00', '236.00');
INSERT INTO `bbs_sms_data` VALUES ('136', 'Россия', 'СМАРТС', '4124', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('137', 'Россия', 'СМАРТС', '3747', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('138', 'Россия', 'СМАРТС', '4481', '254.24', '300.00');

INSERT INTO `bbs_sms_data` VALUES ('139', 'Россия', 'Дельта Телеком (skylink)', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('140', 'Россия', 'Дельта Телеком (skylink)', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('141', 'Россия', 'Дельта Телеком (skylink)', '1131', '14.00', '16.52');
INSERT INTO `bbs_sms_data` VALUES ('142', 'Россия', 'Дельта Телеком (skylink)', '1141', '23.00', '27.14');
INSERT INTO `bbs_sms_data` VALUES ('143', 'Россия', 'Дельта Телеком (skylink)', '1151', '35.00', '41.30');
INSERT INTO `bbs_sms_data` VALUES ('144', 'Россия', 'Дельта Телеком (skylink)', '5013', '38.00', '44.84');
INSERT INTO `bbs_sms_data` VALUES ('145', 'Россия', 'Дельта Телеком (skylink)', '1899', '65.00', '76.70');
INSERT INTO `bbs_sms_data` VALUES ('146', 'Россия', 'Дельта Телеком (skylink)', '1161', '95.00', '112.10');
INSERT INTO `bbs_sms_data` VALUES ('147', 'Россия', 'Дельта Телеком (skylink)', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('148', 'Россия', 'Дельта Телеком (skylink)', '7781', '155.00', '182.90');
INSERT INTO `bbs_sms_data` VALUES ('149', 'Россия', 'Дельта Телеком (skylink)', '5537', '155.00', '182.90');
INSERT INTO `bbs_sms_data` VALUES ('150', 'Россия', 'Дельта Телеком (skylink)', '4124', '230.00', '271.40');
INSERT INTO `bbs_sms_data` VALUES ('151', 'Россия', 'Дельта Телеком (skylink)', '3747', '300.00', '354.00');
INSERT INTO `bbs_sms_data` VALUES ('152', 'Россия', 'Дельта Телеком (skylink)', '4481', '300.00', '354.00');

INSERT INTO `bbs_sms_data` VALUES ('153', 'Россия', 'АКОС', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('154', 'Россия', 'АКОС', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('155', 'Россия', 'АКОС', '1131', '14.00', '16.52');
INSERT INTO `bbs_sms_data` VALUES ('156', 'Россия', 'АКОС', '1141', '23.00', '27.14');
INSERT INTO `bbs_sms_data` VALUES ('157', 'Россия', 'АКОС', '1151', '35.00', '41.30');
INSERT INTO `bbs_sms_data` VALUES ('158', 'Россия', 'АКОС', '5013', '38.00', '44.84');
INSERT INTO `bbs_sms_data` VALUES ('159', 'Россия', 'АКОС', '1899', '65.60', '77.40');
INSERT INTO `bbs_sms_data` VALUES ('160', 'Россия', 'АКОС', '1161', '96.00', '113.28');
INSERT INTO `bbs_sms_data` VALUES ('161', 'Россия', 'АКОС', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('162', 'Россия', 'АКОС', '5537', '148.80', '175.58');
INSERT INTO `bbs_sms_data` VALUES ('163', 'Россия', 'АКОС', '7781', '148.80', '175.58');
INSERT INTO `bbs_sms_data` VALUES ('164', 'Россия', 'АКОС', '4481', '228.82', '270.00');
INSERT INTO `bbs_sms_data` VALUES ('165', 'Россия', 'АКОС', '3747', '228.82', '270.00');
INSERT INTO `bbs_sms_data` VALUES ('166', 'Россия', 'АКОС', '4124', '228.82', '270.00');

INSERT INTO `bbs_sms_data` VALUES ('167', 'Россия', 'Ульяновск GSM', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('168', 'Россия', 'Ульяновск GSM', '1121', '2.86', '3.37');
INSERT INTO `bbs_sms_data` VALUES ('169', 'Россия', 'Ульяновск GSM', '1131', '13.11', '15.46');
INSERT INTO `bbs_sms_data` VALUES ('170', 'Россия', 'Ульяновск GSM', '1141', '21.21', '25.02');
INSERT INTO `bbs_sms_data` VALUES ('171', 'Россия', 'Ульяновск GSM', '5013', '36.56', '43.14');
INSERT INTO `bbs_sms_data` VALUES ('172', 'Россия', 'Ульяновск GSM', '1151', '36.56', '43.14');
INSERT INTO `bbs_sms_data` VALUES ('173', 'Россия', 'Ульяновск GSM', '1899', '70.30', '82.95');
INSERT INTO `bbs_sms_data` VALUES ('174', 'Россия', 'Ульяновск GSM', '1161', '98.42', '116.13');
INSERT INTO `bbs_sms_data` VALUES ('175', 'Россия', 'Ульяновск GSM', '5014', '112.48', '132.72');
INSERT INTO `bbs_sms_data` VALUES ('176', 'Россия', 'Ульяновск GSM', '5537', '127.12', '150.00');
INSERT INTO `bbs_sms_data` VALUES ('177', 'Россия', 'Ульяновск GSM', '7781', '169.49', '200.00');
INSERT INTO `bbs_sms_data` VALUES ('178', 'Россия', 'Ульяновск GSM', '3747', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('179', 'Россия', 'Ульяновск GSM', '4124', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('180', 'Россия', 'Ульяновск GSM', '4481', '254.24', '300.00');

INSERT INTO `bbs_sms_data` VALUES ('181', 'Россия', 'Енисей Телеком', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('182', 'Россия', 'Енисей Телеком', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('183', 'Россия', 'Енисей Телеком', '1131', '14.00', '16.52');
INSERT INTO `bbs_sms_data` VALUES ('184', 'Россия', 'Енисей Телеком', '1141', '23.00', '27.14');
INSERT INTO `bbs_sms_data` VALUES ('185', 'Россия', 'Енисей Телеком', '1151', '35.00', '41.30');
INSERT INTO `bbs_sms_data` VALUES ('186', 'Россия', 'Енисей Телеком', '5013', '38.00', '44.84');
INSERT INTO `bbs_sms_data` VALUES ('187', 'Россия', 'Енисей Телеком', '1899', '65.00', '76.70');
INSERT INTO `bbs_sms_data` VALUES ('188', 'Россия', 'Енисей Телеком', '1161', '95.00', '112.10');
INSERT INTO `bbs_sms_data` VALUES ('189', 'Россия', 'Енисей Телеком', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('190', 'Россия', 'Енисей Телеком', '5537', '145.00', '171.10');
INSERT INTO `bbs_sms_data` VALUES ('191', 'Россия', 'Енисей Телеком', '7781', '145.00', '171.10');
INSERT INTO `bbs_sms_data` VALUES ('192', 'Россия', 'Енисей Телеком', '4124', '228.81', '270.00');
INSERT INTO `bbs_sms_data` VALUES ('193', 'Россия', 'Енисей Телеком', '4481', '228.81', '270.00');
INSERT INTO `bbs_sms_data` VALUES ('194', 'Россия', 'Енисей Телеком', '3747', '228.81', '270.00');

INSERT INTO `bbs_sms_data` VALUES ('195', 'Россия', 'Татинком', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('196', 'Россия', 'Татинком', '1121', '2.86', '3.37');
INSERT INTO `bbs_sms_data` VALUES ('197', 'Россия', 'Татинком', '1131', '13.11', '15.46');
INSERT INTO `bbs_sms_data` VALUES ('198', 'Россия', 'Татинком', '1141', '21.21', '25.02');
INSERT INTO `bbs_sms_data` VALUES ('199', 'Россия', 'Татинком', '1151', '36.56', '43.14');
INSERT INTO `bbs_sms_data` VALUES ('200', 'Россия', 'Татинком', '5013', '36.56', '43.14');
INSERT INTO `bbs_sms_data` VALUES ('201', 'Россия', 'Татинком', '7781', '42.18', '49.77');
INSERT INTO `bbs_sms_data` VALUES ('202', 'Россия', 'Татинком', '1899', '70.30', '82.95');
INSERT INTO `bbs_sms_data` VALUES ('203', 'Россия', 'Татинком', '1161', '98.42', '116.13');
INSERT INTO `bbs_sms_data` VALUES ('204', 'Россия', 'Татинком', '5014', '112.48', '132.72');
INSERT INTO `bbs_sms_data` VALUES ('205', 'Россия', 'Татинком', '5537', '127.12', '150.00');
INSERT INTO `bbs_sms_data` VALUES ('206', 'Россия', 'Татинком', '4481', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('207', 'Россия', 'Татинком', '4124', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('208', 'Россия', 'Татинком', '3747', '254.24', '300.00');

INSERT INTO `bbs_sms_data` VALUES ('209', 'Россия', 'НСС', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('210', 'Россия', 'НСС', '1121', '2.86', '3.37');
INSERT INTO `bbs_sms_data` VALUES ('211', 'Россия', 'НСС', '1131', '13.11', '15.46');
INSERT INTO `bbs_sms_data` VALUES ('212', 'Россия', 'НСС', '1141', '21.21', '25.02');
INSERT INTO `bbs_sms_data` VALUES ('213', 'Россия', 'НСС', '1151', '36.56', '43.14');
INSERT INTO `bbs_sms_data` VALUES ('214', 'Россия', 'НСС', '5013', '36.56', '43.14');
INSERT INTO `bbs_sms_data` VALUES ('215', 'Россия', 'НСС', '1899', '70.30', '82.95');
INSERT INTO `bbs_sms_data` VALUES ('216', 'Россия', 'НСС', '1161', '98.42', '116.13');
INSERT INTO `bbs_sms_data` VALUES ('217', 'Россия', 'НСС', '5014', '112.48', '132.72');
INSERT INTO `bbs_sms_data` VALUES ('218', 'Россия', 'НСС', '5537', '127.12', '150.00');
INSERT INTO `bbs_sms_data` VALUES ('219', 'Россия', 'НСС', '4124', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('220', 'Россия', 'НСС', '3747', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('221', 'Россия', 'НСС', '4481', '254.24', '300.00');

INSERT INTO `bbs_sms_data` VALUES ('222', 'Россия', 'Астарта', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('223', 'Россия', 'Астарта', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('224', 'Россия', 'Астарта', '1131', '14.00', '16.52');
INSERT INTO `bbs_sms_data` VALUES ('225', 'Россия', 'Астарта', '1141', '23.00', '27.14');
INSERT INTO `bbs_sms_data` VALUES ('226', 'Россия', 'Астарта', '1151', '35.00', '41.30');
INSERT INTO `bbs_sms_data` VALUES ('227', 'Россия', 'Астарта', '5013', '38.00', '44.84');
INSERT INTO `bbs_sms_data` VALUES ('228', 'Россия', 'Астарта', '1899', '65.00', '76.70');
INSERT INTO `bbs_sms_data` VALUES ('229', 'Россия', 'Астарта', '1161', '95.00', '112.10');
INSERT INTO `bbs_sms_data` VALUES ('230', 'Россия', 'Астарта', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('231', 'Россия', 'Астарта', '7781', '155.00', '182.90');
INSERT INTO `bbs_sms_data` VALUES ('232', 'Россия', 'Астарта', '5537', '155.00', '182.90');
INSERT INTO `bbs_sms_data` VALUES ('233', 'Россия', 'Астарта', '4481', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('234', 'Россия', 'Астарта', '4124', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('235', 'Россия', 'Астарта', '3747', '300.00', '354.00');

INSERT INTO `bbs_sms_data` VALUES ('236', 'Россия', 'МСС', '1121', '2.54', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('237', 'Россия', 'МСС', '1121', '3.00', '3.54');
INSERT INTO `bbs_sms_data` VALUES ('238', 'Россия', 'МСС', '1131', '14.00', '16.52');
INSERT INTO `bbs_sms_data` VALUES ('239', 'Россия', 'МСС', '1141', '23.00', '27.14');
INSERT INTO `bbs_sms_data` VALUES ('240', 'Россия', 'МСС', '1151', '35.00', '41.30');
INSERT INTO `bbs_sms_data` VALUES ('241', 'Россия', 'МСС', '5013', '38.00', '44.84');
INSERT INTO `bbs_sms_data` VALUES ('242', 'Россия', 'МСС', '1899', '65.00', '76.70');
INSERT INTO `bbs_sms_data` VALUES ('243', 'Россия', 'МСС', '1161', '95.00', '112.10');
INSERT INTO `bbs_sms_data` VALUES ('244', 'Россия', 'МСС', '5014', '120.00', '141.60');
INSERT INTO `bbs_sms_data` VALUES ('245', 'Россия', 'МСС', '5537', '155.00', '182.90');
INSERT INTO `bbs_sms_data` VALUES ('246', 'Россия', 'МСС', '7781', '155.00', '182.90');
INSERT INTO `bbs_sms_data` VALUES ('247', 'Россия', 'МСС', '4481', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('248', 'Россия', 'МСС', '4124', '254.24', '300.00');
INSERT INTO `bbs_sms_data` VALUES ('249', 'Россия', 'МСС', '3747', '300.00', '354.00');

INSERT INTO `bbs_sms_data` VALUES ('250', 'Украина', 'Киевстар (Украина)', '7510', '2.50', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('251', 'Украина', 'Киевстар (Украина)', '7520', '13.33', '16.00');
INSERT INTO `bbs_sms_data` VALUES ('252', 'Украина', 'Киевстар (Украина)', '7530', '25.00', '30.00');
INSERT INTO `bbs_sms_data` VALUES ('253', 'Украина', 'Киевстар (Украина)', '7540', '41.67', '50.00');

INSERT INTO `bbs_sms_data` VALUES ('254', 'Украина', 'МТС Украина', '7510', '2.50', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('255', 'Украина', 'МТС Украина', '7520', '13.33', '16.00');
INSERT INTO `bbs_sms_data` VALUES ('256', 'Украина', 'МТС Украина', '7530', '25.00', '30.00');
INSERT INTO `bbs_sms_data` VALUES ('257', 'Украина', 'МТС Украина', '7540', '41.67', '50.00');

INSERT INTO `bbs_sms_data` VALUES ('258', 'Украина', 'Life', '7510', '2.50', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('259', 'Украина', 'Life', '7520', '13.33', '16.00');
INSERT INTO `bbs_sms_data` VALUES ('260', 'Украина', 'Life', '7530', '25.00', '30.00');
INSERT INTO `bbs_sms_data` VALUES ('261', 'Украина', 'Life', '7540', '41.67', '50.00');

INSERT INTO `bbs_sms_data` VALUES ('262', 'Беларусь', 'МТС', '1121', '3500.40', '4200.48');
INSERT INTO `bbs_sms_data` VALUES ('263', 'Беларусь', 'МТС', '1131', '5900.00', '7080.00');
INSERT INTO `bbs_sms_data` VALUES ('264', 'Беларусь', 'МТС', '1141', '6900.00', '8280.00');
INSERT INTO `bbs_sms_data` VALUES ('265', 'Беларусь', 'МТС', '1151', '9900.00', '11880.00');
INSERT INTO `bbs_sms_data` VALUES ('266', 'Беларусь', 'МТС', '5013', '15900.00', '19080.00');
INSERT INTO `bbs_sms_data` VALUES ('267', 'Беларусь', 'МТС', '1899', '19899.60', '23879.52');
INSERT INTO `bbs_sms_data` VALUES ('268', 'Беларусь', 'МТС', '5014', '24900.00', '29880.00');
INSERT INTO `bbs_sms_data` VALUES ('269', 'Беларусь', 'МТС', '7781', '29900.40', '35880.48');

INSERT INTO `bbs_sms_data` VALUES ('270', 'Беларусь', 'БелСел', '1121', '1299.99', '1559.98');
INSERT INTO `bbs_sms_data` VALUES ('271', 'Беларусь', 'БелСел', '1131', '3500.00', '4200.00');
INSERT INTO `bbs_sms_data` VALUES ('272', 'Беларусь', 'БелСел', '1141', '5900.00', '7080.00');
INSERT INTO `bbs_sms_data` VALUES ('273', 'Беларусь', 'БелСел', '1151', '8300.00', '9960.00');
INSERT INTO `bbs_sms_data` VALUES ('274', 'Беларусь', 'БелСел', '5013', '9900.00', '11880.00');
INSERT INTO `bbs_sms_data` VALUES ('275', 'Беларусь', 'БелСел', '1899', '12900.00', '15480.00');
INSERT INTO `bbs_sms_data` VALUES ('276', 'Беларусь', 'БелСел', '5014', '15900.00', '19080.00');
INSERT INTO `bbs_sms_data` VALUES ('277', 'Беларусь', 'БелСел', '7781', '19899.99', '23879.98');

INSERT INTO `bbs_sms_data` VALUES ('278', 'Беларусь', 'Велком', '1121', '3500.40', '4200.48');
INSERT INTO `bbs_sms_data` VALUES ('279', 'Беларусь', 'Велком', '1131', '5900.00', '7080.00');
INSERT INTO `bbs_sms_data` VALUES ('280', 'Беларусь', 'Велком', '1141', '6900.00', '8280.00');
INSERT INTO `bbs_sms_data` VALUES ('281', 'Беларусь', 'Велком', '1151', '9900.00', '11880.00');
INSERT INTO `bbs_sms_data` VALUES ('282', 'Беларусь', 'Велком', '5013', '15900.00', '19080.00');
INSERT INTO `bbs_sms_data` VALUES ('283', 'Беларусь', 'Велком', '1899', '19899.60', '23879.52');
INSERT INTO `bbs_sms_data` VALUES ('284', 'Беларусь', 'Велком', '5014', '24900.00', '29880.00');
INSERT INTO `bbs_sms_data` VALUES ('285', 'Беларусь', 'Велком', '7781', '29900.40', '35880.48');

INSERT INTO `bbs_sms_data` VALUES ('286', 'Беларусь', 'Life', '1121', '1299.60', '1559.52');
INSERT INTO `bbs_sms_data` VALUES ('287', 'Беларусь', 'Life', '1131', '3500.40', '4200.48');
INSERT INTO `bbs_sms_data` VALUES ('288', 'Беларусь', 'Life', '1141', '5900.40', '7080.48');
INSERT INTO `bbs_sms_data` VALUES ('289', 'Беларусь', 'Life', '1151', '8300.40', '9960.48');
INSERT INTO `bbs_sms_data` VALUES ('290', 'Беларусь', 'Life', '5013', '9900.00', '11880.00');
INSERT INTO `bbs_sms_data` VALUES ('291', 'Беларусь', 'Life', '1899', '12900.00', '15480.00');
INSERT INTO `bbs_sms_data` VALUES ('292', 'Беларусь', 'Life', '5014', '15900.00', '19080.00');
INSERT INTO `bbs_sms_data` VALUES ('293', 'Беларусь', 'Life', '7781', '19899.60', '23879.52');

INSERT INTO `bbs_sms_data` VALUES ('294', 'Казахстан', 'K-Cell (Казахстан)', '7720', '89.29', '101.79');
INSERT INTO `bbs_sms_data` VALUES ('295', 'Казахстан', 'K-Cell (Казахстан)', '7730', '223.21', '254.45');
INSERT INTO `bbs_sms_data` VALUES ('296', 'Казахстан', 'K-Cell (Казахстан)', '7750', '357.14', '407.13');
INSERT INTO `bbs_sms_data` VALUES ('297', 'Казахстан', 'K-Cell (Казахстан)', '7790', '535.71', '610.70');

INSERT INTO `bbs_sms_data` VALUES ('298', 'Казахстан', 'КарТел', '7720', '89.29', '101.79');
INSERT INTO `bbs_sms_data` VALUES ('299', 'Казахстан', 'КарТел', '7730', '223.21', '254.45');
INSERT INTO `bbs_sms_data` VALUES ('300', 'Казахстан', 'КарТел', '7750', '357.14', '407.13');
INSERT INTO `bbs_sms_data` VALUES ('301', 'Казахстан', 'КарТел', '7790', '535.71', '610.70');

INSERT INTO `bbs_sms_data` VALUES ('302', 'Таджикистан', 'Индиго(Таджикистан)', '1131', '0.36', '0.43');
INSERT INTO `bbs_sms_data` VALUES ('303', 'Таджикистан', 'Индиго(Таджикистан)', '1141', '0.72', '0.86');
INSERT INTO `bbs_sms_data` VALUES ('304', 'Таджикистан', 'Индиго(Таджикистан)', '1151', '1.20', '1.44');
INSERT INTO `bbs_sms_data` VALUES ('305', 'Таджикистан', 'Индиго(Таджикистан)', '1161', '3.60', '4.32');
INSERT INTO `bbs_sms_data` VALUES ('306', 'Таджикистан', 'Индиго(Таджикистан)', '1171', '6.00', '7.20');

INSERT INTO `bbs_sms_data` VALUES ('307', 'Таджикистан', 'MLT(Таджикистан)', '1131', '0.36', '0.43');
INSERT INTO `bbs_sms_data` VALUES ('308', 'Таджикистан', 'MLT(Таджикистан)', '1141', '0.72', '0.86');
INSERT INTO `bbs_sms_data` VALUES ('309', 'Таджикистан', 'MLT(Таджикистан)', '1151', '1.20', '1.44');
INSERT INTO `bbs_sms_data` VALUES ('310', 'Таджикистан', 'MLT(Таджикистан)', '1161', '3.60', '4.32');
INSERT INTO `bbs_sms_data` VALUES ('311', 'Таджикистан', 'MLT(Таджикистан)', '1171', '6.00', '7.20');

INSERT INTO `bbs_sms_data` VALUES ('312', 'Таджикистан', 'Вавилон-М', '1131', '0.36', '0.43');
INSERT INTO `bbs_sms_data` VALUES ('313', 'Таджикистан', 'Вавилон-М', '1141', '0.72', '0.86');
INSERT INTO `bbs_sms_data` VALUES ('314', 'Таджикистан', 'Вавилон-М', '1151', '1.20', '1.44');
INSERT INTO `bbs_sms_data` VALUES ('315', 'Таджикистан', 'Вавилон-М', '1161', '3.60', '4.32');
INSERT INTO `bbs_sms_data` VALUES ('316', 'Таджикистан', 'Вавилон-М', '1171', '6.00', '7.20');

INSERT INTO `bbs_sms_data` VALUES ('317', 'Таджикистан', 'Билайн', '1131', '0.36', '0.43');
INSERT INTO `bbs_sms_data` VALUES ('318', 'Таджикистан', 'Билайн', '1141', '0.72', '0.86');
INSERT INTO `bbs_sms_data` VALUES ('319', 'Таджикистан', 'Билайн', '1151', '1.20', '1.44');
INSERT INTO `bbs_sms_data` VALUES ('320', 'Таджикистан', 'Билайн', '1161', '3.60', '4.32');
INSERT INTO `bbs_sms_data` VALUES ('321', 'Таджикистан', 'Билайн', '1171', '6.00', '7.20');

INSERT INTO `bbs_sms_data` VALUES ('322', 'Киргизия', 'Билайн (Киргизия)', '4152', '31.58', '37.89');
INSERT INTO `bbs_sms_data` VALUES ('323', 'Киргизия', 'Билайн (Киргизия)', '4153', '52.63', '63.15');
INSERT INTO `bbs_sms_data` VALUES ('324', 'Киргизия', 'Билайн (Киргизия)', '4156', '157.89', '189.46');
INSERT INTO `bbs_sms_data` VALUES ('325', 'Киргизия', 'Билайн (Киргизия)', '4157', '263.16', '315.79');

INSERT INTO `bbs_sms_data` VALUES ('326', 'Киргизия', 'МегаКом', '4152', '31.58', '37.89');
INSERT INTO `bbs_sms_data` VALUES ('327', 'Киргизия', 'МегаКом', '4153', '52.63', '63.15');
INSERT INTO `bbs_sms_data` VALUES ('328', 'Киргизия', 'МегаКом', '4156', '157.89', '189.46');
INSERT INTO `bbs_sms_data` VALUES ('329', 'Киргизия', 'МегаКом', '4157', '263.16', '315.79');

INSERT INTO `bbs_sms_data` VALUES ('330', 'Киргизия', 'Сотел', '4152', '31.58', '37.89');
INSERT INTO `bbs_sms_data` VALUES ('331', 'Киргизия', 'Сотел', '4153', '52.63', '63.15');
INSERT INTO `bbs_sms_data` VALUES ('332', 'Киргизия', 'Сотел', '4156', '157.89', '189.46');
INSERT INTO `bbs_sms_data` VALUES ('333', 'Киргизия', 'Сотел', '4157', '263.16', '315.79');
INSERT INTO `bbs_sms_data` VALUES ('334', 'Киргизия', 'Кател', '4152', '31.58', '37.89');
INSERT INTO `bbs_sms_data` VALUES ('335', 'Киргизия', 'Кател', '4153', '52.63', '63.15');
INSERT INTO `bbs_sms_data` VALUES ('336', 'Киргизия', 'Кател', '4156', '157.89', '189.46');
INSERT INTO `bbs_sms_data` VALUES ('337', 'Киргизия', 'Кател', '4157', '263.16', '315.79');

INSERT INTO `bbs_sms_data` VALUES ('338', 'Латвия', 'Lmt (Латвия)', '1871', '0.29', '0.35');
INSERT INTO `bbs_sms_data` VALUES ('339', 'Латвия', 'Lmt (Латвия)', '1872', '0.50', '0.61');
INSERT INTO `bbs_sms_data` VALUES ('340', 'Латвия', 'Lmt (Латвия)', '1873', '1.69', '2.06');
INSERT INTO `bbs_sms_data` VALUES ('341', 'Латвия', 'Lmt (Латвия)', '1874', '2.54', '3.09');

INSERT INTO `bbs_sms_data` VALUES ('342', 'Латвия', 'Bite (Латвия)', '1871', '0.29', '0.35');
INSERT INTO `bbs_sms_data` VALUES ('343', 'Латвия', 'Bite (Латвия)', '1872', '0.49', '0.59');
INSERT INTO `bbs_sms_data` VALUES ('344', 'Латвия', 'Bite (Латвия)', '1873', '1.56', '1.90');
INSERT INTO `bbs_sms_data` VALUES ('345', 'Латвия', 'Bite (Латвия)', '1874', '2.42', '2.95');

INSERT INTO `bbs_sms_data` VALUES ('346', 'Литва', 'Bite (Литва)', '1381', '0.84', '1.01');
INSERT INTO `bbs_sms_data` VALUES ('347', 'Литва', 'Bite (Литва)', '1391', '2.54', '3.07');
INSERT INTO `bbs_sms_data` VALUES ('348', 'Литва', 'Bite (Литва)', '1635', '5.76', '6.96');
INSERT INTO `bbs_sms_data` VALUES ('349', 'Литва', 'Bite (Литва)', '1645', '8.72', '10.55');

INSERT INTO `bbs_sms_data` VALUES ('350', 'Литва', 'Omnitel (Литва)', '1381', '0.84', '1.01');
INSERT INTO `bbs_sms_data` VALUES ('351', 'Литва', 'Omnitel (Литва)', '1391', '2.52', '3.04');
INSERT INTO `bbs_sms_data` VALUES ('352', 'Литва', 'Omnitel (Литва)', '1635', '5.88', '7.11');
INSERT INTO `bbs_sms_data` VALUES ('353', 'Литва', 'Omnitel (Литва)', '1645', '8.41', '10.17');

INSERT INTO `bbs_sms_data` VALUES ('354', 'Литва', 'Tele-2 (Литва)', '1381', '0.83', '1.00');
INSERT INTO `bbs_sms_data` VALUES ('355', 'Литва', 'Tele-2 (Литва)', '1391', '2.48', '3.00');
INSERT INTO `bbs_sms_data` VALUES ('356', 'Литва', 'Tele-2 (Литва)', '1635', '6.61', '8.00');
INSERT INTO `bbs_sms_data` VALUES ('357', 'Литва', 'Tele-2 (Литва)', '1645', '8.26', '10.00');

INSERT INTO `bbs_sms_data` VALUES ('358', 'Эстония', 'Emt (Эстония)', '17010', '0.42', '0.50');
INSERT INTO `bbs_sms_data` VALUES ('359', 'Эстония', 'Emt (Эстония)', '17011', '0.83', '1.00');
INSERT INTO `bbs_sms_data` VALUES ('360', 'Эстония', 'Emt (Эстония)', '17012', '2.08', '2.49');
INSERT INTO `bbs_sms_data` VALUES ('361', 'Эстония', 'Emt (Эстония)', '17013', '2.92', '3.50');

INSERT INTO `bbs_sms_data` VALUES ('362', 'Эстония', 'Elisa (Эстония)', '17010', '0.42', '0.50');
INSERT INTO `bbs_sms_data` VALUES ('363', 'Эстония', 'Elisa (Эстония)', '17011', '0.83', '1.00');
INSERT INTO `bbs_sms_data` VALUES ('364', 'Эстония', 'Elisa (Эстония)', '17012', '2.08', '2.49');
INSERT INTO `bbs_sms_data` VALUES ('365', 'Эстония', 'Elisa (Эстония)', '17013', '2.92', '3.50');

INSERT INTO `bbs_sms_data` VALUES ('366', 'Эстония', 'Tele-2 (Эстония)', '17010', '0.42', '0.50');
INSERT INTO `bbs_sms_data` VALUES ('367', 'Эстония', 'Tele-2 (Эстония)', '17011', '0.83', '1.00');
INSERT INTO `bbs_sms_data` VALUES ('368', 'Эстония', 'Tele-2 (Эстония)', '17012', '2.08', '2.49');
INSERT INTO `bbs_sms_data` VALUES ('369', 'Эстония', 'Tele-2 (Эстония)', '17013', '2.92', '3.50');

INSERT INTO `bbs_sms_data` VALUES ('370', 'Израиль', 'Orange', '4545', '8.55', '9.96');

INSERT INTO `bbs_sms_data` VALUES ('371', 'Израиль', 'Cellcom', '4545', '8.55', '9.96');

INSERT INTO `bbs_sms_data` VALUES ('372', 'Израиль', 'MIRS', '4545', '8.55', '9.96');

INSERT INTO `bbs_sms_data` VALUES ('373', 'Израиль', 'Pelephone', '4545', '8.55', '9.96');

INSERT INTO `bbs_sms_data` VALUES ('374', 'Польша', 'Orange', '7910', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('375', 'Польша', 'Orange', '79866', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('376', 'Польша', 'Orange', '92525', '25.00', '30.50');

INSERT INTO `bbs_sms_data` VALUES ('377', 'Польша', 'Plus', '7910', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('378', 'Польша', 'Plus', '79866', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('379', 'Польша', 'Plus', '92525', '25.00', '30.50');

INSERT INTO `bbs_sms_data` VALUES ('380', 'Польша', 'Era', '79866', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('381', 'Польша', 'Era', '7910', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('382', 'Польша', 'Era', '92525', '25.00', '30.50');

INSERT INTO `bbs_sms_data` VALUES ('383', 'Польша', 'Play', '7910', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('384', 'Польша', 'Play', '79866', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('385', 'Польша', 'Play', '92525', '25.00', '30.50');

INSERT INTO `bbs_sms_data` VALUES ('386', 'Польша', 'Sferia', '79866', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('387', 'Польша', 'Sferia', '92525', '25.00', '30.50');

INSERT INTO `bbs_sms_data` VALUES ('388', 'Польша', 'Halov', '79866', '9.00', '10.98');
INSERT INTO `bbs_sms_data` VALUES ('389', 'Польша', 'Halov', '92525', '25.00', '30.50');

INSERT INTO `bbs_sms_data` VALUES ('390', 'Чехия', 'O2', '9090150', '41.67', '49.58');
INSERT INTO `bbs_sms_data` VALUES ('391', 'Чехия', 'O2', '9090179', '65.83', '78.33');
INSERT INTO `bbs_sms_data` VALUES ('392', 'Чехия', 'O2', '9090199', '82.50', '98.17');

INSERT INTO `bbs_sms_data` VALUES ('393', 'Чехия', 'T-Mobile', '9090150', '41.67', '49.58');
INSERT INTO `bbs_sms_data` VALUES ('394', 'Чехия', 'T-Mobile', '9090179', '65.83', '78.33');
INSERT INTO `bbs_sms_data` VALUES ('395', 'Чехия', 'T-Mobile', '9090199', '82.50', '98.17');

INSERT INTO `bbs_sms_data` VALUES ('396', 'Чехия', 'Vodafone', '9090150', '41.67', '49.58');
INSERT INTO `bbs_sms_data` VALUES ('397', 'Чехия', 'Vodafone', '9090179', '65.83', '78.33');
INSERT INTO `bbs_sms_data` VALUES ('398', 'Чехия', 'Vodafone', '9090199', '82.50', '98.17');

INSERT INTO `bbs_sms_data` VALUES ('399', 'Армения', 'Armentel', '1141', '203.39', '244.06');
INSERT INTO `bbs_sms_data` VALUES ('400', 'Армения', 'Armentel', '1151', '338.97', '406.76');
INSERT INTO `bbs_sms_data` VALUES ('401', 'Армения', 'Armentel', '1161', '1016.95', '1220.34');
INSERT INTO `bbs_sms_data` VALUES ('402', 'Армения', 'Armentel', '1121', '1440.62', '1728.74');

INSERT INTO `bbs_sms_data` VALUES ('403', 'Армения', 'Vivacell', '1141', '203.39', '244.06');
INSERT INTO `bbs_sms_data` VALUES ('404', 'Армения', 'Vivacell', '1151', '338.97', '406.76');
INSERT INTO `bbs_sms_data` VALUES ('405', 'Армения', 'Vivacell', '1161', '1016.95', '1220.34');
INSERT INTO `bbs_sms_data` VALUES ('406', 'Армения', 'Vivacell', '1121', '1440.62', '1728.74');

INSERT INTO `bbs_sms_data` VALUES ('407', 'Азербайджан', 'Azercell', '9012', '1.00', '1.18');
INSERT INTO `bbs_sms_data` VALUES ('408', 'Азербайджан', 'Azercell', '9013', '2.50', '2.95');
INSERT INTO `bbs_sms_data` VALUES ('409', 'Азербайджан', 'Azercell', '9014', '4.00', '4.72');

INSERT INTO `bbs_sms_data` VALUES ('410', 'Грузия', 'Geocell', '8012', '0.84', '1.00');
INSERT INTO `bbs_sms_data` VALUES ('411', 'Грузия', 'Geocell', '8013', '1.39', '1.64');
INSERT INTO `bbs_sms_data` VALUES ('412', 'Грузия', 'Geocell', '8014', '3.08', '3.63');

INSERT INTO `bbs_sms_data` VALUES ('413', 'Грузия', 'Magti', '8012', '0.84', '1.00');
INSERT INTO `bbs_sms_data` VALUES ('414', 'Грузия', 'Magti', '8013', '1.39', '1.64');
INSERT INTO `bbs_sms_data` VALUES ('415', 'Грузия', 'Magti', '8014', '3.08', '3.63');

INSERT INTO `bbs_sms_data` VALUES ('416', 'Болгария', 'Mtel', '1098', '2.00', '2.40');

INSERT INTO `bbs_sms_data` VALUES ('417', 'Болгария', 'GloBul', '1098', '2.00', '2.40');

INSERT INTO `bbs_sms_data` VALUES ('418', 'Болгария', 'Vivatel', '1098', '2.00', '2.40');