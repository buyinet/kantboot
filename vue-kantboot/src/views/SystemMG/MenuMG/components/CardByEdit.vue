<template>
  <el-card>
    <template #header>
      <div class="card-header">
        <span>修改</span>
      </div>
    </template>

    <el-scrollbar
        style="height:calc( 100vh - 305px )"
    >

      <el-form
          label-position="top"
          :model="paramData" label-width="120px">
        <el-form-item label="菜单名称">
          <el-input v-model="paramData.label"/>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="paramData.name"/>
        </el-form-item>
        <el-form-item label="优先级">
          <el-input type="number" v-model="paramData.priority"/>
        </el-form-item>
        <el-form-item label="访问路径" v-if="!paramData.folder">
          <el-input v-model="paramData.path"/>
        </el-form-item>
        <el-form-item label="页面组件路径">
          <el-input v-model="paramData.pagePath"/>
        </el-form-item>
        <el-form-item label="是否显示">
          <el-switch v-model="paramData.show"/>
        </el-form-item>

        <el-form-item label="是否为目录">
          <el-switch v-model="paramData.folder"/>
        </el-form-item>

        <el-form-item label="选择目录" v-if="!paramData.folder">
          <el-select
              v-model="paramData.pid"
              style="width: 100%"
          >
            <el-option
                :key="null"
                label="顶层目录"
                :value="null"
            />
            <el-option
                v-for="item in menus"
                v-show="item.folder"
                :key="item.id"
                :label="item.label"
                :value="item.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="可访问的角色">
          <roles-select :roles-by-select="paramData.roles"/>
        </el-form-item>

        <div>
          <el-divider>
            <el-icon v-if="paramData.icon!=null||paramData.icon!=''">
              <component :is="paramData.icon"></component>
            </el-icon>
            图标
          </el-divider>
          <div>
            <el-scrollbar style="height: 150px">
              <el-icon
                  @click="selectIcon(item)"
                  class="icon-by-selected"
                  :style="paramData.icon==item?'color: rgba(0,0,0,1)':''" size="20px" v-for="(item,index) in $icons">
                <component :is="item"></component>
              </el-icon>
            </el-scrollbar>
          </div>
        </div>
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
        "id":null,
        "path": null,
        "name": null,
        "icon": null,
        "folder": false,
        "label": null,
        "pagePath": null,
        "priority": 1,
        "pid": null,
        "show": true,
        "roles": [],
        "menus":[]
      }

    }
  },
  mounted() {
    this.getMenus();
  },
  methods: {
    getMenus(){
      this.$request.post({
        url: this.$api.menu.findCommonList,
        data: {
          eq:[{"folder":true}]
          , entity:{}},
        stateSuccess: (res) => {
          this.menus=res.data;
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
        url: this.$api.menu.save,
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