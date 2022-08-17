<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>新增</span>
      </div>
    </template>

    <el-scrollbar style="height:calc( 100vh - 305px )">

      <el-form label-position="top" :model="paramData" label-width="120px">

        <el-form-item label="标题">
          <el-input v-model="paramData.title" />
        </el-form-item>

        <el-form-item label="主体">
          <el-input v-model="paramData.bodyName" />
        </el-form-item>

        <el-form-item label="字段">
          <el-input v-model="paramData.bodyField" />
        </el-form-item>

        <el-form-item label="文件类型">
          <el-input v-model="paramData.fileType" />
        </el-form-item>

        <el-form-item label="存储方式">
          <el-radio v-model="paramData.storageType" label="oss">oss (oss存储)</el-radio>
          <el-radio v-model="paramData.storageType" label="path">path (本地路径存储)</el-radio>
        </el-form-item>

        <el-form-item label="选择oss (oss存储)" v-if="paramData.storageType == 'oss'">
          <el-select v-model="paramData.fileOssId" style="width: 100%">
            <el-option v-for="item in fileOssList" :key="item.id" :label="item.title + '[' + item.endpoint + ']'"
              :value="item.id" />
          </el-select>
        </el-form-item>



        <el-form-item label="选择path (本地路径存储)" v-if="paramData.storageType == 'path'">
          <el-select v-model="paramData.filePathId" style="width: 100%">
            <el-option v-for="item in filePathList" :key="item.id" :label="item.title + '[' + item.path + ']'"
              :value="item.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="是否开启授权访问">
          <el-radio v-model="paramData.authorizeVisit" :label="true">是</el-radio>
          <el-radio v-model="paramData.authorizeVisit" :label="false">否</el-radio>
        </el-form-item>

        <el-form-item label="是否开启水印">
          <el-radio v-model="paramData.useWatermark" :label="true">是</el-radio>
          <el-radio v-model="paramData.useWatermark" :label="false">否</el-radio>
        </el-form-item>
        
        <el-form-item label="设置授权回调地址" v-if="paramData.authorizeVisit">
          <el-input v-model="paramData.authorizeVisitCallbackUrl" />
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="paramData.content" type="textarea" />
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
  components: { RolesSelect },
  props: {
    "changeTo": {
      type: Object,
      default: ''
    }
  },
  data() {
    return {
      paramData: {
        "title": "",
        "bodyName": "",
        "bodyField": "",
        "storageType": "",
        "fileType": "",
        "content": "",
        "fileOssId": null,
        "filePathId": null,
        authorizeVisit: false,
        authorizeVisitCallbackUrl:null,
        useWatermark:false
      },
      fileOssList: [],
      filePathList: []
    }
  },
  mounted() {
    this.getFileOssList();
    this.getFilePathList();
  },
  methods: {
    getFileOssList() {
      this.$request.post({
        url: this.$api.fileOss.findCommonList,
        data: { entity: {} },
        stateSuccess: (res) => {
          this.fileOssList = res.data;
        },
        stateFailed: (res) => {

        }
      });
    },
    getFilePathList() {
      this.$request.post({
        url: this.$api.filePath.findCommonList,
        data: { entity: {} },
        stateSuccess: (res) => {
          this.filePathList = res.data;
        },
        stateFailed: (res) => {

        }
      });
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
        url: this.$api.fileParent.save,
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