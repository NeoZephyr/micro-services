<template>
  <a-layout-header class="header">
    <div class="logo"/>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px' }"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/book">
        <router-link to="/admin/book">书本管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
      <a class="login-menu" v-show="user.id">
        <span>您好：{{user.nickname}}</span>
      </a>
      <a class="login-menu" v-show="!user.id" @click="showLoginEditor">
        <span>登录</span>
      </a>
    </a-menu>

    <a-modal
        title="登录"
        v-model:visible="loginEditorVisible"
        :confirm-loading="loginEditorLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.name" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password" />
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent, ref, computed} from 'vue';
import store from "@/store";
import axios from "axios";
import {message} from "ant-design-vue";

export default defineComponent({
  name: 'TheHeader',
  setup() {
    const user = computed(() => store.state.user);
    const loginUser = ref({
      name: "pain",
      password: "123456"
    })
    const loginEditorVisible = ref(false)
    const loginEditorLoading = ref(false)
    const showLoginEditor = () => {
      loginEditorVisible.value = true
    }
    const login = () => {
      loginEditorLoading.value = true
      axios.post('/users/login', loginUser.value).then((response) => {
        loginEditorLoading.value = false
        const result: any = response.data
        if (result.success) {
          loginEditorVisible.value = false
          message.success("登录成功！");
          store.commit("setUser", result.data);
        } else {
          message.error(result.msg);
        }
      });
    }

    return {
      loginEditorVisible,
      loginEditorLoading,
      loginUser,
      user,
      showLoginEditor,
      login
    }
  }
});
</script>

<style>
  /*.logo {*/
  /*  width: 120px;*/
  /*  height: 31px;*/
  /*  float: left;*/
  /*  color: white;*/
  /*  font-size: 18px;*/
  /*}*/
  .login-menu {
    float: right;
    color: white;
    padding-left: 10px;
  }
</style>