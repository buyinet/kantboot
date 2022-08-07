<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>
          系统设置
        </span>
      </div>
    </template>
    <div>

      <el-form
          label-position="top"
          :model="paramData" label-width="120px">

        <el-form-item label="设置Host">
          <el-input placeholder="请输入Host" v-model="paramData.host"></el-input>
          <span class="commit">当前域名加端口号</span>
        </el-form-item>

        <el-form-item label="根据名称查看id">
          <el-input :value="paramData.fileVisitUrl+'/${id}'" disabled></el-input>
        </el-form-item>

        <el-form-item label="根据名称查看文件">
          <el-input :value="paramData.fileVisitUrl+'/${bodyName}/${bodyField}'" disabled></el-input>
        </el-form-item>


        <el-form-item label="设置默认角色">
        <el-select
            style="width: 100%"
            v-model="paramData.roleIdByDefaultUse" placeholder="Select">
          <el-option
              v-for="item in bodyData.roles"
              :key="item.id"
              :label="item.zhName+'['+item.name+']'"
              :value="item.id"
          >
          </el-option>
        </el-select>
        <span class="commit">用户在注册时默认的角色</span>
      </el-form-item>

        <el-form-item label="设置默认微信小程序参数">
          <el-select
              style="width: 100%"
              v-model="paramData.appletWechatParamIdByDefault" placeholder="Select">
            <el-option
                v-for="item in bodyData.wechatAppletParams"
                :key="item.id"
                :label="item.title+'['+item.appId+']'"
                :value="item.id"
            >
            </el-option>
          </el-select>
          <span class="commit">在微信小程序使用的参数</span>
        </el-form-item>

      </el-form>
    </div>
  </el-card>
</template>

<script>
import RolesSelect from "@/components/System/RolesSelect";
export default {
  components:{RolesSelect},
  props: {
    "changeTo": {
      type: Object,
      default: ''
    }
  },
  data() {
    return {
      bodyData: {
        roles:[],
        wechatAppletParams:[]
      },
      paramData: {
        roleIdByDefaultUse:null,
        host:null
      }
    }
  },
  mounted() {
    this.getSetting();
    this.getRoles();
    this.getWechatAppletParams();
  },
  methods: {
    getSetting(){
      this.$request.post({
        url: this.$api.setting.getSetting,
        stateSuccess: (res) => {
          this.paramData=res.data;
        },
        stateFailed: (res) => {
          this.$message.error(res.errMsg);
        }
      });
    },

    getRoles(){
      this.$request.post({
        url: this.$api.role.findCommonList,
        data: {
          entity:{}
        },
        stateSuccess: (res) => {
          this.bodyData.roles = res.data;
        },
        stateFailed: (res) => {

        }
      });
    },

    getWechatAppletParams(){
      this.$request.post({
        url: this.$api.tpWechatAppletParam.findCommonList,
        data: {
          entity:{}
        },
        stateSuccess: (res) => {
          this.bodyData.wechatAppletParams = res.data;
        },
        stateFailed: (res) => {

        }
      });
    }
  }
}
</script>

<style scoped>
.commit{
  font-size: 10px;
  color: gray;
}

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
</style>