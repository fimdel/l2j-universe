DROP TABLE IF EXISTS `olympiad_nobles`;
CREATE TABLE `olympiad_nobles` (
  `char_id` int(11) NOT NULL DEFAULT '0',
  `class_id` smallint(6) unsigned NOT NULL DEFAULT '0',
  `olympiad_points` smallint(6) NOT NULL DEFAULT '0',
  `olympiad_points_past` smallint(6) NOT NULL DEFAULT '0',
  `olympiad_points_past_static` smallint(6) NOT NULL DEFAULT '0',
  `competitions_done` smallint(6) unsigned NOT NULL DEFAULT '0',
  `competitions_win` smallint(6) unsigned NOT NULL DEFAULT '0',
  `competitions_loose` smallint(6) unsigned NOT NULL DEFAULT '0',
  `game_count` smallint(6) unsigned NOT NULL,
  `olympiad_participant` tinyint(3) NOT NULL DEFAULT '0',
  PRIMARY KEY (`char_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

