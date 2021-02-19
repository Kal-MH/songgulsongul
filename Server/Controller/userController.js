/*
 * 경로에 따라서 실행될 함수들
 * 명명규칙 : 분류 + 기능 + restful방식
 */

 var connection = require("../db/db");
 var apiController = require("./apiController");

 var userController = {
     // 프로필
     userProfilePost : function (req, res) {
       const id = req.body.id;

       var param = [id];
       var follower_list = [];
       var follow_target_list = [];
       var post_info = [];

       var sql1 = 'SELECT login_id FROM user JOIN follow ON follow.follower_id = user.id WHERE follow.follow_target_id = ?'; // 나를 팔로우 하는 목록
       var sql2 = 'SELECT login_id FROM user JOIN follow ON follow.follow_target_id = user.id WHERE follow.follower_id = ?'; // 내가 팔로우 하는 목록
       var sql3 = 'SELECT * FROM user JOIN post ON user.id = post.user_id WHERE user.login_id = ?'; // 게시글목록

         connection.query(sql1 + sql2 + sql3, param, function(err, rows){
           var resultCode = 404;

           if (err) {
             console.log(err);
           }
           else{
             resultCode = 200;
             for(let i = 0; i < rows[0].length(); i++){
               follower_list.push(rows[0][i]);
             }

             for(let i = 0; i < rows[1].length(); i++){
               follow_target_list.push(rows[1][i]);
             }

             for(let i = 0; i < rows[2].length(); i++){
               var pdata = {
                 'image': rows[2][i].image,
                 'text' : rows[2][i].text,
                 'post_time' : rows[2][i].post_time,
                 'post_date' : rows[2][i].post_date
               };
               post_info.push(pdata);
             }
           }

           res.json({
             'code': resultCode,
             'follower': follower_list,
             'follow_target': follow_target_list,
             'postinfo': post_info
           });
         });
     },

     // 보관함
     profileKeep : function(req, res){
       var id = req.query.id;
       var param = [id];
       var keep_info = [];

       var sql = 'SELECT * FROM post JOIN keep ON post.id = keep.post_id WHERE keep.user_id = (SELECT id FROM user WHERE user_id = ?)'; // 보관함목록
       connection.query(sql, param, function(err, rows){
         var resultCode = 404;

         if(err){
           console.log(err);
         }
         else{
           resultCode = 200;

           for(let i = 0; i < rows.length(); i++){
             var kdata = {
               'image' : rows[i].image,
               'text' : rows[i].text,
               'post_time' : rows[i].post_time,
               'post_date' : rows[i].user_id
             };
             keep_info.push(kdata);
           }
         }

         res.json({
           'code' : resultCode,
           'keepinfo' : keep_info
         });
       });
     },

     // 프로필수정 - 아이디 중복확인
     profileEditIdCheck : function(req, res){
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
         var message = 'ERROR';

         if(err){
           console.log(err);
         }
         else{
           resultCode = 200;
           message = '정상적으로 수정 완료';

           // 기존 아이디와 비교 후 db갱신
           if(rows[0].login_id !== new_id){
             sql = 'UPDATE user SET login_id = ? WHERE login_id = ?';
             connection.query(sql, [new_id, id], function(err, rows){
               if(err){
                 resultCode = 400;
                 message = 'ERROR';
               }
             });
           }

           // 기존 소개글과 비교 후 db갱신
           if(rows[0].intro !== new_intro){
             sql = 'UPDATE user SET intro = ? WHERE login_id = ?';
             connection.query(sql, [new_intro, id], function(err, rows){
               if(err){
                 resultCode = 400;
                 message = 'ERROR';
               }
             });
           }

           // 기존 SNS계정과 비교 후 db갱신
           if(rows[0].sns !== new_sns){
             sql = 'UPDATE user SET sns = ? WHERE login_id = ?';
             connection.query(sql, [new_sns, id], function(err, rows){
               if(err){
                 resultCode = 400;
                 message = 'ERROR';
               }
             });
           }
         }

         res.json({
           'code' : resultCode,
           'message' : message
         });
       });
     }
 }

 module.exports = userController;
