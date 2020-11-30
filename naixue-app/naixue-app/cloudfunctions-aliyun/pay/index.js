'use strict';
const uniPay = require('unipay')

const {
	wxConfigMp,

} = require('config')
const db = uniCloud.database()
exports.main = async (event, context) => {
	let {
		provider,
		openId,
		remark,
		order_id
	} = event

	//判断订单是否存在,订单支付状态为未支付
	const orderList = await db.collection('orders').where({
		order_id
	}).get()

	if (orderList.data.length !== 1 || orderList.data[0].status !== 1) {
		return {
			code: -2,
			msg: '订单状态不正确'
		}
	}

	//修改订单支付方式和插入订单备注
	await db.collection('orders').where({
		order_id,
	}).update({
		payment_channel: provider,
		order_remark: remark
	})

	//	声明提交getOrderInfo的参数
	let uniPayInstance,
		notifyUrl =
		`https://uni937d4b0-6cc760.service.tcloudbase.com/payment-notify/${provider}_${context.PLATFORM}`,
		openid,
		tradeType

	switch (provider + '_' + context.PLATFORM) {
		case 'wxpay_mp-weixin':
			uniPayInstance = uniPay.initWeixin(wxConfigMp)
			openid = openId
			tradeType = 'JSAPI'
			break;
		case 'alipay_mp-alipay':
			uniPayInstance = uniPay.initAlipay(aliConfigMp)
			openid = userInfo.ali_openid
			break;
		case 'wxpay_app-plus':
			uniPayInstance = uniPay.initWeixin(wxConfigApp)
			tradeType = 'APP'
			break;
		case 'alipay_app-plus':
			uniPayInstance = uniPay.initAlipay(aliConfigApp)
			break;
		case 'wxpay_h5':
			uniPayInstance = uniPay.initWeixin(wxConfigH5)
			tradeType = 'NATIVE'
			break;
		case 'alipay_h5':
			uniPayInstance = uniPay.initAlipay(aliConfigH5)
			// uniPayInstance = uniPay.initAlipay(aliConfigSandbox)
			tradeType = 'NATIVE'
			break;
		default:
			return {
				code: -1,
				msg: '参数错误'
			}
	}
	//获取订单金额
	const {
		totalFee,
	} = orderList.data[0]
	//支付提交    
	let orderInfo

	try {
		// 获取支付信息
		orderInfo = await uniPayInstance.getOrderInfo({
			openid, // App端支付时不需要openid
			order_id,
			totalFee,
			subject: "订单标题",
			body: "奈雪的茶订单详情",
			notifyUrl,
			tradeType
		})
	} catch (e) {
		console.log(e.message)
		return {
			code: -3,
			msg: '获取支付信息失败，请稍后再试'
		}
	}

	//返回orderInfo
	return {
		order_id,
		orderInfo
	}
};
