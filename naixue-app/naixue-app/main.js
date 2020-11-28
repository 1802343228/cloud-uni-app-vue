import Vue from 'vue'
import App from './App'
//引入vuex
import store from './store'

Vue.config.productionTip = false

//设置vuex为全局组件
Vue.prototype.$store = store

App.mpType = 'app'

const app = new Vue({
	...App,
	store

})
app.$mount()