<template>
	<view class="container">
		<view class="main">
			<view v-if="!addressList.length" style="color: #878889;">
				<view>暂无地址信息</view>
				<view>请点击底部按钮添加地址信息</view>
			</view>
			<template v-else>
				<uni-swipe-action>
					<uni-swipe-action-item
						class="address-wrapper"
						:options="swipeOption"
						@click="handleSwipeClick(address._id)"
						v-for="(address, index) in addressList"
						:key="index"
					>
						<view class="address" @click="tapAddress(address)">
							<view class="left flex-fill overflow-hidden mr-20">
								<view class="font-size-lg font-weight-bold text-truncate" style="margin-bottom: 10rpx;">
									{{ address.street }}
								</view>
								<view class="font-size-sm text-color-assist">
									{{ address.accept_name }} {{ !address.gender ? '先生' : '女士' }}
									{{ address.mobile }}
								</view>
							</view>
							<image src="/static/images/edit.png" class="edit-icon" @click="edit(address)"></image>
						</view>
					</uni-swipe-action-item>
				</uni-swipe-action>
			</template>
			<view class="address-button"><button type="primary" size="default" @click="add">新增地址</button></view>
		</view>
	</view>
</template>

<script>
import uniSwipeAction from '@/components/uni-swipe-action/uni-swipe-action.vue';
import uniSwipeActionItem from '@/components/uni-swipe-action-item/uni-swipe-action-item.vue';
import { mapState, mapMutations } from 'vuex';
export default {
	components: {
		uniSwipeAction,
		uniSwipeActionItem
	},
	data() {
		return {
			addressList: [],
			swipeOption: [
				{
					text: '移除',
					style: {
						backgroundColor: '#D12E32'
					}
				}
			]
		};
	},
	computed: {
		...mapState(['choseAddress'])
	},
	onLoad() {
		this.getAddress();
	},
	onShow() {
		this.getAddress();
	},
	methods: {
		...mapMutations(['SET_ADDRESS', 'SET_ORDERTYPE']),
		//获取地址列表
		getAddress() {
			uni.showLoading({
				title: '数据加载中...'
			});
			return uniCloud
				.callFunction({
					name: 'validateToken',
					data: {
						token: uni.getStorageSync('token')
					}
				})
				.then(res => {
					if (res.result.status === 0) {
						uni.hideLoading();
						return uniCloud.callFunction({
							name: 'address',
							data: {
								openId: res.result.openId,
								action: 'getList'
							}
						});
					} else {
						uni.hideLoading();
						uni.showModal({
							content: res.result.msg,
							showCancel: false
						});
					}
				})
				.then(resData => {
					this.addressList = resData.result.data;
				});
		},
		//删除地址
		handleSwipeClick(id) {
			uni.showModal({
				title: '提示',
				content: '确定要删除?',
				success: res => {
					if (res.confirm) {
						return uniCloud
							.callFunction({
								name: 'address',
								data: {
									id: id,
									action: 'deleteAddress'
								}
							})
							.then(res => {
								console.log(res);
								if (res.result.status === 0) {
									uni.showToast({ title: '删除成功!', icon: 'success' });
									this.getAddress();
								}
								uni.showModal({
									content: res.result.msg,
									showCancel: false
								});
							});
					}
				}
			});
		},
		//添加地址
		add() {
			uni.navigateTo({
				url: '/pages/address/add'
			});
		},
		//编辑地址
		edit(address) {
			this.SET_ADDRESS(address);
			uni.navigateTo({
				url: '/pages/address/edit'
			});
		},
		//选取地址并跳转到点餐界面
		tapAddress(address) {
			this.SET_ADDRESS(address);
			this.SET_ORDERTYPE('takeout');
			uni.switchTab({
				url: '../menu/menu'
			});
		}
	}
};
</script>

<style lang="scss" scoped>
.main {
	width: 100%;
	padding: 30rpx;
	display: flex;
	flex-direction: column;
	.address-wrapper {
		margin-bottom: 30rpx;
	}
	.address {
		width: 100%;
		padding: 40rpx 30rpx;
		background-color: #ffffff;
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 10rpx;
		.right {
			flex: 1;
			overflow: hidden;
			display: flex;
			flex-direction: column;
		}
		.edit-icon {
			width: 50rpx;
			height: 50rpx;
			flex-shrink: 0;
		}
	}
	.address-button {
		height: 100rpx;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		padding: 10rpx 0;
		display: flex;
		align-items: center;
		justify-content: center;
		button {
			height: 80rpx;
			width: 80%;
			border-radius: 50rem !important;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
}
</style>