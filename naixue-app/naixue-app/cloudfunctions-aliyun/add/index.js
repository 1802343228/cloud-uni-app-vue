'use strict';
//声明数据库
const db = uniCloud.database()
//event为客户端上传的参数
exports.main = async (event, context) => {
const collection = db.collection('message')
const res = await collection.add(event)
return res
};