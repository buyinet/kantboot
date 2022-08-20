<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>作品管理</span>
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


      <el-table-column prop="name" label="染发前图片">
        <template #header>
          <div>染发前图片</div>
        </template>
        <template #default="scope">
          <el-image
          :preview-src-list="[scope.row.fileUrlByFrontCoverImage]"
          :z-index="100"
          :preview-teleported="true"
          :initial-index="1"
          fit="cover"
          style="width: 100px; height: 150px"
          :src="scope.row.fileUrlByFrontCoverImage" />
        </template>
      </el-table-column>
            <el-table-column prop="name" label="染发后图片">
        <template #header>
          <div>染发后图片</div>
        </template>
        <template #default="scope">
          <el-image
          fit="cover"
           :z-index="100"
            :preview-src-list="[scope.row.fileUrlByBackCoverImage]"
          :preview-teleported="true"
          style="width: 100px; height: 150px"
          :src="scope.row.fileUrlByBackCoverImage" />
        </template>
      </el-table-column>
      <el-table-column prop="process" label="操作说明">
        <template #header>
          <div>操作说明</div>
          <el-input v-model="paramData.data.and.vague[0].process"
                    @input="findCommonPage()"
                    size="small" placeholder="输入进行搜索" />
        </template>
        <template #default="scope">
          <view v-for="(item,index) in scope.row.process.split('\n')"
          style="text-indent:65rpx;"
          >
          {{item}}
          </view>
        </template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建时间"/>

      <el-table-column fixed="right" label="操作" width="240">
        <template #default="scope">

                    <el-button link type="primary" size="small"
                     v-if="(changeTo.showCard=='CardByApply'&&changeTo.itemSelected.id!=scope.row.id)||changeTo.showCard!='CardByApply'"
          @click="toShowCardByApply(scope.row)"
          >
            审核
          </el-button>
                    <el-button link type="primary" size="small"
                     v-if="changeTo.showCard=='CardByApply'&&changeTo.itemSelected.id==scope.row.id"
                     @click="cancel"
          >
            取消审核
          </el-button>

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
        "sortField": "gmtCreate",
        "data": {
          "and": {
            "eq": [],
            "like": [],
            "gt": [],
            "lt": [],
            "ge": [],
            "le": [],
            "vague":[{"name":null,"content":null,"process":null}]
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
    toShowCardByApply(item){
      this.changeTo.rightSpan = 8;
      this.changeTo.showCard = "CardByApply";
      this.changeTo.itemSelected=item;
    },
    toShowCardByEdit(item){
      this.changeTo.rightSpan = 8;
      this.changeTo.showCard = "CardByEdit";
      this.changeTo.itemSelected=item;
    },
    removeById(id){
      this.$request.post({
        url: this.$api.ranfaWork.remove,
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
        url: this.$api.ranfaWork.findCommonPage,
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