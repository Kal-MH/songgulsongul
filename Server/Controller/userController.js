/*
 * 경로에 따라서 실행될 함수들
 * 명명규칙 : 분류 + 기능 + restful방식
 */

 var connection = require("../db/db");
 var apiController = require("./apiController");

 var userController = {
     // 프로필
     userProfilePost : function (req, res) {
       var status = req.body.status;
       const id = req.body.id;

       var param = [id];
       var follower_list[];
       var followed_lis[];

       // my profile
       if(status === 1){

         var sql1 = 'SELECT login_id FROM user as u JOIN (SELECT follow.follower_id FROM user join follow ON user.id = follow.followed_id WHERE user.login_id = ?) as f where u.id = f.follower_id'; // 나를 팔로우하는 목록
         var sql2 = 'SELECT login_id FROM user as u JOIN (SELECT follow.followed_id FROM user join follow ON user.id = follow.follower_id WHERE user.login_id = ?) as f where u.id = f.followed_id'; // 내가 팔로우 하는 목록
         var sql3 = 'SELECT * FROM user JOIN post ON user.id = post.user_id WHERE user.login_id = ?'; // 게시글목록
         var sql4 = 'SELECT * FROM user JOIN keep ON user.id = keep.user_id WHERE user.login_id = ?'; // 보관함 목록

         connection.query(sql1 + sql2 + sql3 + sql4, param, function(err, rows){
           var resultCode = 404;
           var message = '에러가 발생했습니다.';

           if (err) {
             console.log(err);
           }
           else{
             resultCode = 200;
             for(var i = 0; i < rows[0].length(); i++){
               follower_list.push(rows[0][i]);
             }
             for(var i = 0; i < rows[1].length(); i++){
               followed_list.push(rows[1][i]);
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
     },

     // 프로필수정 - 아이디 중복확인
     profileIdCheck : function(req, res){
       apiController.dupIdCheck(req, res);
     },

     // 프로필수정
     profileEdit : function(req, res) {
       var id = req.query.id; // 기존 아이디
       var new_id = req.query.newId; // 변경된 아이디
       var new_intro = req.query.newIntro;
       var new_sns = req.query.newSNS;
       var param = [id];

       var sql = 'SELECT * FROM user WHERE login_id = ?';
       connection.query(sql, param, function(err, rows){
         var resultCode = 404;

         if(err){
           console.log(err);
         }
         else{
           resultCode = 200;

           // 기존 아이디와 비교 후 db갱신
           if(rows[0].login_id !== new_id){
             sql = 'UPDATE user SET login_id = ? WHERE login_id = ?';
             connection.query(sql, [new_id, id], function(err, rows){
             });
           }

           // 기존 소개글과 비교 후 db갱신

           // 기존 SNS계정과 비교 후 db갱신

         }
       });

     }
 }

 module.exports = userController;
