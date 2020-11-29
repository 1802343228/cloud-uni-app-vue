'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	if (event.action == 'getList') {
		const openId = event.openId
		const res = await db.collection('addresses').where({
			openId
		}).get()
		const resData = res.data
		return {
			status: 0,
			data: resData
		}
	} else if (event.action == 'deleteAddress') {
		const _id = event.id
		const res = await db.collection('addresses').where({
			_id
		}).remove()

		if (res.deleted === 1) {
			return {
				status: 0,
				msg: '删除成功'
			}
		} else {
			return {
				status: -1,
				msg: '删除失败'
			}
		}
	} else if (event.action == 'addAddress') {
		const data = event.data
		const street = event.data.street
		const addressDB = await db.collection('addresses').where({
			street
		}).get()
		if (addressDB.data.length === 0) {
			const res = await db.collection('addresses').add(data)
			return {
				status: 0,
				msg: '添加成功'
			}
		}
		return {
			status: -1,
			msg: '地址已存在'
		}
	} else if (event.action == 'editAddress') {
		let _id = event.data._id
		let fields = {
			accept_name: event.data.accept_name,
			gender: event.data.gender,
			mobile: event.data.mobile,
			area: event.data.area,
			street: event.data.street
		}
		const res = await db.collection('addresses').where({
			_id
		}).update({
			...fields
		})
		if (res.updated === 1) {
			return {
				status: 0,
				msg: '数据修改成功'
			}
		} else if (res.updated === 0) {
			return {
				status: -1,
				msg: '地址未改变'
			}
		}
	} else if (event.action == 'getOne') {
		const _id = event.id
		const res = await db.collection('addresses').where({
			_id
		}).limit(1).get()
		const resData = res.data
		return {
			status: 0,
			data: resData
		}
	}
};
