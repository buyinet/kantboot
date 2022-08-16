<template>
	<view>

<view style="
		background-color: #2979ff;
		padding-bottom: 300rpx;
		box-shadow: 0 0 60rpx rgba(118,118,118,.6);
		">
		<view style="color:rgba(0,0,0,0)">111</view>
		<view class="balance-box">
			<u-row>
				<u-col text-align="center" :span="4">
					<view style="font-size: 50rpx;font-weight:bolder;">{{userInfo.balance}}</view>
					<view style="font-size: 30rpx;">我的匠师币</view>
				</u-col>
				<u-col text-align="center" :span="2">
					<view style="color: rgba(0,0,0,0);">1</view>
					<view style="position: absolute;top:0;left:48%;transform: translateX(-50%);font-size: 100rpx;">⇌</view>
				</u-col>
				<u-col text-align="center" :span="4">
					<view style="font-size: 50rpx;font-weight:bolder;">￥{{userInfo.balance/100}}</view>
					<view style="font-size: 30rpx;">可提现金额</view>
				</u-col>
			</u-row>
		</view>
		
		<!-- border-bottom: 1rpx solid rgba(118,118,118,.9); -->
		<view style="height: 15rpx;"></view>
		<view style="
			display: inline-block;
			text-align: center;
			margin-left: 50%;
			transform: translateX(-50%);
			color:rgba(225,225,225,.9);
			font-size: 25rpx;">染发一号 - 匠师币提现</view>
</view>		

	<view style="position: fixed;bottom:100rpx;width: 100%;">
	<u-button
	@click="tixianToWechat()"
	 :customStyle="{
		 'width':'490rpx'
	 }"
	 :disabled="userInfo.balance==0"
	 :ripple="true" type="success" ripple-bg-color="rgba(255,255,255,.7)">
		<u-icon name="weixin-circle-fill"
			style="position: absolute;color:#FFFFFF;left: 60rpx;font-size: 45rpx;"></u-icon>
		{{userInfo.balance==0?"无匠师币可提现":"提现到微信余额"}}
	</u-button>
	</view>
		<u-toast ref="uToast" />
	</view>
</template>

<script>

</script>

<style>

</style>
