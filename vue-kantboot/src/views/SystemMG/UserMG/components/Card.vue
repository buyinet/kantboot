<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>用户管理</span>
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

      <el-table-column prop="username" label="账号">
        <template #header>
          <div>账号</div>
          <el-input v-model="paramData.data.and.vague[0].username"
                    @input="findCommonPage()"
                    size="small" placeholder="输入进行搜索" />
        </template>
      </el-table-column>

      <el-table-column prop="nickname" label="昵称">
        <template #header>
          <div>昵称</div>
          <el-input v-model="paramData.data.and.vague[0].nickname"
                    @input="findCommonPage()"
                    size="small" placeholder="输入进行搜索" />
        </template>
      </el-table-column>

      <el-table-column prop="show" label="角色">
        <template #header>
          <div>角色</div>
          <el-input v-model="paramData.data.and.vague[0].roles[0].zhName"
                    @input="findCommonPage()"
                    size="small" placeholder="输入进行搜索" />
        </template>
        <template #default="scope">
          <span v-for="(item,index) in scope.row.roles">{{item.zhName}}
            <span v-if="index<scope.row.roles.length-1">、</span></span>
        </template>
      </el-table-column>
      <el-table-column prop="password" label="密码摘要"/>

      <el-table-column prop="phoneNumber" label="手机号码">
        <template #header>
          <div>手机号码</div>
          <el-input v-model="paramData.data.and.vague[0].phoneNumber"
                    @input="findCommonPage()"
                    size="small" placeholder="输入进行搜索" />
        </template>
      </el-table-column>

      <el-table-column prop="idCard" label="真实姓名">
        <template #header>
          <div>真实姓名</div>
          <el-input v-model="paramData.data.and.vague[0].realname"
                    @input="findCommonPage()"
                    size="small" placeholder="输入进行搜索" />
        </template>
      </el-table-column>

      <el-table-column prop="idCard" label="身份证">
        <template #header>
          <div>身份证</div>
          <el-input v-model="paramData.data.and.vague[0].idCard"
                    @input="findCommonPage()"
                    size="small" placeholder="输入进行搜索" />
        </template>
      </el-table-column>

      <el-table-column prop="createIp" label="创建时IP"/>
      <el-table-column prop="createDevice" label="创建时UA"/>
      <el-table-column prop="gmtCreate" label="创建时间"/>


      <el-table-column fixed="right" label="操作" width="120">
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
        @current-change="findCommonPage()"
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
        "sortField": "id",
        "data": {
          "and": {
            "eq": [],
            "like": [],
            "gt": [],
            "lt": [],
            "ge": [],
            "le": [],
            "vague":[
              {"username":"","roles":[{"zhName":null}],"phoneNumber":"","nickname":"","realname":"","idCard":""}
            ],
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
        url: this.$api.user.remove,
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
        url: this.$api.user.findCommonPage,
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