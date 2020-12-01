import Vue from 'vue'
import store from 'store'
import App from './App'
import myIcon from '@/components/myIcon.vue';
import request from '@/common/request';

Vue.config.productionTip = false;

App.mpType = 'app';
Vue.prototype.$statusBarHeight = uni.getSystemInfoSync().statusBarHeight;
Vue.prototype.$http = request;


Vue.component('myIcon',myIcon);

const app = new Vue({
	store,
	...App
}).$mount()
