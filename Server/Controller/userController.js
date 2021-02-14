/*
 * 경로에 따라서 실행될 함수들
 * 명명규칙 : 분류 + 기능 + restful방식
 */

 var connection = require("../db/db");

 var userController = {
     // 프로필
     userProfilePost : function (req, res) {
       var status = req.body.status;
       const id = req.body.id;

       var follower_list[];
       var followed_lis[];

       // my profile
       if(status === 1){

         var sql1 = 'SELECT * FROM user JOIN follow ON user.login_id = WHERE user.login_id = ?''; // 팔로우, 팔로워 목록
         var sql2 = 'SELECT * FROM user JOIN post ON user.id = post.user_id WHERE user.login_id = ?';// 게시글목록
         var sql3 = 'SELECT * FROM user JOIN keep ON';// 보관함 목록

         connection.query(sql1 + sql2 + sql3, params, function(err, rows){
           var resultCode = 404;
           var message = '에러가 발생했습니다.';

           if (err) {
             console.log(err);
           }
           else{
             resultCode = 200;
             for(var i = 0; i < rows[1].length(); i++){
               follower_list.push(rows[1][i].follower_id);
               followed_list.push(rows[1][i].followed_id);
             }
           }

           res.json({
             'code': resultCode,
             'follower': follower_list,
             'followed': followed_list,
             'postinfo': rows[0],
             'keepinfo': rows[2]
           });
         });
       }
     }
 }

 module.exports = userController;
