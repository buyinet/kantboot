<template>
	<view style="position: relative;">
		<video :style="style" :src="url1"></video>
		<view 
		v-if="false"
		style="
		position: absolute;
		z-index: 100;
		top:0;
		background-color: rgba(255,255,255,.7);
		width: 100%;
		height: 100%;color: #FFF;
		text-align: center;
		">
		<view style="margin-top: 50%;transform: translateY(-150%);">
			<u-icon name="moments"
			:customStyle="{marginLeft:'50%',transform: 'translateX(-50%) rotate('+deg+'deg)'}"
			 color="#FFF" size="150"></u-icon>		
		<view>正在加载与校验中...</view>
		</view>
		
		</view>
		
	</view>
</template>

<script>
	var Request = getApp().globalData.Request;
	var Api = getApp().globalData.Api;
	
	export default {
		name: "RolesInput",
		props: {
			"style": {
				type: String,
				default: []
			},
			"src": {
				type: String,
				default: []
			}
		},
		data() {
			return {
				url1: "",
				isFinall:true,
				deg:0,
				inter:new Object()
			};
		},
		mounted() {
			console.log(this.src);
			this.flush();
			// this.inter=setInterval(()=>{
			// 	this.deg++;
			// },10);
		},
		methods: {
			flush() {
				var startTime=new Date().getTime();
				this.isFinall=false;
				const fs = uni.getFileSystemManager() // 文件管理器API
				Request.request({
					url: this.src,
					success: (res) => {
						console.log("----"+JSON.stringify(res));
						if(res.data.state!==2000){
							uni.setStorageSync("ranfaWorkIdOfPlay",null);
						}
						this.url1=res.data.data.url;
						clearInterval(this.inter);
						// var fileName = `${wx.env.USER_DATA_PATH}/${res.header["Content-MD5"]}`
						// console.log("正在成功文件...");
						// fs.writeFile({
						// 	filePath: fileName,
						// 	data: res.data,
						// 	encoding: 'binary',
						// 	success: (res1) => {
						// 		this.url1=fileName;
						// 		this.isFinall=true;
						// 		var endTime=new Date().getTime();
						// 		console.log("所有时间"+(endTime-startTime));
						// 		clearInterval(this.inter);
						// 	}
						// })
					},
				});
			}

		}
	}
</script>

<style>
</style>
