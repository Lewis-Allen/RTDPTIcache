CREATE TABLE `template` (
  `TemplateId` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(50) NOT NULL,
  PRIMARY KEY (`templateId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

/*
-- Query: SELECT * FROM template
-- Date: 2019-07-06 11:41
*/
INSERT INTO `template` (`templateId`,`name`) VALUES (1,'default');
INSERT INTO `template` (`templateId`,`name`) VALUES (2,'single');
INSERT INTO `template` (`templateId`,`name`) VALUES (3,'fourbuses');
INSERT INTO `template` (`templateId`,`name`) VALUES (4,'onetrain');
INSERT INTO `template` (`templateId`,`name`) VALUES (5,'timetable');
