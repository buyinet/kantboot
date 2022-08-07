// var path = "http://localhost/";
var path="https://aaahair.top/";

var url = {
	
	path: path,
	
	// 用户
	user: {
		login: path + "kantboot-system/user/login",
		join: path + "kantboot-system/user/join",
		loginOut: path + "kantboot-system/user/login_out",
		updatePassword: path + "kantboot-system/user/update_password",
		getUserInfo: path + "kantboot-system/user/get_user_info",
		save: path + "kantboot-system/user/save",
		findAll: path + "kantboot-system/user/find_all",
		findMine: path + "kantboot-system/user/find_mine",
		remove: path + "kantboot-system/user/remove",
		addBalanceYuan: path + "kantboot-system/user/add_balance_yuan",
		findMineList: path + "kantboot-system/user/find_mine_list",
		findCommonPage: path + "kantboot-system/user/find_common_page"
	},
	
	cesAuthUserByWechat: {
		loginByApplet: path + "kantboot-third-party/user_of_wechat/login"
	},
	
	authPayGoods: {
		createPayingParam: path + "kantboot-pay/pay_goods_parent/create_paying_param"
	},

	// 菜单
	menu: {
		save: path + "kantboot-system/menu/save",
		findAll: path + "kantboot-system/menu/find_all",
		findMine: path + "kantboot-system/menu/find_mine",
		findMineList: path + "kantboot-system/menu/find_mine_list",
		findCommonPage: path + "kantboot-system/menu/find_common_page",
		remove: path + "kantboot-system/menu/remove"
	},


	// 角色
	role: {
		save: path + "kantboot-system/role/save",
		findAll: path + "kantboot-system/role/find_all",
		remove: path + "kantboot-system/role/remove",
		findCommonPage: path + "kantboot-system/role/find_common_page",
		findCommonList: path + "kantboot-system/role/find_common_list"
	},

	permission: {
		save: path + "kantboot-system/permission/save",
		findAll: path + "kantboot-system/permission/find_all",
		remove: path + "kantboot-system/permission/remove",
		findCommonPage: path + "kantboot-system/permission/find_common_page",
		findCommonList: path + "kantboot-system/permission/find_common_list"
	},
	// 组织
	dept: {
		save: path + "kantboot-system/dept/save",
		findAll: path + "kantboot-system/dept/find_all",
		remove: path + "kantboot-system/dept/remove"
	},
	setting: {
		setRoleIdByUserJoin: path + "kantboot-system/setting/set_role_id_by_user_join",
		setAuthAppletWechatIdByUserJoin: path + "kantboot-system/setting/set_auth_applet_wechat_id_by_user_join",
		setAuthPayNotifyId: path + "kantboot-system/setting/set_auth_pay_notify_id",
		getSetting: path + "kantboot-system/setting/get_setting"
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
		save: path + "project-ranfa/ranfa_work/save",
		change: path + "project-ranfa/ranfa_work/change",
		findAll: path + "project-ranfa/ranfa_work/find_all",
		remove: path + "project-ranfa/ranfa_work/remove",
		findCommonPage:path + "project-ranfa/ranfa_work/find_common_page",
		findCommonList:path + "project-ranfa/ranfa_work/find_common_list",
		findCommonPageByBuySuccess:path+"project-ranfa/ranfa_work/find_common_page_by_user_self_buy"
	}
	

}

export default url;
