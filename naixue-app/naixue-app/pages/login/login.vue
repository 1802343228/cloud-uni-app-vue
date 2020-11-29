<template>
	<view class="container">
		<view class="intro">
			<image src="/static/images/logo.png"></image>
			<view class="tips">
				一杯好茶，一口软欧包
				<br />
				在奈雪遇见两种美好
			</view>
		</view>
		<view class="bottom">
			<!-- #ifdef MP-WEIXIN -->
			<button type="primary" size="default" class="login-btn" open-type="getUserInfo" lang="zh_CN" @getuserinfo="wxLogin">
				<image src="/static/images/mine/wechat.png"></image>
				微信一键登录
			</button>
			<!-- #endif -->
			<view class="d-flex flex-column justify-content-evenly align-items-center text-center" style="height: 30vh;">
				<view class="w-100 font-size-base text-color-assist">新用户登录即加入会员，享会员权益</view>
				<view class="w-100 row d-flex just-content-around align-items-center font-size-sm text-color-assist">
					<view class="grid">
						<image src="/static/images/mine/rhyl.png"></image>
						<view>入会有礼</view>
					</view>
					<view class="grid">
						<image src="/static/images/mine/jfdh.png"></image>
						<view>积分兑换</view>
					</view>
					<view class="grid">
						<image src="/static/images/mine/sjtq.png"></image>
						<view>升级特权</view>
					</view>
					<view class="grid">
						<image src="/static/images/mine/srtq.png"></image>
						<view>生日特权</view>
					</view>
					<view class="grid">
						<image src="/static/images/mine/nxbz.png"></image>
						<view>奈雪宝藏</view>
					</view>
				</view>
				<view class="font-size-base text-color-primary">会员权益说明</view>
			</view>
		</view>
	</view>
</template>

<script>
import { mapState, mapMutations } from "vuex";
export default {
	data() {
		return {};
	},
	computed: {
		...mapState(["userInfo", "isLogin"])
	},
	methods: {
		...mapMutations(["Login"]),
		wxLogin(e) {
			const that = this;
			let userInfo = e.detail.userInfo;
			console.log(userInfo);
			uni.showLoading({
				title: "登录中..."
			});
			return new Promise((resolve, reject) => {
				uni.login({
					provider: "weixin",
					success(login_res) {
						if (login_res.code) {
							resolve(login_res.code);
						} else {
							reject(new Error("微信登录失败"));
						}
					},
					fail(e) {
						reject(new Error("微信登录失败"));
					}
				});
			})
				.then(code => {
					console.log("code:", code);
					return uniCloud.callFunction({
						name: "login",
						data: {
							code,
							userInfo
						}
					});
				})
				.then(res => {
					uni.hideLoading();
					console.log(res);
					if (res.result.status !== 0) {
						return Promise.reject(new Error(res.result.msg));
					}
					console.log(res.result.data);
					that.Login(res.result.data);
					uni.setStorage({
						key: "token",
						data: res.result.token
					});
					uni.showModal({
						content: " 登录成功",
						showCancel: false
					});
					uni.hideLoading();
					uni.navigateBack();
				})
				.catch(err => {
					console.log(err);
					uni.hideLoading();
					uni.showModal({
						content: "出现错误,请稍后再试." + err.message,
						showCancel: false
					});
				});
		}
	}
};
</script>

<style lang="scss" scoped>
.intro {
	width: 100%;
	height: 60vh;
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: space-evenly;
	font-size: font-size-base;
	color: text-color-assist;

	image {
		width: 165rpx;
		height: 165rpx;
	}
	.tips {
		line-height: 72rpx;
		text-align: center;
	}
}
.bottom {
	height: 40vh;
	display: flex;
	flex-direction: column;
	justify-content: space-between;
	padding: 0 40rpx;
	.login-btn {
		width: 100%;
		border-radius: 50rem !important;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 10rpx 0;
		image {
			width: 36rpx;
			height: 30rpx;
			margin-right: 10rpx;
		}
	}
	.row {
		.grid {
			width: 20%;
			image {
				width: 60rpx;
				height: 60rpx;
				margin-bottom: 10rpx;
			}
		}
	}
}
</style>
