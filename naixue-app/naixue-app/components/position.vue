<template>
	<view>
	    <view class="page-body">
	        <view class="page-section page-section-gap">
	            <map id="myMap" style="width: 100%; height: 300px;" :latitude="latitude" :longitude="longitude" :markers="markers" show-location enable-scroll @tap="bindtap" 
				@callouttap="callouttap" @regionchange="regionchange" @updated="updated">
	            </map>
	        </view>
			<view class="store-tips">
				<cover-view class="store-des-box" v-if="storeFlag" @tap="storeDesEvn">
					<cover-image class="store-img" src="../../static/hch-position/1.jpg"></cover-image>
					<cover-view class="store-des">
						<cover-view class="store-name">{{store.name}}</cover-view>
						<cover-view class="store-address">地址:{{store.address}}</cover-view>					
						<cover-view class="store-tel">电话:{{store.tel}}</cover-view>					
					</cover-view>
				</cover-view>
				<cover-image v-if="storeAdFlag" class="store-ad" src="../../static/hch-position/ad.jpg"></cover-image>
				<cover-image v-if="storeAdFlag" class="store-clear" @tap="hideAd" src="../../static/hch-position/clear.png"></cover-image>
			</view>
			<cover-view class="near-num">距离最近的门店{{distanceL}}千米</cover-view>
			<cover-image class="address-icon" src="../../static/hch-position/定位.png"></cover-image>
	    </view>
	</view>
</template>

<script>
	props:{
			storeData:{
				type:Array,
				default:[
					{
						id:1,
						name:'门店1号',
						address:'厦门市思明区汇景商业广场XXX1号',
						tel:'12345678900',
					},{
						id:2,
						name:'门店2号',
						address:'厦门市思明区汇景商业广场XXX2号',
						tel:'12345678900',
					},
				]
			},
			 markers:{
				 type:Array,
				 default: [
				{
					id:1,
					latitude: 24.4483294023427,
					longitude: 118.08479034393311,
					iconPath: '../../static/hch-position/门店.png',
					callout:{
						content:"门店1号店",
						borderRadius:10,
						padding:10,
						display:"ALWAYS",
					}
				}, {
					id:2,
					latitude: 24.45580,
					longitude: 118.12266,
					iconPath: '../../static/hch-position/门店.png',
					callout:{
						content:"门店2号店",
						borderRadius:10,
						padding:10,
						display:"ALWAYS",
					}
				}]
			 },
		},
	    data() {
	        return {
				store:{},
				storeFlag:false,
				storeAdFlag:true,
				distanceL:0,//附近店门的距离
	   //          storeData:[
				// 	{
				// 		id:1,
				// 		name:'门店1号',
				// 		address:'厦门市思明区汇景商业广场XXX1号',
				// 		tel:'12345678900',
				// 	},{
				// 		id:2,
				// 		name:'门店2号',
				// 		address:'厦门市思明区汇景商业广场XXX2号',
				// 		tel:'12345678900',
				// 	},
				// ],
	            latitude: 39.909,
	            longitude: 116.39742,
	    //         markers: [
					// {
					// id:1,
	    //             latitude: 24.4483294023427,
	    //             longitude: 118.08479034393311,
	    //             iconPath: '../../static/hch-position/门店.png',
					// callout:{
					// 	content:"门店1号店",
					// 	borderRadius:10,
					// 	padding:10,
					// 	display:"ALWAYS",
					// }
	    //         }, {
					// id:2,
	    //             latitude: 24.45580,
	    //             longitude: 118.12266,
	    //             iconPath: '../../static/hch-position/门店.png',
					// callout:{
					// 	content:"门店2号店",
					// 	borderRadius:10,
					// 	padding:10,
					// 	display:"ALWAYS",
					// }
	    //         }],
				controls:[
					
				]
	        }
	    },
	    methods: {
			// 点击了门店信息
			storeDesEvn(){
				uni.showToast({title:'自己自定义跳门店详情页介绍',duration:2000,icon:'none'});
			},
			// 点击气泡
			callouttap(e){
				this.storeFlag=true;
				this.storeAdFlag=false;
				for (let i = 0; i < this.storeData.length; i++) {
					const ele = this.storeData[i];
					if(ele.id==e.markerId){
						this.store = ele;
						break;
					}
				}
				
			},
			// 点击地图
			bindtap(id){
				this.storeFlag=false;
				// this.storeAdFlag=true;
			},
			// 隐藏广告图片
			hideAd(){
				this.storeAdFlag=false;
			},
			updated(){
				let _this = this;
				wx.getLocation({
				  type: 'gcj02', //返回可以用于wx.openLocation的经纬度
				  success: function (res) {
					_this.latitude = res.latitude;
					_this.longitude = res.longitude;
					_this.nearDistance(_this.markers,_this.latitude,_this.longitude)
				  },
				   fail: function(res){
					   console.log(res);
				   }
			  })
			},
			// 改变视野时改变经纬度
			regionchange(e){
				let _this = this;
				// 使用 wx.createMapContext 获取 map 上下文
				var mapCtx = wx.createMapContext('myMap',this)
				mapCtx.getCenterLocation({
					success:function(res){
						_this.centerLatitude = res.latitude;
						_this.centerLongitude = res.longitude;
						_this.nearDistance(_this.markers,_this.centerLatitude,_this.centerLongitude)
					},
					fail: function(res){
					   console.log(res);
					}
				}) //获取当前地图的中心经纬度
			},
			// 两点间距离
			distance(la1, lo1, la2, lo2) {
				var La1 = la1 * Math.PI / 180.0;
				var La2 = la2 * Math.PI / 180.0;
				var La3 = La1 - La2;
				var Lb3 = lo1 * Math.PI / 180.0 - lo2 * Math.PI / 180.0;
				var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(La3 / 2), 2) + Math.cos(La1) * Math.cos(La2) * Math.pow(Math.sin(Lb3 / 2), 2)));
				s = s * 6378.137;//地球半径
				s = Math.round(s * 10000) / 10000;
				return s
			},
			// 计算最近的距离
			nearDistance(array,centerLatitude,centerLongitude){
				let temp=[]
				for (let i = 0,l=array.length; i < l; i++) {
					const element =  array[i];
					let d = this.distance(element.latitude,element.longitude,centerLatitude,centerLongitude);
					temp.push(d)
				};
				this.distanceL = Math.min.apply(null, temp);
			}
			
			
	    },
		onShow() {
			
		},
		onReady() {
			
	  },
	}
