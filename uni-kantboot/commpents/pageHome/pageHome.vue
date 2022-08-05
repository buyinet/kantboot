<template>
	<view>
		
		<view class="header">
			<image class="top-image" src="../../static/1.png"></image>			
			<image class="avatar" :src="userInfo.avatarUrl"></image>
			<text class="nickname">{{userInfo.nickname}}</text>
			<text class="phonenumber" v-if="userInfo.phoneNumber!=null">{{userInfo.phoneNumber}}</text>
			<text class="phonenumber" v-else>无绑定手机号码</text>
		</view>
		
		<view class="money">
			<view class="top" @click="toPage('/pages/wallet/wallet')">
				<image class="logo" src="../../static/2.png"></image>
				<text class="title">我的钱包</text>
				<!-- <u-icon class="to" name="arrow-right"></u-icon> -->
				<view @click="toPage('/pages/wallet/wallet')" class="line"></view>
			</view>
			
			<view class="bottom">
				<view class="left">
					<view class="top2">{{userInfo.balance}}</view>
					<view class="bottom2">匠师币</view>
				</view>
				<view class="right" @click="toPage('/pages/tixian/tixian')">
					<view class="top2">
						<image class="img" src="../../static/3.png"></image>
					</view>
					<view class="bottom2">去提现</view>
				</view>
			</view>
			
		</view>
		
		<view style="height: 20rpx;"></view>
		
		<view class="click-to">
			<view @click="toPage('/pages/my_buy/my_buy')">
				<image 
				style="width: 45rpx;"
				mode="widthFix"
				src="../../static/home_icon1.png">
				</image>
				<text>已购买的教学视频</text></view>
			<view
				@click="toPage('/pages/my_work/my_work')"
			>
				<image
				style="width: 45rpx;"
				mode="widthFix"
				src="../../static/home_icon2.png">
				</image>
				<text>我的作品</text></view>
			<view
			 @click="toPage('/pages/upload_work/upload_work')"
			>
				<image
				style="width: 45rpx;"
				mode="widthFix"
				src="../../static/home_icon3.png">
				</image>
				<text>上传作品</text></view>
			<view
				@click="toPage('/pages/my_collect/my_collect')"
				>
				<image
				style="width: 45rpx;"
				mode="widthFix"
				src="../../static/home_icon4.png">
				</image>
				<text>收藏</text></view>
			<view @click="toPage('/pages/guanyuwomen/guanyuwomen')">
				<image 
					style="width: 45rpx;"
					mode="widthFix"
					src="../../static/home_icon5.png">
				</image>
				<text>平台说明</text></view>
		</view>
	</view>
</template>

<script>
var Request = getApp().globalData.Request;
var Api = getApp().globalData.Api;

let that;
	export default {
		data() {
			return {
				userInfo:{
					"nickname":"用户",
					"gender":0,
					"realname":null,
					"username":null,
					"authz":1,
					"avatarUrl":"https://thirdwx.qlogo.cn"+
					"/mmopen/vi_32/"+
					"POgEwh4mIHO4nibH0KlMECNj"+
					"jGxQUq24ZEaGT4poC6icRiccVGKS"+
					"yXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132",
					"phoneNumber":null,
					"vipLv":1,
					"balance":0,
					"gmtCreate":"2021-10-11 11:31:32",
					"gmtModified":"2021-10-11 11:31:32",
					"craftsmanCoin":"0"}
			}
		},
		mounted() {
			that=this;
			this.getUserInfo();
		},
		onShow() {
			this.getUserInfo();
		},
		methods: {
			toPage(page){
				uni.navigateTo({
					url:page
				})
			},
		getUserInfo(){
			Request.request({
				url:Api.user.getUserInfo,
				success:(res)=>{
					// console.log(res);

					if(Request.isSuccess(res)){
						this.userInfo = res.data.data;
						console.log(JSON.stringify(this.userInfo));
					}
				}
				});
			}
		}
	}
</script>

