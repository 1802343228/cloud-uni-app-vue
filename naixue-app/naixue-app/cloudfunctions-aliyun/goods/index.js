'use strict';
const db = uniCloud.database()
exports.main = async (event, context) => {
	const res = await db.collection('category').aggregate().lookup({
		from:'goods',
		localField:'goods_type',
		foreignField:'goods+type',
		as:'goods_list'
	}).end()
	return res
};
