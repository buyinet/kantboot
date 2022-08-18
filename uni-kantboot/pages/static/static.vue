<template>
	<view>
		<component v-show="pageComponent=='pageIndex'" is="pageIndex"></component>
		<component v-show="pageComponent=='pagePlay'" is="pagePlay"></component>
		<component v-show="pageComponent=='pageHome'" is="pageHome"></component>


		<u-popup :overlayOpacity=".7" :show="checkShow" :customStyle="{overflowY: 'scroll'}" :safeAreaInsetTop="true"
			@close="closeCheck" @open="openCheck" class="brand-box" mode="right">
<!-- 			<view>
				<image src="../../static/logo2.png" mode="widthFix" style="
				 margin-top: 50rpx;
					width: 100rpx;
					margin-left: 50%;
					transform: translateX(-50%);
				 "></image>
			</view> -->
			<view style="height: 30rpx;"></view>
			<view style="margin-top: 50rpx;text-align: center;font-weight: bold;color: rgba(0,0,0,.2);">
				品牌选择
			</view>
			<view class="brand" @click="brandToChange(null);closeCheck()">
				<text v-if="brandByChange.id!=null">全部</text>
				<text v-if="brandByChange.id==null" style="opacity: .4;">全部</text>
			</view>
			<view class="brand" @click="brandToChange(item.id);closeCheck()" v-for="(item,index) in ranfaBrands">
				<text v-if="item.id!=brandByChange.id">
					{{item.name}}</text>
				<text v-if="item.id==brandByChange.id" style="opacity: .4;">{{item.name}}</text>
			</view>
		</u-popup>

		<u-popup :overlayOpacity=".7" :show="checkShow" :customStyle="{overflowY: 'scroll'}"
			:safeAreaInsetTop="true" @close="closeCheck" @open="openCheck" class="brand-box" mode="left">
	<!-- 		<view>
				<image src="../../static/logo2.png" mode="widthFix" style="
				 margin-top: 50rpx;
					width: 100rpx;
					margin-left: 50%;
					transform: translateX(-50%);
				 "></image>
			</view> -->
			<view style="margin-top:30rpx;text-align: center;font-weight: bold;color: rgba(0,0,0,.2);">
				分类选择
			</view>
			<view class="brand" @click="techniqueToChange(null);closeCheck()"
			v-if="null!=techniqueByChange.id"
			>
				全部
			</view>
			<view class="brand" 
			v-if="null==techniqueByChange.id"
			>
				全部
			</view>
			<view class="brand" v-for="(item,index) in ranfaTechniques"
			@click="techniqueToChange(item.id);closeCheck()"
			>
				<text v-if="item.id!=techniqueByChange.id">{{item.name}}</text>
				<text v-if="item.id==techniqueByChange.id"
					style="opacity: .7;">{{item.name}}</text>
			</view>
		</u-popup>


		<view class="tabbar-view" :style="'bottom:'+tabBar.bottom+'rpx;'+'width:'+tabBar.width+'rpx'">

			<u-tabbar :value="tabBar.selectedIndex" @change="tabBarChange" :inactiveColor="tabBar.color"
				:activeColor="tabBar.selectedColor" :fixed="false" :border="false" :placeholder="false"
				:safeAreaInsetBottom="false">

				<u-tabbar-item v-for="(item,index) in tabBar.list" :text="item.text">
					<image class="u-page__item__slot-icon" slot="active-icon" :src="item.selectedIconPath"></image>
					<image class="u-page__item__slot-icon" slot="inactive-icon" :src="item.iconPath"></image>
				</u-tabbar-item>


			</u-tabbar>
		</view>
		<u-toast ref="uToast"></u-toast>
	</view>
</template>

