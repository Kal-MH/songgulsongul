// 명명규칙
// 컨트롤러_함수_테이블_mysql메소드

var sql = {
    post_upload_post_insert : "insert into post (image, text, post_time, post_date, user_id) values (?, ?, now(), now(), ?);",
    post_upload_item_insert : "insert into item_tag(post_id, name, lowprice, highprice, url, picture) values(?, ?, ?, ?, ?, ?);"

}

module.exports = sql;