</script>

<style>
	.content {
		text-align: center;
		height: 400upx;
	}
	.page-section{
		z-index: 0;
	}
	.store-tips{
		width: 600upx;
		height: 100upx;
		margin: 10upx auto;
		border-radius: 10upx;
		position: absolute;
		top: 10upx;
		left: 50%;
		transform: translate(-50%,0);
		z-index:2;
		overflow: hidden;
		.store-des-box{
			background: #fff;
		}
		.store-img{
			width: 80upx;
			height: 80upx;
			border-radious:10upx;
			margin: 10upx;
			float: left;
		}
		.store-des{
			padding-top: 8upx;
			float: left;
			line-height: 1;
			font-size: 22upx;
			color: #666;
			padding-left: 20upx;
			.store-name{
				font-weight: 600;
				color: deeppink;
			}
		}
		.store-clear{
			width: 30upx;
			height: 30upx;
			position: absolute;
			top:7rpx;
			right:7upx;
			maigin:30upx;
	
		}
	}
	.address-icon{
		width:38rpx;
		height:40rpx;
		position:absolute;
		top:22%;
		left:50%;
		z-index:2;
		margin-bottom:-20upx;
		margin-left:-20upx;
	
	}
	.near-num{
		padding: 10upx 20upx;
		border-radius: 10upx;
		position:absolute;
		top:17%;
		left:50%;
		z-index:2;
		font-size:24upx;
		background:#fff;
		transform: translate(-50%,0)
	}
</style>
