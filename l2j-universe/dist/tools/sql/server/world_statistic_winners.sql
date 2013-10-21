CREATE TABLE IF NOT EXISTS world_statistic_winners (
    `objId`         INT        NOT NULL,
    `categoryId`    INT        NOT NULL,
    `subCategoryId` INT        NOT NULL,
    `value`         BIGINT(20) NOT NULL,
    `date`          BIGINT(20) NOT NULL,
    PRIMARY KEY (`objId`, `categoryId`, `subCategoryId`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;