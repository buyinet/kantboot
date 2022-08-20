<template>
  <el-row :gutter="20">
    <el-col :span="24-changeTo.rightSpan">
      <card :change-to="changeTo"></card>
    </el-col>
    <el-col :span="changeTo.rightSpan">
      <card-by-add
          :change-to="changeTo"
          v-if="changeTo.showCard=='CardByAdd'"
      ></card-by-add>

      <card-by-edit
          :change-to="changeTo"
          v-if="changeTo.showCard=='CardByEdit'"
      ></card-by-edit>

      <card-by-apply
          :change-to="changeTo"
          v-if="changeTo.showCard=='CardByApply'"
      ></card-by-apply>

    </el-col>
  </el-row>
</template>

<script>
import Card from "./components/Card";
import CardByAdd from "./components/CardByAdd";
import CardByEdit from "./components/CardByEdit";
import CardByApply from "./components/CardByApply";
export default {
  components: {
    CardByEdit,
    CardByAdd,
    Card,
    CardByApply
  },
  data() {
    return {
      changeTo: {
        number: 0,
        showCard: null,
        rightSpan: 0,
        itemSelected: {}
      },
    }
  },
  mounted() {
  },
  methods: {
  }
}
</script>
