<template>
	<view class="content">
		<view class="input_box">
			<textarea placeholder="请输入您要留言的内容" class="textarea border" v-model="message.content"></textarea>
			<input type="text" class="input-box border" v-model="message.name" placeholder="请输入用户名:" />
			<button @click="addMsg" class="submit">提交</button>
		</view>

		<view class="list">
			<view class="item" v-for="(item, index) in listData" :key="index">
				<view class="word">
					<text class="user_name">{{ item.name }}</text>
					<text class="content small">{{ item.content }}</text>
				</view>
				<text class="delete" @click="removeMsg(item._id)">删除</text>
			</view>
		</view>
	</view>
</template>

<script>
export default {
	data() {
		return {
			message: {
				name: "",
				content: ""
			},
			listData: [
				{
					_id: "",
					name: "",
					content: ""
				}
			]
		};
	},
	onLoad() {
		this.getData();
	},
	methods: {
		addMsg() {
			let data = this.message;
			uniCloud
				.callFunction({
					name: "add",
					data: data
				})
				.then(res => {
					this.getData();
					this.message.name = "";
					this.message.content = "";
				});
		},
		getData() {
			uniCloud
				.callFunction({
					name: "get"
				})
				.then(res => {
					this.listData = res.result.data;
				});
		},
		removeMsg(id) {
			console.log(id);
			let _id = id;
			uniCloud
				.callFunction({
					name: "remove",
					data: {
						_id
					}
				})
				.then(res => {
					this.getData();
				});
		}
	}
};
</script>

<style>
	.input_box {
		width: 750rpx;
	}
.textarea {
	width: 100%;
	margin: 5rpx;
}
.border {
	border: 1rpx solid #f1f1f1;
	border-radius: 10rpx;
}
.input-box {
	margin: 5rpx;
	height: 100rpx;
}
.submit {
	background-color: #A1CED7;
	width: 98%;
	border-radius: 20rpx;
}
.word {
	margin: 40rpx;
}
.user_name {
	color: #106d80;
	font-weight: 500;
}
.small {
	color: #333333;
	font-size: 20rpx;
	margin-left: 10rpx;
}
.delete {
	background-color: #ff844b;
	color: #FFFFFF;
	margin: 40rpx;
	border-radius: 20rpx;
	padding: 5rpx;
}
</style>
