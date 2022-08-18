<template>
	<view class="page">
		<view style="font-weight: bold;">上传您的染发作品
		<view
		 class="to_s"
		 style="display: inline;
		font-weight: lighter;
		font-size: 25rpx;
		margin-left: 20rpx;
		border-bottom: 1rpx solid #5EB2F3;
		color: #5EB2F3;
		"
		@click="toPage('/pages/shangchuanshuoming/shangchuanshuoming')"
		>点击查看《上传说明》</view>
		</view>
		<view style="height: 20rpx;"></view>
		<view>
			<view style="height: 10rpx;"></view>
			<u-row>
				<u-col :span="5">
					<view class="ti">染发前图片：</view>
					<view style="height: 10rpx;position: relative;"></view>
					<u-upload :fileList="coverFrontImageList" @afterRead="uploadCover" class="upload-file"
						width="300rpx" height="440rpx" :customStyle="{
							'position':'relative'
						}" :previewFullImage="true" name="1" multiple :maxCount="1">

						<image :src="coverFrontImage" mode="aspectFill" style="
							border-radius: 10rpx;
							width: 300rpx;height:440rpx;
							background-color: rgba(118,118,118,.1);">
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
						width="300rpx" height="440rpx" :customStyle="{
							'position':'relative'
						}" :previewFullImage="true" name="1" multiple :maxCount="1">

						<image :src="coverBackImage" mode="aspectFill" style="
							border-radius: 10rpx;
							width: 300rpx;height:440rpx;
							background-color: rgba(118,118,118,.1);">
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
					<view class="ee" v-for="(item,index) in videos" @click="videoByPlay=item;videoByPlayIndex=index">
						<view style="color: rgba(0,0,0,.7);">第{{index+1}}集</view>
						<view style="font-size: 25rpx;color: rgba(0,0,0,.3);font-weight: lighter;">点击查看</view>
					</view>
				</view>
				<u-upload :fileList="videoList" @afterRead="uploadVideo" class="upload-file" width="312.5rpx"
					height="440rpx" :previewFullImage="true" name="1" multiple :maxCount="1">
					
					<view class="ee">
						<view style="color: rgba(0,0,0,.7);font-size: 25rpx;">添加视频</view>
						<view style="font-size: 25rpx;color: rgba(0,0,0,.3);font-weight: lighter;">点击添加</view>
					</view>
					
					
				</u-upload>
			</view>
		</view>
		<view style="height: 20rpx;"></view>
		<view>
			<view class="ti">品牌：
			<view style="height: 10rpx;"></view>
			<view
			 v-if="brandBySelected.name!=null"
			 style="display: inline;margin-right: 10rpx;">
			 
			 {{brandBySelected.name}}</view>
				<view 
				v-if="brandBySelected.name==null"
				@click="openCheck()"
				style="color:gray;display: inline;font-weight: bold;font-size: 25rpx;">点击选择</view>
				<view 
				v-if="brandBySelected.name!=null"
				@click="openCheck()"
				style="color:gray;display: inline;font-weight: bold;font-size: 25rpx;">更改选择</view>
			</view>
		</view>
		<view style="height: 30rpx;"></view>
		<view>
			<view class="ti">操作流程：</view>
			<view style="height: 10rpx;"></view>
			<textarea class="progress-text"
			 v-model="paramData.process"
			 :autoHeight="true" placeholder="请输入操作流程"></textarea>
		</view>
		<view style="height: 30rpx;"></view>
		<u-button 
		@click="toSubmit()"
		:customStyle="{'width':'500rpx','borderRadius':'100rpx'}" type="primary">提交</u-button>

		<view style="position: fixed;
		 z-index: 100;
		 top:0;left:0;
		 width: 100%;
		 height: 100vh;
		 background-color: aliceblue;
		 " v-if="videoByPlay!=null">
			<kt-video style="width: 100%;height: 50vh;" :src="videoByPlay"></kt-video>
			<view style="
			text-align: center;
			font-size: 60rpx;
			font-weight: lighter;
			margin-top: 20%;transform: translateY(-50%);">
				<view>第 {{videoByPlayIndex+1}} 集</view>
				<view style="
					color: rgba(0,0,0,.6);
					font-size: 35rpx;">
					共有 {{videos.length}} 集
				</view>
			</view>
			<view @click="deleteVideo(videoByPlayIndex)" style="
				position: fixed;
				bottom: 0;
				text-align: center;
				right: 0;
				box-shadow: 0 0 10rpx  rgba(255,0,0,.6);
				color: #f0f0f0;
				font-weight: bold;
				padding: 100rpx 50rpx 50rpx 100rpx;
				border-radius: 300rpx 0 0 0;
				background-color: rgba(255,0,0,.6);
				">
				<view>删 除</view>
				<view style="font-weight: lighter;
			font-size: 25rpx;">DELETE</view>
			</view>

			<view @click="videoByPlay=null" style="
				position: fixed;
				bottom: 0;
				text-align: center;
				left: 0;
				box-shadow: 0 0 10rpx  rgba(0,0,0,.6);
				color: #f0f0f0;
				font-weight: bold;
				padding: 50rpx 100rpx 50rpx 50rpx;
				border-radius: 0 300rpx 0 0;
				background-color: rgba(0,0,0,.6);
				">
				<view>返回</view>
				<view style="font-weight: lighter;
			font-size: 25rpx;">GO BACK</view>
			</view>
		</view>



		<u-popup :overlayOpacity=".7" :show="checkShow" :customStyle="{overflowY: 'scroll'}" :safeAreaInsetTop="true"
			@close="closeCheck" @open="openCheck" class="brand-box" mode="right">
			<view style="height: 30rpx;"></view>
			<view style="padding: 20rpx;font-size:30rpx;text-align: center;font-weight: 400;color: rgba(0,0,0,.2);">
				品牌选择
			</view>

			<view class="brand" @click="
			paramData.ranfaBrandId=item.id;
			brandBySelected=item;closeCheck()" v-for="(item,index) in ranfaBrands">
				<text v-if="item.id!=brandBySelected.id">
					{{item.name}}</text>
				<text v-if="item.id==brandBySelected.id" style="opacity: .4;">{{item.name}}</text>
			</view>
		</u-popup>
		
		<view 
		style="width:100%;height: 100%;
		background-color: rgba(255,255,255,.8);
		position: fixed;
		top:0;
		z-index: 100;
		left:0;
		"
		
		v-if="isVideoUploadProgressShow">
			<view class="p-box">
				<view
				 :style="'width:'+videoUploadProgress*4+'rpx;height:'+videoUploadProgress*4+'rpx;'"
				 class="p"></view>
				<view class="p-text">{{videoUploadProgress}}%</view>
			</view>
			
		</view>
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
				paramData:{
					fileIdByFrontCoverImage:null,
					fileIdByBackCoverImage:null,
					ranfaBrandId:null,
					process:null
				},
				coverFrontImageList: [],
				coverFrontImage: null,
				coverBackImageList: [],
				coverBackImage: null,
				videoList: [],
				videos: [],
				videoByPlay: null,
				videoByPlayIndex: 0,
				ranfaBrands:[],
				checkShow:false,
				brandBySelected:{
					id:null,
					name:null
				},
				videoUploadProgress:40,
				isVideoUploadProgressShow:false
			}
		},
		mounted() {
			this.getRanfaBrands();
			// setInterval(()=>{
			// 	this.videoUploadProgress++;
			// },10);
		},
		methods: {
			toPage(page) {
				uni.navigateTo({
					url: page
				})
			},
			toSubmit(){
			// 	Request.request({
			// 		url:
			// 	});
			},
			closeCheck() {
				this.checkShow = false;
			},
			openCheck() {
				this.checkShow = true;
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
			deleteVideo(index) {
				this.videos.splice(index, 1);
				this.videoByPlay = null;
			},
			uploadCover(res) {
				uni.uploadFile({
					url: Api.path + "kantboot-file/file/upload/ranfa/teachCover",
					filePath: res.file[0].url,
					name: 'file',
					success: (resp) => {
						var json = JSON.parse(resp.data);
						this.coverFrontImage = json.data.visitUrlById;
						this.paramData.fileIdByFrontCoverImage=json.data.id;
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
						this.paramData.fileIdByBackCoverImage=json.data.id;
					},

					fail: (resp) => { //失败
					},

					complete: () => { //不论成功、失败都执行		
					}
				});
		
			},
			uploadVideo(res) {
				this.isVideoUploadProgressShow=true;
				const uploadTask = uni.uploadFile({
					url: Api.path + "kantboot-file/file/upload/ranfa/teachVideo",
					filePath: res.file[0].url,
					name: 'file',
					success: (resp) => {
						var json = JSON.parse(resp.data);
						// this.videoUploadProgress=100;
						// this.isVideoUploadProgressShow=false;
						this.videos.push(json.data.visitUrlById);
						this.videoUploadProgress=100;
						this.isVideoUploadProgressShow=false;
					},

					fail: (resp) => { //失败
					},

					complete: () => { //不论成功、失败都执行		
					}
				});
				uploadTask.onProgressUpdate((res) => {
					this.videoUploadProgress=res.progress;
					if(this.videoUploadProgress>=98){
						this.videoUploadProgress=98
						this.isVideoUploadProgressShow=false;
					}
				    console.log('上传进度' + res.progress);  
				});
			}

		}
	}
