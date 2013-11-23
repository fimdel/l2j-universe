CREATE TABLE IF NOT EXISTS world_statistic_winners_monthly (
    `objId` bigint(35) NOT NULL,
    `categoryId` bigint(35) NOT NULL,
    `subCategoryId` bigint(35) NOT NULL,
    `value` bigint(255) NOT NULL,
    `classId` bigint(35) NOT NULL,
    `race`          bigint(35) NOT NULL,
    `sex`           bigint(35) NOT NULL,
    `hairstyle`     bigint(35) NOT NULL,
    `haircolor`     bigint(35) NOT NULL,
    `face`          bigint(35) NOT NULL,
    `necklace`      bigint(35) NOT NULL,
    `head`          bigint(35) NOT NULL,
    `rhand` bigint(35) NOT NULL,
    `lhand` bigint(35) NOT NULL,
    `gloves`        bigint(35) NOT NULL,
    `chest` bigint(35) NOT NULL,
    `pants` bigint(35) NOT NULL,
    `boots` bigint(35) NOT NULL,
    `cloak` bigint(35) NOT NULL,
    `hair1` bigint(35) NOT NULL,
    `hair2` bigint(35) NOT NULL,
    `date` bigint(35) NOT NULL,
    PRIMARY KEY (`objId`, `categoryId`, `subCategoryId`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;