create database if not EXISTS webpage;
use webpage;

CREATE TABLE IF NOT EXISTS `keyword` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`keyword` varchar(767),
`relationship` MEDIUMTEXT,
PRIMARY KEY (`id`)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8;