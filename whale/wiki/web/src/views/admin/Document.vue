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
      title="编辑文档"
      v-model:visible="editorVisible"
      :confirm-loading="editorLoading"
      @ok="handleEditorOk"
  >
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name" />
      </a-form-item>
      <a-form-item label="父文档">
        <a-tree-select
            v-model:value="doc.parent"
            style="width: 100%"
            :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
            :tree-data="selectTree"
            placeholder="请选择"
            tree-default-expand-all
            :replaceFields="{title: 'name', key: 'id', value: 'id'}"
        >
        </a-tree-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort" />
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
import {useRoute} from "vue-router";

export default defineComponent({
  name: 'Document',
  setup() {
    const route = useRoute()
    const tree = ref()
    tree.value = []
    const documents = ref()
    const searchParam = ref()
    searchParam.value = {}
    const loading = ref(false)

    const selectTree = ref()
    selectTree.value = []
    const doc = ref({})
    const editorVisible = ref(false)
    const editorLoading = ref(false)

    const columns = [
      {
        title: "名称",
        dataIndex: "name"
      },
      {
        title: "父文档",
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

    const findRoot = (roots: any, id: any) : any => {
      for (let i = 0; i < roots.length; i++) {
        if (roots[i].id === id) {
          return roots[i]
        }

        const children = roots[i].children

        if (ObjectUtils.isNotEmpty(children)) {
          const root = findRoot(children, id)

          if (root != null) {
            return root
          }
        }
      }

      return null
    }

    const disableTree = (root: any) => {
      root.disabled = true
      const children = root.children

      if (ObjectUtils.isNotEmpty(children)) {
        for (let i = 0; i < children.length; i++) {
          disableTree(children[i])
        }
      }
    }

    const setDisable = (roots: any, id: any) => {
      const root = findRoot(roots, id)

      if (root != null) {
        disableTree(root)
      }
    }

    const handleQuery = () => {
      loading.value = true
      // tree.value = []

      axios.get("/documents?bookId=" + (route.query.bookId)).then((response) => {
        loading.value = false
        const result: any = response.data

        if (result.success) {
          documents.value = result.data
          tree.value = ObjectUtils.arrayToTree(documents.value, 0)
        } else {
          message.error(result.msg)
        }
      })
    }

    const handleEditorOk = () => {
      editorLoading.value = true
      const docInstance: any = doc.value

      if (docInstance.id) {
        axios.put("/documents/" + docInstance.id, doc.value).then((response) => {
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
        axios.post("/documents", doc.value).then((response) => {
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
      axios.delete("/documents/" + id).then((response) => {
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
      doc.value = ObjectUtils.copy(record)
      selectTree.value = ObjectUtils.copy(tree.value)
      setDisable(selectTree.value, record.id)
      selectTree.value.unshift({id: 0, name: "无"})
    }

    const add = () => {
      editorVisible.value = true
      doc.value = {
        bookId: route.query.bookId
      }
      selectTree.value = ObjectUtils.copy(tree.value)
      selectTree.value.unshift({id: 0, name: "无"})
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
      doc,
      selectTree,
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