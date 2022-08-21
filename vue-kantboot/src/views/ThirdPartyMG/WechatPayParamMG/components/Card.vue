<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>商户号管理</span>
        <el-button
            v-if="changeTo.showCard!='CardByAdd'"
            @click="toShowCardByAdd()"
            class="button"
            text>新增
        </el-button>
        <el-button
            v-if="changeTo.showCard=='CardByAdd'"
            @click="cancel()"
            class="button"
            text>取消新增
        </el-button>
      </div>
    </template>
    <el-table
        height="calc(100vh - 350px)"
        row-key="id"
        default-expand-all
        :cell-style="{'textAlign':'center'}"
        :header-cell-style="{'textAlign':'center'}"
        :data="bodyData.content" style="width: 100%">

      <el-table-column label="序号">
        <template #default="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="title" label="标题"/>
      <el-table-column prop="mchId" label="Mch Id"/>
      <el-table-column prop="mchKey" label="Mch Key"/>
      <el-table-column prop="content" label="描述"/>
      <el-table-column prop="fileUrlByApiclientCertP12" label="提现证书（文件url）"/>
      <el-table-column prop="gmtCreate" label="创建时间"/>

      <el-table-column fixed="right" label="操作" width="240">
        <template #default="scope">

          <el-button link type="primary" size="small"
                     v-if="(changeTo.showCard=='CardByEdit'&&changeTo.itemSelected.id!=scope.row.id)||changeTo.showCard!='CardByEdit'"
          @click="toShowCardByEdit(scope.row)"
          >
            修改
          </el-button>
          <el-button link type="primary" size="small"
                     v-if="changeTo.showCard=='CardByEdit'&&changeTo.itemSelected.id==scope.row.id"
                     @click="cancel"
          >
            取消修改
          </el-button>
          <el-popconfirm
              v-if="!(changeTo.showCard=='CardByEdit'&&changeTo.itemSelected.id==scope.row.id)"
              title="确定删除这个菜单吗?"
                         icon-color="red"
                         confirmButtonType="danger"
                         confirmButtonText="确定"
                         cancelButtonText="取消"
                         @confirm="removeById(scope.row.id)"
          >
            <template #reference>
              <el-button link type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
        style="margin-left: 50%;"
        layout="prev, pager, next"
        v-model:current-page="paramData.pageNumber"
        v-model:total="bodyData.totalElements"
        v-model:page-size="paramData.pageSize"
        @current-change="findCommonPage"
    />

  </el-card>
</template>

<script>
export default {
  props: {
    "changeTo": {
      type: Object,
      default: ''
    }
  },
  data() {
    return {
      bodyData: {},
      paramData: {
        "pageNumber": 1,
        "pageSize": 10,
        "sortType": "DESC",
        "sortField": "gmtModified",
        "data": {
          "and": {
            "eq": [],
            "like": [],
            "gt": [],
            "lt": [],
            "ge": [],
            "le": []
          },
          "or": {
            "eq": [],
            "like": [],
            "gt": [],
            "lt": [],
            "ge": [],
            "le": []
          },
          "entity": {},
          "notNull": [],
          "isNull": []
        }
      }
    }
  },
  mounted() {
    this.findCommonPage();
  },
  methods: {
    toShowCardByAdd() {
      this.changeTo.rightSpan = 8;
      this.changeTo.showCard = "CardByAdd";
    },
    toShowCardByEdit(item){
      this.changeTo.rightSpan = 8;
      this.changeTo.showCard = "CardByEdit";
      this.changeTo.itemSelected=item;
    },
    removeById(id){
      this.$request.post({
        url: this.$api.tpWechatAppletPayParam.remove,
        data: {"id":id},
        stateSuccess: (res) => {
          this.$message(res.msg);
          this.findCommonPage();
        },
        stateFailed: (res) => {
          this.$message.error(res.errMsg);
        }
      });
    },
    cancel() {
      this.changeTo.rightSpan = 0;
      this.changeTo.showCard = null;
    },
    findCommonPage() {

      this.$request.post({
        url: this.$api.tpWechatAppletPayParam.findCommonPage,
        data: this.paramData,
        stateSuccess: (res) => {
          this.bodyData = res.data;
        },
        stateFailed: (res) => {

        }
      });
    }
  },
  watch: {
    "changeTo": {
      handler(newVal, oldVal) {
        this.findCommonPage();
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
</style>