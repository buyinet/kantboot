<template>
	<button :class="'login-btn'" 
	:disabled="btnDisabled"
	open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">
		<u-notify ref="uNotify" message="Hi uView" :show="true"></u-notify>
		<image class="icon" src="@/static/icon/wechat_sun.svg"></image>
		{{btnVal}}
	</button>
</template>

<script>
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;

	export default {
		data() {
			return {
				btnDisabled:false,
				btnVal:"点击登录"
			}
		},
		onShow() {
			console.log("123")
		},
		mounted() {

		},
		created() {},
		methods: {
			
			loginRequest(json){
				Request.requestSync({
					url: Api.cesAuthUserByWechat.loginByApplet,
					data: json,
					success: (res2) => {
						if (Request.isSuccess(res2)) {
							uni.setStorageSync("token", res2.data.data.token);
							this.$refs.uNotify.primary('登录成功');
							console.log("==="+uni.getStorageSync("routeTo"));
							console.log("(uni.getStorageSync('routeTo')==null)="+(uni.getStorageSync("routeTo")==null));
							console.log("(uni.getStorageSync('routeTo')=='')="+(uni.getStorageSync("routeTo")==""));
							
							if(uni.getStorageSync("routeTo")==null||uni.getStorageSync("routeTo")==""){
								uni.reLaunch({
									url:"/pages/static/static"
								});										
							}else{
								uni.reLaunch({
									url:uni.getStorageSync("routeTo")
								});
									uni.setStorageSync("routeTo",null)
							}
							return false;
						}
						this.btnDisabled=false;
					},
					faild(res2) {
						console.log("123");
						this.btnVal="出现了失误，再次点击";
						setTimeout(()=>{
							this.btnVal="点击登录";
						},1000);
						this.btnDisabled=true;
					}
					
				});
			},
			getPhoneNumber(res) {
				uni.login({
					provider: "weixin",
					success: (res1) => {
						var json = {
							"code": res1.code,
							"encryptedData": res.detail.encryptedData,
							"iv": res.detail.iv
						};
						this.btnDisabled=true;
						setTimeout(()=>{
							this.loginRequest(json);
						},800);
					}
				});
			}
		}
	}
</script>

<style lang="scss">
	.login-btn {
		position: relative;
		cursor: pointer;
		overflow: hidden;
		transition: background-color 3s;
		font-size: 35rpx;
		padding-top: 10rpx;
		padding-bottom: 10rpx;
		border-radius: 100rpx;
		font-weight: bold;
		border: none;
		background-color: $kt-background-wechat;
		color: #fff;
		box-shadow: 0 10rpx 40rpx rgba(0,178,106,.6);
		
		.icon {
			position: absolute;
			left: 30rpx;
			top: 50%;
			transform: translateY(-50%);
			width: 60rpx;
			height: 60rpx;
		}
	}

	.login-btn::after {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background-image: radial-gradient(circle, #ccc 10%, transparent 100%);
		transform: scale(10);
		opacity: 0;
		transition: all 3s;
	}

	.login-btn:active::after {
		transform: scale(0);
		opacity: .5;
		transition: 0s;
	}

</style>
