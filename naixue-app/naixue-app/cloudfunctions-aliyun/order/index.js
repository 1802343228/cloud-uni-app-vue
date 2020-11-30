'use strict';
const crypto = require('crypto')
const {
	getOffsetDate,
	getNowFormatDate
} = require('time')
const db = uniCloud.database()
const _ = db.command
exports.main = async (event, context) => {
	//判定order行为
	if(event.action == 'addTakein') {
		const {
			openId,
			goodsInOrder
		} = event.data
		const uid = openId
		//生成订单号
		const order_id = parseInt(Data.now() / 1000) +crypto
		.createHash('md5')
		.update(uid,'utf-8')
		.digest('hex').substring(0,8);
		
		//计算订单总金额
		const totalFee = goodsInOrder.reduce((acc,cur) => acc + cur.number * cur.price,0)
		
		//门店位置
		const chooseStore = event.data.chooseStore
		//生成订单
		const orderResult = await ab.collection('orders').add({
			uid,//用户openid
			order_id,
			order_type:0,
			goodsInOrder,
			totalFee,
			chooseStore,
			status:1
		})
		if(!orderResult.id) {
			return {
				code:-3,
				msg:'提交订单失败，请稍后再试'
			}
		}
		return {
			order_id
		}
	} else if(event.action == 'addTakeout') {
		const {
			goodsInOrder,
			openId
		} = event.data
		//关联用户openId
		const uid = openId
		//生成订单Id
		const order_id = parseInt(Data.now() / 1000) + crypto
		.createHash('md5')
		.update(uid,'utf-8')
		.digest('hex').substring(0,8);
		//计算总金额
		//计算订单总金额
		const totalFee = goodsInOrder.reduce((acc,cur) => acc + cur.number * cur.price,0) +3
		const order_address = event.data.order_address
		const orderResult = await db.collection('orders').add({
			uid,
			order_type:1,
			order_id,
			goodsInOrder,
			totalFee,
			order_address,
			status:1
		})
		if(!orderResult.id) {
			return {
				code:-3,
				msg:'提交订单失败，请稍后再试'
			}
		}
		return {
			order_id
		}
		
	} else if(event.action == 'getOrder') {
		let order_id = event.order_id
		const resResult = await db.collection('orders').where({
			order_id
		}).get()
		return resResult
	} else if(event.action == 'orderCurren') {
		let order_id = event.order_id
		const status = await db.collection('orders').where({
			order_id
		}).field({
			status:1
		}).get().then(res => {
			return res.data[0].status
		})
		if(status === 2) {
			//查询订单所属门店
			const store = await db.collection('orders').where({
				order_id
			}).field({
				chooseStore:1
			}).get().then(res => {
				return res.data[0].chooseStore
			})
			const num = await db.collection('orders').where({
				status:3,
				chooseStore:store
			}).count().then(res => {
				return res.total
			})
			
			const time = getOffsetDate(8)
			const nowTime = getNowFormatDate(time)
			const nTime = new Date(time.setMinutes(time.getMinutes() + num *2))
			const takeTime = getNowFormatDate(nTime)
			//生成取餐码
			const code = Math.floor(Math.random() * 700 + 800)
			const updateResult = await db.collection('orders').where({
				order_id
			}).update({
				createOrderTime:nowTime,
				takeTime:takeTime,
				sort_num:code,
				status:3
			})
			const fields  ={
				chooseStore:1,
				order_id:1,
				status:1,
				order_type:1,
				goodsInOrder:1,
				totalFee:1,
				remark:1,
				sort_num:1,
				payment_channel:1,
				createOrderTime:1,
				takeTime:1
			}
			const resResult = await db.collection('orders').where({
				order_id
			}).field(fields)
			.get().then(res => {
				return res.data[0]
			})
			resResult.peopleNum = num
			if(updateResult.id || updateResult.updated === 1) {
				return {
					'status':0,
					'data':resResult,
				}
			} else {
				return {
					'status':-1,
					'msg':'后台数据错误'
				}
			}
		} else if(status === 3 || status === 4 || status === 5) {
			const fields = {
				chooseStore:1,
				order_id:1,
				status:1,
				order_type:1,
				goodsInOrder:1,
				totalFee:1,
				remark:1,
				sort_num:1,
				payment_channel:1,
				createOrderTime:1,
				takeTime:1
			}
			const resResult = await db.collection('orders').where({
				order_id
			}).field(fields)
			.get().then(res => {
				return res.data[0]
			})
			return {
				'status':1,
				'data':resResult,
			}
		}
	} else if(event.action === 'orderUpdate') {
		let order_id = event.order_id
		const status  =await db.collection('orders').where({
			order_id
		}).field({
			status:1
		}).get().then(res => {
			return res.data[0].status
		})
		return status
	}
	
};
