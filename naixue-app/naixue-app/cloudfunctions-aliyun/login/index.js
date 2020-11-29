'use strict';
const crypto = require('crypto');
const jwt = require('jwt-simple');
const db = uniCloud.database();
const loginConfig = {
	appId:'wxedc274aca2fb67b1',
	appSecret:'2b1914a3ba4e98bc5ac941010e55e126'
}
exports.main = async (event, context) => {
	//event为客户端上传的参数
		console.log(event.userInfo);
		console.log(event.code);
		let data = {
			appid: loginConfig.appId,
			secret: loginConfig.appSecret,
			js_code: event.code,
			grant_type: 'authorization_code'
		};
		const res = await uniCloud.httpclient.request('https://api.weixin.qq.com/sns/jscode2session', {
			method: 'GET',
			data,
			dataType: 'json'
		});
		console.log(res.status);
		console.log(res.data);
		console.log(res.data.openid);
		const success = res.status === 200 && res.data && res.data.openid;
	
		if (!success) {
			return {
				status: -1,
				msg: '微信登录失败'
			}
		}
	
		const openId = res.data.openid
		const tonkenExp = 7200000;
	
		let time = new Date().toUTCString();
		let tokenSecret = crypto.randomBytes(16).toString('hex'),
			token = jwt.encode(openId, tokenSecret);
		let userInfo = {
			openId: res.data.openid,
			nickName: event.userInfo.nickName,
			avatarUrl: event.userInfo.avatarUrl,
			gender: event.userInfo.gender,
			city: event.userInfo.city,
			balance: '',
			couponNum: '',
			currentValue: '',
			giftBalance: '',
			level: '',
			pointNum: '',
			createTime: time,
			lastLoginTime: time,
			needValue: ''
		}
	
		let userUpdate = {
			openId: res.data.openid,
			nickName: event.userInfo.nickName,
			avatarUrl: event.userInfo.avatarUrl,
			gender: event.userInfo.gender,
			city: event.userInfo.city,
			lastLoginTime: time
		}
		let userResult
	
		const userInDB = await db.collection('users').where({
			openId
		}).limit(1).get();
		if (userInDB.data && userInDB.data.length === 0) {
			userResult = await db.collection('users').add({
				...userInfo,
				tokenSecret,
				exp: Date.now() + tonkenExp
			});
		} else {
			userResult = await db.collection('users').where({
				openId
			}).update({
				...userUpdate,
				tokenSecret,
				exp: Date.now() + tonkenExp
			});
		}
		const fields = {
			nickName: 1,
			avatarUrl: 1,
			balance: 1,
			couponNum: 1,
			currentValue: 1,
			giftBalance: 1,
			level: 1,
			pointNum: 1,
			gender: 1,
			needValue: 1
		}
	
	
		let ResultOK = await db.collection('users').where({
			openId
		}).field(fields).get();
		let ResultData = ResultOK.data[0];
		if (userResult.id || userResult.updated === 1) {
			return {
				"status": 0,
				"data": ResultData,
				"token": token,
				"msg": '登录成功'
			}
		}
	
		//返回数据给客户端
		return {
			"status": -1,
			"msg": '微信登录失败'
		}
};
