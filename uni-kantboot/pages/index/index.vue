<template>
	<view>
		<image mode="widthFix"
				 class="back-image" 
				 src="../../static/4.png"></image>			
		
		<view style="height: 100px;"></view>
		<view class="btn_images">
			<image class="image_change" src="../../static/index_1.png"></image>
			<image class="image_play"  src="../../static/index_3.png"></image>
			<image class="image_collection" src="../../static/index_2.png"></image>
		</view>
	</view>
</template>

<script>
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;

	export default {
		data() {
			return {
				aa: null
			}
		},
		methods: {
			pay() {
				Request.requestSync({
					url: Api.authPayGoods.createPayingParam,
					data: {
						goodsParentName: "test01", //此类商品统一识别名称，必填
						payParam: [{
							goodsId: 1, //商品id,必填
							number: 1, //数量，选填
							coupons: [], //优惠券,选填
							otherParam: [{ //其它参数

							}]
						}]
					},
					success: (res) => {
						uni.requestPayment({
							provider: 'wxpay', // 服务提提供商
							...res.data.data, // 签名
							success: function(res1) {
								console.log('支付成功', res1);
								// 业务逻辑。。。
							},
							fail: function(err1) {
								console.log('支付失败', err1);
							}
						});

					}
				});
			}
		}
	}
</script>

<style lang="scss">
.back-image{
	position: fixed;
	
	top:0;
	left: 0;
	width: 750rpx;
}
.btn_images {
	position: fixed;
	width: 100%;
	height: 400rpx;
	z-index: 100;
	bottom: 0;
	image {
		width:200rpx;
		height: 200rpx;
	}
	image:active {
		width: 180rpx;
		height: 180rpx;
	}
	.image_change{
		position: absolute;
		left:20rpx;
		top: 30rpx;
	}
	.image_collection{
		position: absolute;
		right:20rpx;
		top: 30rpx;
		
	}
	.image_collection:active{
		transform: translate(10rpx,-10rpx);
	}
	.image_play{
		position: absolute;
		bottom: 30rpx;
		margin-left: 50%;
		transform: translateX(-50%);

	}

}
</style>
