CREATE TABLE IF NOT EXISTS world_statistic_winners_monthly (
    `objId`         INT        NOT NULL,
    `categoryId`    INT        NOT NULL,
    `subCategoryId` INT        NOT NULL,
    `value`         BIGINT(20) NOT NULL,
    `classId`       INT        NOT NULL,
    `race`          INT        NOT NULL,
    `sex`           TINYINT    NOT NULL,
    `hairstyle`     INT        NOT NULL,
    `haircolor`     INT        NOT NULL,
    `face`          INT        NOT NULL,
    `necklace`      INT        NOT NULL,
    `head`          INT        NOT NULL,
    `rhand`         INT        NOT NULL,
    `lhand`         INT        NOT NULL,
    `gloves`        INT        NOT NULL,
    `chest`         INT        NOT NULL,
    `pants`         INT        NOT NULL,
    `boots`         INT        NOT NULL,
    `cloak`         INT        NOT NULL,
    `hair1`         INT        NOT NULL,
    `hair2`         INT        NOT NULL,
    `date`          BIGINT(20) NOT NULL,
    PRIMARY KEY (`objId`, `categoryId`, `subCategoryId`)
)
  ENGINE = MyISAM
  DEFAULT CHARSET = utf8;