<template>
  <view class="container position-relative">
    <view style="margin-bottom: 130rpx">
      <view class="section-1">
        <template v-if="orderType == 'takein'">
          <list-cell class="location">
            <view
              class="flex-fill d-flex justify-content-between align-items-center"
            >
              <view class="store-name flex-fill">{{ storeName }}</view>
              <image src="/static/images/navigator-1.png" class="arrow"></image>
            </view>
          </list-cell>
        </template>
        <template v-else>
          <list-cell @click="chooseAddress">
            <view class="w-100 d-flex flex-column">
              <view
                class="d-flex align-items-center justify-content-between mb-10"
              >
                <view class="font-size-extra-lg text-color-base">{{
                  choseAddress.street
                }}</view>
                <image
                  src="/static/images/navigator-1.png"
                  class="arrow"
                ></image>
              </view>
              <view
                class="d-flex text-color-assist font-size-sm align-items-center"
              >
                <view class="mr-10">{{ choseAddress.accept_name }}</view>
                <view class="mr-10">{{
                  !choseAddress.gender ? "先生" : "女士"
                }}</view>
                <view>{{ choseAddress.mobile }}</view>
              </view>
            </view>
          </list-cell>
        </template>
        <template v-if="orderType == 'takein'">
          <list-cell arrow class="meal-time">
            <view
              class="flex-fill d-flex justify-content-between align-items-center"
            >
              <view class="title">取餐时间</view>
              <view class="time">立即用餐</view>
            </view>
          </list-cell>
        </template>
        <template v-else>
          <list-cell>
            <view class="w-100 d-flex flex-column">
              <view
                class="d-flex align-items-center font-size-base text-color-base"
              >
                <view class="flex-fill">预计送达时间</view>
                <view class="mr-10">{{ costTime }}</view>
                <image
                  src="/static/images/navigator-1.png"
                  class="arrow"
                ></image>
              </view>
              <view class="font-size-base text-color-primary"
                >特殊时期减少接触，请修改下方订单备注</view
              >
            </view>
          </list-cell>
        </template>
      </view>
      <!-- 购物车列表 begin -->
      <view class="section-2">
        <view class="cart d-flex flex-column">
          <list-cell last v-for="(item, index) in cart" :key="index">
            <view class="w-100 d-flex flex-column">
              <view class="d-flex align-items-center mb-10">
                <view class="name-and-props overflow-hidden">
                  <view class="text-color-base font-size-lg">{{
                    item.name
                  }}</view>
                </view>
                <view
                  class="d-flex flex-fill justify-content-between align-items-center text-color-base font-size-lg"
                >
                  <view>x{{ item.number }}</view>
                  <view>￥{{ item.price }}</view>
                </view>
              </view>
              <view
                class="text-truncate font-size-base text-color-assist"
                v-if="item.props_text"
              >
                {{ item.props_text }}
              </view>
            </view>
          </list-cell>
          <template v-if="orderType == 'takeout'">
            <list-cell last>
              <view
                class="w-100 d-flex font-size-base align-items-center justify-content-between"
              >
                <view>配送费</view>
                <view>￥3</view>
              </view>
            </list-cell>
          </template>
        </view>
        <list-cell arrow @click="goToPackages">
          <view
            class="flex-fill d-flex justify-content-between align-items-center"
          >
            <view class="text-color-base">奈雪券</view>
            <view class="text-color-primary">超值购买优惠券大礼包</view>
          </view>
        </list-cell>
        <list-cell arrow>
          <view
            class="flex-fill d-flex justify-content-between align-items-center"
          >
            <view class="text-color-base">礼品卡</view>
            <view class="text-color-primary">请选择</view>
          </view>
        </list-cell>
        <list-cell last>
          <view class="flex-fill d-flex justify-content-end align-items-center">
            <view>总计￥{{ total }},实付</view>
            <view class="font-size-extra-lg font-weight-bold"
              >￥{{ totalFee }}</view
            >
          </view>
        </list-cell>
      </view>
      <!-- 购物车列表 end -->
      <view
        class="d-flex align-items-center justify-content-start font-size-sm text-color-warning"
        style="padding: 20rpx 0"
      >
        <view class="iconfont iconhelp line-height-100"></view>
        <view>优惠券不与满赠、满减活动共享</view>
      </view>
      <!-- 支付方式 begin -->
      <view class="payment">
        <list-cell last :hover="false"><text>支付方式</text></list-cell>
        <radio-group>
          <label
            class="d-flex align-items-center justify-content-between w-100 bg-white"
          >
            <image
              src="../../static/images/pay/payment.png"
              class="image"
            ></image>
            <view class="flex-fill">余额支付（余额￥0）</view>
            <view class="font-size-sm">余额不足</view>
            <radio value="default" color="#ADB838" disabled="true" />
          </label>
          <label
            class="d-flex align-items-center justify-content-between w-100 bg-white"
          >
            <image
              src="../../static/images/pay/weichat-pay.png"
              class="image"
            ></image>
            <view class="flex-fill">微信支付</view>
            <radio value="weichat" checked="true" color="#ADB838" />
          </label>
        </radio-group>
      </view>
      <!-- 支付方式 end -->
      <list-cell arrow last @click="goToRemark">
        <view
          class="d-flex flex-fill align-items-center justify-content-between overflow-hidden"
        >
          <view class="flex-shrink-0 mr-20">备注</view>
          <view class="text-color-primary flex-fill text-truncate text-right">
            {{ form.remark || "点击填写备注" }}
          </view>
        </view>
      </list-cell>
    </view>
    <!-- 付款栏 begin -->
    <view
      class="w-100 pay-box position-fixed fixed-bottom d-flex align-items-center justify-content-between bg-white"
    >
      <view class="font-size-sm" style="margin-left: 20rpx">合计：</view>
      <view class="font-size-lg flex-fill">￥{{ totalFee }}</view>
      <view
        class="bg-primary h-100 d-flex align-items-center just-content-center text-color-white font-size-base"
        style="padding: 0 60rpx"
        @tap="submit"
      >
        付款
      </view>
    </view>
    <!-- 付款栏 end -->
    <modal
      :show="ensureAddressModalVisible"
      custom
      :mask-closable="false"
      :radius="0"
      width="90%"
    >
      <view class="modal-content">
        <view class="d-flex justify-content-end">
          <image
            src="/static/images/pay/close.png"
            style="width: 40rpx; height: 40rpx"
            @tap="ensureAddressModalVisible = false"
          ></image>
        </view>
        <view
          class="d-flex just-content-center align-items-center"
          style="margin-bottom: 40px"
        >
          <view class="font-size-extra-lg text-color-base"
            >请再次确认下单地址</view
          >
        </view>
        <view
          class="d-flex font-size-base text-color-base font-weight-bold align-items-center justify-content-between mb-20"
        >
          <view
            >{{ choseAddress.accept_name }}
            {{ choseAddress.gender ? "女士" : "先生" }}</view
          >
          <view>{{ choseAddress.mobile }}</view>
        </view>
        <view
          class="d-flex font-size-sm text-color-assist align-items-center justify-content-between mb-40"
        >
          <view>{{ choseAddress.street }}</view>
          <button
            type="primary"
            size="mini"
            plain
            class="change-address-btn"
            @tap="chooseAddress"
          >
            修改地址
          </button>
        </view>
        <button type="primary" class="pay_btn" @tap="pay">确认并付款</button>
      </view>
    </modal>
  </view>
