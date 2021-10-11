<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-table
          :columns="columns"
          :row-key="record => record.id"
          :data-source="books"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar">
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-button type="danger">
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>

  <a-modal
      title="编辑书本表单"
      v-model:visible="editorVisible"
      :confirm-loading="editorLoading"
      @ok="handleEditorOk"
  >
    <a-form :model="book" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="book.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="book.name" />
      </a-form-item>
      <a-form-item label="分类">
        <a-input v-model:value="book.categoryId" />
      </a-form-item>
      <a-form-item label="子分类">
        <a-input v-model:value="book.subCategoryId" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="book.description" type="textarea" />
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

export default defineComponent({
  name: 'Book',
  setup() {
    const books = ref()
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0
    })
    const loading = ref(false)

    const book = ref({})
    const editorVisible = ref(false)
    const editorLoading = ref(false)

    const columns = [
      {
        title: "封面",
        dataIndex: "cover",
        slots: { customRender: "cover" }
      },
      {
        title: "名称",
        dataIndex: "name"
      },
      {
        title: "分类",
        key: "categoryId",
        dataIndex: "categoryId"
      },
      {
        title: "子分类",
        key: "subCategoryId",
        dataIndex: "subCategoryId"
      },
      {
        title: "文档数",
        dataIndex: "docCount"
      },
      {
        title: "阅读数",
        dataIndex: "viewCount"
      },
      {
        title: "点赞数",
        dataIndex: "voteCount"
      },
      {
        title: "Action",
        key: "action",
        slots: { customRender: "action" }
      }
    ]

    const handleQuery = (params: any) => {
      loading.value = true
      axios.get("/books", {
        params: {
          page: params.page,
          size: params.size
        }
      }).then((response) => {
        loading.value = false
        const result: any = response.data
        const data: any = result.data
        books.value = data.rows
        pagination.value.current = params.page
        pagination.value.total = data.total
      })
    }

    const handleTableChange = (pagination: any) => {
      console.log("pagination:" + pagination)
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    }

    const handleEditorOk = () => {
      editorLoading.value = true
      setTimeout(() => {
        editorLoading.value = false
        editorVisible.value = false
      }, 1000)
    }

    const edit = (record: any) => {
      editorVisible.value = true
      book.value = record
    }

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      })
    })

    return {
      books,
      pagination,
      columns,
      loading,
      editorVisible,
      editorLoading,
      book,
      handleTableChange,
      edit,
      handleEditorOk
    }
  }
});
</script>