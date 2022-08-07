<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>编辑</span>
      </div>
    </template>

    <el-scrollbar
        style="height:calc( 100vh - 305px )"

    >

      <el-form
          label-position="top"
          :model="paramData" label-width="120px">
        <el-form-item label="标题">
          <el-input v-model="paramData.title"/>
        </el-form-item>

        <el-form-item label="名称">
          <el-input v-model="paramData.name"/>
        </el-form-item>

        <el-form-item label="支付前回调地址">
          <el-input v-model="paramData.payBeforeUrl"/>
        </el-form-item>

        <el-form-item label="支付后回调地址">
          <el-input v-model="paramData.payAfterUrl"/>
        </el-form-item>


        <el-form-item label="描述">
          <el-input v-model="paramData.content"/>
        </el-form-item>


        <el-form-item label="参数">
          <el-input v-model="paramData.paramStr" type="textarea" :autosize="true"/>
          <span style="font-size: 10px;">示例：{{'[{"name":"id","comment":"商品id","type":"Number","demo":"1","isNotNull":true}]'}}</span>
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
      paramData: {
        "title":"",
        "name":"",
        "paramStr":"",
        "payBeforeUrl":"",
		"payAfterUrl":"",
        "content":"",
		"param":null
      }
  
    }
  },
  mounted() {

  },
  methods: {
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
		this.paramData.param=null;
      this.$request.post({
        url: this.$api.authGoodsParent.save,
        data: this.paramData,
        stateSuccess: (res) => {
          this.changeTo.number++;
          this.$message.success(res.msg);
        },
        stateFailed: (res) => {

        }
      });
    }
  },
  watch: {
    paramData: {
      handler(newVal, oldVal) {
        if (this.paramData.folder) {
          this.paramData.pid = null;
        }
      },
      deep: true,
      immediate: true
    },
    "changeTo": {
      handler(newVal, oldVal) {
        this.paramData = this.changeTo.itemSelected;
      },
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