<template>
	<view class="container">
		<view class="main">
			<!-- 导航部分 -->
			<view class="nav">
				<!-- 头部 -->
				<view class="header">
					<view class="left" v-if="orderType == 'takein'">
						<view class="store-name" @tap="tapStore">
							<text>{{ choseStore.name }}</text>
							<view class="iconfont icon-arrow-right"></view>
						</view>
						<view class="store-location">
							<image src="/static/images/order/location.png" style="width: 30rpx; height: 30rpx;" class="mr-10"></image>
							<text v-if="choseStore.distance">距离您{{ choseStore.distance }}km</text>
							<text v-else>请选择您附近门店</text>
						</view>
					</view>
					<view class="left overflow-hidden" v-else>
						<view class="d-flex align-items-center overflow-hidden">
							<image src="/static/images/order/location.png" style="width: 30rpx; height: 30rpx;" class="mr-10"></image>
							<view class="font-size-extra-lg text-color-base font-weight-bold text-truncate">
								<text>{{ choseAddress.street }}</text>
							</view>
						</view>
						<view class="font-size-sm text-color-assist overflow-hidden text-truncate">
							由
							<text class="text-color-base" style="margin: 0 10rpx">{{ choseAddress.store_name }}</text>
							配送
						</view>
					</view>
					<view class="right">
						<view class="dinein" :class="{ active: orderType == 'takein' }" @tap="tapTakein"><text>自取</text></view>
						<view class="takeout" :class="{ active: orderType == 'takeout' }" @tap="tapTakeOut"><text>外卖</text></view>
					</view>
				</view>
				<!-- 优惠信息 -->
				<view class="coupon">
					<text class="title">"霸气mini卡"超级购券活动，赶紧去购买</text>
					<view class="iconfont icon-arrow-right"></view>
				</view>
			</view>
			<!-- 点餐信息 -->
			<view class="content">
				<!-- left category -->
				<scroll-view scroll-y="true" class="menus" :scroll-into-view="menuScrollIntoView">
					<view class="wrapper">
						<view
							class="menu"
							:id="`menu-${item._id}`"
							:class="{ current: item._id === currentCateId }"
							v-for="(item, index) in goods"
							:key="index"
							@tap="handleMenuTap(item._id)"
						>
							<text>{{ item.name }}</text>
							<view class="dot" v-show="menuCartNum(item._id)">{{ menuCartNum(item._id) }}</view>
						</view>
					</view>
				</scroll-view>
				<!-- right goods -->
				<scroll-view scroll-y="true" class="goods" scroll-with-animation :scroll-top="cateScrollTop" @scroll="handleGoodsScroll">
					<view class="wrapper">
						<swiper class="ads" id="ads" :indicator-dots="true" :autoplay="true" :interval="3000" indicator-dots>
							<swiper-item v-for="(item, index) in ads" :key="index"><image :src="item.image"></image></swiper-item>
						</swiper>
						<!-- goods-list -->
						<view class="list">
							<view class="category" :id="`cate-${item._id}`" v-for="(item, index) in goods" :key="index">
								<view class="title">
									<text>{{ item.name }}</text>
									<image :src="item.icon" class="icon"></image>
								</view>
								<view class="items">
									<view class="good" v-for="(good, key) in item.goods_list" :key="key">
										<image :src="good.images" class="image"></image>
										<view class="right">
											<text class="name">{{ good.name }}</text>
											<text class="tips">{{ good.content }}</text>
											<view class="price_and_action">
												<text class="price">￥{{ good.price }}</text>
												<view class="btn-group" v-if="good.property">
													<button type="primary" class="btn property_btn" hover-class="none" size="mini" @tap="showGoodDetailModal(item, good)">
														选规格
													</button>
													<view class="dot" v-show="goodCartNum(good._id)">{{ goodCartNum(good._id) }}</view>
												</view>
												<!-- 购物车加减 -->
												<view class="btn-group" v-else>
													<button
														type="default"
														v-show="goodCartNum(good._id)"
														plain
														class="btn reduce_btn"
														size="mini"
														hover-class="none"
														@tap="handleReduceFromCart(item, good)"
													>
														<view class="iconfont icon-sami-select"></view>
													</button>
													<view class="number" v-show="goodCartNum(good._id)">{{ goodCartNum(good._id) }}</view>
													<button type="primary" class="btn add_btn" size="min" hover-class="none" @tap="handleAddToCart(item, good, 1)">
														<view class="iconfont icon-add-select"></view>
													</button>
												</view>
											</view>
										</view>
									</view>
								</view>
							</view>
						</view>
					</view>
				</scroll-view>
			</view>
			<!-- 菜单栏结束 -->
			<!-- 购物车开始 -->
			<view class="cart-box" v-if="cart.length > 0">
				<!-- 商品数量 -->
				<view class="mark">
					<image src="../../static/images/menu/cart.png" class="cart-img" @tap="openCartPopup"></image>
					<view class="tag">{{ getCartGoodsNumber }}</view>
				</view>
				<!-- 商品价格 -->
				<view class="price">￥{{ getCartGoodsPrice }}</view>
				<!-- 结算按钮 -->
				<button type="primary" class="pay-btn" :disabled="disabledPay" @tap="topay">{{ disabledPay ? `差${spread}元起送` : "去结算" }}</button>
			</view>
		</view>
		<!-- main end -->
		<!-- modal开始 -->
		<modal :show="goodDetailModalVisible" class="good-detail-modal" color="#5A5B5C" width="90%" custom padding="0rpx" radius="12rpx">
			<view class="cover">
				<image v-if="good.images" :src="good.images" class="image"></image>
				<view class="btn-group">
					<image src="/static/images/menu/share-good.png"></image>
					<image src="/static/images/menu/close.png" @tap="closeGoodDetailModal"></image>
				</view>
			</view>
			<scroll-view class="detail" scroll-y>
				<view class="wrapper">
					<view class="basic">
						<view class="name">{{ good.name }}</view>
						<view class="tips">{{ good.content }}</view>
					</view>
					<view class="properties" v-if="good.property">
						<view class="property" v-for="(item, index) in good.property" :key="index">
							<view class="title">
								<text class="name">{{ item.name }}</text>
								<view class="desc" v-if="item.desc">({{ item.desc }})</view>
							</view>
							<view class="values">
								<view class="value" v-for="(value, key) in item.values" :key="key" :class="{ default: value.is_default }" @tap="changePropertyDefault(index, key)">
									{{ value.value }}
								</view>
							</view>
						</view>
					</view>
				</view>
			</scroll-view>
			<view class="action">
				<view class="left">
					<view class="price">￥{{ good.price }}</view>
					<view class="props" v-if="getGoodSelectedProps(good)">{{ getGoodSelectedProps(good) }}</view>
				</view>
				<view class="btn-group">
					<button type="default" plain class="btn" size="mini" hover-class="none" @tap="handlePropertyReduce"><view class="iconfont icon-sami-select"></view></button>
					<view class="number">{{ good.number }}</view>
					<button type="primary" class="btn" size="min" hover-class="none" @tap="handlePropertyAdd"><view class="iconfont icon-add-select"></view></button>
				</view>
			</view>
			<view class="add-to-cart-btn" @tap="handleAddToCartInModal"><view>加入购物车</view></view>
		</modal>
		<!-- modal结束 -->
		<popup-layer direction="top" :showPop="cartPopupVisible" class="cart-popup">
			<template slot="content">
				<view class="top"><text @tap="handleCartClear">清空</text></view>
				<scroll-view class="cart-list" scroll-y>
					<view class="wrapper">
						<view class="item" v-for="(item, index) in cart" :key="index">
							<view class="left">
								<view class="name">{{ item.name }}</view>
								<view class="props">{{ item.props_text }}</view>
							</view>
							<view class="center">
								<text>￥{{ item.price }}</text>
							</view>
							<view class="right">
								<button type="default" plain size="mini" class="btn" hover-class="none" @tap="handleCartItemReduce(index)">
									<view class="iconfont icon-sami-select"></view>
								</button>
								<view class="number">{{ item.number }}</view>
								<button type="primary" class="btn" size="min" hover-class="none" @tap="handleCartItemAdd(index)">
									<view class="iconfont icon-add-select"></view>
								</button>
							</view>
						</view>
						<view class="item" v-if="orderType == 'takeout'">
							<view class="left"><view class="name">配送费</view></view>
							<view class="center"><text>￥3</text></view>
							<view class="right invisible">
								<button type="default" plain size="mini" class="btn" hover-class="none"><view class="iconfont icon-sami-select"></view></button>
								<view class="number">1</view>
								<button type="primary" class="btn" size="min" hover-class="none"><view class="iconfont icon-add-select"></view></button>
							</view>
						</view>
					</view>
				</scroll-view>
			</template>
		</popup-layer>
	</view>
