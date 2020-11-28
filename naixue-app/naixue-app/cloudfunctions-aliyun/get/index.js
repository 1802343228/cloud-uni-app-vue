'use strict';
//声明数据库
const db = uniCloud.database()
exports.main = async (event, context) => {
const collection = db.collection('message')
const res = await collection.orderBy("_id","desc").get()
return res
};
