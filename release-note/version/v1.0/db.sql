CREATE DATABASE IF NOT EXISTS extension DEFAULT CHARACTER SET = utf8mb4;

Use extension;
-- ----------------------------
-- Table structure for extension_user
-- ----------------------------
CREATE TABLE `extension_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(50) NOT NULL COMMENT '应用名',
  `version` bigint(20) NOT NULL,
  `date_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `extension_spi` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NOT NULL,
  `spi_interface` varchar(128) NOT NULL,
  `description` varchar(128) DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `date_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `extension_extension` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `spi_id` bigint(20) NOT NULL,
  `biz_code` varchar(50) NOT NULL,
  `invoke_method` varchar(255) NOT NULL,
  `is_default` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否默认 1默认 0非默认',
  `expire_time` INT DEFAULT NULL,
  `remark` varchar(255) NOT NULL,
  `version` bigint(20) NOT NULL,
  `date_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;