<script>
	import pageIndex from '../../commpents/pageIndex/pageIndex.vue';
	import pageHome from '../../commpents/pageHome/pageHome.vue';
	import pagePlay from '../../commpents/pagePlay/pagePlay.vue';

	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;

	export default {

		components: {
			pageIndex,
			pageHome,
			pagePlay
		},
		onLoad(option) {
			var pageComponent = option.pageComponent;
			if (pageComponent == null) {
				this.pageComponent = "pageIndex";
			} else {
				this.pageComponent = pageComponent;
			}
			var tabBarList = this.tabBar.list;
			for (var i = 0; i < tabBarList.length; i++) {
				if (tabBarList[i].component == pageComponent) {
					this.tabBar.selectedIndex = i;
				}
			}
			this.tabBarChange(this.tabBar.selectedIndex);
			this.getRanfaBrands();
			this.getBrandByChange();
		},
		async onShareAppMessage(res) {
			return {
				title: '超多染发技巧，尽在染发一号！', //标题
				path: '/pages/static/static', //可以指定动态路径
				imageUrl: 'https://ranfa.oss-cn-hangzhou.aliyuncs.com/watermark/2022/8/17/001.png', //分享图
				desc: '染发一号'
			};
		},
		//2.分享到朋友圈
		onShareTimeline(res) {
			return {
				title: '超多染发技巧，尽在染发一号！', //标题
				path: '/pages/static/static', //可以指定动态路径
				imageUrl: 'https://ranfa.oss-cn-hangzhou.aliyuncs.com/watermark/2022/8/17/001.png', //分享图
				desc: '染发一号'
			};
		},
		data() {
			return {
				techniqueByChange:{},
				brandByChange: {},
				ranfaBrands: [],
				checkShow: false,
				checkTypeShow: false,
				path: Api.path,
				interval: null,
				ranfaTechniques: [],
				pageComponent: "pageIndex",
				"tabBar": {
					bottom: 30,
					width: 700,
					selectedIndex: 0,
					"color": "#8a8a8a",
					"selectedColor": "#5EB2F3",
					"borderStyle": "white",
					"backgroundColor": "#FFFFFF",
					"list": [{
							"pagePath": "pages/index/index",
							"text": "首页",
							component: "pageIndex",
							"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/index",
							"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/index_selected"
						},
						{
							"pagePath": "pages/play/play",
							"text": "正在播放",
							component: "pagePlay",
							"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/video",
							"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/video_selected"
						},
						{
							"pagePath": "pages/home/home",
							"text": "我的",
							component: "pageHome",
							"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/mine",
							"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/mine_selected"
						}
					]
				},

				"listByNoIndex": [{
						"pagePath": "pages/index/index",
						"text": "首页",
						component: "pageIndex",
						"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/index",
						"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/index_selected"
					},
					{
						"pagePath": "pages/play/play",
						"text": "正在播放",
						component: "pagePlay",
						"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/video",
						"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/video_selected"
					},
					{
						"pagePath": "pages/home/home",
						"text": "我的",
						component: "pageHome",
						"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/mine",
						"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/mine_selected"
					}
				],
				"listByIndex": [
					{
						"pagePath": null,
						"text": "分类",
						"method": "openCheckType",
						component: null,
						"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/check_selected",
						"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/check_selected"
					},
					{
						"pagePath": "pages/index/index",
						"text": "首页",
						selectedIndex:1,
						component: "pageIndex",
						"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/index",
						"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/index_selected"
					},
					{
						"pagePath": "pages/play/play",
						"text": "正在播放",
						selectedIndex:1,
						
						component: "pagePlay",
						"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/video",
						"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/video_selected"
					},
					{
						"pagePath": "pages/home/home",
						"text": "我的",
						selectedIndex:2,
						
						component: "pageHome",
						"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/mine",
						"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/mine_selected"
					},
					// {
					// 	"pagePath": null,
					// 	"text": "品牌",
					// 	"method": "openCheck",
					// 	component: null,
					// 	"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/check_selected",
					// 	"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/check_selected"
					// },
				],
			}
		},
		mounted() {
			this.getRanfaTechniques();
		},
		
		methods: {
			getTechniqueByChange() {
				Request.request({
					url: Api.ranfaWork.techniqueByChange,
					success: (res) => {
						this.techniqueByChange = res.data.data;
						this.$store.state.techniqueByChange = this.techniqueByChange;
					}
				});
			},
			techniqueToChange(techniqueId){
				if(this.techniqueId!=null){
					this.brandToChange(null);
					this.brandByChange={id:null};
					
				}
				Request.request({
					url: Api.ranfaWork.techniqueToChange,
					data: {
						"ranfaTechniqueId": techniqueId
					},
					success: (res) => {
				
						this.getTechniqueByChange();
						this.$refs.uToast.show({
							message: "点击切换后生效"
						});

						this.closeCheckType();
					}
				});
			},
			getRanfaTechniques() {
				Request.request({
					url: Api.ranfaTechnique.findCommonList,
					data: {
						entity: {}
					},
					success: (res) => {

						this.ranfaTechniques = res.data.data;
						// console.log(JSON.stringify(res));
						this.$forceUpdate();
					}
				});
			},
			brandToChange(ranfaBrandId) {
				if(ranfaBrandId!=null){
					this.techniqueToChange(null);
					this.techniqueByChange={id:null};
				}
				Request.request({
					url: Api.ranfaWork.brandToChange,
					data: {
						"ranfaBrandId": ranfaBrandId
					},
					success: (res) => {
						this.getBrandByChange();
						this.$refs.uToast.show({
							message: "点击切换后生效"
						});
						this.closeCheck();
					}
				});
			},
			getBrandByChange() {
				Request.request({
					url: Api.ranfaWork.brandByChange,
					success: (res) => {
						this.brandByChange = res.data.data;
						console.log(JSON.stringify(this.brandByChange));
						this.$store.state.brandByChange = this.brandByChange;
					}
				});
			},
			closeCheck() {
				this.checkShow = false;
				this.checkTypeShow = false;
			},
			openCheck() {
				this.checkShow = true;
			},
			closeCheckType() {
				this.checkTypeShow = false
			},
			openCheckType() {
				this.checkTypeShow = true;
			},
			getRanfaBrands() {
				Request.request({
					url: Api.ranfaBrand.findCommonList,
					data: {
						entity: {}
					},
					success: (res) => {

						this.ranfaBrands = res.data.data;
						// console.log(JSON.stringify(res));
						this.$forceUpdate();
					}
				});
			},
			toCheck() {

			},
			tabBarChange(index) {
				var tabBarList = this.tabBar.list[index];
				
				if (this.tabBar.list[index].component == null) {
					if (tabBarList.method == "openCheckType") {
						this.openCheckType();
						this.openCheck();
					}
				}
				
				if (this.tabBar.list[index].component != null) {
					this.tabBar.selectedIndex = index;
					this.pageComponent = this.tabBar.list[index].component;
				}

				if (this.interval != null) {
					clearInterval(this.interval);
				}

				if (this.pageComponent != "pageIndex") {
					if(this.pageComponent == "pagePlay"){
						this.tabBar.selectedIndex=1;
					}
					if(this.pageComponent == "pageHome"){
						this.tabBar.selectedIndex=2;
					}
					// this.tabBar.selectedIndex=tabBarList.selectedIndex;
					// this.tabBar.bottom = 0;
					// this.tabBar.width = 750;
					this.tabBar.list = this.listByNoIndex;
					this.$forceUpdate();
					this.interval=setInterval(()=>{
						if(this.tabBar.bottom>0){
							this.tabBar.bottom-=3;
						}
						if(this.tabBar.width<750){
							this.tabBar.width+=3;
						}
						if(this.tabBar.width>=750&&this.tabBar.bottom<=0){
							clearInterval(this.interval);
						}
					},13);
				} else {
					// this.tabBar.width = 700;
					// this.tabBar.bottom = 40;
					this.tabBar.selectedIndex=1;
					this.tabBar.list = this.listByIndex;
					this.$forceUpdate();
						this.interval=setInterval(()=>{
							if(this.tabBar.bottom<40){
								this.tabBar.bottom+=3;
							}
							if(this.tabBar.width>700){
								this.tabBar.width-=3;
							}
							if(this.tabBar.width<=700&&this.tabBar.bottom>=40){
								clearInterval(this.interval);
							}
						},13);
				}
			}
		},
		watch: {
			"pageComponent":{
				handler(newVal, oldVal) {
					console.log("切换了");
					this.brandToChange(null);
					this.techniqueToChange(null);
				},
				deep: true,
				immediate: true
			},
			"brandByChange":{
				handler(newVal, oldVal) {
						console.log(JSON.stringify(this.techniqueByChange)+"-------");
						var te=null;
						var bd=null;
						if((this.techniqueByChange.id==null||this.techniqueByChange.id==0)
						&&
						(this.brandByChange.id==null||this.brandByChange.id==0))
						{
							this.listByIndex[0].text="分类";
							return false;
						}
						if((this.techniqueByChange.id==null||this.techniqueByChange.id==0)){
							// te="全部";
						}
						if((this.techniqueByChange.id!=null&&this.techniqueByChange.id!=0)){
							te=this.techniqueByChange.name;
						}
						if((this.brandByChange.id==null||this.brandByChange.id==0)){
							// bd="全部";
						}
						if((this.brandByChange.id!=null&&this.brandByChange.id!=0)){
							bd=this.brandByChange.name;
						}
						this.listByIndex[0].text=bd;
						// this.listByIndex[0].text="分类:"+this.techniqueByChange.name;
					},
					deep: true,
					immediate: true
				},
			"techniqueByChange": {
				handler(newVal, oldVal) {
					console.log(JSON.stringify(this.techniqueByChange)+"-------");
					var te=null;
					var bd=null;
					if((this.techniqueByChange.id==null||this.techniqueByChange.id==0)
					&&
					(this.brandByChange.id==null||this.brandByChange.id==0))
					{
						this.listByIndex[0].text="分类";
						return false;
					}
					if((this.techniqueByChange.id==null||this.techniqueByChange.id==0)){
						// te="全部";
					}
					if((this.techniqueByChange.id!=null&&this.techniqueByChange.id!=0)){
						te=this.techniqueByChange.name;
					}
					if((this.brandByChange.id==null||this.brandByChange.id==0)){
						// bd="全部";
					}
					if((this.brandByChange.id!=null&&this.brandByChange.id!=0)){
						bd=this.brandByChange.name;
					}
					this.listByIndex[0].text=te;
					// this.listByIndex[0].text="分类:"+this.techniqueByChange.name;
				},
				deep: true,
				immediate: true
			}
		}
	}
</script>

<style lang="scss">
	.u-page__item__slot-icon {
		height: 40rpx;
		width: 40rpx;
	}

	.tabbar {}

	.tabbar-view {
		position: fixed;
		bottom: 40rpx;
		width: 700rpx;
		margin-left: 50%;
		padding: 10rpx;
		background-color: #FFF;
		border-radius: 20rpx;
		box-shadow: 0 0 10rpx rgba(118, 118, 118, .2);
		transform: translateX(-50%);
	}

	.brand {
		padding: 20rpx;
		text-align: center;
		color: gray;
	}

	.brand:active {
		color: rgba(118, 118, 118, .5);
	}
</style>
