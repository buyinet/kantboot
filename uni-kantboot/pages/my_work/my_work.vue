<template>
	<view>
		<view style="
		position: fixed;
		box-shadow: 0 0 10rpx gray;
		width: 100%;
		background-color: #FFFFFF;
		z-index: 100;
		box-sizing: border-box;
		padding: 10rpx 30rpx 10rpx 30rpx">
			<u-subsection fontSize="25" @change="subCurrChange" :list="['审核已通过','未审核','审核不通过','全部']" :current="subCurr">
			</u-subsection>
		</view>
		<view style="height: 80rpx;"></view>
		
		<u-popup :overlayOpacity=".7" :show="checkShow" :customStyle="{overflowY: 'scroll'}" :safeAreaInsetTop="true"
			@close="closeCheck" @open="openCheck" class="brand-box" mode="right">
			<view style="text-align: center;font-weight: bold;color: rgba(0,0,0,.2);">
				品牌选择
			</view>
			<view class="brand" @click="
			 paramData.pageNumber=1;
			 ranfaBrandBySelected={id:null};
			 paramData.data.and.eq[0].ranfaBrandId=null;this.closeCheck();this.findCommon();">
				<text v-if="null!=paramData.data.and.eq[0].ranfaBrandId">全部</text>
				<text v-if="null==paramData.data.and.eq[0].ranfaBrandId" style="opacity: .7;">全部</text>
			</view>
			<view class="brand" v-for="(item,index) in ranfaBrands" @click="
			 paramData.pageNumber=1;
			 ranfaBrandBySelected=item;
			 paramData.data.and.eq[0].ranfaBrandId=item.id;this.closeCheck();this.findCommon();">
				<text v-if="item.id!=paramData.data.and.eq[0].ranfaBrandId">{{item.name}}</text>
				<text v-if="item.id==paramData.data.and.eq[0].ranfaBrandId" style="opacity: .7;">{{item.name}}</text>
			</view>
		</u-popup>

		<u-popup :overlayOpacity=".7" :show="checkShow2" :customStyle="{overflowY: 'scroll'}" :safeAreaInsetTop="true"
			@close="closeCheck2" @open="openCheck2" class="brand-box" mode="right">
			<view style="text-align: center;font-weight: bold;color: rgba(0,0,0,.2);">
				分类选择
			</view>
			<view class="brand" @click="
			 paramData.pageNumber=1;
			 ranfaTechniqueBySelected={id:null};
			 paramData.data.and.eq[0].ranfaTechniques[0].id=null;this.closeCheck2();this.findCommon();">
				<text v-if="null!=paramData.data.and.eq[0].ranfaTechniques[0].id">全部</text>
				<text v-if="null==paramData.data.and.eq[0].ranfaTechniques[0].id" style="opacity: .7;">全部</text>
			</view>
			<view class="brand" v-for="(item,index) in ranfaTechniques" @click="
			 paramData.pageNumber=1;
			 ranfaTechniqueBySelected=item;
			 paramData.data.and.eq[0].ranfaTechniques[0].id=item.id;this.closeCheck2();this.findCommon();">
				<text v-if="item.id!=paramData.data.and.eq[0].ranfaTechniques[0].id">{{item.name}}</text>
				<text v-if="item.id==paramData.data.and.eq[0].ranfaTechniques[0].id"
					style="opacity: .7;">{{item.name}}</text>
			</view>
		</u-popup>

		<view>
			<view v-if="bodyData.content.length==0" class="not-to">
				<image mode="widthFix" src="../../static/5.png"></image>
				<view>无视频</view>
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
								left: 285rpx
								">染发后</text>
						<image class="right_img" mode="aspectFill" :src="item.fileUrlByBackCoverImage"></image>
					</view>
					<view class="right">
						<view v-if="item.title!=null" class="title">{{item.title}}</view>
						<view style="height: 20rpx;"></view>
						<view style="font-size: 25rpx;">
							<view style="color: gray;">编号：{{item.iden}}
							</view>
							<view style="height: 20rpx;"></view>
							<view style="color: gray;">品牌：{{item.ranfaBrand.name}}</view>
							<view style="height: 20rpx;"></view>
							<view style="color: gray;">状态：
							<text v-if="item.auditStatus==0">
								未审核
							</text>
							<text v-if="item.auditStatus==1">
								已通过
							</text>
							<text v-if="item.auditStatus==2">
								审核不通过
							</text>

							</view>
							
							<view style="height: 20rpx;"></view>
							<view style="color: gray;">分类:
								<view v-for="item1 in item.ranfaTechniques"
								 v-if="item.ranfaTechniques.length>0"
								 style="
								display: inline-block;
								margin-left: 10rpx;
								padding: 3rpx 10rpx 3rpx 10rpx;
								border-radius: 10rpx;
								box-shadow: 0 5rpx 10rpx rgba(118,118,118,.3);">
									{{item1.name}}
								</view>
								
								<view
								 v-if="item.ranfaTechniques.length==0"
								 style="
								display: inline-block;
								margin-left: 10rpx;
								padding: 3rpx 10rpx 3rpx 10rpx;
								border-radius: 10rpx;
								">
									未分类
								</view>
							</view>

						</view>

						<view class="bottom">
							<view class="left2">
								价格：<text>{{item.price/100}}元</text>
							</view>
