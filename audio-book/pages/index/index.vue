<template>
	<view class="content">
		<view :style="{height:`${statusBarHeight}px`}"></view>
		<search-box></search-box>
		<rotation-chart :imgArr="swiperImages"></rotation-chart>
		<function-sort :functionSortArr="functionSortArr"></function-sort>
		<recommond :Rebooks="Rebooks"></recommond>
		
		<block v-for="(item, index) in bookResources" :key="index">
			<list-header> <template v-slot:title> {{item.headerTitle}} </template> </list-header>
			<book-list :bookListArr="item.books"></book-list>
		</block>
		<myaudio></myaudio>
		<view style="height: 160rpx;"> </view>
	</view>
</template>

<script>
	import myaudio from '@/components/myaudio.vue';
	import bookList from '@/components/bookList.vue';
	import searchBox from '@/components/searchBox.vue';
	import listHeader from '@/components/listHeader.vue';
	import functionSort from '@/components/functionSort.vue';
	import rotationChart from '@/components/rotationChart.vue';
	import recommond from '@/components/compound/recommond.vue';
	export default {
		data() {
			return {
				statusBarHeight: this.$statusBarHeight,
				functionSortArr: [{
						iconId: 'icon-icon-test',
						iconColor: 'text-warning',
						name: '看榜单'
					},
					{
						iconId: 'icon-startRead',
						iconColor: 'text-hover-primary',
						name: '听小说'
					},
					{
						iconId: 'icon-icon09',
						iconColor: 'text-light-black',
						name: '听音乐'
					},
					{
						iconId: 'icon-zhishi',
						iconColor: 'text-success',
						name: '听知识'
					}
				],
				swiperImages: [],
				Rebooks: [],
				bookResources: [],
			}
		},
		components: {
			myaudio,
			bookList,
			searchBox,
			listHeader,
			functionSort,
			rotationChart,
			recommond
		},
		onLoad() {
			this.$http.get('/app_index').then(res => {
				this.swiperImages = res.swiperImages;
				this.Rebooks = res.Rebooks;
				this.bookResources = res.bookResources
			})
		}
	}
</script>

<style>
</style>
