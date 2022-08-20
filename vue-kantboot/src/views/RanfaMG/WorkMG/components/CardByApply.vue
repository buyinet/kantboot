<template>
    <el-card>
        <template #header>
            <div class="card-header">
                <span>审核</span>
            </div>
        </template>

        <el-scrollbar style="height:calc( 100vh - 305px )">

            <el-form label-position="top" :model="paramData" label-width="120px">

                <el-row>
                    <el-col :span="6">

                        <el-form-item label="染发前图片：">
                            <el-image :preview-src-list="[paramData.fileUrlByFrontCoverImage]" :z-index="100"
                                :preview-teleported="true" :initial-index="1" fit="cover"
                                style="width: 100%; height: 150px;border-radius: 10px;"
                                :src="paramData.fileUrlByFrontCoverImage" />
                        </el-form-item>

                    </el-col>
                    <el-col :span="1"></el-col>
                    <el-col :span="6">

                        <el-form-item label="染发后图片：">
                            <el-image :preview-src-list="[paramData.fileUrlByBackCoverImage]" :z-index="100"
                                :preview-teleported="true" :initial-index="1" fit="cover"
                                style="width: 100%; height: 150px;border-radius: 10px;"
                                :src="paramData.fileUrlByBackCoverImage" />
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-form-item label="视频:">

                    <div style="width:100%" v-for="(item,index) in paramData.fileUrlsOfVideo">
                        <div style="text-align: center;">第{{index+1}}集</div>
                    <KtVideo :src="item" width="400" height="150"></KtVideo>

                    </div>
                </el-form-item>
                <div style="height: 20px"></div>
                <el-form-item>
                    <div style="display: inline-block;margin-left: 50%;transform: translateX(-50%)">
                    <el-radio-group v-model="paramData.auditStatus" size="large">
                            <el-radio-button :label="1" >审核通过</el-radio-button>
                            <el-radio-button :label="2" >审核不通过</el-radio-button>
                    </el-radio-group>
                    </div>
                </el-form-item>
                <div style="height: 20px"></div>
                <el-form-item 
                v-if="paramData.auditStatus==2"
                label="审核不通过原因:">
                    <el-input v-model="paramData.reasonsForFailureInAudit" placeholder="输入审核不通过原因"></el-input>
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
import KtVideo from "@/components/System/kt/ktFile/KtVideo.vue";

export default {
    components: { RolesSelect, KtVideo },
    props: {
        "changeTo": {
            type: Object,
            default: ''
        }
    },
    data() {
        return {
            paramData: {
                "name": "",
                "auditStatus": 2,
                "reasonsForFailureInAudit":null
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
            // if (this.paramData.folder && (this.paramData.pagePath == null || this.paramData.pagePath == "")) {
            //     this.paramData.pagePath = "DefaultFolder.vue";
            // }
            this.$request.post({
                url: this.$api.ranfaWork.toExamine,
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