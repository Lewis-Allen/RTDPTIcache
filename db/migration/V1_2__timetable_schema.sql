CREATE TABLE `timetable` (
  `TimetableId` int(11) NOT NULL AUTO_INCREMENT,
  `TimetableName` varchar(100) NOT NULL,
  `TimetableData` varchar(5000) NOT NULL,
  `CreatedDate` datetime NOT NULL,
  `LastUsedDate` datetime NOT NULL,
  PRIMARY KEY (`TimetableId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;