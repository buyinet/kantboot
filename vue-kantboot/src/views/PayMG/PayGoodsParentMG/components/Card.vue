<template>
	<el-card>
		<template #header>
			<div class="card-header">
				<span>商品统一管理</span>
				<el-button v-if="changeTo.showCard!='CardByAdd'" @click="toShowCardByAdd()" class="button" text>新增
				</el-button>
				<el-button v-if="changeTo.showCard=='CardByAdd'" @click="cancel()" class="button" text>取消新增
				</el-button>
			</div>
		</template>
		<el-table
		:default-expand-all="false"
		height="calc(100vh - 350px)" row-key="id" default-expand-all :cell-style="{'textAlign':'center'}"
			:header-cell-style="{'textAlign':'center'}" :data="bodyData.content" style="width: 100%">
			<el-table-column type="expand" label="传参" width="100">
				<template #default="props">
					<div>
						
						<v-md-editor v-model="props.row.paramMdStr" mode="preview"></v-md-editor>
						<v-md-editor v-model="props.row.collectionParamMdStr" mode="preview"></v-md-editor>
					</div>
				</template>
			</el-table-column>


			<el-table-column label="序号">
				<template #default="scope">
					{{ scope.$index + 1 }}
				</template>
			</el-table-column>

			<el-table-column prop="title" label="标题" />
			<el-table-column prop="name" label="名称" />
			<el-table-column prop="payBeforeUrl" label="支付前回调地址" />
			<el-table-column prop="payAfterUrl" label="支付后回调地址" />
			<el-table-column prop="content" label="描述" />
			<el-table-column prop="gmtCreate" label="创建时间" />

			<el-table-column fixed="right" label="操作" width="240">
				<template #default="scope">

					<el-button link type="primary" size="small"
						v-if="(changeTo.showCard=='CardByEdit'&&changeTo.itemSelected.id!=scope.row.id)||changeTo.showCard!='CardByEdit'"
						@click="toShowCardByEdit(scope.row)">
						修改
					</el-button>
					<el-button link type="primary" size="small"
						v-if="changeTo.showCard=='CardByEdit'&&changeTo.itemSelected.id==scope.row.id" @click="cancel">
						取消修改
					</el-button>
					<el-popconfirm v-if="!(changeTo.showCard=='CardByEdit'&&changeTo.itemSelected.id==scope.row.id)"
						title="确定删除这个菜单吗?" icon-color="red" confirmButtonType="danger" confirmButtonText="确定"
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
			toShowCardByEdit(item) {
				this.changeTo.rightSpan = 8;
				this.changeTo.showCard = "CardByEdit";
				this.changeTo.itemSelected = item;
			},
			removeById(id) {
				this.$request.post({
					url: this.$api.authGoodsParent.remove,
					data: {
						"id": id
					},
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
					url: this.$api.authGoodsParent.findCommonPage,
					data: this.paramData,
					stateSuccess: (res) => {
						this.bodyData = res.data;
						for (var i = 0; i < this.bodyData.content.length; i++) {
							var noStart = "```json";
							var noEnd = "``` ";
							var content=this.bodyData.content[i];
							var param=this.bodyData.content[i].param;
							this.bodyData.content[i]["paramMdStr"] =
								`
> 以下为请求参数示例-支付传参:
\`\`\`json
/**
 * 支付方式:
 * 	微信支付：wechat；
 * 	支付宝支付：alipay；
 * 在微信小程序或支付宝小程序中可忽略填写
 * /
{
	goodsParentName: "${content.name}", //此类商品统一识别名称，必填
	type: "", //支付方式
	payParams:[{
		goodsId:1, //商品id,必填
		number:1, //数量，选填
		coupons:[], //优惠券,选填
		otherParam:[{ //其它参数
	
		}]
	}]
}
\`\`\`
							`;

this.bodyData.content[i]["collectionParamMdStr"] =
								`
> 以下为请求参数示例-收藏传参:
\`\`\`json
{
	goodsParentName: "${content.name}", //此类商品统一识别名称，必填
	collectionParams:[{
		goodsId:1, //商品id,必填
		number:1, //数量，选填
		otherParam:[{ //其它参数
	
		}]
	}]
}
\`\`\`
							`;

			// ${param.map((item) => {
			// 	var str = item.isNotNull?"必填":"选填";
			// 	return item.name+":"+item.demo+", //"+item.comment+","+str
			// }).join("\n")}	
}
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
