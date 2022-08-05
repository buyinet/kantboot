import Vue from 'vue';
import App from './App';
import Request from './utils/Request.js';
import Api from './utils/Api.js';
import store from './store';
import uView from '@/uni_modules/uview-ui'

Vue.use(uView)

Vue.prototype.$store = store;

// 如此配置即可
uni.$u.config.unit = 'rpx';

Vue.config.productionTip = false;

App.mpType = 'app';

Vue.prototype.$request = Request;
Vue.prototype.$api = Api;


const app = new Vue({
    ...App
});
app.$mount();
