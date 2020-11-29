<template>
	<view>
		<view v-if="isLogin" class="font-size-lg">
			<text>{{userInfo.nickName}}</text>
		</view>
		<view v-else class="font-size-lg font-weight-bold" @click="login">请点击授权登录</view>
	</view>
</template>

<script>
	import { mapState } from 'vuex';
	export default {
		data() {
			return {
				
			}
		},
		computed:{
			...mapState (['userInfo','isLogin'])
		},
		methods: {
			//登录
			login() {
				uni.navigateTo({
					url:'/pages/login/login'
				});
			}
		}
	}
</script>

<style>

</style>
