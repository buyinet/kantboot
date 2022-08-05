<template>
	<el-breadcrumb>
		<el-breadcrumb-item v-for="(item,index) in $store.state.breadcrumb"
                        :to="item.to"
    >
      <el-dropdown trigger="contextmenu">
    <span>
      {{ item.text }}
    </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>
              <el-button link  @click="deleteByIndex(index)" type="danger" size="small">删除</el-button>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-breadcrumb-item>
	</el-breadcrumb>
</template>

<script>
export default {
  name: 'Static',
  methods:{
    deleteByIndex(index){
      this.$store.state.breadcrumb.splice(index,1);
    }
  }
}
</script>

<style scoped>
	.el-breadcrumb {
		box-shadow: 0 0 10px rgba(118,118,118,.6) inset;
		width: calc(100vw - 240px);
		padding: 20px 10px 20px 10px;
		border-radius: 10px;
		box-sizing: border-box;
		margin-top: 20px;
		margin-left: 50%;
		transform: translateX(-50%);
		background-color: #FFFFFF;
	}
</style>
