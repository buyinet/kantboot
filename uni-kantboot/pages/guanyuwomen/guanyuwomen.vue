<template>
	<view style="letter-spacing:4rpx;padding: 30rpx 60rpx 0 60rpx;box-sizing: border-box;">
		<!-- <u-alert-tips type="warning" :title="warning" :description="description"></u-alert-tips> -->
		<view style="height: 30rpx;"></view>
		<view style="text-indent:65rpx;color: rgba(0,0,0,.8);font-size: 28rpx;">
		温馨提示：平台所有作品均为发廊实际操作上传，不同染膏品牌，不同发质，不同温度，不同涂抹方法和不同涂抹量可能出现相应的色差，操作方法仅供参考，学习，探讨，欢迎各位同行多上传作品，让更多的实际操作数据，来达到更完美准确的美丽效果。</view>
		<view style="height: 20rpx;"></view>
		<view style="font-size: 28rpx;font-weight: bold;color: rgba(0,0,0,.8);">
			<view>
				1.平台将收取观看者一元的服务费，用于鼓励上传作品者和平台服务费用（作品上传者每一个播放获取0.6元，例如作品10000播放量，上传作品者将获6000元）。
			</view>
			<view style="height: 20rpx;"></view>
			<view>
			2.平台轻松保留您的经典作品和配方，方便以后查看和推荐给其他客户，也可以让你的精典作品获得更多收入，一次上传，长期收益，让技术转变财富。
			</view>
			<view style="height: 20rpx;"></view>
			<view>
			4.本平台为提高作者的收入，如出现意外数据丢失，请上传者和购买者自行保存作品。
			</view>
			<view style="height: 20rpx;"></view>
			<view>
				5.如有疑问，关注（染发一地）公众号，联系我们
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
