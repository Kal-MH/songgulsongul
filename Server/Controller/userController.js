/*
 * 경로에 따라서 실행될 함수들
 * 명명규칙 : 분류 + 기능 + restful방식
 */

 var connection = require("../db/db");
 var apiController = require("./apiController");
 var statusCode = require("../config/serverStatusCode");

 var userController = {
     // 프로필
     userProfilePost : function (req, res) {
       const id = req.body.id;
       const status = req.body.status;

       var params = [id, id, id, id];
       var follower_cnt;
       var follow_cnt;
       var post_info = [];
       var profile_info = [];

       var sql1 = 'SELECT COUNT(*) AS cnt FROM user JOIN follow ON follow.follower_id = user.id WHERE user.login_id = ?;'; // 팔로우 수
       var sql2 = 'SELECT COUNT(*) AS cnt FROM user JOIN follow ON follow.follow_target_id = user.id WHERE user.login_id = ?;'; // 팔로워 수
       var sql3 = 'SELECT post.id, post.image FROM user JOIN post ON user.id = post.user_id WHERE user.login_id = ?;'; // 게시글목록
       var sql4 = 'SELECT * FROM user WHERE login_id = ?;'; // 프로필 데이터(포인트, 소개글, sns 주소)
       var sql5 = 'SELECT COUNT(*) AS flag FROM user JOIN follow ON follow.follower_id = user.id WHERE user.login_id = ? AND follow_target_id IN (SELECT id FROM user WHERE login_id = ?);'
       var sql = sql1 + sql2 + sql3 + sql4;

       if(status !== 1){ // 선택한 사용자의 프로필일 경우
         const user_id = req.body.user_id;
         console.log(id + " " + user_id);

         sql += sql5;
         params = [];
         params.push(user_id, user_id, user_id, user_id, id, user_id);
       }

         connection.query(sql, params, function(err, results){
           var resultCode = statusCode.CLIENT_ERROR;
           console.log(id)

           if (err) {
             console.log(err);
             res.json({
               'code': resultCode
             })
           }
           else{
             resultCode = statusCode.OK;
             follow_cnt = results[0][0].cnt;
             follower_cnt = results[1][0].cnt;

             for(let i = 0; i < results[2].length; i++){
               var pdata = {
                 'image': results[2][i].image,
                 'postId': results[2][i].id,
               };
               post_info.push(pdata);
             }

             var prodata = {
               'profile_image': results[3][0].img_profile,
               'intro': results[3][0].intro,
               'sns': results[3][0].sns_url,
               'userId': results[3][0].login_id
             }

             if(status === 1){ // 로그인한 사용자의 프로필일 경우
               prodata.point = results[3][0].point;
             }
             else{
               prodata.flag = results[4][0].flag;
             }

             profile_info.push(prodata);
             console.log(profile_info);
             res.json({
               'code': resultCode,
               'followerCnt': follower_cnt,
               'followCnt': follow_cnt,
               'postInfo': post_info,
               'profileInfo': profile_info
             })
           }
           console.log(resultCode);
         })
     },

     // 팔로우
     userFollowPost: function(req,res){
       const login_id = req.body.loginId;
       const user_id = req.body.userId;

       var params = [login_id, user_id];
       var sql1 = 'SELECT id FROM user WHERE login_id = ?;';
       var sql2 = 'SELECT id FROM user WHERE login_id = ?;';
       connection.query(sql1 + sql2, params, function(err, rows){
         var resultCode = statusCode.CLIENT_ERROR;

         if(err){
           console.log(err);
           res.json({
             'code': resultCode
           })
         }
         else{
           resultCode = statusCode.OK;
           var sql = 'INSERT INTO follow VALUES(?,?);';
           connection.query(sql, [rows[0][0].id, rows[1][0].id], function(err, rows){
             if(err){
               console.log(err);
               resultCode = statusCode.CLIENT_ERROR;
               res.json({
                 'code': resultCode
               })
             }
           })
           res.json({
             'code': resultCode
           })
         }
         console.log(resultCode);
       })
     },

     // 언팔로우
     userUnfollowPost : function(req,res){
       const login_id = req.body.loginId;
       const user_id = req.body.userId;

       var params = [login_id, user_id];
       var sql1 = 'SELECT id FROM user WHERE login_id = ?;';
       var sql2 = 'SELECT id FROM user WHERE login_id = ?;';

       connection.query(sql1 + sql2, params, function(err, rows){
         var resultCode = statusCode.SERVER_ERROR;

         if(err){
           console.log(err);
           res.json({
             'code': resultCode
           })
         }
         else{
           resultCode = statusCode.OK;
           var sql = 'DELETE FROM follow WHERE follower_id = ? AND follow_target_id = ?;';
           connection.query(sql, [rows[0][0].id, rows[1][0].id], function(err, rows){
             if(err){
               console.log(err);
               resultCode = statusCode.CLIENT_ERROR;
               res.json({
                 'code': resultCode
               })
             }
           })
           res.json({
             'code' : resultCode
           })
         }
         console.log(resultCode);
       })
     },

     // 로그인한 사용자의 팔로우 리스트 얻을 경우 --> userlFollowList
     // 선택한 사용자의 팔로우 리스트 얻을 경우 --> userFollowList

     // 로그인한 사용자의 팔로우 리스트
     userLFollowList: function(req, res){
       const id = req.body.id;
       var param = [id];
       var follow_info = [];

       var sql = 'SELECT * FROM user JOIN follow ON follow.follow_target_id = user.id WHERE follow.follower_id = (SELECT id FROM user WHERE login_id = ?);'; // 로그인한 사용자의 팔로우 리스트
       connection.query(sql, param, function(err, rows){
         var resultCode = statusCode.CLIENT_ERROR;

         if(err){
           console.log(err);
           res.json({
             'code': resultCode
           })
         }
         else{
           resultCode = statusCode.OK;

           for(let i = 0; i < rows.length; i++){
             var followInfo = {
               'image': rows[i].img_profile,
               'userId': rows[i].login_id
             };
              console.log(rows[i].login_id);
             follow_info.push(followInfo);
           }
           console.log(follow_info);

           res.json({
             'code': resultCode,
             'followinfo': follow_info
           })
         }
         console.log(resultCode);
       })
     },

     // 팔로우 리스트
     userFollowList : function(req, res){
       const id = req.body.id;
       const user_id = req.body.user_id;
       var params = [id, user_id];
       var login_follow_info = [];
       var user_follow_info = [];

       var sql = 'SELECT * FROM user JOIN follow ON follow.follow_target_id = user.id WHERE follow.follower_id = (SELECT id FROM user WHERE login_id = ?);'; // 선택한 사용자의 팔로우 리스트
       sql += sql;
       connection.query(sql, params, function(err, rows){
         var resultCode = statusCode.CLIENT_ERROR;

         if(err){
           console.log(err);
           res.json({
             'code': resultCode
           })
         }
         else{
           resultCode = statusCode.OK;

           for(let i = 0; i < rows[0].length; i++){
             var loginFollowInfo = {
               'userId': rows[0][i].login_id
             };
             login_follow_info.push(loginFollowInfo);
           }

           for(let i = 0; i < rows[1].length; i++){
             var userFollowInfo = {
               'image': rows[1][i].img_profile,
               'userId': rows[1][i].login_id
             };
             user_follow_info.push(userFollowInfo);
           }

           console.log(login_follow_info);
           console.log(user_follow_info);

           res.json({
             'code': resultCode,
             'loginFollowInfo': login_follow_info,
             'userFollowInfo': user_follow_info
           })
         }
         console.log(resultCode);
       })
     },

     // 팔로워 리스트
     userFollowerList : function(req, res){
       const id = req.body.id;
       const status = req.body.status;
       var params = [id, id];
       var follower_info = [];
       var following_info = [];

       var sql = 'SELECT * FROM user JOIN follow ON follow.follower_id = user.id WHERE follow.follow_target_id = (SELECT id FROM user WHERE login_id = ?);'; // 사용자의 팔로워 리스트
       var sql2 = 'SELECT * FROM user JOIN follow ON follow.follow_target_id = user.id WHERE follow.follower_id = (SELECT id FROM user WHERE login_id = ?);'
       if(status !== 1){
         params = [];
         params.push(req.body.user_id, id);
       }
       connection.query(sql+sql2, params, function(err, rows){
         var resultCode = statusCode.CLIENT_ERROR;

         if(err){
           console.log(err);
           res.json({
             'code': resultCode
           })
         }
         else{
           resultCode = statusCode.OK;

           for(let i = 0; i < rows[0].length; i++){
             var followerInfo = {
               'image': rows[0][i].img_profile,
               'userId': rows[0][i].login_id
             };
             follower_info.push(followerInfo);
           }

           for(let i = 0; i < rows[1].length; i++){
             var followingInfo = {
               'userId': rows[1][i].login_id
             };
             following_info.push(followingInfo);
           }

           console.log(follower_info);
           console.log(following_info);
           res.json({
             'code': resultCode,
             'followerInfo': follower_info,
             'followingInfo': following_info
           })
         }
         console.log(statusCode);
       })
     },

     // 보관함
     profileKeep : function(req, res){
       const id = req.body.id;
       var param = [id];
       var keep_info = [];
       var keep_cnt;

       var sql = 'SELECT * FROM post JOIN keep ON post.id = keep.post_id WHERE keep.user_id = (SELECT id FROM user WHERE user_id = ?)'; // 보관함목록
       connection.query(sql, param, function(err, rows){
         var resultCode = statusCode.CLIENT_ERROR;

         if(err){
           console.log(err);
           res.json({
             'code': resultCode
           })
         }
         else{
           resultCode = statusCode.OK;

           for(let i = 0; i < rows.length(); i++){
             var kdata = {
               'image' : rows[i].image,
               'postId': rows[i].id
             };
             keep_info.push(kdata);
           }
           keep_cnt = rows.length();

           res.json({
             'code' : resultCode,
             'keepinfo' : keep_info,
             'keepcnt': keep_cnt
           })
         }
       })
     },

     // 프로필수정 - 아이디 중복확인
     profileEditIdCheck : function(req, res){
       apiController.dupIdCheck(req, res);
     },

     // 프로필수정
     profileEdit : function(req, res) {
       const id = req.body.id; // 기존 아이디
       const new_id = req.body.newId; // 변경된 아이디
       const new_intro = req.body.newIntro;
       const new_sns = req.body.newSNS;
       const new_image = req.body.profileImage;
       var param = [id];

       var sql = 'SELECT * FROM user WHERE login_id = ?';
       connection.query(sql, param, function(err, rows){
         var resultCode = statusCode.CLIENT_ERROR;

         if(err){
           console.log(err);
           res.json({
             'code': resultCode
           })
         }
         else{
           resultCode = statusCode.OK;

           // 기존 아이디와 비교 후 db갱신
           if(rows[0].login_id !== new_id){
             sql = 'UPDATE user SET login_id = ? WHERE login_id = ?';
             connection.query(sql, [new_id, id], function(err, rows){
               if(err){
                 resultCode = statusCode.SERVER_ERROR;
                 res.json({
                   'code': resultCode
                 })
               }
             })
           }

           // 기존 소개글과 비교 후 db갱신
           if(rows[0].intro !== new_intro){
             sql = 'UPDATE user SET intro = ? WHERE login_id = ?';
             connection.query(sql, [new_intro, id], function(err, rows){
               if(err){
                 resultCode = statusCode.SERVER_ERROR;
                 res.json({
                   'code': resultCode
                 })
               }
             })
           }

           // 기존 SNS계정과 비교 후 db갱신
           if(rows[0].sns !== new_sns){
             sql = 'UPDATE user SET sns = ? WHERE login_id = ?';
             connection.query(sql, [new_sns, id], function(err, rows){
               if(err){
                 resultCode = statusCode.SERVER_ERROR;
                 res.json({
                   'code': resultCode
                 })
               }
             })
           }

           // 프로필 이미지 db갱신
           sql = 'UPDATE user SET img_profile = ? WHERE login_id = ?';
           connection.query(sql, [new_image, id], function(err, rows){
             if(err){
               resultCode = statusCode.SERVER_ERROR;
               res.json({
                 'code': resultCode
               })
             }
           })

           res.json({
             'code': resultCode
           })
         }
       })
     },

     // 회원 탈퇴
     userDataDelete : function(req, res) {
       const id = req.body.id;

       var param = [id];
       var sql = 'DELETE FROM user WHERE login_id = ?'

       connection.query(sql, param, function(err, rows){
         var resultCode = statusCode.SERVER_ERROR;

         if(err) {
           console.log(err);
           res.json({
             'code': resultCode
           })
         }
         else{
           resultCode = statusCode.OK;

           res.json({
             'code': resultCode
           })
         }
       })
     }
 }

 module.exports = userController;
