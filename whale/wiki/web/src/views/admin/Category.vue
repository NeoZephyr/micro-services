<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-form layout="inline" :model="searchParam">
        <a-form-item>
          <a-input v-model:value="searchParam.name" placeholder="名称" />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleQuery()">
            查询
          </a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add">
            新增
          </a-button>
        </a-form-item>
      </a-form>
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="tree"
          :pagination=false
          :loading="loading"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="编辑分类"
      v-model:visible="editorVisible"
      :confirm-loading="editorLoading"
      @ok="handleEditorOk"
  >
    <a-form :model="category" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-select
            ref="select"
            v-model:value="category.parent"
        >
          <a-select-option value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in tree" :key="c.id" :value="c.id" :disabled="category.id === c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>

<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import { message } from 'ant-design-vue';
import {ObjectUtils} from "@/util/ObjectUtils";

export default defineComponent({
  name: 'Category',
  setup() {
    const tree = ref()
    tree.value = []
    const categories = ref()
    const searchParam = ref()
    searchParam.value = {}
    const loading = ref(false)

    const category = ref({})
    const editorVisible = ref(false)
    const editorLoading = ref(false)

    const columns = [
      {
        title: "名称",
        dataIndex: "name"
      },
      {
        title: "父分类",
        key: "parent",
        dataIndex: "parent"
      },
      {
        title: "顺序",
        dataIndex: "sort"
      },
      {
        title: "Action",
        key: "action",
        slots: { customRender: "action" }
      }
    ]

    const handleQuery = () => {
      loading.value = true

      axios.get("/categories").then((response) => {
        loading.value = false
        const result: any = response.data

        if (result.success) {
          categories.value = result.data
          tree.value = ObjectUtils.arrayToTree(categories.value, 0)
        } else {
          message.error(result.msg)
        }
      })
    }

    const handleEditorOk = () => {
      editorLoading.value = true
      const categoryInstance: any = category.value

      if (categoryInstance.id) {
        axios.put("/categories/" + categoryInstance.id, category.value).then((response) => {
          const data: any = response.data
          editorLoading.value = false

          if (data.success) {
            editorVisible.value = false

            handleQuery()
          } else {
            message.error(data.msg)
          }
        })
      } else {
        axios.post("/categories", category.value).then((response) => {
          const data: any = response.data
          editorLoading.value = false

          if (data.success) {
            editorVisible.value = false

            handleQuery()
          } else {
            message.error(data.msg)
          }
        })
      }
    }

    const handleDelete = (id: number) => {
      axios.delete("/categories/" + id).then((response) => {
        const data: any = response.data

        if (data.success) {
          handleQuery()
        } else {
          message.error(data.msg);
        }
      })
    }

    const edit = (record: any) => {
      editorVisible.value = true
      category.value = ObjectUtils.copy(record)
    }

    const add = () => {
      editorVisible.value = true
      category.value = {}
    }

    onMounted(() => {
      handleQuery()
    })

    return {
      tree,
      columns,
      loading,
      editorVisible,
      editorLoading,
      category,
      searchParam,
      edit,
      add,
      handleQuery,
      handleDelete,
      handleEditorOk
    }
  }
});
</script>