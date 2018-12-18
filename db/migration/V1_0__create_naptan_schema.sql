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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

INSERT INTO `test`.`naptan`
(
	`SMS`,
	`LongDescription`,
	`Active`,
	`SystemCodeNumber`,
	`Easting`,
	`Lat`,
	`Lng`,
	`Identifier`,
	`Northing`,
	`Retrieve`
)
VALUES
(
	'brimwtp',
	'Varley Halls',
	'True',
	'149000001995',
	'533160',
	'50.86423',
	'-0.10918',
	'o/s',
	'108895',
	1
),
(
    'bridpjt',
    'Natal Road',
    'True',
    '149000006061',
    '532480',
    '50.84302',
    '-0.01197',
    'adj',
    '106517',
    1
);