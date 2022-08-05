import {
	createApp
} from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import request from '@/api/request.js';
import api from '@/api/api.js';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue';
import {
	ElMessage
} from 'element-plus';
import {
	ElNotification
} from 'element-plus';
import VueMarkdownEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'; 
import Prism from 'prismjs';
import mavonEditor from 'mavon-editor'import 'mavon-editor/dist/css/index.css'

VueMarkdownEditor.use(vuepressTheme, {
	Prism,
});


var icons = [];
var app = createApp(App);
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
	app.component("IconEl" + key, component);
	icons.push("IconEl" + key);
}
app.config.globalProperties.$icons = icons;
app.config.globalProperties.$request = request;
app.config.globalProperties.$api = api;
app.config.globalProperties.$message = ElMessage;
app.config.globalProperties.$notification = ElNotification;

app.use(store);
app.use(router);
app.use(ElementPlus);
app.use(VueMarkdownEditor);
app.use(mavonEditor);
app.mount('#app');
