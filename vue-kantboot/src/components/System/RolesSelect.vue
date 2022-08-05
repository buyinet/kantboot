<template>
    <el-select
        v-model="rolesBySelect1"
        style="width: 100%"
        multiple>
      <el-option
          v-for="item in roles"
          :key="item.id"
          :label="item.zhName"
          :value="JSON.stringify(item)"/>
    </el-select>
</template>

<script>
export default {
  name: "RolesInput",
  props:{
    "rolesBySelect": {
      type: Array,
      default: []
    }
  },
  data(){
    return {
      paramData: {
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
        },
      roles:[],
      rolesBySelect1:[],
      rolesBySelect2:[]
    }
  },
  mounted() {
    this.rolesBySelect2.splice(0,this.rolesBySelect2.length);
    for (var i=0;i<this.rolesBySelect.length;i++){
      this.rolesBySelect2.push(
          JSON.stringify(this.rolesBySelect[i])
      );
    }
    this.rolesBySelect1=this.rolesBySelect2;
    this.getRoles();
  },
  methods:{

    getRoles(){
        this.$request.post({
          url: this.$api.role.findCommonList,
          data: this.paramData,
          stateSuccess: (res) => {
            this.roles = res.data;
          },
          stateFailed: (res) => {

          }
        });
    }
  },
  watch: {
    rolesBySelect: function(){
      this.rolesBySelect2.splice(0,this.rolesBySelect2.length);
      for (var i=0;i<this.rolesBySelect.length;i++){
        this.rolesBySelect2.push(
            JSON.stringify(this.rolesBySelect[i])
        );
      }
      this.rolesBySelect1=this.rolesBySelect2;
    },
    rolesBySelect1: function() {
      this.rolesBySelect.splice(0,this.rolesBySelect.length)
      for (var i=0;i<this.rolesBySelect1.length;i++){
        this.rolesBySelect.push(
            JSON.parse(this.rolesBySelect1[i])
        );
      }
    }
  },
}
</script>

<style scoped>

</style>