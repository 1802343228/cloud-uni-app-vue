'use strict';
const {
    wxConfigMp,
    wxConfigApp,
    wxConfigH5,
    aliConfigMp,
    aliConfigApp,
    aliConfigH5,
    // aliConfigSandbox
} = require('config')
const uniPay = require('unipay')
const db = uniCloud.database()
exports.main = async (event, context) => {
	let uniPayInstance
	    switch (event.path.substring(1)) {
	        case 'wxpay_mp-weixin':
	            uniPayInstance = uniPay.initWeixin(wxConfigMp)
	            break;
	        case 'wxpay_app-plus':
	            uniPayInstance = uniPay.initWeixin(wxConfigApp)
	            break;
	        case 'wxpay_h5':
	            uniPayInstance = uniPay.initWeixin(wxConfigH5)
	            break;
	        case 'alipay_mp-alipay':
	            uniPayInstance = uniPay.initAlipay(aliConfigMp)
	            break;
	        case 'alipay_app-plus':
	            uniPayInstance = uniPay.initAlipay(aliConfigApp)
	            break;
	        case 'alipay_h5':
	            uniPayInstance = uniPay.initAlipay(aliConfigH5)
	            // uniPayInstance = uniPay.initAlipay(aliConfigSandbox)
	            break;
	        default:
	            console.log('---------参数错误-------')
	            return {
	                code: -1,
	                msg: '参数错误'
	            }
	    }
		let verifyResult = await uniPayInstance.verifyPaymentNotify(event)
		    if (!verifyResult) {
		        console.log('---------!verifyResult-------')
		        return {}
		    }
			
		 let {
		        order_id,
		        totalFee,
		        transactionId,
		        resultCode
		    } = verifyResult	
			
		const orderList = await db.collection('orders').where({
		        order_id
		    }).get()
		
		    if (orderList.data.length < 0) {
		        console.log('---------orderList.data.length < 0-------')
		        return {}
		    }	
			
		if (totalFee === orderDetail.totalFee && (resultCode === 'SUCCESS' || resultCode === 'FINISHED')) {
		        console.log('---------updatedb-------')
		        await db.collection('orders').where({
		            order_id
		        }).update({
		            status: 2, //改变支付状态
		            transactionId
		        })
		    }	
		
		  console.log("verifyResult: " + JSON.stringify(verifyResult));
		    return {}	
};
