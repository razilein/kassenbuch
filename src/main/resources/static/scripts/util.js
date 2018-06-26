function getDateAsString() {
  var today = new Date();

  var day = today.getDate();
  day = day < 10 ? '0' + day : day;

  var month = today.getMonth() + 1;
  month = month < 10 ? '0' + month : month;
  return today.getFullYear() + '-' + month + '-' + day;
}

function hasMessages(result) {
  return result && (result.success || result.info || result.warning || result.error);
}
