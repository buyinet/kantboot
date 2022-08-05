<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>新增</span>
      </div>
    </template>

    <el-scrollbar
        style="height:calc( 100vh - 305px )"
    >

      <el-form
          label-position="top"
          :model="paramData" label-width="120px">
        <el-form-item label="账号">
          <el-input v-model="paramData.username"/>
        </el-form-item>

        <el-form-item label="昵称">
          <el-input v-model="paramData.nickname"/>
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="paramData.passwordByInput"/>
        </el-form-item>
        <el-form-item label="密码摘要对比">
          <el-input type="textarea" v-model="paramData.password" disabled/>
        </el-form-item>

        <el-form-item label="角色">
          <roles-select :roles-by-select="paramData.roles" />
        </el-form-item>

        <el-form-item label="真实姓名">
          <el-input v-model="paramData.realname"/>
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="paramData.phoneNumber"/>
        </el-form-item>
        <el-form-item label="身份证">
          <el-input v-model="paramData.idCard"/>
        </el-form-item>
        <div style="height: 20px"></div>
        <el-form-item>
          <div style="display: inline-block;margin-left: 50%;transform: translateX(-50%)">
            <el-button type="primary" @click="toSave()">确定</el-button>
            <el-button @click="cancel">取消</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-scrollbar>

  </el-card>
</template>
<script>
import RolesSelect from "@/components/System/RolesSelect";
import bcrypt from 'bcryptjs';

export default {
  components: {RolesSelect},
  props: {
    "changeTo": {
      type: Object,
      default: ''
    }
  },
  data() {
    return {
      paramData: {
        "username": "",
        "nickname": "",
        "phoneNumber": "",
        "roles": [],
        "password": "",
        "passwordByInput": "",
      }
    }
  },
  mounted() {
  },
  methods: {
    addUser() {
      console.log(this.encryptPassword(this.password))
    },
    encryptPassword(password) {
      const salt = bcrypt.genSaltSync(10)
      return bcrypt.hashSync(password, salt)
    },
    cancel() {
      this.changeTo.rightSpan = 0;
      this.changeTo.showCard = null;
    },
    selectIcon(iconStr) {
      if (this.paramData.icon == iconStr) {
        this.paramData.icon = null;
        return false;
      }
      this.paramData.icon = iconStr;
    },
    toSave() {
      if (this.paramData.folder && (this.paramData.pagePath == null || this.paramData.pagePath == "")) {
        this.paramData.pagePath = "DefaultFolder.vue";
      }
      this.$request.post({
        url: this.$api.user.save,
        data: this.paramData,
        stateSuccess: (res) => {
          this.changeTo.number++;
          this.$message.success(res.msg);
        },
        stateFailed: (res) => {

        }
      });
    }
  }
  ,
  watch: {
    "paramData.passwordByInput": {
      handler(newVal, oldVal) {
        if(this.paramData.passwordByInput==null||this.paramData.passwordByInput==""){
          this.paramData.password=null;
          return false;
        }
        this.paramData.password = this.encryptPassword(this.paramData.passwordByInput);
      }
      ,
      deep: true,
      immediate: true
    }

  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 480px;
}

.icon-by-selected {
  padding: 25px;
  color: rgba(0, 0, 0, .3)
}

.el-button {
  padding: 20px;
}
</style>