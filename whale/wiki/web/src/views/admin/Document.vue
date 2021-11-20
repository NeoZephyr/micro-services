<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <a-row :gutter="24">
        <a-col :span="8">
          <p>
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
          </p>
          <a-table
              v-if="tree.length > 0"
              :columns="columns"
              :row-key="record => record.id"
              :data-source="tree"
              :pagination=false
              :loading="loading"
              size="small"
              :defaultExpandAllRows="true"
          >
            <template #name="{ text, record }">
              {{record.sort}} {{text}}
            </template>
            <template v-slot:action="{ text, record }">
              <a-space size="small">
                <a-button type="primary" @click="edit(record)" size="small">
                  编辑
                </a-button>
                <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
                >
                  <a-button type="danger" size="small">
                    删除
                  </a-button>
                </a-popconfirm>
              </a-space>
            </template>
          </a-table>
        </a-col>
        <a-col :span="16">
          <p>
            <a-form layout="inline">
              <a-form-item>
                <a-button type="primary" @click="handleSave()">
                  保存
                </a-button>
              </a-form-item>
            </a-form>
          </p>
          <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }" layout="vertical">
            <a-form-item>
              <a-input v-model:value="doc.name" placeholder="名称" />
            </a-form-item>
            <a-form-item>
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
            <a-form-item>
              <a-input v-model:value="doc.sort" placeholder="顺序" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" @click="handlePreview()">
                <EyeOutlined /> 内容预览
              </a-button>
            </a-form-item>
            <a-form-item>
              <div id="content"></div>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>

      <a-drawer width="900" placement="right" :closable="false" :visible="drawerVisible" @close="onDrawerClose">
        <div class="wangeditor" :innerHTML="previewHtml"></div>
      </a-drawer>

    </a-layout-content>
  </a-layout>
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
import {message} from 'ant-design-vue';
import {ObjectUtils} from "@/util/ObjectUtils";
import {useRoute} from "vue-router";
import E from 'wangeditor';

export default defineComponent({
  name: 'AdminDocument',
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
    const doc = ref()
    doc.value = {
      bookId: route.query.bookId
    }
    const editorVisible = ref(false)
    const editorLoading = ref(false)

    const drawerVisible = ref(false)
    const previewHtml = ref()

    let editor: E
    // const editor = new E('#content');

    const columns = [
      {
        title: "名称",
        dataIndex: "name",
        slots: { customRender: "name" }
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
          selectTree.value = ObjectUtils.copy(tree.value)
          selectTree.value.unshift({id: 0, name: "无"})
        } else {
          message.error(result.msg)
        }
      })
    }

    const handleQueryContent = () => {
      axios.get("/documents/" + (doc.value.id) + "/content").then((response) => {
        const result: any = response.data

        if (result.success) {
          editor.txt.html(result.data)
        } else {
          message.error(result.msg)
        }
      })
    }

    const handleSave = () => {
      editorLoading.value = true
      const docInstance: any = doc.value
      docInstance.content = editor.txt.html()

      if (docInstance.id) {
        axios.put("/documents/" + docInstance.id, doc.value).then((response) => {
          const data: any = response.data
          editorLoading.value = false

          if (data.success) {
            editorVisible.value = false
            message.success("更新成功")

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
            message.success("新增成功")

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
      editor.txt.html("")
      editorVisible.value = true
      doc.value = ObjectUtils.copy(record)
      handleQueryContent()
      selectTree.value = ObjectUtils.copy(tree.value)
      setDisable(selectTree.value, record.id)
      selectTree.value.unshift({id: 0, name: "无"})
    }

    const add = () => {
      editor.txt.html("")
      editorVisible.value = true
      doc.value = {
        bookId: route.query.bookId
      }
      selectTree.value = ObjectUtils.copy(tree.value)
      selectTree.value.unshift({id: 0, name: "无"})
    }

    const handlePreview = () => {
      previewHtml.value = editor.txt.html()
      drawerVisible.value = true
    }

    const onDrawerClose = () => {
      drawerVisible.value = false
    }

    onMounted(() => {
      handleQuery()
      editor = new E("#content")
      editor.config.zIndex = 0
      editor.create()
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
      previewHtml,
      drawerVisible,
      edit,
      add,
      handleQuery,
      handleDelete,
      handleSave,
      handlePreview,
      onDrawerClose
    }
  }
});
</script>