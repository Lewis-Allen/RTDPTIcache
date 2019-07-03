CREATE TABLE `dashboard` (
  `DashboardId` int(11) NOT NULL AUTO_INCREMENT,
  `Template` varchar(50) NOT NULL,
  `SwitchId` int(11) DEFAULT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `DashboardData` varchar(10000) NOT NULL,
  `CreatedDate` datetime NOT NULL,
  `LastUsedDate` datetime NOT NULL,
  PRIMARY KEY (`DashboardId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

INSERT INTO `dashboard` (`DashboardId`,`Template`,`SwitchId`,`Name`,`DashboardData`,`CreatedDate`,`LastUsedDate`) VALUES (1,'single',NULL,NULL,'{\"template\":\"single\",\"buses\":[\"149000006062\"]}','2019-07-01 00:45:09','2019-07-01 00:45:09');
INSERT INTO `dashboard` (`DashboardId`,`Template`,`SwitchId`,`Name`,`DashboardData`,`CreatedDate`,`LastUsedDate`) VALUES (2,'default',NULL,NULL,'{\"template\":\"default\",\"trains\":[\"BTN\"]}','2019-07-01 00:45:26','2019-07-01 00:45:26');
INSERT INTO `dashboard` (`DashboardId`,`Template`,`SwitchId`,`Name`,`DashboardData`,`CreatedDate`,`LastUsedDate`) VALUES (3,'fourbuses',NULL,NULL,'{\"template\":\"fourbuses\",\"buses\":[\"149000006061\",\"149000006062\",\"149000006070\",\"149000006230\"]}','2019-07-01 00:45:46','2019-07-01 00:45:46');
INSERT INTO `dashboard` (`DashboardId`,`Template`,`SwitchId`,`Name`,`DashboardData`,`CreatedDate`,`LastUsedDate`) VALUES (4,'default',NULL,NULL,'{\"template\":\"default\",\"trains\":[\"BTN\",\"FMR\"]}','2019-07-01 00:46:18','2019-07-01 00:46:18');
INSERT INTO `dashboard` (`DashboardId`,`Template`,`SwitchId`,`Name`,`DashboardData`,`CreatedDate`,`LastUsedDate`) VALUES (5,'fourbuses',6,NULL,'{\"template\":\"fourbuses\",\"flipTo\":\"onetrain\",\"buses\":[\"149000006061\",\"149000006062\",\"149000006070\",\"149000006230\"],\"trains\":[\"BTN\"]}','2019-07-01 00:46:58','2019-07-01 00:46:58');
INSERT INTO `dashboard` (`DashboardId`,`Template`,`SwitchId`,`Name`,`DashboardData`,`CreatedDate`,`LastUsedDate`) VALUES (6,'onetrain',5,NULL,'{\"template\":\"onetrain\",\"buses\":[\"149000006061\",\"149000006062\",\"149000006070\",\"149000006230\"],\"flipTo\":\"fourbuses\",\"trains\":[\"BTN\"]}','2019-07-01 00:46:59','2019-07-01 00:46:59');
INSERT INTO `dashboard` (`DashboardId`,`Template`,`SwitchId`,`Name`,`DashboardData`,`CreatedDate`,`LastUsedDate`) VALUES (7,'single',NULL,'Opposite Brighton University','{\"template\":\"single\",\"name\":\"Opposite Brighton University\",\"buses\":[\"149000006062\"]}','2019-07-01 00:47:53','2019-07-01 00:47:53');
