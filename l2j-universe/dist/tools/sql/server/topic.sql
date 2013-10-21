CREATE TABLE IF NOT EXISTS `topic` (
  `topic_id` int NOT NULL default 0,
  `topic_forum_id` int NOT NULL default 0,
  `topic_name` varchar(255) character set utf8 NOT NULL,
  `topic_date` bigint NOT NULL default 0,
  `topic_ownername` varchar(255) character set utf8 NOT NULL default 0,
  `topic_ownerid` int NOT NULL default 0,
  `topic_type` int NOT NULL default 0,
  `topic_reply` int NOT NULL default 0,
  KEY `topic_forum_id` (`topic_forum_id`)
) ENGINE=MyISAM;
