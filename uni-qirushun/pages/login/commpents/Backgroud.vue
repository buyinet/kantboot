<template>
	<view class="background">
		<view :class="'pao'" v-for="(item,index) in paos" :style="
		'left:'+item.x+'vw;top:'+item.y+
		'vh;background-color:rgba('+item.rColor+','+item.gColor+','+0+',.5);box-shadow: 0 2rpx 30rpx rgba('+item.rColor+', '+item.gColor+', '+0+', .4),0 0 60rpx rgba('+item.rColor+', '+item.gColor+', '+0+', .4) inset;'
		"></view>
		<view style="background-color: rgba(0,0,255,.7);
		width: 100vw;height: 100vh;position: fixed;"></view>
	</view>
</template>

<script>
export default {
		components: {},
		data() {
			return {
				paoShow:false,
				paos:[]
			}
		},
		created() {
			for(var i=0;i<100;i++){
				var rColor=this.randomNum(0,255);
				var gColor=this.randomNum(0,255);
				var bColor=this.randomNum(0,255);
				var x=this.randomNum(0,100);
				var y=-40;
				var yy=this.randomNum(0,1);
				if(yy==1){
					y=120;
				}
				this.paos.push({
					x:x,
					y:y,
					xMove:0,
					moveNum:0,
					rColor:rColor,
					gColor:gColor,
					bColor:bColor
				});
			}
			setInterval(()=>{
				for(var i=0;i<this.paos.length;i++){
					var xNum=this.randomNum(0,1);
					var pao=this.paos[i];

					if(xNum==0&&pao.moveNum==0){
						pao.xMove=1;
					}
					if(xNum==1&&pao.moveNum==0){
						pao.xMove=-1;
					}
					var yNum=this.randomNum(0,1);
					if(yNum==0&&pao.moveNum==0){
						pao.yMove=1;
					}
					if(yNum==1&&pao.moveNum==0){
						pao.yMove=-1;
					}
					
					if(pao.moveNum==0){
						pao.moveNum=this.randomNum(10,50);	
					}
					
					if(pao.moveNum>0){
						pao.moveNum--;
						pao.x+=pao.xMove;
						pao.y+=pao.yMove;
						if(pao.x>140){
							pao.xMove=-1;
							pao.rColor=this.randomNum(0,255);
							pao.gColor=this.randomNum(0,255);
							pao.bColor=this.randomNum(0,255);

						}else if(pao.x<-40){
							pao.xMove=1;
							pao.rColor=this.randomNum(0,255);
							pao.gColor=this.randomNum(0,255);
							pao.bColor=this.randomNum(0,255);

						}

						if(pao.y>140){
							pao.yMove=-1;
							pao.rColor=this.randomNum(0,255);
							pao.gColor=this.randomNum(0,255);
							pao.bColor=this.randomNum(0,255);

						}else if(pao.y<-40){
							pao.yMove=1;
							pao.rColor=this.randomNum(0,255);
							pao.gColor=this.randomNum(0,255);
							pao.bColor=this.randomNum(0,255);
						}

					}
				}
			},40);
		},
		methods: {
			randomNum(minNum,maxNum){ 
			    switch(arguments.length){ 
			        case 1: 
			            return parseInt(Math.random()*minNum+1,10); 
			        break; 
			        case 2: 
			            return parseInt(Math.random()*(maxNum-minNum+1)+minNum,10); 
			        break; 
			            default: 
			                return 0; 
			            break; 
			    } 
			} 
		}
	}

</script>

<style>
.background{
	width:100vw;
	height: 100vh;
}
.pao{
	position: fixed;
	border-radius: 55%;
	width: 150rpx;
	height: 150rpx;
	color:rgba(255,255,255,.3);
	font-weight: bold;
	font-size: 10rpx;
	line-height: 300rpx;
	text-align: center;
	opacity: .2;
}
.red{
	background-color: rgba(255, 0, 0, .4);
	box-shadow: 0 2rpx 30rpx rgba(255, 0, 0, .4)
	,0 0 60rpx rgba(255, 0, 0, .4) inset;
	width: 200rpx;
	height: 200rpx;
}

.blue{
	background-color: rgba(0, 0, 255, .4);
	box-shadow: 0 2rpx 30rpx rgba(0, 0, 255, .4)
	,0 0 60rpx rgba(0, 0, 255, .4) inset;	
}
</style>