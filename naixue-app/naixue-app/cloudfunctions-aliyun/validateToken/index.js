'use strict';
const jwt = require('jwt-simple')
const db = uniCloud.database()
exports.main = async (event, context) => {
	const token = event.token
	//解码token, 得到openId
	const openId = JSON.parse(Buffer.from(token.split('.')[1], 'base64').toString())
	//验证数 据库有无此openId用户
	const userInDB = await db.collection('users').where({
		openId: openId
	}).get()
	if (userInDB.data.length !== 1) {
		return {
			status: -1,
			errCode: 'TOKEN_INVALID',
			msg: '查无此人'
		}
	}
	//解码token,并且通过验证exp时间判断token是否过期
	const userInfoDB = userInDB.data[0]
	let userInfoDecode
	userInfoDecode = jwt.decode(token, userInfoDB.tokenSecret)
	if (userInfoDB.exp > Date.now() && userInfoDecode == openId) {
		return {
			status: 0,
			openId: userInfoDecode,
			msg: 'token验证成功'
		}
	}
	//如果时间超过token下发时限则判断为失效
	if (userInfoDB.exp < Date.now()) {
		return {
			status: -3,
			errCode: 'TOKEN_EXPIRED',
			msg: 'token已失效'
		}
	}
};
