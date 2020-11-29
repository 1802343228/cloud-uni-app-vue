<template>
	<view>
		<h3>订单总价：{{total}}</h3>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				total:''
			};
		},
		methods: {
			
		},
		onLoad(options) {
			if(options.total) {
				this.total = options.total;
				console.log('订单总价：'+this.total);
			}
		}
	}
</script>

<style>

</style>
