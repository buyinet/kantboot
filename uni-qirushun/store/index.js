import Vue from 'vue';
import Vuex from 'vuex';
import Api from '../utils/Api.js';

Vue.use(Vuex);

export default new Vuex.Store({
	state: {
		userInfoByWechatApplet: {
			nickName: null,
			gender: null,
			language: null,
			city: null,
			province: null,
			country: null,
			avatarUrl: null
		},
		brandByChange:{}
	},
	mutations: {

	},
	actions: {

	},
	modules: {}
})
