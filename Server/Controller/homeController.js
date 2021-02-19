/*
 * 경로에 따라서 실행될 함수들
 * 명명규칙 : 분류 + 기능 + restful방식
 */

const connection = require("../db/db");
const smtpTransport = require("../config/email");
const crypto = require('crypto');

 var homeController = {
     //회원가입
     homeJoinPost : function (req, res) {
        var email = req.body.email;
        var password = req.body.password;
        var login_id = req.body.loginId;
        var sns = req.body.snsAddress;
        var img_profile = req.file.path;

        console.log(req.body)
        //비밀번호 암호화
        crypto.randomBytes(64, function (err, buf) {
          crypto.pbkdf2(password, buf.toString('base64'), 100, 64, 'sha512', function (err, key) {
            var hashed_password = key.toString('base64');
            var salt = buf.toString('base64');
            // 삽입을 수행하는 sql문.
            var sql = 'INSERT INTO user (email, login_id, password, salt, sns, img_profile) VALUES (?, ?, ?, ?, ?, ?)';
            var params = [email, login_id, hashed_password, salt, sns, img_profile];
            
            connection.query(sql, params, function (err, result) {
                var resultCode = 404;
                var message = '에러가 발생했습니다';
        
                if (err) {
                    console.log(err);
                } else {
                    resultCode = 200;
                    message = '회원가입에 성공했습니다.';
                }
                console.log(result);
        
                res.json({
                    'code': resultCode,
                    'message': message
                });
            });
          })
        })
    },
    //로그인
    homeLoginPost :  function (req, res) {
      var resultCode = 204;
      var message = '에러가 발생했습니다.';
  
      if (req.user){
          resultCode = 200;
          message = message = '로그인 성공! ' + req.user.login_id + '님 환영합니다!';
      }

      res.json({
          'code' : resultCode,
          'message' : message
      })
        // var login_id = req.body.login_id;
        // var password = req.body.password;
        // var sql = 'select * from user where login_id = ?';

        // console.log(login_id, password);
    
        // var params = [login_id];
        // connection.query(sql, params, function(err, result) {
        //   var resultCode = 404;
        //   var message = '에러가 발생했습니다.';
    
        //   if(err){
        //     console.log(err);
        //   }else{
        //     if(result.length === 0){
        //       resultCode = 204;
        //       message = '존재하지 않는 계정입니다!';
        //     }
        //     else if(password !== result[0].password) {
        //       resultCode = 204;
        //       message = '비밀번호가 틀렸습니다!';
        //     }
        //     else{
        //       resultCode = 200;
        //       message = '로그인 성공! ' + result[0].login_id + '님 환영합니다!';
        //     }
        //   }
    
        //   res.json({
        //     'code': resultCode,
        //     'message': message
        //   });
        // });
    },
    //아이디찾기
    findId : function (req, res) {
      const email = req.body.email;
      console.log(email);

      
      var sql = "select * from user where email = ?";
      connection.query(sql, email, async function (err, result) {
        if (err){
          res.json({
            'code' : 400,
            'message' : "error"
          })
        } else if (result.length === 0){
          res.json({
            'code' : 204,
            'message' : "no exists"
          })
        } else {
          const mailOptions = {
            from:"paper.pen.smu@gmail.com",
            to : email,
            subject : "아이디 찾기.",
            text : "아이디 : " + result[0].login_id
          }

          await smtpTransport.sendMail(mailOptions, function (err, response) {
            if (err){
              console.log(err);
              resultCode = 400;
            } else {
              resultCode = 200;
              message = "success"
            }
            res.json({
              'code' : resultCode,
              'message' : message
            })
            smtpTransport.close();
          })
        }
      })
    },
    //비밀번호 찾기
    findPassword : function (req, res) {
      const email = req.body.email;
      const login_id = req.body.login_id;
      
      var sql = "select * from user where email = ? and login_id = ?";
      var params = [email, login_id];
      connection.query(sql, params, function (err, result) {
        if (err){
          res.json({
            'code' : 404,
            'message' : "error"
          })
        } else if (result.length === 0){
          res.json({
            'code' : 204,
            'message' : "no exists"
          })
        } else {
          //임시 비밀번호 생성
          var tmpPassword = Math.random().toString(20).slice(2);
          //비밀번호 암호화
          crypto.randomBytes(64, function (err, buf) {
            crypto.pbkdf2(tmpPassword, buf.toString('base64'), 100, 64, 'sha512', function (err, key) {
              var hashed_password = key.toString('base64');
              var salt = buf.toString('base64');
              // 삽입을 수행하는 sql문.
              var passwordSql = "update user set password = ?, salt = ? where email = ? and login_id = ?"
              var passwordParams = [hashed_password, salt, email, login_id];
              
              connection.query(passwordSql, passwordParams, async function (err, result) {
                if (err){
                  res.json({
                    'code' : 404,
                    'message' : "error"
                  })
                } else {
                  const mailOptions = {
                    from:"paper.pen.smu@gmail.com",
                    to : email,
                    subject : "임시비밀번호 발급.",
                    text : "임시비밀번호 : " + tmpPassword
                  }
        
                  await smtpTransport.sendMail(mailOptions, function (err, response) {
                    if (err){
                      console.log(err);
                      resultCode = 400;
                    } else {
                      resultCode = 200;
                      message = "success"
                    }
                    res.json({
                      'code' : resultCode,
                      'message' : message
                    })
                    smtpTransport.close();
                  })
                }
              })
              
            })
          })
        }
      })
    },
    //임시 컨트롤러 - 유저 프로필 보기(img업로드 확인을 위해)
    tmpgetMeProfile : function (req, res) {
      const me = req.user;

      if (!me){
        res.redirect("/public/login.html");
      } else {
        res.render("profile.ejs", {user : me});
      }

    },
    logout : function (req, res) {
      req.logout();
      res.redirect("/public/login.html");
    }
 }

 module.exports = homeController;