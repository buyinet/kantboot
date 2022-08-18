<template>
	<view>
		<view style="position: fixed;
		height: 100vh;
		width: 100vw;
		z-index: -1;
		background-color: #FFF;
		top:0;
		left: 0;
		"></view>
		</u-navbar>
		<!-- v-show="pageData.videoUrls==null" -->
		<view v-if="bodyData.fileUrlsOfVideo.length==0" style="text-align: center;color:gray;font-size: 28rpx;">

			<view @click="toIndexPage()" style="position: fixed;
			left:50%;
			top:50%;
			transform: translate(-50%,-50%);">
				<image class="sss_image" mode="widthFix" src="../../static/index_3.png">
				</image>
				<view>暂无选中的视频</view>
				<view style="height: 10rpx;"></view>
				<view>请选择视频播放</view>
			</view>
		</view>
		<view v-if="bodyData.fileUrlsOfVideo.length!=0">
			<kt-video 
				style="width: 750rpx;
					height: 500rpx;
					padding: 0;margin: 0;"
				:src="bodyData.fileUrlsOfVideo[fileUrlsOfVideoIndex]"	
			></kt-video>
<!-- 			<video style="width: 750rpx;height: 400rpx;
			padding: 0;margin: 0;" 
			:src="bodyData.fileUrlsOfVideo[fileUrlsOfVideoIndex]"></video> -->
		</view>

		<view style="padding: 30rpx 30rpx 0 30rpx;font-size: 30rpx;">
			<u-subsection bgColor="#5EB2F3" :activeColor="'#5EB2F3'" :inactiveColor="'#FFFFFF'" :list="list1"
				fontSize="35" @change="sectionChange" :current="current"></u-subsection>

		</view>
		<view v-if="current==0" 
		
		style="margin-top: calc(50% - 230rpx);transform: 
		translateY(-50%);;">
			<u-grid
			 col="5"
			 
			 align="center"
			 :border="false">
				<u-grid-item
				v-for="(item,index) in bodyData.fileUrlsOfVideo" 
				@click="videoSelect(index)"
				:key="index">
				
					<view 
					v-if="bodyData.fileUrlsOfVideo.length>1"
					:style="
					
					'font-weight: bold;font-size: 50rpx;'
					+
					(fileUrlsOfVideoIndex==index
					?'color:rgba(0,0,0,.4)'
					:
					'color:rgba(0,0,0,1)'
					)
					">
						{{index+1}}
					</view>
					<view
					v-if="bodyData.fileUrlsOfVideo.length==1"
					:style="
					'font-weight: bold;font-size: 50rpx;'
					">
						{{index+1}}
					</view>
					<text
					v-if="bodyData.fileUrlsOfVideo.length>1"
					:style="(fileUrlsOfVideoIndex==index
					?'color:rgba(0,0,0,.4);font-size:25rpx'
					:
					'color:rgba(0,0,0,1)'
					)"
					>
					{{fileUrlsOfVideoIndex==index?"正在播放...":"第"+(index-(-1))+"集"}}
					</text>
					
					<text v-if="bodyData.fileUrlsOfVideo.length==1">
					第{{(index-(-1))}}集
					</text>
				</u-grid-item>
			</u-grid>
			<view style="height: 20rpx;"></view>
			<view 
			v-if="bodyData.fileUrlsOfVideo.length==1"
			style="
				font-size: 28rpx;
				color:rgba(0,0,0,.4);
				text-align: center;
				">该视频只有1集</view>
			<view
			v-if="bodyData.fileUrlsOfVideo.length!=1"
			style="
				font-size: 28rpx;
				color:rgba(0,0,0,.4);
				text-align: center;
				">该视频共有{{bodyData.fileUrlsOfVideo.length}}集</view>
		</view>


		<view v-if="current==1" style="box-sizing: border-box;padding: 0 30rpx 10rpx 30rpx;">
			<view style="height: 40rpx;"></view>
			<view>

			</view>
			<view style="box-sizing: border-box;padding: 10rpx;font-size: 30rpx;">
				<view style="color: gray;margin-bottom: 30rpx;" v-for="
				(item,index) in bodyData.process.split('\n')">
					{{item}}
				</view>
			</view>

		</view>
	</view>
