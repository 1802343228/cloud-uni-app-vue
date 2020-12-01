<template>
	<view>
		<page-title>榜单</page-title>
		<book-list :bookListArr="loadedBookList"></book-list>
		<uni-load-more :status="loadMoreStatus"></uni-load-more>
	</view>
</template>

<script>
	import bookList from '@/components/bookList.vue';
	import pageTitle from '@/components/pageTitle.vue';
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	export default {
		data() {
			return {
				loadedBookList: [],
				loadMoreStatus: 'loading'
			}
		},
		components: {
			bookList,
			pageTitle,
			uniLoadMore
		},
		methods: {
			// 初始化加载
			async initLoadMore() {
				let obj = await this.$http.get('/readList')
				setTimeout(() => {
					if (obj.books.length) {
						this.loadedBookList = [...obj.books];
						this.loadMoreStatus = 'more'
					}
				}, 2000)
			},
			// 触底加载
			async loadMore() {
				let obj = await this.$http.get('/readList')
				switch (obj.isLoadMore) {
					case '正在加载中':
						return
						break;
					case '没有更多了':
						this.loadMoreStatus = 'noMore'
						break;
					case '上拉加载显示更多':
						{
							this.loadMoreStatus = 'loading';
							setTimeout(() => {
								this.loadedBookList = [...this.loadedBookList, ...obj.books];
								this.loadMoreStatus = 'more'
							}, 1500)
						}
						break;
				}
			}
		},
		onLoad() {
			this.initLoadMore()
		},
		onReachBottom() {
			this.loadMore()
		}
	}
</script>

<style>

</style>
