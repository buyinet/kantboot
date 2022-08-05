// var path = "http://localhost/";
var path="https://aaahair.top/";

var url = {
	
	path: path,
	
	// 用户
	user: {
		login: path + "kantboot-user/user/login",
		join: path + "kantboot-user/user/join",
		loginOut: path + "kantboot-user/user/login_out",
		updatePassword: path + "kantboot-user/user/update_password",
		getUserInfo: path + "kantboot-user/user/get_user_info",
		save: path + "kantboot-user/user/save",
		findAll: path + "kantboot-user/user/find_all",
		findMine: path + "kantboot-user/user/find_mine",
		remove: path + "kantboot-user/user/remove",
		addBalanceYuan: path + "kantboot-user/user/add_balance_yuan",
		findMineList: path + "kantboot-user/user/find_mine_list",
		findCommonPage: path + "kantboot-user/user/find_common_page"
	},
	
	cesAuthUserByWechat: {
		loginByApplet: path + "kantboot-third-party/user_of_wechat/login"
	},
	
	authPayGoods: {
		createPayingParam: path + "kantboot-auth-user/auth_pay_goods/create_paying_param"
	},

	// 菜单
	menu: {
		save: path + "kantboot-user/menu/save",
		findAll: path + "kantboot-user/menu/find_all",
		findMine: path + "kantboot-user/menu/find_mine",
		findMineList: path + "kantboot-user/menu/find_mine_list",
		findCommonPage: path + "kantboot-user/menu/find_common_page",
		remove: path + "kantboot-user/menu/remove"
	},


	// 角色
	role: {
		save: path + "kantboot-user/role/save",
		findAll: path + "kantboot-user/role/find_all",
		remove: path + "kantboot-user/role/remove",
		findCommonPage: path + "kantboot-user/role/find_common_page",
		findCommonList: path + "kantboot-user/role/find_common_list"
	},

	permission: {
		save: path + "kantboot-user/permission/save",
		findAll: path + "kantboot-user/permission/find_all",
		remove: path + "kantboot-user/permission/remove",
		findCommonPage: path + "kantboot-user/permission/find_common_page",
		findCommonList: path + "kantboot-user/permission/find_common_list"
	},
	// 组织
	dept: {
		save: path + "kantboot-user/dept/save",
		findAll: path + "kantboot-user/dept/find_all",
		remove: path + "kantboot-user/dept/remove"
	},
	setting: {
		setRoleIdByUserJoin: path + "kantboot-user/setting/set_role_id_by_user_join",
		setAuthAppletWechatIdByUserJoin: path + "kantboot-user/setting/set_auth_applet_wechat_id_by_user_join",
		setAuthPayNotifyId: path + "kantboot-user/setting/set_auth_pay_notify_id",
		getSetting: path + "kantboot-user/setting/get_setting"
	},
	authWechatApplet: {
		save: path + "kantboot-auth-user/auth_wechat_applet/save",
		findAll: path + "kantboot-auth-user/auth_wechat_applet/find_all",
		remove: path + "kantboot-auth-user/auth_wechat_applet/remove",
		findCommonPage: path + "kantboot-auth-user/auth_wechat_applet/find_common_page",
		findCommonList: path + "kantboot-auth-user/auth_wechat_applet/find_common_list",
	},
	payWechatPay: {
		save: path + "kantboot-auth-user/auth_pay_wechat_pay/save",
		findAll: path + "kantboot-auth-user/auth_pay_wechat_pay/find_all",
		remove: path + "kantboot-auth-user/auth_pay_wechat_pay/remove",
		findCommonPage: path + "kantboot-auth-user/auth_pay_wechat_pay/find_common_page",
		findCommonList: path + "kantboot-auth-user/auth_pay_wechat_pay/find_common_list"
	},
	payNotify: {
		save: path + "kantboot-auth-user/auth_pay_notify/save",
		findAll: path + "kantboot-auth-user/auth_pay_notify/find_all",
		remove: path + "kantboot-auth-user/auth_pay_notify/remove",
		findCommonPage: path + "kantboot-auth-user/auth_pay_notify/find_common_page",
		findCommonList: path + "kantboot-auth-user/auth_pay_notify/find_common_list"
	},
	authGoodsParent: {
		save: path + "kantboot-auth-user/auth_goods_parent/save",
		findAll: path + "kantboot-auth-user/auth_goods_parent/find_all",
		remove: path + "kantboot-auth-user/auth_goods_parent/remove",
		findCommonPage: path + "kantboot-auth-user/auth_goods_parent/find_common_page",
		findCommonList: path + "kantboot-auth-user/auth_goods_parent/find_common_list"
	},
	fileOss: {
		save: path + "kantboot-file/file_oss/save",
		findAll: path + "kantboot-file/file_oss/find_all",
		remove: path + "kantboot-file/file_oss/remove",
		findCommonPage: path + "kantboot-file/file_oss/find_common_page",
		findCommonList: path + "kantboot-file/file_oss/find_common_list"
	},
	filePath: {
		save: path + "kantboot-file/file_path/save",
		findAll: path + "kantboot-file/file_path/find_all",
		remove: path + "kantboot-file/file_path/remove",
		findCommonPage: path + "kantboot-file/file_path/find_common_page",
		findCommonList: path + "kantboot-file/file_path/find_common_list"
	},
	fileParent: {
		save: path + "kantboot-file/file_parent/save",
		findAll: path + "kantboot-file/file_parent/find_all",
		remove: path + "kantboot-file/file_parent/remove",
		findCommonPage: path + "kantboot-file/file_parent/find_common_page",
		findCommonList: path + "kantboot-file/file_parent/find_common_list"
	},
	file: {
		save: path + "kantboot-file/file/save",
		findAll: path + "kantboot-file/file/find_all",
		remove: path + "kantboot-file/file/remove",
		findCommonPage: path + "kantboot-file/file/find_common_page",
		findCommonList: path + "kantboot-file/file/find_common_list",
		uniappBodyImage: path + "kantboot-file/file/visit/uniapp/bodyImage/",
	},
	
	ranfaBrand:{
		save: path + "ranfa/ranfa_brand/save",
		findAll: path + "ranfa/ranfa_brand/find_all",
		remove: path + "ranfa/ranfa_brand/remove",
		findCommonPage:path + "ranfa/ranfa_brand/find_common_page",
		findCommonList:path + "ranfa/ranfa_brand/find_common_list"
	},
	
	ranfaTechnique:{
		save: path + "ranfa/ranfa_technique/save",
		findAll: path + "ranfa/ranfa_technique/find_all",
		remove: path + "ranfa/ranfa_technique/remove",
		findCommonPage:path + "ranfa/ranfa_technique/find_common_page",
		findCommonList:path + "ranfa/ranfa_technique/find_common_list"
	},
	
	ranfaWork:{
		save: path + "ranfa/ranfa_work/save",
		change: path + "ranfa/ranfa_work/change",
		findAll: path + "ranfa/ranfa_work/find_all",
		remove: path + "ranfa/ranfa_work/remove",
		findCommonPage:path + "ranfa/ranfa_work/find_common_page",
		findCommonList:path + "ranfa/ranfa_work/find_common_list"
	}
	

}

export default url;
