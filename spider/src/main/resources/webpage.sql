create database if not EXISTS webpage;
use webpage;

CREATE TABLE IF NOT EXISTS `webpage` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`headers` blob,
`text` longtext DEFAULT NULL, 
`status` int(11) DEFAULT NULL,
`batchId` varchar(32) CHARACTER SET latin1 DEFAULT NULL,
`score` float DEFAULT NULL,
`baseUrl` varchar(255) DEFAULT NULL,
`content` longtext,
`title` varchar(767) DEFAULT NULL,
`parentUrl` varchar(255) DEFAULT NULL,
`inlinks` mediumblob,
`outlinks` mediumblob,
`fetchTime` bigint(20) DEFAULT NULL,
PRIMARY KEY (`id`),
UNIQUE KEY (`baseUrl`)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8;