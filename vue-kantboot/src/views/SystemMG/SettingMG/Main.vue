<template>
  <el-row :gutter="20">
    <el-col :span="8">
    </el-col>

    <el-col :span="8">
      <card :change-to="changeTo"></card>
    </el-col>
    <el-col :span="8">
    </el-col>
  </el-row>
</template>

<script>
import Card from "./components/Card";
import CardByAdd from "./components/CardByAdd";
import CardByEdit from "./components/CardByEdit";

export default {
  components: {
    CardByEdit,
    CardByAdd,
    Card
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
