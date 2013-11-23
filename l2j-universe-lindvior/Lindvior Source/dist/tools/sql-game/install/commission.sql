CREATE TABLE IF NOT EXISTS `commission_shop` (
  `auction_id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
  `obj_id` INT NOT NULL,
  `seller_id` INT NOT NULL,
  `item_name` varchar(128) CHARACTER SET UTF8 NOT NULL,
  `price` bigint(20) NOT NULL,
  `item_type` varchar(32) NOT NULL,
  `sale_days` tinyint(4) NOT NULL,
  `sale_end_time` bigint(20) NOT NULL,
  `seller_name` varchar(16) CHARACTER SET UTF8 NOT NULL,
  PRIMARY KEY (`auction_id`)
) ENGINE=MyISAM;