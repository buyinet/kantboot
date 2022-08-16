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
			
			<view class="brand" @click="brandToChange(null)" >
				<text v-if="brandByChange.id!=null">全部</text>
				<text v-if="brandByChange.id==null" style="opacity: .4;">全部</text>
			</view>
			<view class="brand" @click="brandToChange(item.id)" v-for="(item,index) in ranfaBrands">
				<text v-if="item.id!=brandByChange.id">
					{{item.name}}</text>
				<text v-if="item.id==brandByChange.id" style="opacity: .4;">{{item.name}}</text>
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
			this.pageComponent = pageComponent;
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
		data() {
			return {
				brandByChange: {},
				ranfaBrands: [],
				checkShow: false,
				path: Api.path,
				interval: null,
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
				}
			}
		},
		methods: {
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
					}
				});
			},
			closeCheck() {
				this.checkShow = false
			},
			openCheck() {
				this.checkShow = true;
			},
			getRanfaBrands() {
				Request.request({
					url: Api.ranfaBrand.findCommonList,
					data: {
						entity: {}
					},
					success: (res) => {

						this.ranfaBrands = res.data.data;
						this.$forceUpdate();
					}
				});
			},
			toCheck() {

			},
			tabBarChange(index) {
				this.tabBar.bottom = 0;
				this.tabBar.width = 750;
				// this.tabBar.list = this.listByNoIndex;
				this.tabBar.selectedIndex=index
				this.pageComponent = this.tabBar.list[index].component;
			}
		},
		watch: {
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
		// border-radius: 20rpx;
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
