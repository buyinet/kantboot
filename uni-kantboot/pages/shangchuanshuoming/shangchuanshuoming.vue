<template>
	<view style="letter-spacing:4rpx;padding: 30rpx 60rpx 0 60rpx;box-sizing: border-box;">
		<!-- <u-alert-tips type="warning" :title="warning" :description="description"></u-alert-tips> -->
		<view style="height: 30rpx;"></view>
		<view style="color: rgba(0,0,0,.8);font-size: 28rpx;font-weight: bold;">
		上传作品说明：
		</view>
		<view style="height: 20rpx;"></view>
		<view style="text-indent:65rpx;font-size: 28rpx;font-weight: 400;color: rgba(0,0,0,.8);">
			<view>
				1.先拍两张照片（染发前、染发后各一张）。
			</view>
			<view style="height: 20rpx;"></view>
			<view>
			2.录制3段以上的染发过程视频（分别在染前、染中、染后每段时间在10-20秒）。
			</view>
			<view style="height: 20rpx;"></view>
			<view>
			3.上传完成后将染膏配方、比例、操作顺序、每个过程的停放时间
			或加热时间、涂抹方法、涂抹顺序、注意事项（越仔细越好）。
			</view>
			<view style="height: 20rpx;"></view>
			<view>
			4、审核通过后，您的作品就可以挣钱了（观看人数越多，收益越大）。
			</view>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				warning: "温馨提示：\n平台所有作品均为发廊实际操作上传" +
					"不同染膏品牌，不同发质，不同温度，不同涂抹方法和不同涂抹量可能出现相应的色差，操作方法仅供参考，学习，探讨，欢迎各位同行多上传作品，让更多的实际操作数据，来达到更完美准确的美丽效果。",
			}
		},
		onShow() {
			var routes = getCurrentPages();
			var route = routes[routes.length - 1].route;
			uni.setStorageSync("routeTo", '/' + route);
		},
		methods: {

		}
	}
</script>

<style>

</style>
