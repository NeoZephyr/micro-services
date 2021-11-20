<template>
  <a-layout>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <h3 v-if="tree.length === 0">对不起，还没有找到相关文档！</h3>
      <a-row :gutter="24">
        <a-col :span="6">
          <a-tree
              v-if="tree.length > 0"
              :tree-data="tree"
              @select="onSelect"
              :replaceFields="{title: 'name', key: 'id', value: 'id'}"
              :defaultExpandAll="true"
              :defaultSelectedKeys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div class="wangeditor" :innerHTML="html"></div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>

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

    const html = ref()
    const defaultSelectedKeys = ref()
    defaultSelectedKeys.value = []
    const doc = ref()
    doc.value = {}

    const handleQuery = () => {
      // tree.value = []

      axios.get("/documents?bookId=" + (route.query.bookId)).then((response) => {
        const result: any = response.data

        if (result.success) {
          documents.value = result.data
          tree.value = ObjectUtils.arrayToTree(documents.value, 0)

          if (ObjectUtils.isNotEmpty(tree.value)) {
            defaultSelectedKeys.value = [tree.value[0].id]
            handleQueryContent(tree.value[0].id)
          }
        } else {
          message.error(result.msg)
        }
      })
    }

    const handleQueryContent = (documentId: number) => {
      axios.get("/documents/" + documentId + "/content").then((response) => {
        const result: any = response.data

        if (result.success) {
          html.value = result.data
        } else {
          message.error(result.msg)
        }
      })
    }

    const onSelect = (selectedKeys: any, info: any) => {
      console.log(selectedKeys, info)
      if (ObjectUtils.isNotEmpty(selectedKeys)) {
        handleQueryContent(selectedKeys[0])
      }
    }

    onMounted(() => {
      handleQuery()
    })

    return {
      tree,
      html,
      handleQuery,
      onSelect,
      defaultSelectedKeys
    }
  }
});
</script>

<style>
/* table 样式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}
.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}
.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

.wangeditor blockquote p {
  font-family: "YouYuan";
  margin: 20px 10px !important;
  font-size: 14px !important;
  font-weight: 60;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}
.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
ul, ol {
  margin: 10px 0 10px 20px;
}
</style>