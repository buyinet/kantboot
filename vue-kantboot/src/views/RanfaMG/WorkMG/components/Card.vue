<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>作品管理</span>
        <el-button v-if="changeTo.showCard != 'CardByAdd'" @click="toShowCardByAdd()" class="button" text>新增
        </el-button>
        <el-button v-if="changeTo.showCard == 'CardByAdd'" @click="cancel()" class="button" text>取消新增
        </el-button>
      </div>
    </template>
    <el-table height="calc(100vh - 350px)" row-key="id" default-expand-all :cell-style="{ 'textAlign': 'center' }"
      :header-cell-style="{ 'textAlign': 'center' }" :data="bodyData.content" style="width: 100%">

      <el-table-column label="序号">
        <template #default="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="用户昵称" prop="userByUpload.nickname">
                <template #header>
          <div>用户昵称</div>
          <el-input v-model="paramData.data.and.vague[0].userByUpload.nickname" @input="findCommonPage()" size="small"
            placeholder="输入进行搜索" />
        </template>
      </el-table-column>
      <el-table-column label="用户手机号" prop="userByUpload.phoneNumber">
                <template #header>
          <div>用户手机号</div>
          <el-input v-model="paramData.data.and.vague[0].userByUpload.phoneNumber" @input="findCommonPage()" size="small"
            placeholder="输入进行搜索" />
        </template>
      </el-table-column>
      <el-table-column label="编号" prop="iden">
                <template #header>
          <div>编号</div>
          <el-input v-model="paramData.data.and.vague[0].iden" @input="findCommonPage()" size="small"
            placeholder="输入进行搜索" />
        </template>
      </el-table-column>

      <el-table-column prop="name" label="染发前图片">
        <template #header>
          <div>染发前图片</div>
        </template>
        <template #default="scope">
          <el-image :preview-src-list="[scope.row.fileUrlByFrontCoverImage]" :z-index="100" :preview-teleported="true"
            :initial-index="1" fit="cover" style="width: 100px; height: 150px"
            :src="scope.row.fileUrlByFrontCoverImage" />
        </template>
      </el-table-column>

      <el-table-column prop="name" label="染发后图片">
        <template #header>
          <div>染发后图片</div>
        </template>
        <template #default="scope">
          <el-image fit="cover" :z-index="100" :preview-src-list="[scope.row.fileUrlByBackCoverImage]"
            :preview-teleported="true" style="width: 100px; height: 150px" :src="scope.row.fileUrlByBackCoverImage" />
        </template>
      </el-table-column>

      <el-table-column prop="ranfaBrand.name" label="品牌">
        <template #header>
          <div>品牌</div>
          <el-input v-model="paramData.data.and.vague[0].ranfaBrand.name" @input="findCommonPage()" size="small"
            placeholder="输入进行搜索" />
        </template>
      </el-table-column>

      <el-table-column prop="ranfaTechniques" label="分类">
        <template #header>
          <div>分类</div>
          <el-input v-model="paramData.data.and.vague[0].ranfaTechniques[0].name" @input="findCommonPage()" size="small"
            placeholder="输入进行搜索" />
        </template>
        <template #default="scope">
          <span v-for="(item, index) in scope.row.ranfaTechniques">{{ item.name }}
            <span v-if="index < scope.row.ranfaTechniques.length - 1">、</span>
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="auditStatus" label="审核">
        <template #header>
          <div>审核</div>
          <el-select v-model="paramData.data.and.eq[0].auditStatus" @change="findCommonPage()" class="m-2"
            placeholder="Select" size="large">
            <el-option :key="null" label="全部" :value="null" />
            <el-option :key="0" label="未审核" :value="0" />

            <el-option :key="1" label="已通过" :value="1" />

            <el-option :key="2" label="未通过" :value="2" />
             <el-option :key="3" label="屏蔽" :value="3" />
          </el-select>

        </template>
        <template #default="scope">
          <span>
            <span v-if="scope.row.auditStatus == 0">未审核</span>
            <span v-if="scope.row.auditStatus == 1">已通过</span>
            <div v-if="scope.row.auditStatus == 2">
              <div>未通过</div>
              <div style="color:gray">
              未通过原因：
              {{scope.row.reasonsForFailureInAudit}}</div>
            </div>
            <span v-if="scope.row.auditStatus == 3">屏蔽</span>
          </span>

        </template>
      </el-table-column>
      <el-table-column prop="process" label="操作说明">
        <template #header>
          <div>操作说明</div>
          <el-input v-model="paramData.data.and.vague[0].process" @input="findCommonPage()" size="small"
            placeholder="输入进行搜索" />
        </template>
        <template #default="scope">
          <view v-if="scope.row.process!=null">
          <view
          v-for="(item, index) in scope.row.process.split('\n')" style="text-indent:65rpx;">
            {{ item }}
          </view>
          </view>