</template>

<script>
	import ktVideo from '../../commpents/kt/ktFile/ktVideo.vue';
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;
	export default {
		components: {
			ktVideo
		},
		data() {
			return {
				list1: ['选择集数', '步骤介绍'],
				// 或者如下，也可以配置keyName参数修改对象键名
				// list: [{name: '未付款'}, {name: '待评价'}, {name: '已付款'}],
				current: 0,
				fileUrlsOfVideoIndex: 0,
				bodyData: {
					id: null,
					fileUrlsOfVideo: []
				}
			}
		},
		mounted() {
			this.flush();
		},
		onShow() {
			var routes = getCurrentPages();
			var route = routes[routes.length - 1].route;
			uni.setStorageSync("routeTo", '/' + route);
		},methods: {
			videoSelect(index){
				this.fileUrlsOfVideoIndex=index;
				console.log(
				this.bodyData.fileUrlsOfVideo[
					this.bodyData.fileUrlsOfVideoIndex
				]);
				this.$forceUpdate();
				// console.log("选择了第"+(index-(-1))+"集");
			},
			sectionChange(index) {
				this.current = index;
			},
			flush() {
				var ranfaWorkIdOfPlay = uni.getStorageSync("ranfaWorkIdOfPlay");
				if (ranfaWorkIdOfPlay == null) {
					return false;
				}
				Request.request({
					url: Api.ranfaWork.findById,
					data: {
						id: ranfaWorkIdOfPlay
					},
					success: (res) => {
						this.bodyData = res.data.data;
						this.fileUrlsOfVideoIndex=0;

					}
				});
			},
			toIndexPage() {
				uni.reLaunch({
					url: "/pages/static/static?pageComponent=pageIndex",
				});
			}
		},
		watch: {

		}
	}
</script>

<style lang="scss">
	page {
		background-color: #F2F2F2;
	}

	.to_ji {
		padding: 20rpx 60rpx 20rpx 60rpx;
		border-radius: 10rpx;
		box-shadow: 0 0 10rpx rgba(118, 118, 118, .1);
	}

	.to_ji:active {
		// color: rgba(62, 161, 238, .8);
		box-shadow: 0 0 8rpx rgba(118, 118, 118, .1);
		opacity: .7;
	}

	.sss_image {
		width: 200rpx;
	}

	.sss_image:active {
		width: 188rpx;
	}

	.header {
		height: 100rpx;
		font-size: 35rpx;
		text-align: center;
		line-height: 120rpx;
		font-weight: bold;
		z-index: 1;
		color: rgba(0, 0, 0, .8);
	}

	.exp {
		display: inline-block;
		padding: 0rpx 20rpx 0rpx 20rpx;
		font-size: 35rpx;
		height: 60rpx;
		line-height: 60rpx;
		border-radius: 10rpx;
		box-shadow:
			0 5rpx 10rpx rgba(118, 118, 118, .3),
			0 -10rpx 5rpx rgba(118, 118, 118, .1) inset;
		color: rgba(0, 0, 0, .6);
		margin: 20rpx;
	}

	.exp:active {
		padding: 0rpx 20rpx 0rpx 20rpx;
		line-height: 70rpx;
		box-shadow:
			0 3rpx 10rpx rgba(118, 118, 118, .3),
			0 -6rpx 5rpx rgba(118, 118, 118, .1) inset;
		margin-bottom: 0;
		margin-top: 0;
	}

	.exp-box {
		display: inline-block;
		margin-left: 50%;
		transform: translateX(-50%);
		padding: 40rpx 40rpx 20rpx 40rpx;
		box-sizing: border-box;
	}
</style>