<style lang="scss">
	page{
		background-color: #F5F9FF;
	}
.header{
	position: fixed;
	width: 100%;
	height:400rpx;
	top: 0;
	.avatar{
		position: absolute;
		top:132rpx;
		left:20rpx;
		width:106rpx;
		height: 106rpx;
		border-radius: 55%;
	}
	.nickname{
		position: absolute;
		top:150rpx;
		left:150rpx;
		height: 29rpx;
		font-size: 32rpx;
		font-family: PingFang SC;
		font-weight: 400;
		color: #FFFFFF;
	}
	.phonenumber{
		position: absolute;
		width: 179rpx;
		height: 19rpx;
		font-size: 24rpx;
		font-family: PingFang SC;
		font-weight: 400;
		color: #DDDDDD;
		top:200rpx;
		left:150rpx;
	}
	.top-image{
		position: absolute;
		width: 100%;
		height:400rpx;
	}	
}
.click-to{
	padding-top: 20rpx;
	position: fixed;
	box-shadow:  0 0 10rpx rgba(118,118,118,.1);
	border-radius: 20rpx;
	background-color: #FFFFFF;
	width: 710rpx;
	height: 70vh;
	margin-left: 50%;
	transform: translateX(-50%);
	view {
		padding: 30rpx;
		width: 80%;
		margin-left: 50%;
		transform: translateX(-50%);
		color: #666666 ;
		border-bottom: 1rpx solid #F2F2F2;
		image{
			position: absolute;
			margin-right: 20rpx;
		}
		text{
			height: 26rpx;
			font-size: 28rpx;
			font-family: PingFang SC;
			font-weight: 400;
			color: #666666;
			line-height: 30rpx;
			padding-left: 60rpx;
		}
	}
	view:active{
		margin-top: 20rpx;
		margin-bottom: 20rpx;
		border-radius: 20rpx;
		background-color: rgba(118,118,118,.1);
	}
	
}
.money{
	position: relative;
	width: 710rpx;
	height: 218rpx;
	box-shadow: 0 0 10rpx rgba(118,118,118,.1);
	background: #FFFFFF;
	border-radius: 18rpx;
	margin-left: 20rpx;
	margin-top: 280rpx;
	.bottom{
		position: absolute;
		width: 100%;
		top: 105rpx;
		.left{
			position: absolute;
			left: 126rpx;
			width: 100rpx;
			.top2{
				text-align: center;
				width: 100rpx;
				height: 36rpx;
				font-size: 48rpx;
				font-family: PingFang SC;
				font-weight: 600;
				color: #3FA1EE;
				text-align: center;
				
			}
			.bottom2{
				margin-top: 30rpx;
				text-align: center;
				width: 100rpx;
				height: 23rpx;
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: 400;
				color: #666666;
			}
		}
		.right{
			position: absolute;
			right: 126rpx;
			width: 84rpx;
			.top2{
				width: 84rpx;
				height: 36rpx;
				.img{
					width: 49rpx;
					height: 46rpx;
					margin-left: 50%;
					transform: translateX(-50%);
				}
			}
			.bottom2{
				margin-top: 30rpx;
				text-align: center;
				width: 84rpx;
				height: 23rpx;
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: 400;
				color: #666666;
			}
		}
	}
	.top{
		position: relative;
		width: 100%;
		.title{
			position: absolute;
			font-size: 26rpx;
			top: 24rpx;
			left: 89rpx;
			font-weight: 600;
			color: #333333;
		}
		.logo{
			position: absolute;
			top:17rpx;
			left:34rpx;
			width: 43rpx;
			height: 43rpx;
		}
		.to{
			position: absolute;
			right: 35rpx;
			font-size: 18rpx;
			color: #999999;
			top:35rpx;
			font-weight: bold;
		}
		.line{
			position: absolute;
			width: 643rpx;
			height: 1px;
			border: 1px solid #EEEEEE;
			top:78rpx;
			left:33rpx;
		}
	}
}

</style>
