CREATE TABLE `naptan` (
  `SMS` text,
  `LongDescription` text,
  `Active` text,
  `SystemCodeNumber` text,
  `Easting` text,
  `Lat` text,
  `Lng` text,
  `Identifier` text,
  `Northing` text,
  `Retrieve` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8_general_ci;