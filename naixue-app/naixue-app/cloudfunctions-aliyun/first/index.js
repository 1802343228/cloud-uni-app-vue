'use strict';
//声明数据库
const db = uniCloud.database()
//event为客户端上传的参数
exports.main = async (event, context) => {
// 获取表'user-info'的集合对象
const collection = db.collection('user-info')
// 获取表中的10条数据，结果为json格式
const res = await collection.limit(10).get()
console.log(res);
// 返回json给客户端
return res
};