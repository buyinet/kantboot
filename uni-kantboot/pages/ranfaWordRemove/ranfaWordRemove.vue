<template>
	<view>
		<view style="
			position: fixed;
			background-color: red;
			width: 100vw;
			height: 100vh;
			z-index: -1;
			"></view>
			
			<view style="
				position: fixed;
				width:100%;
				top:50%;
				transform: translateY(-50%);
			">
				
			<view style="
				padding: 0 60rpx 0 60rpx;
				color: #fff;
				font-weight: lighter;
			">
					<view>编号：{{ranfaWork.iden}}</view>
			</view>
			
		<view style="padding: 10rpx 40rpx 10rpx 40rpx;">
			<u-row>
				<u-col :span=".25"></u-col>

				<u-col :span="5.5" :customStyle="{'position': 'relative'}">
					<view class="cover-image-view">
						<image
						 mode="aspectFill"
						 class="cover-image" :src="ranfaWork.fileUrlByFrontCoverImage"></image>
						<view
						style="
							font-weight: bold;
							color: #fff;
							text-align: center;
							text-shadow: 0 10rpx 20rpx red;
						"
						>染发前</view>
					</view>
				</u-col>
				<u-col :span="0.5"></u-col>
				<u-col :span="5.5" :customStyle="{'position': 'relative'}">
					<view class="cover-image-view">
						<image
						 mode="aspectFill"
						 class="cover-image" :src="ranfaWork.fileUrlByBackCoverImage"></image>
						<view
						style="
							font-weight: bold;
							color: #fff;
							text-align: center;
							text-shadow: 0 10rpx 20rpx red;
						"
						>染发后{{id}}</view>
					</view>
				</u-col>
				<u-col :span=".25"></u-col>

			</u-row>
		</view>
		<view style="height: 20rpx;"></view>
		<view style="
			padding: 0 60rpx 0 60rpx;
			color: #fff;
			font-weight: lighter;
		">
			<u-button
			@click="removeBySelf()"
			 type="error">确认删除</u-button>
			<view style="height: 20rpx;"></view>
			<u-button
			@click="toBack()"
			>返回</u-button>
		</view>
		</view>
		<u-toast ref="uToast"></u-toast>
	</view>
</template>

<script>
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;
	export default {
		data() {
			return {
				id:null,
				ranfaWork: {}
			}
		},
		onLoad(option) {
			// console.log(option.ranfaWorkId);
			this.id=option.ranfaWorkId;
			this.findById(option.ranfaWorkId)
		},
		mounted() {

		},
		methods: {
			toBack(){
				uni.navigateBack({
					delta:1
				});
			},
			findById(id) {
				Request.request({
					url: Api.ranfaWork.findById,
					data: {
						"id": id
					},
					success: (res) => {
						this.ranfaWork = res.data.data;
					}
				});
			},
			removeBySelf(){
				Request.request({
					url: Api.ranfaWork.removeByUploadSelf,
					data: {
						"id": this.id
					},
					success: (res) => {
						this.$refs.uToast.show({
							message: "删除成功",
						});
						setTimeout(()=>{							
							uni.navigateBack({
									delta:1
								});
						},500);
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.cover-image-view {
		background-color: rgba(255,255,255,.5);
		padding: 20rpx;
		border-radius: 20rpx;
		
		.cover-image {
			width: 100%;
			height: 350rpx;
			border-radius: 20rpx;
		}
	}
</style>
