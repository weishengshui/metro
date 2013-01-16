
alter table MerchandiseFile add width int;
alter table MerchandiseFile add height int;

alter table FileItem add width int;
alter table FileItem add height int;

/*
Navicat MySQL Data Transfer

Source Server         : metro
Source Server Version : 50154
Source Host           : 192.168.4.97:3306
Source Database       : metro

Target Server Type    : MYSQL
Target Server Version : 50154
File Encoding         : 65001

Date: 2013-01-06 10:18:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `Resources`
-- ----------------------------
DROP TABLE IF EXISTS `Resources`;
CREATE TABLE `Resources` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `button_rights` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `type` int(255) DEFAULT NULL,
  `url` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of Resources
-- ----------------------------
INSERT INTO `Resources` VALUES ('1', '会员信息管理', '1', '0', null);
INSERT INTO `Resources` VALUES ('2', '会员信息注册', '2', '1', 'member/memberRegist');
INSERT INTO `Resources` VALUES ('3', '会员信息维护', '3', '1', 'member/memberIndex');
INSERT INTO `Resources` VALUES ('4', '合作品牌管理', '3', '0', null);
INSERT INTO `Resources` VALUES ('5', '品牌新增', '4', '4', 'brand/show');
INSERT INTO `Resources` VALUES ('6', '品牌维护', '4', '4', 'brand/list');
INSERT INTO `Resources` VALUES ('7', '站台管理', '5', '0', 'xxx');
INSERT INTO `Resources` VALUES ('8', '站台信息管理', null, '7', null);
INSERT INTO `Resources` VALUES ('9', '站点新增', null, '8', 'line/sitePage');
INSERT INTO `Resources` VALUES ('10', '站点维护', null, '8', 'line/siteIndex');
INSERT INTO `Resources` VALUES ('11', '线路新增', null, '8', 'line/linePage');
INSERT INTO `Resources` VALUES ('12', '线路维护', null, '8', 'line/index');
INSERT INTO `Resources` VALUES ('13', '门店管理', null, '7', null);
INSERT INTO `Resources` VALUES ('14', '门店新增', null, '13', 'line/shopPage');
INSERT INTO `Resources` VALUES ('15', '门店维护', null, '13', null);
INSERT INTO `Resources` VALUES ('16', '门店关系维护', null, '13', null);
INSERT INTO `Resources` VALUES ('17', '活动管理', null, '0', null);
INSERT INTO `Resources` VALUES ('18', '活动新增', null, '17', null);
INSERT INTO `Resources` VALUES ('19', '活动维护', null, '17', null);
INSERT INTO `Resources` VALUES ('20', '商品管理', null, '0', null);
INSERT INTO `Resources` VALUES ('21', '商品类别新增', null, '20', 'category/add');
INSERT INTO `Resources` VALUES ('22', '商品类型维护', null, '20', 'category/maintain');
INSERT INTO `Resources` VALUES ('23', '商品新增', null, '20', 'merchandise/add');
INSERT INTO `Resources` VALUES ('24', '商品维护', null, '20', 'merchandise/list');
INSERT INTO `Resources` VALUES ('25', '商品类别与商品维护', null, '20', 'merchandise/merCate');
INSERT INTO `Resources` VALUES ('26', '积分信息及规则管理', null, '0', null);
INSERT INTO `Resources` VALUES ('27', '积分管理', null, '26', null);
INSERT INTO `Resources` VALUES ('28', '积分基本信息维护', null, '27', 'integralManagement/show');
INSERT INTO `Resources` VALUES ('29', '积分信息历史', null, '27', 'integralManagement/unitLedger');
INSERT INTO `Resources` VALUES ('30', '积分失效管理', null, '26', null);
INSERT INTO `Resources` VALUES ('31', '失效积分统计', null, '30', null);
INSERT INTO `Resources` VALUES ('32', '失效积分历史', null, '30', null);
INSERT INTO `Resources` VALUES ('33', '积分规则定义', null, '26', null);
INSERT INTO `Resources` VALUES ('34', '会员生日积分倍数', null, '33', 'integralRule/birthRule');
INSERT INTO `Resources` VALUES ('35', '会员消费积分规则倍数', null, '33', 'integralRule/ruleIndex');
INSERT INTO `Resources` VALUES ('36', '勋章奖励规则', null, '0', null);
INSERT INTO `Resources` VALUES ('37', '勋章奖励规则新增', null, '36', null);
INSERT INTO `Resources` VALUES ('38', '勋章奖励规则维护', null, '36', null);
INSERT INTO `Resources` VALUES ('39', '短信营销', null, '0', null);
INSERT INTO `Resources` VALUES ('40', '任务新增', null, '39', 'message/add');
INSERT INTO `Resources` VALUES ('41', '任务维护', null, '39', 'message/list');
INSERT INTO `Resources` VALUES ('42', '报表', null, '0', null);
INSERT INTO `Resources` VALUES ('43', '会员分析报表', null, '42', null);
INSERT INTO `Resources` VALUES ('44', '积分分析报表', null, '42', null);
INSERT INTO `Resources` VALUES ('45', '系统管理', null, '0', null);
INSERT INTO `Resources` VALUES ('46', '用户管理', null, '45', 'user/userIndex');
INSERT INTO `Resources` VALUES ('47', '角色管理', null, '45', 'user/roleList');

/*
Navicat MySQL Data Transfer

Source Server         : metro
Source Server Version : 50154
Source Host           : 192.168.4.97:3306
Source Database       : metro

Target Server Type    : MYSQL
Target Server Version : 50154
File Encoding         : 65001

Date: 2013-01-06 12:21:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `Role`
-- ----------------------------
DROP TABLE IF EXISTS `Role`;
CREATE TABLE `Role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_desc` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `role_name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of Role
-- ----------------------------
INSERT INTO `Role` VALUES ('1', '23', '2222');
INSERT INTO `Role` VALUES ('2', '2', '2332432');
INSERT INTO `Role` VALUES ('3', '系统管理员', '系统管理员');
INSERT INTO `Role` VALUES ('4', '测试权限', '测试');



/*
Navicat MySQL Data Transfer

Source Server         : metro
Source Server Version : 50154
Source Host           : 192.168.4.97:3306
Source Database       : metro

Target Server Type    : MYSQL
Target Server Version : 50154
File Encoding         : 65001

Date: 2013-01-06 12:21:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `RoleResources`
-- ----------------------------
DROP TABLE IF EXISTS `RoleResources`;
CREATE TABLE `RoleResources` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resources_id` int(255) DEFAULT NULL,
  `rights` int(11) DEFAULT NULL,
  `role_id` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=110 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of RoleResources
-- ----------------------------
INSERT INTO `RoleResources` VALUES ('36', '2', '2', '4');
INSERT INTO `RoleResources` VALUES ('29', '6', '4', '1');
INSERT INTO `RoleResources` VALUES ('34', '2', '2', '2');
INSERT INTO `RoleResources` VALUES ('33', '3', null, '1');
INSERT INTO `RoleResources` VALUES ('37', '3', null, '4');
INSERT INTO `RoleResources` VALUES ('73', '2', '2', '3');
INSERT INTO `RoleResources` VALUES ('74', '3', null, '3');
INSERT INTO `RoleResources` VALUES ('75', '5', null, '3');
INSERT INTO `RoleResources` VALUES ('76', '6', '4', '3');
INSERT INTO `RoleResources` VALUES ('77', '8', null, '3');
INSERT INTO `RoleResources` VALUES ('78', '9', null, '3');
INSERT INTO `RoleResources` VALUES ('79', '10', null, '3');
INSERT INTO `RoleResources` VALUES ('80', '11', null, '3');
INSERT INTO `RoleResources` VALUES ('81', '12', null, '3');
INSERT INTO `RoleResources` VALUES ('82', '13', null, '3');
INSERT INTO `RoleResources` VALUES ('83', '14', null, '3');
INSERT INTO `RoleResources` VALUES ('84', '15', null, '3');
INSERT INTO `RoleResources` VALUES ('85', '16', null, '3');
INSERT INTO `RoleResources` VALUES ('86', '18', null, '3');
INSERT INTO `RoleResources` VALUES ('87', '19', null, '3');
INSERT INTO `RoleResources` VALUES ('88', '21', null, '3');
INSERT INTO `RoleResources` VALUES ('89', '22', null, '3');
INSERT INTO `RoleResources` VALUES ('90', '35', null, '3');
INSERT INTO `RoleResources` VALUES ('91', '23', null, '3');
INSERT INTO `RoleResources` VALUES ('92', '24', null, '3');
INSERT INTO `RoleResources` VALUES ('93', '25', null, '3');
INSERT INTO `RoleResources` VALUES ('94', '27', null, '3');
INSERT INTO `RoleResources` VALUES ('95', '28', null, '3');
INSERT INTO `RoleResources` VALUES ('96', '29', null, '3');
INSERT INTO `RoleResources` VALUES ('97', '30', null, '3');
INSERT INTO `RoleResources` VALUES ('98', '31', null, '3');
INSERT INTO `RoleResources` VALUES ('99', '32', null, '3');
INSERT INTO `RoleResources` VALUES ('100', '33', null, '3');
INSERT INTO `RoleResources` VALUES ('101', '34', null, '3');
INSERT INTO `RoleResources` VALUES ('102', '37', null, '3');
INSERT INTO `RoleResources` VALUES ('103', '38', null, '3');
INSERT INTO `RoleResources` VALUES ('104', '40', null, '3');
INSERT INTO `RoleResources` VALUES ('105', '41', null, '3');
INSERT INTO `RoleResources` VALUES ('106', '43', null, '3');
INSERT INTO `RoleResources` VALUES ('107', '44', null, '3');
INSERT INTO `RoleResources` VALUES ('108', '46', null, '3');
INSERT INTO `RoleResources` VALUES ('109', '47', null, '3');



/*
Navicat MySQL Data Transfer

Source Server         : metro
Source Server Version : 50154
Source Host           : 192.168.4.97:3306
Source Database       : metro

Target Server Type    : MYSQL
Target Server Version : 50154
File Encoding         : 65001

Date: 2013-01-06 12:20:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `UserInfo`
-- ----------------------------
DROP TABLE IF EXISTS `UserInfo`;
CREATE TABLE `UserInfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `disable` int(11) DEFAULT NULL,
  `password` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `userName` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of UserInfo
-- ----------------------------
INSERT INTO `UserInfo` VALUES ('1', '0', '21232f297a57a5a743894a0e4a801fc3', 'admin');
INSERT INTO `UserInfo` VALUES ('2', '0', '098f6bcd4621d373cade4e832627b4f6', 'test');


/*
Navicat MySQL Data Transfer

Source Server         : metro
Source Server Version : 50154
Source Host           : 192.168.4.97:3306
Source Database       : metro

Target Server Type    : MYSQL
Target Server Version : 50154
File Encoding         : 65001

Date: 2013-01-06 12:21:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `UserRole`
-- ----------------------------
DROP TABLE IF EXISTS `UserRole`;
CREATE TABLE `UserRole` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(255) DEFAULT NULL,
  `user_id` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of UserRole
-- ----------------------------
INSERT INTO `UserRole` VALUES ('16', '4', '2');
INSERT INTO `UserRole` VALUES ('15', '3', '1');


