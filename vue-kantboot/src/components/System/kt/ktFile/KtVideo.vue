<template>
		<video :width="width" :height="height" :src="url1">
             <source :src="url1" type="video/*" />
        </video>
</template>

<script>
	
	
	export default {
		props: {
            "width":{
                type: Object,
				default: []
            },
            "height":{
                type: Object,
				default: []
            },
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
				 this.$request.post({
					url: this.src,
					success: (res) => {
						console.log("----"+JSON.stringify(res));
						this.url1=res.data.url;
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
