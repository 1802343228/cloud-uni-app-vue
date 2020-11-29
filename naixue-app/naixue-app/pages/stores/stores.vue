<template>
	<view class="container">
	        <!-- 门店地图标记 -->
	        <view class="page-body">
	            <view class="page-section">
	                <map id="myMap" style="width: 100%; height: 400rpx;" :latitude="latitude" :longitude="longitude"
	                :markers="markers"  >
	                </map>
	            </view>
	        </view>
	        <view class="content">
	            <view class="store" v-for="(store,index) in storeData" :key="index" @tap="tapStore(store)">
	                <view class="store-left">
	                    <view class="store-title">
	                        <view class="store-name">{{store.name}}</view>
	                        <view class="store-status">营业中</view>
	                    </view>
	                    <view class="store-content">
	                        <view class="store-distance">距离您{{store.distance}}km</view>
	                        <text class="store-text">{{store.street}}</text>
	                        <text class="store-text">营业时间：10：00-22:30</text>
	                    </view>
	                </view>
	                <view class="store-right">
	                    <view class="look">
	                        去看看
	                    </view>
	                    <view class="icon">
	                        <view class="round">
	                            <image  src="../../static/images/store/store-tel.png" ></image>
	                        </view>
	                        <view class="round">
	                            <image  src="../../static/images/store/store-nav.png" ></image>
	                        </view>
	                    </view>
	                </view>
	            </view>
	        </view>
	    </view>
</template>

<script>
	import { mapMutations } from 'vuex';
	export default {
		data() {
			return {
				latitude:'31.963087',
				longitude:'118.836784',
				markers:[],
				storeData:[]
			}
		},
		onLoad() {
			this.getStoreData();
		},
		methods: {
			...mapMutations(['SET_ORDERTYPE','SET_STORE']),
			getStoreData() {
				return new Promise((resolve,reject) => {
					//获取用户定位
					uni.getLocation({
						type:'gcj02',
						success(resLocation) {
							if(resLocation.latitude && resLocation.longitude) {
								resolve(resLocation);
							}
						}
					});
				}).then(local => {
					let la1 = local.latitude;
					let lo1 = local.longitude;
					return uniCloud.callFunction({
						name:'stores'
					}).then(resStore => {
						let temp = [];
						let l =resStore.result.length;
						if(l >= 1) {
							for(let i = 0;i<l;i++) {
								let element = resStore.result[i];
								//调用封装的方法，计算门店和用户的距离
								let d = this.distance(element.latitude,element.longitude,la1,lo1);
								element.distance = d;
								temp.push(element);
							}
							//由近至远排序
							this.storeData = temp.sort(function(a,b){
								return a.distance - b.distance;
							});
							return this.storeData
						}
					});
				}).then(resMap => {
					this.latitude = resMap[0].latitude;
					this.longitude = resMap[0].longitude;
					//在地图上标记点
					let map = [];
					let len = resMap.length;
					if(len >= 1) {
						for(let i = 0;i<len;i++) {
							let maps = {};
							maps.id = i;
							maps.latitude = resMap[i].latitude;
							maps.longitude = resMap[i].longitude;
							maps.iconPath = '/static/images/logo.png';
							maps.width = 30;
							maps.height = 30;
							maps.callout = {content:resMap[i].name,display:"ALWAYS"};
							map.push(maps);
						}
						this.markers = map;
						console.log(this.markers);
					}
				});
			},
			//计算距离的方法（入参为两点的经纬度）
			distance(la1,lo1,la2,lo2) {
				var La1 = (la1 * Math.PI) /180.0;
				var La2 = (la2 * Math.PI) /180.0;
				var La3 = La1 - La2;
				var Lb3 = (lo1 * Math.PI) /180.0 - (lo2 * Math.PI) /180.0;
				var s = 2* Math.asin(Math.sqrt(
				Math.pow(Math.sin(La3/2),2)+Math.cos(La1)*Math.cos(La2)*
				Math.pow(Math.sin(Lb3/2),2)
				)
				);
				//地球半径
				s = s*6378.137;
				s = Math.round(s*10000)/10000;
				s = s.toFixed(1);
				return s ;
			},
			//选取门店地址并且跳转到点餐界面
			tapStore(store) {
				this.SET_ORDERTYPE('takein');
				this.SET_STORE(store);
				uni.switchTab({
					url:'../menu/menu'
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
.page-map {
	width: 100%;
	height: 400rpx;
}
.content {
	width: 100%;
	height: 100%;
}
.store {
	display: flex;
	flex-wrap: nowrap;
	margin: 10rpx 20rpx;
	padding: 20rpx;
	background-color: #ffffff;
	box-shadow: $box-shadow;
}
.store-left {
	width: 70%;
	display: flex;
	flex-direction: column;
	.store-title {
		display: flex;
		flex-wrap: nowrap;
		.store-name {
			color: $text-color-black;
			font-size: $font-size-lg;
			font-weight: bold;
			margin-right: 50rpx;
			white-space: nowrap;
			text-overflow: ellipsis;
			overflow: hidden;
		}
		.store-status {
			color: $color-primary;
			font-size: $font-size-sm;
			padding: 0 5rpx;
			border: 1rpx solid $color-primary;
			white-space: nowrap;
		}
	}

	.store-content {
		margin-top: 60rpx;
		font-size: $font-size-sm;
		display: flex;
		flex-direction: column;
		.store-text {
			color: $text-color-assist;
			white-space: nowrap;
			text-overflow: ellipsis;
			overflow: hidden;
		}
	}
}
.store-right {
	width: 30%;
	display: flex;
	flex-direction: column;
	align-items: flex-end;
	.look {
		font-size: $font-size-sm;
		color: $color-primary;
	}
	.icon {
		display: flex;
		flex-wrap: nowrap;
		align-items: center;
		margin-top: 60rpx;
		justify-content: flex-end;
		.round {
			border-radius: 50%;
			width: 60rpx;
			height: 60rpx;
			margin: 10rpx;
			background-color: #cad43f;
			image {
				display: block;
				width: 40rpx;
				height: 40rpx;
				margin: 10rpx;
				align-items: center;
			}
		}
	}
}
</style>
