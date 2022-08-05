<template>
	<view>
		<component v-show="pageComponent=='pageIndex'" is="pageIndex"></component>
		<component v-show="pageComponent=='pageHome'" is="pageHome"></component>

<view class="tabbar-view" :style="'bottom:'+tabBar.bottom+'rpx;'+'width:'+tabBar.width+'rpx'">
	
<u-tabbar
	:value="tabBar.selectedIndex"
	@change="tabBarChange"
	:inactiveColor="tabBar.color"
	:activeColor="tabBar.selectedColor"
	:fixed="false"
	:border="false"
	:placeholder="false"
	:safeAreaInsetBottom="false"
>

	<u-tabbar-item 
		v-for="(item,index) in tabBar.list" 
		:text="item.text">
		<image
			class="u-page__item__slot-icon"
			slot="active-icon"
			:src="item.selectedIconPath"
		></image>
		<image
			class="u-page__item__slot-icon"
			slot="inactive-icon"
			:src="item.iconPath"
		></image>
	</u-tabbar-item>


</u-tabbar>
</view>

	</view>
</template>

<script>
	import pageIndex from '../../commpents/pageIndex/pageIndex.vue';
	import pageHome from '../../commpents/pageHome/pageHome.vue';

	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;
	
	export default {
		components:{
			pageIndex,
			pageHome
		},
		data() {
			return {
				path:Api.path,
				interval:null,
				pageComponent:"pageIndex",
				"tabBar": {
					bottom:30,
					width:700,
					selectedIndex:0,
					"color": "#8a8a8a",
					"selectedColor": "#5EB2F3",
					"borderStyle": "white",
					"backgroundColor": "#FFFFFF",
					"list": [
						{
						"pagePath": "pages/index/index",
						"text": "首页",
						component:"pageIndex",
						"iconPath":Api.path+"kantboot-file/file/visit/tabbar/icon/index",
						"selectedIconPath":Api.path+"kantboot-file/file/visit/tabbar/icon/index_selected"
					}, 
					{
					    "pagePath" : "pages/play/play",
					    "text": "正在播放",
						component:"pageIndex1",
						"iconPath":Api.path+"kantboot-file/file/visit/tabbar/icon/video",
						"selectedIconPath":Api.path+"kantboot-file/file/visit/tabbar/icon/video_selected"
					},
					{
						"pagePath": "pages/home/home",
						"text": "我的",
						component:"pageHome",
						"iconPath":Api.path+"kantboot-file/file/visit/tabbar/icon/mine",
						"selectedIconPath":Api.path+"kantboot-file/file/visit/tabbar/icon/mine_selected"
					}
					]
				},
			}
		},
		methods: {
			tabBarChange(index){
				this.tabBar.selectedIndex=index;
				this.pageComponent=this.tabBar.list[index].component;
				if(this.interval!=null){
					clearInterval(this.interval);
				}
				if(this.pageComponent!="pageIndex"){
					this.tabBar.bottom=0;
					this.tabBar.width=750;
					// this.interval=setInterval(()=>{
					// 	if(this.tabBar.bottom>0){
					// 		this.tabBar.bottom-=3;
					// 	}
					// 	if(this.tabBar.width<750){
					// 		this.tabBar.width+=3;
					// 	}
					// 	if(this.tabBar.width>=750&&this.tabBar.bottom<=0){
					// 		clearInterval(this.interval);
					// 	}
					// },13);
				}else{
					this.tabBar.width=700;
					this.tabBar.bottom=40;
				// 	this.interval=setInterval(()=>{
				// 		if(this.tabBar.bottom<40){
				// 			this.tabBar.bottom+=3;
				// 		}
				// 		if(this.tabBar.width>700){
				// 			this.tabBar.width-=3;
				// 		}
				// 		if(this.tabBar.width<=700&&this.tabBar.bottom>=40){
				// 			clearInterval(this.interval);
				// 		}
				// 	},13);
				}
			}
		}
	}
</script>

<style>
.u-page__item__slot-icon{
	height: 40rpx;
	width: 40rpx;
}
.tabbar{
	
}
.tabbar-view{
	position: fixed;
	bottom: 40rpx;
	width: 700rpx;
	margin-left: 50%;
	padding: 10rpx;
	background-color: #FFF;
	border-radius: 20rpx;
	box-shadow: 0 0 10rpx rgba(118,118,118,.2);
	transform: translateX(-50%);
}
</style>
