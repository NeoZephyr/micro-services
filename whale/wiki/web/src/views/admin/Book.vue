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
          <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
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
      title="编辑书本"
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
import { message } from 'ant-design-vue';
import {ObjectUtils} from "@/util/ObjectUtils";

export default defineComponent({
  name: 'Book',
  setup() {
    const books = ref()
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    })
    const searchParam = ref()
    searchParam.value = {}
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
          size: params.size,
          name: searchParam.value.name
        }
      }).then((response) => {
        loading.value = false
        const result: any = response.data
        const data: any = result.data

        if (result.success) {
          books.value = data.rows
          pagination.value.current = params.page
          pagination.value.total = data.total
        } else {
          message.error(result.msg)
        }
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
      const bookInstance: any = book.value

      if (bookInstance.id) {
        axios.put("/books/" + bookInstance.id, book.value).then((response) => {
          const data: any = response.data
          editorLoading.value = false

          if (data.success) {
            editorVisible.value = false

            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            })
          } else {
            message.error(data.msg)
          }
        })
      } else {
        axios.post("/books", book.value).then((response) => {
          const data: any = response.data
          editorLoading.value = false

          if (data.success) {
            editorVisible.value = false

            handleQuery({
              page: pagination.value.current,
              size: pagination.value.pageSize
            })
          } else {
            message.error(data.msg)
          }
        })
      }
    }

    const handleDelete = (id: number) => {
      axios.delete("/books/" + id).then((response) => {
        const data: any = response.data

        if (data.success) {
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize
          })
        } else {
          message.error(data.msg);
        }
      })
    }

    const edit = (record: any) => {
      editorVisible.value = true
      book.value = ObjectUtils.copy(record)
    }

    const add = () => {
      editorVisible.value = true
      book.value = {}
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
      searchParam,
      handleTableChange,
      edit,
      add,
      handleQuery,
      handleDelete,
      handleEditorOk
    }
  }
});
</script>