<template>
  <div>
    <el-icon v-show="!$store.state.aside.collapse" @click="setCollapse(true)">
      <component is="IconElFold"></component>
    </el-icon>
    <el-icon v-show="$store.state.aside.collapse" @click="setCollapse(false)">
      <component is="IconElExpand"></component>
    </el-icon>
    <span class="page-title">
			{{ $store.state.header.pageTitle }}
		</span>
    <span class="float-right">
    <el-dropdown>
    <span class="el-dropdown-link">
      个人信息：{{ $store.state.userInfo.nickname }}
      <el-icon class="el-icon--right">
        <arrow-down/>
      </el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item>角色：
          <span v-for="(item,index) in $store.state.userInfo.roles">{{item.zhName}}
          <span v-if="index<$store.state.userInfo.roles.length-1">、</span>
          </span>
        </el-dropdown-item>
        <el-dropdown-item>昵称：{{ $store.state.userInfo.nickname }}</el-dropdown-item>
        <el-dropdown-item>手机：{{ $store.state.userInfo.phoneNumber }}</el-dropdown-item>
        <el-dropdown-item>身份证：{{ $store.state.userInfo.idCard }}</el-dropdown-item>
        <el-dropdown-item @click="loginOut" divided>退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
    </span>
  </div>
</template>

<script>
export default {
  name: 'Static',
  data() {
    return {
      collapse: this.$store.state.aside.collapse
    }
  },
  mounted() {
    this.collapse = this.$store.state.aside.collapse;
  },
  methods: {
    setCollapse(varr) {
      this.$store.state.aside.collapse = varr;
    },
    loginOut() {
      this.$request.post({
        url: this.$api.user.loginOut,
        stateSuccess: (res) => {
          this.$message.success(res.msg);
          setTimeout(() => {
            this.$store.state.userInfo = null;
            this.$router.push({"path": "/login"});
          }, 500);
        },
        stateFailed: (res) => {
          this.$message.error(res.errMsg);
        }
      });
    }
  },
  watch: {}
}
</script>

<style scoped>
.page-title {
  margin-left: 20px;
  font-size: 15px;
  color: rgba(0, 0, 0, .8);
}

.float-right {
  float: right;
  font-size: 15px;
}
</style>
