<template>
	<view>
		<view style="
			position: fixed;
			width: 100%;
			text-align: center;
			margin-left: 50%;
			margin-top: 50%;
			transform: translate(-50%,-50%);
			">
			<view >（请复制订单编号）</view>
			<view style="font-size: 60rpx;font-weight: lighter;">是否确认继续?</view>
			<view style="height: 20rpx;"></view>
			<view><u-button 
			bindcontact="handleContact"
			type="primary"
			 open-type="contact"
			:customStyle="{'width':'400rpx'}"
			session-from="123"
			>确认</u-button></view>
			<view style="height: 20rpx;"></view>
		<view><u-button
		type="info"
		@click="toBack()"
		:customStyle="{'width':'400rpx'}">取消</u-button></view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			}
		},
		methods: {
			handleContact (e) {
			        console.log(e.detail.path)
			        console.log(e.detail.query)
			},
			toBack(){
				uni.navigateBack({
					delta:1
				});
			}
		}
	}
</script>

<style>

</style>
