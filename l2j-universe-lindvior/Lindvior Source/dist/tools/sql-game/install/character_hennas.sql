DROP TABLE IF EXISTS `character_hennas`;
CREATE TABLE `character_hennas` (
  `char_obj_id` int(11) NOT NULL DEFAULT '0',
  `symbol_id` smallint(6) unsigned NOT NULL DEFAULT '0',
  `slot` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `class_index` tinyint(3) unsigned NOT NULL DEFAULT '0',
  KEY `char_obj_id` (`char_obj_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;