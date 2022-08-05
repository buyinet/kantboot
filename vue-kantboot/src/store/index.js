import { createStore } from 'vuex'

export default createStore({
  state: {
    userInfo:{
        username:null,
        nickname:null,
        idCard:null,
        phoneNumber:null,
    },
	aside:{
        // 控制左侧导航栏的开关
		collapse:false
	},
    header:{
      // 页面标题
      pageTitle:null
    },
      /**
       * 面包屑
       */
    breadcrumb:[],
	menus:[]
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
  }
})