<!-- 							<view v-if="item.auditStatus==1&&!item.buy" @click="pay(item.id)" class="edit_text">
								购买
							</view> -->
							<view
							 v-if="item.auditStatus==1"
							 style="
							display: inline;
							width:60rpx;
							margin-left: 60rpx;
							" class="edit_text"
								@click="toPage('/pages/indexToShare/indexToShare?ranfaWorkId='+item.id)">
								分享
							</view>
							<view
							 v-if="item.auditStatus!=1"
							 style="
							display: inline;
							width:60rpx;
							color: rgba(255,0,0,.6);
							margin-left: 60rpx;
							"
							 @click="
							 toPage('/pages/ranfaWordRemove/ranfaWordRemove?ranfaWorkId='+item.id)"
							 class="edit_text">
								删除
							</view>
							<view style="
							position: absolute;
							left: 175rpx;
							display: inline;width:60rpx;" 
							@click="toPage('/pages/play2/play2?ranfaWorkId='+item.id)"
							class="edit_text"
								>
								查看
							</view>
							<view 
							 v-if="item.auditStatus!=1"
							 @click="toPage('/pages/edit_work/edit_work?ranfaWorkId='+item.id)"
							style="
							position: absolute;
							display: inline;width:60rpx;
							left: 120rpx;
							" class="edit_text"
								>
								修改
							</view>
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
					<text @click="openCheck()" v-if="ranfaBrandBySelected.id==null" class="grid-text"
						style="font-weight: bold;color: gray;">选择品牌</text>
					<text @click="openCheck()" v-if="ranfaBrandBySelected.id!=null" class="grid-text" style="font-weight: bold;
					font-size: 25rpx;
					color: gray;">
						<text style="font-size: 15rpx;">品牌：</text>
						<text v-if="ranfaBrandBySelected.name.length<4">{{ranfaBrandBySelected.name}}</text>
						<text v-if="ranfaBrandBySelected.name.length>=4"
							style="font-size: 20rpx;">{{ranfaBrandBySelected.name}}</text>

					</text>
				</u-grid-item>
				<u-grid-item>
					<text @click="openCheck2()" v-if="ranfaTechniqueBySelected.id==null" class="grid-text"
						style="font-weight: bold;color: gray;">选择分类</text>
					<text @click="openCheck2()" v-if="ranfaTechniqueBySelected.id!=null" class="grid-text" style="font-weight: bold;
					font-size: 25rpx;
					color: gray;">
						<text style="font-size: 15rpx;">分类：</text>
						<text v-if="ranfaTechniqueBySelected.name.length<4">
						{{ranfaTechniqueBySelected.name}}
						</text>
						<text v-if="ranfaTechniqueBySelected.name.length>=4"
							style="font-size: 20rpx;">
						{{ranfaTechniqueBySelected.name}}
						</text>

					</text>
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
				subCurr: 1,
				ranfaBrandBySelected: {
					id: null
				},
				ranfaTechniqueBySelected: {

				},
				ranfaTechniques: [],
				ranfaBrands: [],
				checkShow: false,
				checkShow2: false,
				paramData: {
					"pageNumber": 1,
					"pageSize": 5,
					"sortType": "DESC",
					"sortField": "gmtModified",
					"data": {
						"and": {
							"eq": [{
								auditStatus: 1,
								ranfaBrandId: null,
								ranfaTechniques: [{
									id: null
								}]
							}, ],
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
		onShow() {
			this.findCommon();
		},
		mounted() {
			this.findCommon();
			this.getRanfaBrands();
			this.getRanfaTechniques();
		},
		methods: {
			subCurrChange(res) {
				this.bodyData.content=[];
				this.subCurr = res;
				if(this.subCurr==0){
					this.paramData.data.and.eq[0].auditStatus="1";
					this.findCommon();
					return false;
				}
				if(this.subCurr==1){
					this.paramData.data.and.eq[0].auditStatus="0";
					this.findCommon();
					return false;
				
				}
				if(this.subCurr==2){
					this.paramData.data.and.eq[0].auditStatus="2";
					this.findCommon();
					return false;
				
				}
				if(this.subCurr==3){
					this.paramData.data.and.eq[0].auditStatus=null;
					this.findCommon();
					return false;
				}
			},
			toPage(page) {
				uni.navigateTo({
					url: page
				})
			},
			pay(id) {
				Request.requestSync({
					url: Api.authPayGoods.createPayingParam,
					data: {
						goodsParentName: "ranfa", //此类商品统一识别名称，必填
						payParams: [{
							goodsId: id, //商品id,必填
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
							success: (res1) => {
								this.findCommon();
								//支付成功后，对此id的商品重新获取，主要判断是否真的购买，以改变bodyData.buy来渲染页面
								Request.request({
									url: Api.ranfaWork.findById,
									data: {
										id: id
									},
									success: (res) => {
										if (res.data.data.buy) {
											this.toPlay(id);
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
			getRanfaBrands() {
				Request.request({
					url: Api.ranfaBrand.findCommonList,
					data: {
						entity: {}
					},
					success: (res) => {

						this.ranfaBrands = res.data.data;
						// console.log(JSON.stringify(res));
						this.$forceUpdate();
					}
				});
			},
			closeCheck() {
				this.checkShow = false
			},
			openCheck() {
				this.checkShow = true;
			},
			closeCheck2() {
				this.checkShow2 = false
			},
			openCheck2() {
				this.checkShow2 = true;
			},
			toPlay(id) {
				uni.setStorageSync("ranfaWorkIdOfPlay", id)
				uni.navigateTo({
					url: "/pages/play/play",
				})
			},
			findCommon() {
				if (this.paramData.pageNumber < 1) {
					this.paramData.pageNumber = 1;
					return false;
				}

				Request.requestSync({
					url: Api.ranfaWork.findByUploadSelf,
					data: this.paramData,
					success: (res) => {
						this.bodyData = res.data.data;
						if (this.paramData.pageNumber > this.bodyData.totalPages - 1) {
							this.paramData.pageNumber = this.bodyData.totalPages - 1;
							// return false;
						}
						this.$forceUpdate();

					}
				});

			}
		},
		watch: {
			subCurr: {
				immediate: true,
				handler(newVal) {
					this.bodyData.content=[];
					if (this.subCurr == 0) {
						this.paramData.data.and.eq[0].auditStatus = "1";
						this.findCommon();
						return false;
					}
					if (this.subCurr == 1) {
						this.paramData.data.and.eq[0].auditStatus = "0";
						this.findCommon();
						return false;

					}
					if (this.subCurr == 2) {
						this.paramData.data.and.eq[0].auditStatus = "2";
						this.findCommon();
						return false;

					}
					if (this.subCurr == 3) {
						this.paramData.data.and.eq[0].auditStatus = null;
						this.findCommon();
						return false;
					}
				}
			}
		},
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

	.brand {
		padding: 20rpx;
		text-align: center;
		color: gray;
	}

	.brand:active {
		color: rgba(118, 118, 118, .5);
	}

	.box {
		position: relative;
		box-shadow: 0 5rpx 5rpx rgba(118, 118, 118, .1);
		width: 730rpx;
		height: 270rpx;
		padding: 20rpx;

		.left {
			width: 360rpx;
			margin-left: 20rpx;

			image {
				width: 177rpx;
				height: 270rpx;
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
				top: 250rpx;

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

	.edit_text1 {
		position: absolute;
		color: #4EC1FF;
		opacity: .9;
		width: 140rpx;
		font-size: 20rpx;
		position: absolute;
	}

	.edit_text1:active {
		opacity: .2;
	}

	.grid-text {
		font-size: 25rpx;
		padding: 0 0 10rpx 0;
	}
</style>
