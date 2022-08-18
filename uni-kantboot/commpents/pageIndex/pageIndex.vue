<template>
	<view>
		<image mode="aspectFill" class="back-image" src="../../static/4.png"></image>

		<view class="cover_images">
			<u-row customStyle="margin-bottom: 0rpx">
				<u-col span="1"></u-col>
				<u-col span="7.5">
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
					<view style="height: 10rpx;"></view>
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
					<view style="position: absolute;top:50rpx;
					right: 20rpx;">
						<view class="text3" 
						@click="toPage('/pages/guanyuwomen/guanyuwomen')">
						{{">"}}平台说明{{"<"}}</view>
						<view style="height: 30rpx;"></view>
						<!-- <view class="text3">{{">"}}品牌选择{{"<"}}</view> -->
					</view>
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
				payBtnClick:true,
				bodyData: {

				}
			}
		},
		onShow() {
			var routes = getCurrentPages();
			var route = routes[routes.length - 1].route+"?pageComponent=pageIndex";
			uni.setStorageSync("routeTo", '/' + route);

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
			getTechniqueByChange() {
				Request.request({
					url: Api.ranfaWork.techniqueByChange,
					success: (res) => {
						this.techniqueByChange = res.data.data;
						this.$store.state.techniqueByChange = this.techniqueByChange;
					}
				});
			},
			techniqueToChange(techniqueId){
				if(this.techniqueId!=null){
					this.brandToChange(null);
					this.brandByChange={id:null};
					
				}
				Request.request({
					url: Api.ranfaWork.techniqueToChange,
					data: {
						"ranfaTechniqueId": techniqueId
					},
					success: (res) => {
				
						this.getTechniqueByChange();
		
						this.closeCheckType();
					}
				});
			},
			getRanfaTechniques() {
				Request.request({
					url: Api.ranfaTechnique.findCommonList,
					data: {
						entity: {}
					},
					success: (res) => {
			
						this.ranfaTechniques = res.data.data;
						// console.log(JSON.stringify(res));
						this.$forceUpdate();
					}
				});
			},
			brandToChange(ranfaBrandId) {
				if(ranfaBrandId!=null){
					this.techniqueToChange(null);
					this.techniqueByChange={id:null};
				}
				Request.request({
					url: Api.ranfaWork.brandToChange,
					data: {
						"ranfaBrandId": ranfaBrandId
					},
					success: (res) => {
						this.getBrandByChange();
						this.closeCheck();
					}
				});
			},
			getBrandByChange() {
				Request.request({
					url: Api.ranfaWork.brandByChange,
					success: (res) => {
						this.brandByChange = res.data.data;
						console.log(JSON.stringify(this.brandByChange));
						this.$store.state.brandByChange = this.brandByChange;
					}
				});
			},
			
			toPage(page) {
				uni.navigateTo({
					url: page
				})
			},
			change() {
				if(!this.payBtnClick){
					return false;
				}
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
				if(!this.payBtnClick){
					return false;
				}
				
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
						this.payBtnClick=false;
						setTimeout(()=>{
							this.payBtnClick=true;
						});
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
											this.payBtnClick=true;
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
	.text3{
		font-weight: bold;
		color:#F0F0F0;
		font-size: 25rpx;
		border:1rpx solid #F0F0F0;
		padding: 10rpx;
		border-radius: 10rpx;
		text-shadow: 0 10rpx 10rpx rgba(118,118,118,.3);
	}
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
			width: 120rpx;
			height: 120rpx;

		}

		.btn_image:active {
			width: 110rpx;
			height: 110rpx;
			margin: 12rpx;
		}


	}

	.cover_images {
		position: fixed;
		width: 100%;
		bottom: 180rpx;

		.cover_image_view {
			position: relative;
			border: 5rpx solid #42a1e8;
			// box-sizing: border-box;
			background-color: #42a1e8;
			width: 100%;
			height: calc(50vh - 80px);
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
				font-size: 35rpx;
				// font-weight: bold;
				font-weight: lighter;
				bottom:0rpx;
				right: 0rpx;
				// text-shadow: 0 0 10rpx gray;
				background-color: rgba(66,161,232,1);
				padding: 5rpx 20rpx 5rpx 20rpx;
				border-radius: 20rpx 0 20rpx 0;
			}
		}

	}
</style>
