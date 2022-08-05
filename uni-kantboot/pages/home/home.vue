<template>
	<view>

		<view class="top">
			<image mode="widthFix" class="back-image" src="../../static/1.png"></image>			
		</view>

	</view>
</template>

<script>
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;
	
	export default {
		data() {
			return {
				api:Api
			}
		},
		mounted() {
			
		},
		methods: {
			
		}
	}
</script>

<style lang="scss">
.top {
	width: 100%;
	
	.back-image {
		width: 100%;
		box-shadow: 0 0 20rpx rgba(118,118,118,.8);		
	}
}
</style>
