CREATE TABLE `timetable` (
  `TimetableId` int(11) NOT NULL AUTO_INCREMENT,
  `TimetableName` varchar(100) NOT NULL,
  `TimetableData` varchar(5000) NOT NULL,
  `CreatedDate` datetime NOT NULL,
  `LastUsedDate` datetime NOT NULL,
  PRIMARY KEY (`TimetableId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

INSERT INTO `timetable` (`TimetableId`,`TimetableName`,`TimetableData`,`CreatedDate`,`LastUsedDate`) VALUES (1,'Bungay High School','18:00,BH2,Halesworth','2019-06-29 14:21:01','2019-06-29 14:25:51');
INSERT INTO `timetable` (`TimetableId`,`TimetableName`,`TimetableData`,`CreatedDate`,`LastUsedDate`) VALUES (2,'Bungay Middle School','18:00,BH2,Halesworth\r\n19:00,BH2,Halesworth','2019-06-29 14:23:34','2019-06-29 14:27:45');
INSERT INTO `timetable` (`TimetableId`,`TimetableName`,`TimetableData`,`CreatedDate`,`LastUsedDate`) VALUES (3,'Bungay Primary School','15:00,UB1,Old Steine\r\n16:00,UB2,Falmer Campus\r\n17:00,UB1,Old Steine\r\n18:00,UB2,Falmer Campus\r\n19:00,UB1,Old Steine\r\n20:00,UB2,Falmer Campus\r\n21:00,UB1,Old Steine\r\n22:00,UB2,Falmer Campus\r\n23:59,UB1,Old Steine','2019-06-29 14:23:34','2019-06-29 14:27:45');
