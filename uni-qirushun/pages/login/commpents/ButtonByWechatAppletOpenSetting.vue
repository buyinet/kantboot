<template>
	<button class="login-btn" @click="getUserProfile" open-type="openSetting">
		<image class="icon" src="@/static/icon/wechat_sun.svg"></image>
		允许用户授权
	</button>
</template>

<script>
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;

	export default {
		data() {
			return {

			}
		},
		onShow() {},
		created() {

		},
		methods: {
           getUserProfile(){
                uni.getUserProfile({
                  lang: 'zh_CN',
                  desc: '用户登录',
                  success: (res) => {
                    console.log('获取到的用户信息', res)
                  },
                  // 失败回调
                  fail: (err) => {
                    console.log('选择了拒绝')
                  },
                })
            },
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
		border-radius: 10rpx;
		font-weight: bold;
		border: none;
		background-color: $kt-background-wechat;
		color: #fff;

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
