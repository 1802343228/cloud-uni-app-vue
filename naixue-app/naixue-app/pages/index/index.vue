<template>
	<view class="content">
		<view class="banner">
			<image src="https://img-shop.qmimg.cn/s23107/2020/04/26/3eb7808bf105262604.jpg" class="bg"></image>
			<view class="intro">
				<view class="greet">您好，{{isLogin ? userInfo.nickName : '游客'}}</view>
				<view class="note">一杯奶茶，一口软欧包，在奈雪遇见两种美好</view>
			</view>
		</view>
		<view class="content"></view>
	</view>
</template>

<script>
	import {mapState} from 'vuex';
	export default {
		data() {
			return {
				title: 'Hello'
			}
		},
		computed:{
			...mapState(['isLogin','orderType','userInfo','choseStore'])
		},
		onLoad() {

		},
		methods: {

		}
	}
</script>

<style lang="scss">
	.banner {
		position: relative;
		height: 600rpx;
		width: 100%;
		//背景图片
		.bg {
			height: 600rpx;
			width: 100%;
		}
		.intro {
			position: absolute;
			display: flex;
			flex-direction: column;
			color: #FFFFFF;
			top: calc(50rpx + var(--status-bar-height));
			left: 40rpx;
			.greet {
				font-size: $font-size-lg;
				margin-bottom: 10rpx;
			}
			.note {
				font-size: $font-size-sm;
			}
		}
	}
</style>
