create database if not EXISTS webpage;
use webpage;

CREATE TABLE IF NOT EXISTS `keyword` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`keyword` varchar(767),
`relationship` varchar(767),
PRIMARY KEY (`id`)
) ENGINE=InnoDB
ROW_FORMAT=COMPRESSED
DEFAULT CHARSET=utf8mb4;