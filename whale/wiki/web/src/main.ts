import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue'
import 'ant-design-vue/dist/antd.css'
import * as Icons from '@ant-design/icons-vue'
import axios from "axios";

axios.defaults.baseURL = process.env.VUE_APP_SERVER

axios.interceptors.request.use(function (config) {
    console.log("request param:", config);
    return config;
}, error => {
    console.log("request error: " + error)
    return Promise.reject(error)
})

axios.interceptors.response.use(function (response) {
    console.log("response:", response)
    return response;
}, error => {
    console.log("response error:", error)
    return Promise.reject(error)
})

const app = createApp(App);

app.use(store).use(router).use(Antd).mount('#app')

// 全局使用图标
const icons: any = Icons

for (const i in icons) {
    app.component(i, icons[i])
}

console.log("env:", process.env.NODE_ENV)
console.log("url:", process.env.VUE_APP_SERVER)