</template>

<script>
import { mapState, mapMutations } from "vuex";
import modal from "@/components/modal/modal";
import popupLayer from "@/components/popup-layer/popup-layer";
export default {
	components: {
		modal,
		popupLayer
	},
	data() {
		return {
			goods: [],
			ads: [
				{
					image: "https://img-shop.qmimg.cn/s23107/2020/04/27/4ebdb582a5185358c4.jpg?imageView2/2/w/600/h/600"
				},
				{ image: "https://images.qmai.cn/s23107/2020/05/08/c25de6ef72d2890630.png?imageView2/2/w/600/h/600" },
				{
					image: "https://img-shop.qmimg.cn/s23107/2020/04/10/add546c1b1561f880d.jpg?imageView2/2/w/600/h/600"
				},
				{ image: "https://images.qmai.cn/s23107/2020/04/30/b3af19e0de8ed42f61.jpg?imageView2/2/w/600/h/600" },
				{ image: "https://img-shop.qmimg.cn/s23107/2020/04/17/8aeb78516d63864420.jpg?imageView2/2/w/600/h/600" }
			],
			menuScrollIntoView: "",
			currentCateId: "5fc354d59d61f70001438b53",
			cateScrollTop: 0,
			sizeCalcState: false,
			cart: [],
			goodDetailModalVisible: false, //控制模态框展示
			good: {}, //当前饮品
			category: {}, //当前饮品所在分类
			cartPopupVisible: false //购物车详情展示
		};
	},
	onLoad() {
		this.init();
	},
	computed: {
		...mapState(["choseStore", "orderType", "choseAddress", "isLogin"]),
		goodCartNum() {
			//计算商品选中数量
			return id => this.cart.reduce((acc,cur) => {
				if(cur.id === id) {
					return (acc += cur.number);
				}
				return acc;
			},0);
		},
		menuCartNum() {
			//计算类别汇总数量
			return id => this.cart.reduce((acc,cur) => {
				if(cur.cate_id === id) {
					return ( acc += cur.number );
				}
				return acc;
			},0);
		},
		getCartGoodsNumber() {
			//计算购物车总数量
			return this.cart.reduce((acc,cur) => acc +cur.number,0);
		},
		getCartGoodsPrice() {
			//计算购物车总价格
			return this.cart.reduce((acc,cur) => acc + cur.number * cur.price,0);
		},
		disabledPay() {
			//判定是否达到起送价格
			return this.orderType == 'takeout' && this.getCartGoodsPrice <38 ? true :false;
		},
		spread() {
			//起送差额
			if(this.orderType != 'takeout') return;
			return parseFloat((38 - this.getCartGoodsPrice).toFixed(2));
		}
	},
	methods: {
		// goStore() {
		// 	uni.navigateTo({
		// 		url: "../store/store"
		// 	});
		// }
		...mapMutations(["SET_ORDERTYPE"]),
		//切换自取
		tapTakein() {
			if(Object.keys(this.choseStore).length != 0) {
				this.SET_ORDERTYPE('takein');
			} else {
				uni.navigateTo({
					url:'../stores/stores'
				});
			}
		},
		//切换外卖
		tapTakeOut() {
			if(!this.isLogin) {
				uni.navigateTo({
					url:'../login/login'
				});
				return;
			}
			uni.navigateTo({
				url:'../address/address'
			})
		},
		//选取门店
		tapStore() {
			uni.navigateTo({
				url:'../stores/stores'
			});
		},
		//点赞自动加载数据
		init() {
			if(Object.keys(this.choseStore).length == 0 && this.orderType == 'takein') {
				uni.navigateTo({
					url:'../stores/stores'
				});
			}
			return uniCloud.callFunction({
				name:'goods'
			}).then(res => {
				this.goods = res.result.data;
			});
		},
		//计算右侧goods的top和bottom的高度
		calcSize() {
			let h = 10;
			//计算右侧banner高度
			let view = uni.createSelectorQuery().select('#ads');
			view.fields({
				size:true
			},
			data => {
				h += Math.floor(data.height);
			}
			).exec();
			this.goods.forEach(item => {
				//计算右侧栏每个good的高
				let view = uni.createSelectorQuery().select(`#cate-${item._id}`);
				view.fields({
					size:true
				},data => {
					item.top = h;
					h += data.height;
					item.bottom = h;
				}).exec();
			});
			this.sizeCalcState = true;
		},
		//左侧分类点击事件
		handleMenuTap(id) {
			if(!this.sizeCalcState) {
				this.calcSize();
			}
			this.currentCateId = id;
			this.$nextTick(() =>(this.cateScrollTop = this.goods.find(item => item._id == id).top));
	
		},
		//右侧滑动事件
		handleGoodsScroll({ detail }) {
			if(!this.sizeCalcState) {
				this.calcSize();
			}
			const { scrollTop }  = detail;
			let tabs = this.goods.filter(item => item.top <= scrollTop).reverse();
			if(tabs.length >0){
				this.currentCateId = tabs[0]._id;
			}
		},
		//添加购物车
		handleAddToCart(cate, good, num) {
			const index = this.cart.findIndex(item => item.id === good._id);
			if(index > -1){
				this.cart[index].number += num;
			}else {
				this.cart.push({
					id:good._id,
					cate_id:cate._id,
					name:good.name,
					price:good.price,
					number:num,
					image:good.images,
					props_text:good.props_text
				});
			}
		},
		//移除购物车
		handleReduceFromCart(item, good) {
			const index = this.cart.findIndex(item => item.id === good._id);
			this.cart[index].number -= 1;
			if(this.cart[index].number <= 0) {
				this.cart.splice(index,1);
			}
		},
		//展示模态框
		showGoodDetailModal(item, good) {
			this.good = JSON.parse(JSON.stringify({...good,number:1}));
			this.category = JSON.parse(JSON.stringify(item));
			this.goodDetailModalVisible = true;
		},
		//关闭模态框
		closeGoodDetailModal() {
			this.goodDetailModalVisible = false;
			this.good = {};
			this.category = {};
		},
		//改变默认属性值
		changePropertyDefault(index, key) {
			this.good.prototype[index].values.forEach(value => this.$set(value,'is_default',0));
			this.good.prototype[index].values[key].is_default = 1;
			this.good.number = 1;
		},
		//计算当前饮品所选属性
		getGoodSelectedProps(good, type = "text") {
			if(good.prototype) {
				let props = [];
				good.property.forEach(({value}) => {
					values.forEach(value => {
						if(value.is_default) {
							props.push(type === 'text' ? value.value : value.id);
						}
					});
				});
				return type === 'text' ? props.join(',') : props;
			}
			return '';
		},
		//模态框加一
		handlePropertyAdd() {
			this.good.number += 1;
		},
		//模态框减1
		handlePropertyReduce() {
			if(this.good.number === 1) return;
			this.good.number -= 1;
		},
		//添加进购物车
		handleAddToCartInModal() {
			const product = Object.assign({},this.good, {
				props_text:this.getGoodSelectedProps(this.good),
				props:this.getGoodSelectedProps(this.good,'id')
			});
			this.handleAddToCart(this.category,product,this.good.number);
			this.closeGoodDetailModal();
		},
		//打开关闭购物车详情
		openCartPopup() {
			this.cartPopupVisible = !this.cartPopupVisible;
		},
		//清空购物车
		handleCartClear() {
			uni.showModal({
				title:'提示',
				content:'确认清空购物车吗？',
				success:({confirm}) => {
					if(confirm) {
						this.cartPopupVisible = false;
						this.cart = [];
					}
				}
			});
		},
		//购物车详情减
		handleCartItemReduce(index) {
			if(this.cart[index].number === 1) {
				this.cart.splice(index,1);
			} else {
				this.cart[index].number -= 1;
			}
			if(!this.cart.length) {
				this.cartPopupVisible = false;
			}
		},
		//购物车详情加
		handleCartItemAdd(index) {
			this.cart[index].number += 1;
		},
		//结算按钮操作
		topay() {
			if(!this.isLogin) {
				uni.navigateTo({
					url:'../login/login'
				});
				return;
			}
			uni.navigateTo({
				url:'../pay/pay?total='+this.getCartGoodsPrice
			});
		}
	}
};
</script>

<style lang="scss" scoped>
@import "~@/pages/menu/menu.scss";
</style>
