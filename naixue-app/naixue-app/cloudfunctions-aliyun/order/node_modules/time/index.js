function getOffsetDate (offset) {
  return new Date(
    Date.now() + (new Date().getTimezoneOffset() + (offset || 0) * 60) * 60000
  )
}
function getNowFormatDate(e) { // 获取时间格式“yyyy-MM-dd HH:MM:SS”
    var date = e;
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
	var strHours = date.getHours();
	var strMinutes = date.getMinutes();
	var strSeconds = date.getSeconds();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
	if (strHours >= 0 && strHours <= 9) {
	    strHours = "0" + strHours;
	}
	if (strMinutes >= 0 && strMinutes <= 9) {
	    strMinutes = "0" + strMinutes;
	}
	if (strSeconds >= 0 && strSeconds <= 9) {
	    strSeconds = "0" + strSeconds;
	}
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + strHours + seperator2 + strMinutes
            + seperator2 + strSeconds;
    return currentdate;
}

module.exports = {
	getOffsetDate,
	getNowFormatDate
}
