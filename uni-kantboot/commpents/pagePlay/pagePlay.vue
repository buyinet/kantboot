<template>
	<view>
		<view v-show="pageData.videoUrls==null"
		style="text-align: center;color:gray;font-size: 28rpx;"
		>
			
			<view 
			@click="toIndexPage()"
			style="position: fixed;
			left:50%;
			top:50%;
			transform: translate(-50%,-50%);">
			<image 
			class="sss_image"
			mode="widthFix"
			src="../../static/index_3.png">	
			</image>
			<view>暂无选中的视频</view>
			<view style="height: 10rpx;"></view>
			<view>请选择视频播放</view>
			</view>
		</view>
		
	</view>
</template>

<script>

	export default {
		data() {
			return {
				list: [ {
					name: '视频简介',
				}],
				pageData:{
					
				},
				videoIndex:0,
				current: 0,
				videoUrls:[]
			}
		},
		mounted() {
		},
		onShow() { 

		},
		methods: {
			toIndexPage(){
				uni.reLaunch({
					url:"/pages/static/static?pageComponent=pageIndex",
				});
			}
		},watch:{

		}
	}
</script>

<style lang="scss" >
page {
	background-color: #F2F2F2;
}
.to_ji{
	padding: 20rpx 60rpx 20rpx 60rpx;
	border-radius: 10rpx;
	box-shadow: 0 0 10rpx rgba(118,118,118,.1);
}
.to_ji:active{
	// color: rgba(62, 161, 238, .8);
	box-shadow: 0 0 8rpx rgba(118,118,118,.1);
	opacity: .7;
}
.sss_image{
	width: 200rpx;
}
.sss_image:active{
	width: 188rpx;
}
</style>
