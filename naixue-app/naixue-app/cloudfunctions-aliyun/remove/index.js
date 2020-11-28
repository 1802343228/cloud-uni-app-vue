'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const collection = db.collection('message')
	const res = await collection.where({
		_id: event._id
	}).remove()
	console.log(res);
	if (res.deleted === 1) {
		return {
			status: 0,
			msg: '成功删除留言板一条数据'
		}
	} else {
		return {
			status: -2,
			msg: '删除数据失败'
		}
	}
};
