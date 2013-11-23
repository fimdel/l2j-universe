CREATE TABLE IF NOT EXISTS `world_statistic` (
    `objId` bigint(35) NOT NULL,
    `categoryId` bigint(35) NOT NULL,
    `subCategoryId` bigint(35) NOT NULL,
    `value` bigint(255) NOT NULL,
    `date` bigint(35) NOT NULL,
    PRIMARY KEY (`objId`, `categoryId`, `subCategoryId`)
)ENGINE = MyISAM