</template>
      </el-table-column>
      <el-table-column prop="gmtCreate" label="创建时间" />

      <el-table-column fixed="right" label="操作" width="240">
        <template #default="scope">

          <el-popconfirm v-if=" scope.row.auditStatus == 1"
            title="确定屏蔽这个作品吗?" icon-color="red" confirmButtonType="danger" confirmButtonText="确定" 
            cancelButtonText="取消"
            @confirm="toAuditStatus3(scope.row.id)">
 <template #reference>
          <el-button link type="primary" size="small">
            屏蔽
          </el-button>

 </template>
          </el-popconfirm>
          <el-button link type="primary" size="small"
            v-if="
            scope.row.auditStatus == 0 &&
            ((changeTo.showCard == 'CardByApply' && changeTo.itemSelected.id != scope.row.id) || 
            changeTo.showCard != 'CardByApply')"
            @click="toShowCardByApply(scope.row)">
            审核
          </el-button>

          <el-button link type="primary" size="small"
            v-if="changeTo.showCard == 'CardByApply' && changeTo.itemSelected.id == scope.row.id" @click="cancel">
            取消审核
          </el-button>

          <el-button link type="primary" size="small"
            v-if="(changeTo.showCard == 'CardByEdit' && changeTo.itemSelected.id != scope.row.id) || changeTo.showCard != 'CardByEdit'"
            @click="toShowCardByEdit(scope.row)">
            修改
          </el-button>

          <el-button link type="primary" size="small"
            v-if="changeTo.showCard == 'CardByEdit' && changeTo.itemSelected.id == scope.row.id" @click="cancel">
            取消修改
          </el-button>
          <el-popconfirm v-if="!(changeTo.showCard == 'CardByEdit' && changeTo.itemSelected.id == scope.row.id)"
            title="确定删除这个菜单吗?" icon-color="red" confirmButtonType="danger" confirmButtonText="确定" cancelButtonText="取消"
            @confirm="removeById(scope.row.id)">
            <template #reference>
              <el-button link type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination style="margin-left: 50%;" layout="prev, pager, next" v-model:current-page="paramData.pageNumber"
      v-model:total="bodyData.totalElements" v-model:page-size="paramData.pageSize" @current-change="findCommonPage" />

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
            "eq": [
              { auditStatus: null,"iden":null }
            ],
            "like": [],
            "gt": [],
            "lt": [],
            "ge": [],
            "le": [],
            "vague": [{ 
              ranfaBrand:{name:null},
              userByUpload:{nickname:null,phoneNumber:null},
              "iden":null,
              "name": null, "content": null, "process": null,"ranfaTechniques":[{"name":null}] }]
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
    toAuditStatus3(id){
      this.$request.post({
        url: this.$api.ranfaWork.toAuditStatus3,
        data: {
          "id":id
        },
        stateSuccess: (res) => {
          this.findCommonPage();
          this.$message.success(res.msg);
        },
        stateFailed: (res) => {

        }
      });
    },
    toShowCardByAdd() {
      this.changeTo.rightSpan = 8;
      this.changeTo.showCard = "CardByAdd";
    },
    toShowCardByApply(item) {
      this.changeTo.rightSpan = 8;
      this.changeTo.showCard = "CardByApply";
      this.changeTo.itemSelected = item;
    },
    toShowCardByEdit(item) {
      this.changeTo.rightSpan = 8;
      this.changeTo.showCard = "CardByEdit";
      this.changeTo.itemSelected = item;
    },
    removeById(id) {
      this.$request.post({
        url: this.$api.ranfaWork.remove,
        data: { "id": id },
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