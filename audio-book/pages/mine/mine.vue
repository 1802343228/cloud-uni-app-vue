<template>
	<view>
		<view :style="{height: `${statusBarHeight}px`}"></view>
		
		<view class="flex align-center justify-between px-2" style="height: 200rpx;">
			<!-- 用户 -->
			<view class="flex align-center">
				<image src="/static/user/me.jpg" mode="aspectFill" style="width: 130rpx;height: 130rpx;border-radius: 100%;"></image>
				<text class="pl-2">陶然然</text>
			</view>
			<!-- 客服 -->
			<view class="flex align-center rounded-circle p-1" style="background-color: #ecf0f3;">
				<my-icon iconId="icon-erji" iconSize="30" class="px-1"></my-icon>
				<text class="font">我的客服</text>
			</view>
		</view>
		
		
		
		<block v-for="(item, index) in ['签到', '设置', '意见反馈', '切换账号']" :key="index">
			<view>
				<view class="flex align-center justify-between px-2 text-light-black"  style="height: 90rpx;">
					<text>{{item}}</text>
					<my-icon iconId="icon-iconfonti"></my-icon>
				</view>
				<view style="height: 12rpx;background-color: #F8F8F8;"></view>
			</view>
		</block>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				statusBarHeight: this.$statusBarHeight
			}
		}
	}
</script>

<style>

</style>
