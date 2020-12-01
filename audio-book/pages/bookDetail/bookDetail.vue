<template>
	<view class="geryTheme">
		<page-title Theme="geryTheme" class="cal">图书详情</page-title>
		
		
		
		<view class="flex align-center py-2 cal" style="height: 250rpx;">
			<image :src="currentBook.imgurl" mode="aspectFill" lazy-load class="flex-1 mx-2 rounded" style="width: 250rpx;height: 210rpx;"></image>
			<view class="flex-2 mx-2">
				<view style="font-size: 45rpx;"> {{currentBook.name}} </view>
				<view class="font mt-1"> 作者: {{currentBook.author}} </view>
				<view class="flex align-center mt-2">
					<button class="flex-1 mx-1"> 分享 </button>
					<button class="flex-1 mx-1" @tap="collectThisBook"> {{collectStatus ? '已收藏' : '收藏'}} </button>
				</view>
			</view>
		</view>



		<view class="shadow bg-white">
			<tab-top :tabArr="['详情', '目录']" :tabIndex="tabIndex" @changeTabIndex="changeTabIndex" class="cal"></tab-top>
			<!-- 具体内容 -->
			<swiper :style="{height: `${calHeight}rpx`}" :current="tabIndex" @change="swiperChangeIndex">
				<!-- 详情 -->
				<swiper-item>
					<scroll-view scroll-y :style="{height: `${calHeight}rpx`}">
						<view>
							<view class="py-2 flex justify-center text-light-black">——简介——</view>
							<view class="px-2 font-lg" style="line-height: 80rpx;"> {{currentBook.synopsis}} </view>
						</view>
					</scroll-view>
				</swiper-item>
				
				<!-- 目录 -->
				<swiper-item>
					<scroll-view scroll-y :style="{height: `${calHeight}rpx`}">
						<block v-for="item in chapterCatalog" :key="item.id">
							<view hover-class="bg-light" class="p-2 text-ellipsis border-bottom" @tap="toReadingPage(item.id)"> {{item.title}} </view>
						</block>
					</scroll-view>
				</swiper-item>
			</swiper>
		</view>
	</view>
</template>

<script>
	let currentBookID,
		collecedBooks = getApp().globalData.collectedBooks;
	import $U from '@/common/unit';
	import tabTop from '@/components/tabTop.vue';
	import pageTitle from '@/components/pageTitle.vue';
	export default {
		data() {
			return {
				calHeight: 0,
				tabIndex: 0,
				collectStatus: false, // 收藏状态
				// 当前书籍信息
				currentBook:{
					name:'',
					author: '',
					synopsis: '',
					imgurl: ''
				},
				chapterCatalog: [] // 目录
			}
		},
		components: {
			tabTop,
			pageTitle
		},
		methods: {
			changeTabIndex(tabIndex) {
				this.tabIndex = tabIndex
			},
			swiperChangeIndex(e){
				this.tabIndex = e.detail.current
			},
			// 跳转到阅读页
			toReadingPage(id) {
				uni.navigateTo({url: `/pages/reading/reading?chapterID=${id}`});
			},
			
			
			// 收藏与取消收藏操作
			collectThisBook(){
				this.collectStatus = !this.collectStatus; // 改变收藏状态
				// 提示信息
				uni.showToast({
					icon:'none',
					title: this.collectStatus ? '已收藏' : '取消收藏成功'
				})
				
				// 收藏或取消收藏
				let index = collecedBooks.findIndex(item => item.id === currentBookID);
				
				this.collectStatus ? collecedBooks.push(this.currentBook) : collecedBooks.splice(index, 1)
			}
		},
		async onLoad(e) {
			currentBookID = Number(e.bookID); // 拿id
			// 请求目录
			let {chapterCatalog} = await this.$http.get('/testSynopsis');
			this.chapterCatalog = chapterCatalog;
			
			// 判断书籍是否有被收藏
			let currentBook = collecedBooks.find(item => item.id === currentBookID)
			if(currentBook){
				this.collectStatus = true;
				return this.currentBook = currentBook
			}
			
			// 未收藏时请求书籍
			let {list} = await this.$http.get('/book');
			this.currentBook = list.find(item => item.id === currentBookID);
			
		},
		mounted() {
			$U.calSurplusHeight({
				pageID: this,
				pos: 'cal',
				success: val => this.calHeight = val
			})
		}
	}
</script>

<style>
	.geryTheme {
		background-color: #a8b0c3;
	}
</style>
