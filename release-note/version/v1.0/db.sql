CREATE DATABASE IF NOT EXISTS extension DEFAULT CHARACTER SET = utf8mb4;

Use extension;
-- ----------------------------
-- Table structure for extension_user
-- ----------------------------
DROP TABLE IF EXISTS `extension_user`;
CREATE TABLE `extension_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `authorized_apps` varchar(255) DEFAULT NULL COMMENT '授权应用',
  `user_mobile` varchar(50) NOT NULL COMMENT '用户手机号',
  `user_name` varchar(50) NOT NULL COMMENT '用户姓名',
  `password` varchar(50) NOT NULL COMMENT '用户密码',
  `creator` varchar(50) NOT NULL COMMENT '用户创建者',
  `modifier` varchar(50) NOT NULL COMMENT '用户修改人',
  `role` varchar(20) DEFAULT NULL COMMENT '用户角色',
  `sys_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效 1有效 0无效',
  `date_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_mobile_sys_flag` (`user_mobile`,`sys_flag`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `extension_app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(50) NOT NULL COMMENT '应用名',
  `creator_id` bigint(20) NOT NULL,
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
  `invoke_method` varchar(50) NOT NULL,
  `is_default` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否默认 1默认 0非默认',
  `expire_time` INT DEFAULT NULL,
  `version` bigint(20) NOT NULL,
  `date_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `date_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4;

-- record
BEGIN;
INSERT INTO `extension_user` VALUES (1, 'test,finance-supply-core,demo', '15700718397', 'admin', 'admin', '', '系统管理员', 'admin', 1, now(), now());
INSERT INTO `extension_user` VALUES (2, 'finance-supply-core,test', '132454305341', '普通用户', 'spi', '', '系统管理员', 'user', 1, now(), now());

INSERT INTO `extension_app` VALUES (1, 'test-app', 1, 0,now(), now());
COMMIT;
