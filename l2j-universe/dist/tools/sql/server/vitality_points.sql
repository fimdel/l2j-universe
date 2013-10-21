CREATE TABLE IF NOT EXISTS `vitality_points` (
	`account_name` VARCHAR(16) NOT NULL,
	`points` INT UNSIGNED NOT NULL DEFAULT 140000,
	PRIMARY KEY (`account_name`)
) ENGINE=MyISAM;