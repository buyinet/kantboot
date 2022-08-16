<template>
	<view>
		<Backgroud style="z-index: -1;"></Backgroud>
		<view style="
			position: fixed;
			text-align: center;
			top:50%;
			transform: translateY(-50%);
			width: 100%;
			color:aliceblue;
			font-size: 100rpx;
			z-index: 100;">
			<image mode="widthFix" style="width: 400rpx;" src="../../static/logo.png"></image>
			<view style="font-size: 55rpx;font-weight: bold;">染 发 一 号</view>
			<view style="height: 10rpx;"></view>
			<view style="font-size: 35rpx;font-weight: 100;">一款染发教学平台</view>
		</view>
		
		<view style="padding: 50rpx;box-sizing: border-box;">
			<!-- #ifdef MP-WEIXIN -->
			<ButtonByWechatAppletGetPhoneNumber
			style="position: fixed;bottom: 200rpx;width: 600rpx;
			box-shadow: 0 10rpx 40rpx rgba(0,178,106,.6);border-radius: 100rpx;"
			></ButtonByWechatAppletGetPhoneNumber>
			<!-- #endif -->
		</view>
	</view>
</template>

<script>
	import Backgroud from './commpents/Backgroud.vue';
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;
	// #ifdef MP-WEIXIN
	import ButtonByWechatAppletGetPhoneNumber from './commpents/ButtonByWechatAppletGetPhoneNumber.vue';
	// #endif

	export default {
		components: {
			Backgroud,
			// #ifdef MP-WEIXIN
			ButtonByWechatAppletGetPhoneNumber,
			// #endif

		},
		data() {
			return {
			}
		},
		onShow() {
			Request.request({
				url:Api.user.getUserInfo,
				success:(res)=>{
					if(res.data.state==2000){
						uni.reLaunch({
							url:"/pages/static/static"
						})
					}
				}
			});
		},
		created() {},
		mounted() {
			
		},
		methods: {
			uNotifyShow(res) {
				this.$refs.uNotify.show(res);
			}
		}
	}
</script>

<style lang="scss">

</style>
