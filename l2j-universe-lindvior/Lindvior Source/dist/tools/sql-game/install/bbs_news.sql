DROP TABLE IF EXISTS `bbs_news`;
CREATE TABLE `bbs_news` (
  `newsId` int(11) NOT NULL AUTO_INCREMENT,
  `newsType` tinyint(1) NOT NULL,
  `newsTitleRu` text CHARACTER SET utf8 NOT NULL,
  `newsTitleEn` text CHARACTER SET utf8 NOT NULL,
  `newsTextRu` text CHARACTER SET utf8 NOT NULL,
  `newsTextEn` text CHARACTER SET utf8 NOT NULL,
  `newsInfoRu` varchar(32) CHARACTER SET utf8 NOT NULL,
  `newsInfoEn` varchar(32) CHARACTER SET utf8 NOT NULL,
  `newsAuthor` varchar(32) CHARACTER SET utf8 NOT NULL,
  `newsDate` date NOT NULL,
  PRIMARY KEY (`newsId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- newsId = ID (Номер новости!) Внимание: Для каждой новости ID должен быть разным.
-- newsType = Тип Новости (0 = Новости проекта, 1 = Новости сервера)
-- newsTitleRu = Заголовок новости (На Русском)
-- newsTitleEn = Заголовок новости (На Английском)
-- newsTextRu = Полный текст новости (На Русском)
-- newsTextEn = Полный текст новости (На Английском)
-- newsInfoRu = Краткая информация. К примеру: Сервер: x3 (На Русском)
-- newsInfoEn = Краткая информация К примеру: Server: x3 (На Английском)
-- newsAuthor = Автор новости
-- newsDate = Дата добовления. (Формат: YY.MM.DD \ 12(Год).06(Месяц).23(День) = 23.06.2012)
INSERT INTO `bbs_news` VALUES ('1', '0', 'Тестовая новость проекта', 'Test news project', 'Полный текст, тестовой новости.', 'Full text, test news.', 'Сервер: x3', 'Server: x3', 'CCCP', '12.06.23');
INSERT INTO `bbs_news` VALUES ('2', '1', 'Тестовая новость сервера', 'Test news server', 'Полный текст, тестовой новости.', 'Full text, test news.', 'Сервер: x3', 'Server: x3', 'CCCP', '12.06.23');