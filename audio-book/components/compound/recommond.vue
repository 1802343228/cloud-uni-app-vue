<template>
	<view>
		<list-header>
			<template v-slot:title> 猜你喜欢 </template>
			<template v-slot:tips> 更多推荐 </template>
		</list-header>
		<view class="flex align-center">
			<block v-for="(item, index) in Rebooks" :key="index">
				<view class="flex flex-column align-center flex-1 position-relative w-100" style="height: 300rpx;" @tap="toBookDetail(index)">
					<image :src="item.src" mode="widthFix" class="w-100"></image>
					<text class="font my-1 text-light-black">{{item.name}}</text>
					<my-icon iconId="icon-erji" iconSize="35" iconColor="text-light-white" class="position-absolute" style="left: 45rpx; top: 20rpx;"></my-icon>
				</view>
			</block>
		</view>
	</view>
</template>

<script>
	import listHeader from '@/components/listHeader.vue';
	export default {
		props: {
			Rebooks: {
				type: Array,
				default: () => []
			}
		},
		components: {
			listHeader
		},
		methods: {
			toBookDetail(bookIndex){
				if (bookIndex === 0) {
					uni.navigateTo({
						url: '/pages/bookDetail/bookDetail?bookID=2001',
					});
				}
			}
		},
	}
</script>

<style>

</style>
