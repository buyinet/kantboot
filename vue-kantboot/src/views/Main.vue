<template>
  <div>
    <el-container>
		<Aside></Aside>
      <el-container>
        <el-main style="margin: 0;padding: 20px;box-sizing: border-box;">
			<el-header>
				<Header></Header>
				<Breadcrumb></Breadcrumb>
			</el-header>
			<router-view></router-view>
		</el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Aside from '@/components/System/Aside.vue'
import Header from '@/components/System/Header.vue'
import Breadcrumb from '@/components/System/Breadcrumb.vue'

export default {
  name: 'Static',
  components: {
	  Aside,
	  Header,
	  Breadcrumb
  },
  data(){
	  return {
	  }
  },
  mounted() {
	  
  }
}
</script>

<style>
.el-header{
	padding: 0;
	background-color: rgba(0,0,0,0);
	height: auto;
	padding: 20px;
}

</style>
