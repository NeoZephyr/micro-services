<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
          mode="inline"
          :style="{ height: '100%', borderRight: 0 }"
          @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined />
          <span>欢迎</span>
        </a-menu-item>
        <a-sub-menu v-for="item in tree" :key="item.id">
          <template v-slot:title>
              <span>
                <user-outlined />
                {{item.name}}
              </span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined />
            <span>
              {{child.name}}
            </span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
        :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="showWelcome">
        欢迎光临
      </div>
      <a-list v-show="!showWelcome" item-layout="vertical" size="large" :grid="{ gutter: 20, column: 3 }" :data-source="books">
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ type, text } in actions" :key="type">
                <component v-bind:is="type" style="margin-right: 8px"/>
                {{ text }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <router-link :to="'/document?bookId=' + item.id">
                  {{ item.name }}
                </router-link>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover"/>
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from 'vue';
import axios from "axios";
import {ObjectUtils} from "@/util/ObjectUtils";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'Home',
  setup() {
    const tree = ref()
    let categories: any
    tree.value = []
    const books = ref()
    const showWelcome = ref(true)
    let categoryId = 0

    const handleQueryCategory = () => {
      axios.get("/categories").then((response) => {
        const result: any = response.data

        if (result.success) {
          categories = result.data
          tree.value = ObjectUtils.arrayToTree(categories, 0)
        } else {
          message.error(result.msg)
        }
      })
    }

    const handleQueryBook = () => {
      axios.get("/books", {
        params: {
          page: 1,
          size: 100,
          subCategoryId: categoryId
        }
      }).then((response) => {
        const result: any = response.data
        const data: any = result.data
        books.value = data.rows
      })
    }

    const handleClick = (e: any) => {
      if (e.key === "welcome") {
        showWelcome.value = true
      } else {
        showWelcome.value = false
        categoryId = e.key
        handleQueryBook()
      }
    }

    onMounted(() => {
      handleQueryCategory()
    })

    return {
      tree,
      books,
      showWelcome,
      handleClick,
      actions: [
        {type: 'StarOutlined', text: '156'},
        {type: 'LikeOutlined', text: '156'},
        {type: 'MessageOutlined', text: '2'},
      ]
    }
  }
});
</script>

<style scoped>
  .ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>