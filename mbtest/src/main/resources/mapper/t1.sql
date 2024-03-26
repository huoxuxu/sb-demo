CREATE TABLE
    `t1` (
             `id` int unsigned NOT NULL AUTO_INCREMENT,
             `code` varchar(50) NOT NULL DEFAULT '' COMMENT '编码',
             `name` varchar(50) NOT NULL DEFAULT '' COMMENT '名称',
             `score` decimal(10, 2) NOT NULL DEFAULT '0' COMMENT '成绩',
             `enabled` tinyint NOT NULL DEFAULT '0' COMMENT '启用',
             `birthday` datetime NULL COMMENT '生日',
             `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
             `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
             `creator` varchar(50) NOT NULL DEFAULT '' COMMENT '创建人',
             `modifier` varchar(50) NOT NULL DEFAULT '' COMMENT '修改人',
             PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
