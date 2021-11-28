<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <a-form layout="inline" :model="searchParam">
        <a-form-item>
          <a-input v-model:value="searchParam.name" placeholder="用户名" />
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
          :data-source="users"
          :pagination="pagination"
          :loading="loading"
          @change="handleTableChange"
      >
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary" @click="resetPassword(record)">
              重置密码
            </a-button>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                title="删除后不可恢复，确认删除?"
                ok-text="是"
                cancel-text="否"
                @confirm="handleDelete(record.name)"
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
      title="编辑用户"
      v-model:visible="editorVisible"
      :confirm-loading="editorLoading"
      @ok="handleEditorOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="用户名">
        <a-input v-model:value="user.name" :disabled="user.id" />
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.nickname" />
      </a-form-item>
      <a-form-item label="密码" v-show="!user.id">
        <a-input v-model:value="user.password"/>
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
  name: 'AdminUser',
  setup() {
    const users = ref()
    const pagination = ref({
      current: 1,
      pageSize: 10,
      total: 0
    })
    const searchParam = ref()
    searchParam.value = {}
    const loading = ref(false)

    const user = ref()
    const editorVisible = ref(false)
    const editorLoading = ref(false)

    const columns = [
      {
        title: "用户名",
        dataIndex: "name"
      },
      {
        title: "昵称",
        dataIndex: "nickname"
      },
      {
        title: "密码",
        dataIndex: "password"
      },
      {
        title: "Action",
        key: "action",
        slots: { customRender: "action" }
      }
    ]

    const handleQuery = (params: any) => {
      loading.value = true

      axios.get("/users", {
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
          users.value = data.rows
          pagination.value.current = params.page
          pagination.value.total = data.total
        } else {
          message.error(result.msg)
        }
      })
    }

    const handleTableChange = (pagination: any) => {
      handleQuery({
        page: pagination.current,
        size: pagination.pageSize
      })
    }

    const handleEditorOk = () => {
      editorLoading.value = true
      const userInstance: any = user.value

      if (userInstance.id) {
        axios.put("/users/" + userInstance.name, user.value).then((response) => {
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
        axios.post("/users", user.value).then((response) => {
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

    const handleDelete = (name: number) => {
      axios.delete("/users/" + name).then((response) => {
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
      user.value = ObjectUtils.copy(record)
    }

    const add = () => {
      editorVisible.value = true
      user.value = {}
    }

    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize
      })
    })

    return {
      users,
      pagination,
      columns,
      loading,
      editorVisible,
      editorLoading,
      user,
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