CREATE TABLE IF NOT EXISTS `servitors` (
  `objId` INT NOT NULL,
  `ownerId` int NOT NULL,
  `curHp` mediumint UNSIGNED,
  `curMp` mediumint UNSIGNED,
  `skill_id` int,
  `skill_lvl` int,
  PRIMARY KEY (objId)
) ENGINE=MyISAM;