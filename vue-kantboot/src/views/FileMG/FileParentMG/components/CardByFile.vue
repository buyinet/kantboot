<template>
    <el-card>
        <template #header>
            <div class="card-header">
                <span>文件 - {{ changeTo.itemSelected.title }}</span>
            </div>
        </template>
        <el-table height="calc(100vh - 350px)" row-key="id" default-expand-all :cell-style="{ 'textAlign': 'center' }"
            :header-cell-style="{ 'textAlign': 'center' }" :data="bodyData.content" style="width: 100%">

            <el-table-column label="序号">
                <template #default="scope">
                    {{ scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column prop="visitUrlById" label="预览或下载" width="300">
                <template #default="scope">
                    <div v-show="false">scope.row.path.endsWith('jpg')</div>
                    <el-image
                        v-if="
                        scope.row.path.endsWith('jpg') || scope.row.path.endsWith('gif') ||
                        scope.row.path.endsWith('jpeg') || scope.row.path.endsWith('svg') ||
                        scope.row.path.endsWith('png') || scope.row.path.endsWith('bmp') || scope.row.path.endsWith('webp')"
                        :preview-teleported="true" :z-index="100" :src="scope.row.visitUrlById"
                        :preview-src-list="[scope.row.visitUrlById]" />

                    <video width="150" height="150"
                     v-if="
                        scope.row.path.endsWith('wmv') || scope.row.path.endsWith('asf') ||
                        scope.row.path.endsWith('asx') || scope.row.path.endsWith('rm') ||
                        scope.row.path.endsWith('rmvb') || scope.row.path.endsWith('mp4') || 
                        scope.row.path.endsWith('3gp') || scope.row.path.endsWith('mov') || 
                        scope.row.path.endsWith('m4v') || scope.row.path.endsWith('avi') || 
                        scope.row.path.endsWith('dat') || scope.row.path.endsWith('mkv') || 
                        scope.row.path.endsWith('flv') || scope.row.path.endsWith('vob')
                        "
                    controls>
                        <source :src="scope.row.visitUrlById" :type="'video/'+scope.row.path.substring(scope.row.path.lastIndexOf('.')+1)">
                    </video>
                </template>
            </el-table-column>
            <el-table-column prop="name" label="name">
                <template #header>
                    <div>name</div>
                    <el-input v-model="paramData.data.and.vague[0].name" @input="findCommonPage()" size="small"
                        placeholder="输入进行搜索" />
                </template>
            </el-table-column>
            <el-table-column prop="visitUrlById" label="访问路径 - 1" width="300" />
            <el-table-column prop="visitUrlByName" label="访问路径 - 2" width="300">
                <template #default="scope">
                    <div v-show="false">{{ scope.row.name == null }}</div>
                    <div v-if="scope.row.name != null && scope.row.name != ''">
                        <div>{{ scope.row.visitUrlByName }}</div>
                    </div>
                </template>
            </el-table-column>


            <el-table-column>
                <template #header>
                    <div>简介</div>
                    <el-input v-model="paramData.data.and.vague[0].content" @input="findCommonPage()" size="small"
                        placeholder="输入进行搜索" />
                </template>
            </el-table-column>

            <el-table-column prop="storageType" label="存储" width="300">
                <template #default="scope">
                    <div v-if="scope.row.fileParent.storageType == 'oss'">
                        <div>oss（oss存储）</div>
                        <div> {{ scope.row.fileParent.fileOss.title }}[ {{ scope.row.fileParent.fileOss.endpoint }} ]
                        </div>
                    </div>

                    <div v-if="scope.row.fileParent.storageType == 'path'">
                        <div>path（本地路径存储）</div>
                        <div> {{ scope.row.fileParent.filePath.title }}[ {{ scope.row.fileParent.filePath.path }} ]
                        </div>
                    </div>
                </template>
            </el-table-column>

            <el-table-column prop="gmtCreate" label="创建时间" width="170" />

            <el-table-column fixed="right" label="操作" width="140">
                <template #default="scope">

                    <el-popconfirm
                        v-if="!(changeTo.showCard == 'CardByEdit' && changeTo.itemSelected.id == scope.row.id)"
                        title="确定删除此项吗?" icon-color="red" confirmButtonType="danger" confirmButtonText="确定"
                        cancelButtonText="取消" @confirm="removeById(scope.row.id)">
                        <template #reference>
                            <el-button link type="danger" size="small">删除</el-button>
                        </template>
                    </el-popconfirm>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination style="margin-left: 50%;" layout="prev, pager, next" v-model:current-page="paramData.pageNumber"
            v-model:total="bodyData.totalElements" v-model:page-size="paramData.pageSize"
            @current-change="findCommonPage" />

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
                        "eq": [{ "fileParentId": 22 }],
                        "like": [],
                        "gt": [],
                        "lt": [],
                        "ge": [],
                        "le": [],
                        "vague": [{
                            name: null,
                            content: null
                        }]
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
        toShowCardByEdit(item) {
            this.changeTo.rightSpan = 8;
            this.changeTo.showCard = "CardByEdit";
            this.changeTo.itemSelected = item;
        },
        removeById(id) {
            this.$request.post({
                url: this.$api.file.remove,
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
                url: this.$api.file.findCommonPage,
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
                this.paramData.data.and.eq[0].fileParentId = this.changeTo.itemSelected.id;
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