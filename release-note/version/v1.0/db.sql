CREATE DATABASE IF NOT EXISTS spi_admin DEFAULT CHARACTER SET = utf8mb4;

Use spi_admin;
-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
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

-- record
BEGIN;
INSERT INTO `t_user` VALUES (1, 'test,finance-supply-core,demo', '15700718397', 'admin', 'admin', '', '系统管理员', 'admin', 1, now(), now());
INSERT INTO `t_user` VALUES (2, 'finance-supply-core,test', '132454305341', '普通用户', 'spi', '', '系统管理员', 'user', 1, now(), now());
COMMIT;