</script>

<style lang="scss" scoped>
	.to_s:active{
		opacity: .6;
	}
	.p-box{
		position: absolute;
		border-radius: 55%;
		// border: 10rpx solid #5EB2F3;
		height: 400rpx;
		width:400rpx;
		background: rgba(94,178,243,.3);
		// box-shadow: inset 20rpx 20rpx 60rpx #5097cf,
		//             inset -20rpx -20rpx 60rpx #6ccdff;
		top:50%;
		left:50%;
		transform: translate(-50%,-50%);
		.p{
			position: absolute;
			width:10rpx;
			height:10rpx;
			border-radius: 55%;
			top:50%;
			left:50%;
			// background-color: #5EB2F3;
			background: rgba(94,178,243,.5);
			box-shadow:  20rpx 20rpx 60rpx #5097cf,
			             -20rpx -20rpx 60rpx #6ccdff;
			transform: translate(-50%,-50%);
		}
		.p-text{
			
		}
	}
	.brand {
		padding: 15rpx;
		text-align: center;
		color: gray;
		font-size: 30rpx;
	}
	
	.brand:active {
		color: rgba(118, 118, 118, .5);
	}
	.page {
		padding: 60rpx;
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
		min-height: 150rpx;
		border: 1rpx solid rgba(118, 118, 118, 1);
		width: calc(100% - 40rpx);
		border-radius: 5rpx;
		padding: 20rpx;
		font-size: 25rpx;
	}

	.progress-text::placeholder {}
</style>
