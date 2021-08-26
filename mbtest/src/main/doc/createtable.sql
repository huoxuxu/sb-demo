CREATE TABLE `t1` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(50) NOT NULL,
  `name` varchar(200) NOT NULL,
  `score` double DEFAULT NULL,
  `enabled` tinyint(4) DEFAULT NULL,
  `birthday` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci
 COMMENT='t1表'