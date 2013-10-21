CREATE TABLE `banned_ips` (
  `ip` VARCHAR(15) NOT NULL,
  `admin` VARCHAR(45) DEFAULT NULL,
  `expiretime` INT UNSIGNED NOT NULL DEFAULT '0',
  `comments` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY  (`ip`),
  UNIQUE KEY `ip` (`ip`)
) ENGINE=MyISAM;
