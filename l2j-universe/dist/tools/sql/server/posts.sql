CREATE TABLE IF NOT EXISTS `posts` (
  `post_id` int NOT NULL default 0,
  `post_owner_name` varchar(255) character set utf8 NOT NULL default '',
  `post_ownerid` int NOT NULL default 0,
  `post_date` bigint NOT NULL default 0,
  `post_topic_id` int NOT NULL default 0,
  `post_forum_id` int NOT NULL default 0,
  `post_txt` text character set utf8 NOT NULL,
  KEY `post_forum_id` (`post_forum_id`,`post_topic_id`)
) ENGINE=MyISAM;