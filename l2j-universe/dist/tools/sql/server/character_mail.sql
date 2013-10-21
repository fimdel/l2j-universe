DROP TABLE IF EXISTS `character_mail`;
CREATE TABLE `character_mail` (
  `char_id` int(11) NOT NULL,
  `message_id` int(11) NOT NULL,
  `is_sender` tinyint(1) NOT NULL,
  PRIMARY KEY (`char_id`,`message_id`),
  KEY `message_id` (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;