</template>

<script>
	import { mapState,mapMutations } from 'vuex';
	import listCell from '@/components/list-cell/list-cell';
	import modal from '@/components/modal/modal';
	export default {
		components:{
			listCell,
			modal
		},
		data() {
			return {
				cart:[],
				totalFee:'',
				order_id:'',
				ensureAddressModalVisible:false,
				form:{
					remark:''
				},
				storeName:''
			};
		},
		onLoad(options) {
			if(options.order_id) {
				uni.setStorage({
					key:'orderId',
					data:options.order_id
				});
			}
			this.order_id = uni.getStorageSync('orderId');
			this.init(this.order_id);
			const { remark } = options;
			remark && this.$set(this.form,'remark',remark);
		},
		computed:{
			...mapState(['orderType','choseAddress','choseStore']),
			costTime() {
				let mydate = new Date();
				let myNewDate = new Date(mydate.setMinutes(mydate.getMinutes() + 20));
				return myNewDate.getHours() + ':' + myNewDate.getMinutes();
			},
			total() {
				if(this.orderType == 'takenin') {
					return this.cart.reduce((acc,cur) => acc + cur.number * cur.price,0);
				}else if(this.orderType === 'takeout') {
					return this.cart.reduce((acc,cur) => acc + cur.number * cur.price,0) +3;
				}
			}
		},
		methods:{
			...mapMutations(['SET_ORDER','SET_STORE','SET_ADDRESS']),
			//初始化函数
			init(id) {
				return uniCloud.callFunction({
					name:'order',
					data:[
						action:'getOrder',
						order_id:id
					]
				}).then(resData => {
					let data = resData.result.data[0];
					if(data.order_type === 0) {
						this.storeName = data.chooseStore;
						this.cart  = data.goodsInOrder;
						this.totalFee = data.totalFee;
					} else if(data.order_type === 1) {
						this.cart = data.goodsInOrder;
						this.totalFee = data.totalFee;
						let id = data.order_address;
						return uniCloud.callFunction({
							name:'address',
							data:id
						}).then(resAddress =>{
							if(resAddress.result.status === 0) {
								this.SET_ADDRESS(resAddress.result.data);
							}
						});
					}
				});
			},
			chooseAddress() {
				uni.navigateTo({
					url:'../address/address'
				});
			},
			goToRemark() {
				uni.navigateTo({
					url:'../remark/remark?remark='+this.form.remark
				});
			},
			submit() {
				if(this.orderType == 'takeout') {
					this.ensureAddressModalVisible = true;
				}else {
					this.pay();
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
.container {
  padding: 30rpx;
}

.arrow {
  width: 50rpx;
  height: 50rpx;
  position: relative;
  margin-right: -10rpx;
}

.location {
  .store-name {
    font-size: $font-size-lg;
  }

  .iconfont {
    font-size: 50rpx;
    line-height: 100%;
    color: $color-primary;
  }
}

.section-1 {
  margin-bottom: 30rpx;
  .contact {
    .contact-tip {
      margin-left: 10rpx;
      border: 2rpx solid $color-primary;
      padding: 6rpx 10rpx;
      color: $color-primary;
    }
  }
}

.section-2 {
  .name-and-props {
    width: 65%;
  }
}

.payment {
  margin-bottom: 30rpx;

  .disabled {
    color: $text-color-grey;
  }

  .payment-icon {
    font-size: 44rpx;
    margin-right: 10rpx;
  }

  .checkbox {
    font-size: 36rpx;
    margin-left: 10rpx;
  }

  .checked {
    color: $color-primary;
  }
  .image {
    width: 40rpx;
    height: 40rpx;
    margin: 10rpx 20rpx;
  }
}

.pay-box {
  box-shadow: 0 0 20rpx rgba(0, 0, 0, 0.1);
  height: 100rpx;
}

.modal-content {
  .change-address-btn {
    line-height: 2;
    padding: 0 1em;
  }
  .pay_btn {
    width: 100%;
    border-radius: 50rem !important;
    line-height: 3;
  }
}
</style>
