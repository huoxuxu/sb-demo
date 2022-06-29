CREATE TABLE `t1`
(
    `id`           bigint         NOT NULL AUTO_INCREMENT,
    `code`         varchar(50)    NOT NULL COMMENT '编码',
    `name`         varchar(150)   NOT NULL COMMENT '名称',
    `age`          int            NOT NULL DEFAULT '0' COMMENT '年龄',
    `score`        decimal(10, 4) NOT NULL DEFAULT '0.0000' COMMENT '成绩',
    `birthday`     datetime                DEFAULT NULL,
    `enabled`      tinyint        NOT NULL DEFAULT '0' COMMENT '是否启用',
    `createTime`   datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_create`   datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `remark`       varchar(255)            DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (`id`),
    UNIQUE KEY `t1_Code_IDX` (`code`,`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='t1表';