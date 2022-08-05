<template>
	<el-card :body-style="{ padding: '0px'}">
		<el-row>
			<el-col :span="12">
				<div style="
							background-color: rgba(118,118,118,.6);
							width: 100%;
							height: 100%;
							"></div>
			</el-col>
			<el-col :span="12">
				<div style="padding: 40px 40px 80px 40px">
					<h2>欢迎来到统一管理平台</h2>
					<el-input :prefix-icon="Avatar" size="large" :input-style="{
								'height':'50px',
							}" v-model="paramData.username" placeholder="输入账号 / 邮箱 / 手机号登录">
						<template #prepend>
							<el-icon>
								<component is="IconElAvatar"></component>
							</el-icon>
						</template>
					</el-input>
					<div style="height: 20px;"></div>
					<el-input :input-style="{
								'height':'50px',
							}" size="large" v-model="paramData.password" type="password" placeholder="输入密码">
						<template #prepend>
							<el-icon>
								<component is="IconElLock"></component>
							</el-icon>
						</template>
					</el-input>
					<div style="height: 20px;"></div>
					<el-button @click="login()" type="primary" style="width: 100%;height: 40px;">登录</el-button>
				</div>
			</el-col>
		</el-row>
	</el-card>
</template>

<script>
	export default {
		data() {
			return {
				paramData: {
					username: null,
					password: null
				}
			}
		},
		mounted() {

		},
		methods: {
			login() {
				//判断账号是否为空，并给出对应提示信息
				if (this.paramData.username == null || this.paramData.username == "") {
					this.$message({
						message: "请输入账号",
						type: 'error',
					});
					return false;
				}
				//判断密码是否为空，并给出对应提示信息
				if (this.paramData.password == null || this.paramData.password == "") {
					this.$message({
						message: "请输入密码",
						type: 'error',
					});
					return false;
				}

				this.$request.post({
					url: this.$api.user.login,
					data: this.paramData,
					stateSuccess: (res) => {
						this.$notification({
							title: '登录成功',
							message: '欢迎进入统一管理平台',
							type: 'success',
							duration: 1000
						});
            this.$store.state.userInfo=res.data.user;
						localStorage.setItem("token", res.data.token);
            this.$request.getMenus();
						this.$router.push({
							path: '/',
						})
					},
					stateFailed: (res) => {}
				});
			}
		}
	}
</script>

<style scoped>
	.el-card {
		position: fixed;
		top: 50%;
		left: 50%;
		width: 800px;
		transform: translate(-50%, -50%);
		padding: 0;
	}
</style>
