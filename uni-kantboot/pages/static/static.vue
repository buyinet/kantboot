<template>
	<view>
		<component v-show="pageComponent=='pageIndex'" is="pageIndex"></component>
		<component v-show="pageComponent=='pagePlay'" is="pagePlay"></component>
		<component v-show="pageComponent=='pageHome'" is="pageHome"></component>


		<u-popup :overlayOpacity=".7" :show="checkShow" :customStyle="{overflowY: 'scroll'}" :safeAreaInsetTop="true"
			@close="closeCheck" @open="openCheck" class="brand-box" mode="right">
			<view>
				<image src="../../static/logo2.png" mode="widthFix" style="
				 margin-top: 50rpx;
					width: 100rpx;
					margin-left: 50%;
					transform: translateX(-50%);
				 "></image>
			</view>

			<view class="brand" @click="brandToChange(null)">
				<text v-if="brandByChange.id!=null">全部</text>
				<text v-if="brandByChange.id==null" style="opacity: .4;">全部</text>
			</view>
			<view class="brand" @click="brandToChange(item.id)" v-for="(item,index) in ranfaBrands">
				<text v-if="item.id!=brandByChange.id">
					{{item.name}}</text>
				<text v-if="item.id==brandByChange.id" style="opacity: .4;">{{item.name}}</text>
			</view>
		</u-popup>

		<u-popup :overlayOpacity=".7" :show="checkTypeShow" :customStyle="{overflowY: 'scroll'}"
			:safeAreaInsetTop="true" @close="closeCheckType" @open="openCheckType" class="brand-box" mode="right">
			<view style="text-align: center;font-weight: bold;color: rgba(0,0,0,.2);">
				分类选择
			</view>
			<view class="brand" @click="null">
				全部
			</view>
			<view class="brand" v-for="(item,index) in ranfaTechniques" @click="
			 paramData.pageNumber=1;
			 ranfaTechniqueBySelected=item;
			 paramData.data.and.eq[0].ranfaTechniques[0].id=item.id;this.closeCheck2();this.findCommon();">
				<text v-if="item.id!=paramData.data.and.eq[0].ranfaTechniques[0].id">{{item.name}}</text>
				<text v-if="item.id==paramData.data.and.eq[0].ranfaTechniques[0].id"
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
				"listByIndex": [{
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
					},
					// {
					// 	"pagePath": null,
					// 	"text": "品牌",
					// 	"method": "openCheck",
					// 	component: null,
					// 	"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/check_selected",
					// 	"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/check_selected"
					// },
					{
						"pagePath": null,
						"text": "分类",
						"method": "openCheckType",
						component: null,
						"iconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/check_selected",
						"selectedIconPath": Api.path + "kantboot-file/file/visit/tabbar/icon/check_selected"
					}
				],
			}
		},
		mounted() {
			this.getRanfaTechniques();
		},
		methods: {
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
						this.$store.state.brandByChange = this.brandByChange;
						console.log(
							JSON.stringify(this.$store.state.brandByChange)
						);
					}
				});
			},
			closeCheck() {
				this.checkShow = false
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
					this.tabBar.bottom = 0;
					this.tabBar.width = 750;
					this.tabBar.list = this.listByNoIndex;
					this.$forceUpdate();
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
				} else {
					this.tabBar.width = 700;
					this.tabBar.bottom = 40;
					this.tabBar.list = this.listByIndex;
					this.$forceUpdate();
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
		},
		watch: {
			// "brandByChange": {
			// 	handler(newVal, oldVal) {
			// 		if(this.brandByChange.id==null||this.brandByChange.id==0){
			// 			this.listByIndex[3].text="品牌";
			// 			return false;
			// 		}
			// 		this.listByIndex[3].text="品牌:"+this.brandByChange.name;
			// 	},
			// 	deep: true,
			// 	immediate: true
			// }
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
