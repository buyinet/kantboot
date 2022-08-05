import axios from 'axios';
import api from './api.js';
import store from '@/store'

import {
    ElMessage
} from 'element-plus';

let config = {
    baseURL: "http://localhost",
    timeout: 10000,
    withCredentials: false
};

const instance = axios.create(config);

// 设置axios的返回拦截（还可以设置request拦截，这里暂时用不上）
instance.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.message.includes('timeout')) {   // 判断请求异常信息中是否含有超时timeout字符串
            console.log("错误回调", error);
            ElMessage({
                message: "网络超时",
                type: 'error'
            });
            return Promise.reject(error);          // reject这个错误信息
        }
        return Promise.reject(error);
    });

var request = new Object();
request.post = function (result) {
    instance({
        method: 'POST',
        url: result.url,
        data: result.data,
        headers: {
            "token": localStorage.getItem("token"),
			"scene":"wechat_applet"
        }
    }).then(function (res) {
        //如果请求状态成功
        if (res.status == 200) {

            if (result.success != null) {
                result.success(res.data);
                return false;
            }

            if (res.data.state == 2000 && result.stateSuccess != null) {
                result.stateSuccess(res.data);
                return false;
            }

            if (res.data.state != 2000 && result.stateFailed != null) {
                ElMessage({
                    message: res.data.errMsg,
                    type: 'error'
                });
                result.stateFailed(res.data);
                return false;
            }

            return false;
        }

        if (result.failed != null) {
            result.failed(res);
        }

    });
    ;

}

request.getMenus = function () {
    request.post({
        url: api.menu.findMineList,
        data: {
            // "pageNumber": 1,
            // "pageSize": 9999,
            // "sortType": "DESC",
            // "sortField": "priority",
            // "data": {}
        },
        stateSuccess: (res) => {
            // 将菜单存储到缓存中
            localStorage.setItem("menus", JSON.stringify(res.data));
            store.state.menus = res.data;
        },
        stateFailed: (res) => {

        }
    });
}

export default request;
