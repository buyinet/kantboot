<template>
	<el-aside :style="'width:'+width+'px'">
		<el-scrollbar>
			<h4 class="title"><span v-show="!$store.state.aside.collapse">统一管理平台{{str}}</span></h4>
			<el-menu :router="true" :collapse="$store.state.aside.collapse" background-color="#2F2F2F"
				text-color="#fff" active-text-color="#ffd04b">
				<template v-for="(item,index) in $store.state.menus">

					<el-menu-item :index="item.path"
                        @click="$store.state.header.pageTitle=item.label;addBreadcrumb(item.label,item.path)"
                        v-if="!item.folder">
						<el-icon>
							<component :is="item.icon"></component>
						</el-icon>
						<span>{{item.label}}</span>
					</el-menu-item>


					<el-sub-menu v-if="item.folder" :index="item.id">
						<template #title>
							<el-icon>
								<component :is="item.icon"></component>
							</el-icon>
							<span>{{item.label}}</span>
						</template>
						<el-menu-item-group>
							<el-menu-item v-for="(item1,index1) in item.children"
                            @click="$store.state.header.pageTitle=item.label+' > '+item1.label;addBreadcrumb(item1.label,item1.path)"
                            :index="item1.path">
                • {{item1.label}}
							</el-menu-item>
						</el-menu-item-group>
					</el-sub-menu>

				</template>
			</el-menu>


		</el-scrollbar>
	</el-aside>
</template>


<script>
	export default {
		name: 'Static',
		data() {
			return {
				width: 200,
				str:"",
			}
		},
  mounted() {
	// let str0="222"
	// let str1="aaa/${str0}";
	// this.str=eval("`"+str1+"`");
  },
    methods:{
      addBreadcrumb(text,to){
        for(var i=0;i<this.$store.state.breadcrumb.length;i++){
          if(to==this.$store.state.breadcrumb[i].to){
            return false;
          }
        }
        this.$store.state.breadcrumb.push({"text":text,"to":to});
        if(this.$store.state.breadcrumb.length>10){
          this.$store.state.breadcrumb.splice(0,1);
        }
      }
    },
		watch: {
			"$store.state.aside.collapse": {
				handler(newVal, oldVal) {
					if (newVal) {
						this.width = 80;
						return;
					}
					this.width = 200;
				},
				deep: true,
				immediate: true
			},
		}
	}
</script>

<style lang="scss" scoped>
	.title {
		color: aliceblue;
		text-align: center;
		font-weight: lighter;
	}

	.el-aside {
		z-index: 1000;
		background-color: #2F2F2F;
		box-shadow: 0 0 30px rgba(118, 118, 118, 1);
		height: 100vh;
		padding: 10px;
		box-sizing: border-box;

		.el-menu {
			background-color: rgba(0, 0, 0, 0);
			height: 100%;
			border: none;
			box-sizing: border-box;
		}

		.el-menu-item {
			border-radius: 20px;
		}


	}
</style>
