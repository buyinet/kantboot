<template>
	<view>
		<image mode="aspectFill" class="back-image" src="../../static/4.png"></image>

		<view class="cover_images">
			<u-row customStyle="margin-bottom: 0rpx">
				<u-col span="1"></u-col>
				<u-col span="5.9">
					<view class="cover_image_view">
						
						<image mode="aspectFill" class="cover_image cover_image_1"
							:src="bodyData.fileUrlByFrontCoverImage">
						</image>
						
<!-- 						<image mode="aspectFill" class="cover_image cover_image_1"
							src="https://ranfa.oss-cn-hangzhou.aliyuncs.com/2022/7/27/kantboot-upload-file/00febcdf-add9-4ee2-9e0e-3f5f788d7ed01644930984862.png">
						</image>
 -->						
						 <view class="cover_text">染发前</view>
					</view>
					<view style="height: 20rpx;"></view>
					<view class="cover_image_view">
						<image mode="aspectFill" class="cover_image cover_image_2"
							:src="bodyData.fileUrlByBackCoverImage">
						</image>
						<view class="cover_text">染发后</view>
					</view>
					<!-- <view>{{bodyData.ranfaBrand.name}}</view> -->
				</u-col>
				<u-col span="1"></u-col>
				<u-col span="4.5">
					<view class="btn_images">
						<image @click="change()" class="btn_image" src="../../static/index_1.png"></image>
						<view style="height: 20rpx;"></view>
						<image @click="pay()" v-show="!bodyData.buy" class="btn_image" src="../../static/index_3.png"></image>
						<image @click="toPlay(bodyData.id)" v-show="bodyData.buy" class="btn_image" src="../../static/index_03.png"></image>
						<view style="height: 20rpx;"></view>
						<image @click="bodyData.collection=true;collection();" class="btn_image" v-show="!bodyData.collection" src="../../static/index_2.png"></image>
						<image @click="bodyData.collection=false;cancelCollection()" v-show="bodyData.collection" class="btn_image" src="../../static/index_2_1.png"></image>
					</view>
				</u-col>
				<u-col span="0.5"></u-col>

			</u-row>
		</view>

		<view style="height: 100px;"></view>
		<!-- 		<view class="btn_images">
			<image @click="change()" class="image_change" src="../../static/index_1.png"></image>
			<image class="image_play" src="../../static/index_3.png"></image>
			<image class="image_collection" src="../../static/index_2.png"></image>
		</view> -->
		<image :src="src" style="position: fixed;" mode="widthFix"></image>

	</view>
</template>

<script>
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;

	export default {
		data() {
			return {
				aa: null,
				src: "",
				bodyData: {

				}
			}
		},
		onShow() {

			// this.change();
		},
		mounted() {
			this.change();
			// const fs = uni.getFileSystemManager() // 文件管理器API
			// uni.request({
			// 	url: "http://localhost/kantboot-file/file/visit/53",
			// 	responseType: 'arraybuffer',
			// 	success: (res)=> {
			// 		var fileName = `${wx.env.USER_DATA_PATH}/${res.header["X-xr-bookmark"]}`
			// 		console.log(res);
			// 		fs.writeFile({
			// 			filePath: fileName,
			// 			data: res.data,
			// 			encoding: 'binary',
			// 			success: (res1) => {
			// 				this.src = fileName;
			// 			}
			// 		})
			// 	},
			// });
		},
		methods: {
			change() {
				Request.request({
					url: Api.ranfaWork.change,
					success: (res) => {
						// console.log("=======++++"+JSON.stringify(res));
						this.bodyData = res.data.data
					}
				});
			},
			toPlay(id){
				uni.setStorageSync("ranfaWorkIdOfPlay",id)
				uni.reLaunch({
					url:"/pages/static/static?pageComponent=pagePlay",
				})
			},
			cancelCollection(){
				Request.requestSync({
					url: Api.authPayGoods.cancelCollection,
					data: {
						goodsParentName: "ranfa", //此类商品统一识别名称，必填
						collectionParams: [{
							goodsId: this.bodyData.id, //商品id,必填
							number: 1, //数量，选填
							coupons: [], //优惠券,选填
							otherParam: [{ //其它参数
				
							}]
						}]
					},
					success: (res)=>{
						Request.request({
							url: Api.ranfaWork.findById,
							data:{
								id:this.bodyData.id
							},
							success: (res) => {
								this.bodyData = res.data.data
								
								this.$forceUpdate();
							}
						});
						this.$forceUpdate();
					}
				});
			},
			collection(){
				Request.requestSync({
					url: Api.authPayGoods.collection,
					data: {
						goodsParentName: "ranfa", //此类商品统一识别名称，必填
						collectionParams: [{
							goodsId: this.bodyData.id, //商品id,必填
							number: 1, //数量，选填
							coupons: [], //优惠券,选填
							otherParam: [{ //其它参数
				
							}]
						}]
					},
					success: (res)=>{
						Request.request({
							url: Api.ranfaWork.findById,
							data:{
								id:this.bodyData.id
							},
							success: (res) => {
								this.bodyData = res.data.data
								
								this.$forceUpdate();
							}
						});
						this.$forceUpdate();
					}
				});
			},
			pay() {
				Request.requestSync({
					url: Api.authPayGoods.createPayingParam,
					data: {
						goodsParentName: "ranfa", //此类商品统一识别名称，必填
						payParams: [{
							goodsId: this.bodyData.id, //商品id,必填
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
							success: (res1)=> {
								//支付成功后，对此id的商品重新获取，主要判断是否真的购买，以改变bodyData.buy来渲染页面
								Request.request({
									url: Api.ranfaWork.findById,
									data:{
										id:this.bodyData.id
									},
									success: (res) => {
										this.bodyData = res.data.data
										if(this.bodyData.buy){
											this.toPlay(this.bodyData.id);
										}
										this.$forceUpdate();
									}
								});
								
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
	.back-image {
		position: fixed;
		z-index: -1;
		top: 0;
		left: 0;
		width: 750rpx;
		height: 100vh;
	}

	.btn_images {
		position: absolute;
		z-index: 100000;
		bottom: 0;
		right: 50rpx;
		.btn_image {
			width: 170rpx;
			height: 170rpx;

		}

		.btn_image:active {
			width: 150rpx;
			height: 150rpx;
			margin: 12rpx;
		}


	}

	.cover_images {
		position: fixed;
		width: 100%;
		bottom: 200rpx;

		.cover_image_view {
			position: relative;
			border: 10rpx solid #42a1e8;
			// box-sizing: border-box;
			background-color: #42a1e8;
			width: 100%;
			height: calc(50vh - 110px);
			box-shadow: 0 0rpx 40rpx rgba(118, 118, 118, .4), 
				0 20rpx 40rpx rgba(0, 0, 0, .4) inset;
			box-shadow:  20rpx 20rpx 60rpx #3889c5,
				             -20rpx -20rpx 60rpx #4cb9ff;
			border-radius: 20rpx;

			.cover_image {
				width: 100%;
				height: 100%;
				float: left;
				border-radius: 20rpx;
			}

			.cover_text {
				position: absolute;
				text-align: center;
				color: #F0F0F0;
				font-weight: 400;
				display: inline;
				z-index: 100;
				font-size: 25rpx;
				bottom:0rpx;
				right: 0rpx;
				background-color: #42a1e8;
				padding: 5rpx 10rpx 2rpx 20rpx;
				border-radius: 20rpx 0 0rpx 0;
			}
		}

	}
</style>
