SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
      `creator` varchar(50) DEFAULT NULL COMMENT '用户创建者',
      `modifier` varchar(50) DEFAULT NULL COMMENT '用户修改人',
      `role` varchar(20) DEFAULT NULL COMMENT '用户角色',
      `sys_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效 1有效 0无效',
      `date_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `date_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      PRIMARY KEY (`id`),
      UNIQUE KEY `unidx_mobile` (`user_mobile`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `t_user`
    ( `authorized_apps`
    , `user_mobile`
    , `user_name`
    , `password`
    , `creator`
    , `modifier`
    , `role`
    , `sys_flag` )
VALUES
( '', '', '系统管理员', 'spi', '', '', 'admin', 1 ),
( '', '', '游客', 'spi', 'admin', 'admin', 'visitor', 1 );
