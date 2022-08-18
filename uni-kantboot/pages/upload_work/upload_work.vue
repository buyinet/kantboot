<template>
	<view class="page">
		<view>上传您的染发作品</view>
		<view style="height: 20rpx;"></view>
		<view>
			<u-row>
				<u-col :span="5">
					<view class="ti">染发前图片：</view>
					<view style="height: 10rpx;position: relative;"></view>
					<u-upload :fileList="coverFrontImageList" @afterRead="uploadCover" class="upload-file"
						width="312.5rpx" height="440rpx" :customStyle="{
							'position':'relative'
						}" :previewFullImage="true" name="1" multiple :maxCount="1">

						<image :src="coverFrontImage" mode="aspectFill" style="
							border-radius: 10rpx;
							width: 312.5rpx;height:440rpx;
							background-color: rgba(118,118,118,.3);">
						</image>
						<u-icon v-if="coverFrontImage==null" size="60" :customStyle="{
								'position':'absolute',
								'z-index':'100',
								'top':'50%',
								'left':'50%',
								'transform':'translate(-50%,-50%)'
							}" name="photo-fill"></u-icon>
					</u-upload>
				</u-col>
				<u-col :span="1.6"></u-col>
				<u-col :span="5">
					<view class="ti">染发后图片：</view>
					<view style="height: 10rpx;position: relative;"></view>
					<u-upload :fileList="coverBackImageList" @afterRead="uploadCoverBack" class="upload-file"
						width="312.5rpx" height="440rpx" :customStyle="{
							'position':'relative'
						}" :previewFullImage="true" name="1" multiple :maxCount="1">

						<image :src="coverBackImage" mode="aspectFill" style="
							border-radius: 10rpx;
							width: 312.5rpx;height:440rpx;
							background-color: rgba(118,118,118,.3);">
						</image>
						<u-icon v-if="coverBackImage==null" size="60" :customStyle="{
								'position':'absolute',
								'z-index':'100',
								'top':'50%',
								'left':'50%',
								'transform':'translate(-50%,-50%)'
							}" name="photo-fill"></u-icon>
					</u-upload>
				</u-col>
			</u-row>
		</view>
		<view style="height: 20rpx;"></view>
		<view style="width: 100%;">
			<view class="ti">染发视频：</view>
			<view style="height: 10rpx;"></view>
			<view class="ee-box">
				<view>
					<view class="ee"
					 v-for="(item,index) in videos"
					 @click="videoByPlay=item"
					 >
						<view style="color: rgba(0,0,0,.7);">第{{index+1}}集</view>
						<view style="font-size: 25rpx;color: rgba(0,0,0,.3);font-weight: lighter;">点击查看</view>
					</view>
				</view>
				<u-upload
				 
				 :fileList="videoList" @afterRead="uploadVideo" class="upload-file"
					width="312.5rpx" height="440rpx" 
				 :previewFullImage="true" name="1" multiple :maxCount="1">
					<view class="ee"
					>
						<view style="color: rgba(0,0,0,.7);font-size: 25rpx;">点击添加</view>
						<view style="font-size: 25rpx;color: rgba(0,0,0,.3);font-weight: lighter;">点击添加</view>
					</view>
				</u-upload>
			</view>
			{{videoByPlay}}
			{{videoByPlay==null}}
			<kt-video v-if="videoByPlay!=null" :src="videoByPlay"></kt-video>
		</view>
		<view style="height: 20rpx;"></view>
		<view>
			<view class="ti">流程：</view>
			<view style="height: 10rpx;"></view>
			<textarea class="progress-text" :autoHeight="true" placeholder="请输入流程"></textarea>
		</view>
		<view style="height: 30rpx;"></view>
		<u-button :customStyle="{'width':'300rpx'}" type="primary">提交</u-button>
	</view>
</template>

<script>
	import ktVideo from '../../commpents/kt/ktFile/ktVideo.vue';
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;
	export default {
		components: {
			ktVideo
		},
		data() {
			return {
				coverFrontImageList: [],
				coverFrontImage: null,
				coverBackImageList: [],
				coverBackImage: null,
				videoList:[],
				videos:[],
				videoByPlay:null
			}
		},
		methods: {
			uploadCover(res) {
				uni.uploadFile({
					url: Api.path + "kantboot-file/file/upload/ranfa/teachCover",
					filePath: res.file[0].url,
					name: 'file',
					success: (resp) => {
						var json = JSON.parse(resp.data);
						this.coverFrontImage = json.data.visitUrlById;
					},

					fail: (resp) => { //失败
					},

					complete: () => { //不论成功、失败都执行		
					}
				});
			},
			uploadCoverBack(res) {
				uni.uploadFile({
					url: Api.path + "kantboot-file/file/upload/ranfa/teachCover",
					filePath: res.file[0].url,
					name: 'file',
					success: (resp) => {
						var json = JSON.parse(resp.data);
						this.coverBackImage = json.data.visitUrlById;
					},

					fail: (resp) => { //失败
					},

					complete: () => { //不论成功、失败都执行		
					}
				});
			},
			uploadVideo(res){
				uni.uploadFile({
					url: Api.path + "kantboot-file/file/upload/ranfa/teachVideo",
					filePath: res.file[0].url,
					name: 'file',
					success: (resp) => {
						var json = JSON.parse(resp.data);
						this.videos.push(json.data.visitUrlById);
					},
				
					fail: (resp) => { //失败
					},
				
					complete: () => { //不论成功、失败都执行		
					}
				});
			}

		}
	}
</script>

<style lang="scss" scoped>
	.page {
		padding: 40rpx;
		font-size: 40rpx;
		box-sizing: border-box;
	}

	.ee {
		margin: 15rpx;
		display: inline-block;
		text-align: center;
	}

	.ee:active {
		opacity: .6;
	}

	.ti {
		font-weight: lighter;
		font-size: 30rpx;
	}

	.upload-file {
		margin-left: 50%;
		transform: translateX(-50%);
	}

	.progress-text {
		min-height: 100rpx;
		border: 1rpx solid rgba(118, 118, 118, 1);
		width: calc(100% - 40rpx);
		border-radius: 5rpx;
		padding: 20rpx;
		font-size: 25rpx;
	}

	.progress-text::placeholder {}
</style>
