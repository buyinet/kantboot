<template>
	<view>
		<view>

			<view v-if="bodyData.content.length==0" class="not-to">
				<image mode="widthFix" src="../../static/5.png"></image>
				<view>暂无已购视频</view>
			</view>
			<view>
				<view style="height: 20rpx;"></view>
				<view class="box" v-for="item in bodyData.content" :style="item==4?'box-shadow:0 0 0 gray;':''">
					<view class="left">
						<text style="
								color: #F0F0F0;
								position: absolute;
								font-size: 25rpx;
								padding: 5rpx 20rpx 0 20rpx;
								background-color: rgba(118,118,118,.7);
								border-radius: 20rpx 0 20rpx 0;
								">染发前</text>
						<image class="left_img" mode="aspectFill" :src="item.fileUrlByFrontCoverImage"></image>

						<text style="
								color: #F0F0F0;
								position: absolute;
								font-size: 25rpx;
								padding: 5rpx 20rpx 0 20rpx;
								background-color: rgba(118,118,118,.7);
								border-radius: 20rpx 0 20rpx 0;
								bottom: 19rpx;
								left: 280rpx
								">染发后</text>
						<image class="right_img" mode="aspectFill" :src="item.fileUrlByBackCoverImage"></image>
					</view>
					<view class="right">
						<view v-if="item.title!=null" class="title">{{item.title}}</view>
						<view style="height: 20rpx;"></view>
						<view style="font-size: 25rpx;">
							<view style="color: gray;">品牌：{{item.ranfaBrand.name}}</view>
							<view style="height: 20rpx;"></view>
							<view style="color: gray;">分类:
								<view v-for="item1 in item.ranfaTechniques" style="
								display: inline-block;
								margin-left: 10rpx;
								padding: 3rpx 10rpx 3rpx 10rpx;
								border-radius: 10rpx;
								box-shadow: 0 5rpx 10rpx rgba(118,118,118,.3);">
									{{item1.name}}
								</view>
							</view>

						</view>

						<view class="bottom">
							<view class="left2">

								价格：<text>{{item.price/100}}元</text>
							</view>
							<view v-if="item.auditStatus==1" @click="toPlay(item.id)" class="edit_text">查看</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<view style="height: 170rpx;"></view>

		<view style="
			position: fixed;
			background-color: rgba(255,255,255,.9);
			box-shadow: 0 0 40rpx rgba(118,118,118,.3);
			bottom: 40rpx;
			border-radius: 40rpx;
			left: 50%;
			padding: 40rpx;
			box-sizing: border-box;
			transform: translateX(-50%);
			width: 680rpx;">
			<u-grid :border="true" col="5">
				<u-grid-item>
					<text class="grid-text" style="font-weight: bold;color: gray;">选择品牌</text>
				</u-grid-item>
				<u-grid-item>
					<text class="grid-text" style="font-weight: bold;color: gray;">选择分类</text>
				</u-grid-item>

				<u-grid-item @click="paramData.pageNumber--;this.findCommon()">
					<u-icon name="arrow-left" size="40"></u-icon>
				</u-grid-item>

				<u-grid-item>
					<text class="grid-text">
						{{bodyData.number+1}} {{"/"}} {{bodyData.totalPages}}
					</text>
				</u-grid-item>
				<u-grid-item @click="paramData.pageNumber++;this.findCommon()">
					<u-icon name="arrow-right" size="40">
					</u-icon>
				</u-grid-item>

			</u-grid>
		</view>

	</view>
</template>

<script>
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;
	export default {
		data() {
			return {

				paramData: {
					"pageNumber": 1,
					"pageSize": 5,
					"sortType": "DESC",
					"sortField": "payGoods.gmtModified",
					"data": {
						"and": {
							"eq": [],
							"vague": [],
							"like": [],
							"gt": [],
							"lt": [],
							"ge": [],
							"le": []
						},
						"or": {
							"eq": [],
							"like": [],
							"gt": [],
							"lt": [],
							"ge": [],
							"le": []
						},
						"entity": {},
						"notNull": [],
						"isNull": []
					}
				},
				bodyData: {}
			}
		},
		mounted() {
			this.findCommon();
		},
		methods: {
			findCommon() {
				console.log(this.paramData.pageNumber);
				if(this.paramData.pageNumber<1){
					this.paramData.pageNumber=1;
					return false;
				}
				if(this.paramData.pageNumber>this.bodyData.totalPages){
					this.paramData.pageNumber=this.bodyData.totalPages;
					return false;
				}
				
				Request.requestSync({
					url: Api.ranfaWork.findCommonPageByBuySuccess,
					data: this.paramData,
					success: (res) => {
						this.bodyData = res.data.data;
					}
				});

			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #FFF;
	}

	.not-to {
		width: 223rpx;
		font-size: 25rpx;
		text-align: center;
		color: #999999;
		margin-top: 50%;
		margin-left: 50%;
		transform: translate(-50%, -50%);

		image {
			width: 100%;
		}
	}

	.box {
		position: relative;
		box-shadow: 0 5rpx 5rpx rgba(118, 118, 118, .1);
		width: 730rpx;
		height: 227rpx;
		padding: 20rpx;

		.left {
			width: 360rpx;
			margin-left: 20rpx;

			image {
				width: 177rpx;
				height: 227rpx;
			}

			.left_img {
				border-radius: 20rpx 0 0 20rpx;
				margin-right: 3rpx;
				// box-shadow: 0 -3rpx 5rpx rgba(118, 118, 118, .4);
			}

			.right_img {
				border-radius: 0 20rpx 20rpx 0;
				// box-shadow: 0 3rpx 5rpx rgba(118, 118, 118, .4);
			}
		}

		.right {
			position: absolute;
			width: 300rpx;
			left: 420rpx;
			top: 0rpx;
			font-size: 24rpx;

			.title {
				width: 299rpx;
				height: 49rpx;
				font-size: 28rpx;
				font-family: PingFang SC;
				font-weight: 400;
				color: #666666;
				line-height: 42rpx;
				margin-top: 10rpx;
			}

			.bottom {
				position: absolute;
				top: 200rpx;

				.left2 {
					font-size: 24rpx;
					color: #999999;

					text {
						color: #E5604B;
					}
				}

				.edit_text {
					position: absolute;
					color: #4EC1FF;
					opacity: .9;
					width: 140rpx;
					font-size: 24rpx;
					position: absolute;
					top: 0;
					left: 180rpx;
				}

				.edit_text:active {
					opacity: .2;
				}
			}
		}
	}

	.grid-text {
		font-size: 25rpx;
		padding: 0 0 10rpx 0;
	}
</style>
