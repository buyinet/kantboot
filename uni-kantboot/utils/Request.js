var util = new Object();

util.isSuccess = function(res) {
	if (res.data.state == 2000) {
		return true;
	}
	return false;
}

/**
 * 向服务端发送请求
 * @param {Object} result
 */
util.request = function(result) {


	result.method = "POST";

	result.header = {
		"token": uni.getStorageSync("token")
	};

	result.complete = function(res) {
		var routes = getCurrentPages();
		var route = routes[routes.length - 1].route;
		uni.setStorageSync("PATH", route);
		// console.log(route!= "/pages/login/login");
		if (res.data.state == 7777 && route != "/pages/login/login") {
			uni.navigateTo({
				url: "/pages/login/login"
			})
			return;
		}
	};
	uni.request(result);
}

util.requestPromise = function(result) {
	return new Promise((resolve, reject) => {

		result.method = "POST";

		result.header = {
			"token": uni.getStorageSync("token")
		};

		result.complete = function(res) {
			var routes = getCurrentPages();
			var route = routes[routes.length - 1].route;
			uni.setStorageSync("PATH", route);

			if (res.status == 200) {
				if (res.data.state == 7777 && route != "/pages/login/login") {
					reject(res.data);
					uni.navigateTo({
						url: "/pages/login/login"
					})
				}
				
				if (result.success != null) {
					result.success(res.data);
					resolve(res.data);
				}
			
				if (res.data.state == 2000 && result.stateSuccess != null) {
					result.stateSuccess(res.data);
					resolve(res.data);
				}
			
				if (res.data.state != 2000 && result.stateFailed != null) {
					result.stateFailed(res.data);
					resolve(res.data);
				}
			

			}
			
		};
		
		uni.request(result);
	});
}

util.requestSync = async function(res) {
	var result=null;
	return await util.requestPromise(res);
}
export default util;
