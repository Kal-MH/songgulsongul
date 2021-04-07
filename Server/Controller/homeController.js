/*
 * 경로에 따라서 실행될 함수들
 * 명명규칙 : 분류 + 기능 + restful방식
 */

const connection = require("../db/db");
const smtpTransport = require("../config/email");
const crypto = require('crypto');
const statusCode = require("../config/serverStatusCode");
const serverConfig = require("../config/serverConfig");

 var homeController = {
     //회원가입
     //안드로이드에서 항목체크를 함으로, 서버에서 따로 데이터 유무 체크 핸들링 부분은 추가하지 않았다.
     homeJoinPost : function (req, res) {
        var email = req.body.email;
        var password = req.body.password;
        var loginId = req.body.login_id;
        var snsUrl = req.body.sns_url;
        var imgProfile = serverConfig.defaultUserProfile;

        
        //비밀번호 암호화
        crypto.randomBytes(64, function (err, buf) {
          crypto.pbkdf2(password, buf.toString('base64'), 100, 64, 'sha512', function (err, key) {
            var hashedPassword = key.toString('base64');
            var salt = buf.toString('base64');
            // 삽입을 수행하는 sql문.
            var sql = 'INSERT INTO user (email, login_id, password, salt, sns_url, img_profile, point) VALUES (?, ?, ?, ?, ?, ?, 1000)';
            var params = [email, loginId, hashedPassword, salt, snsUrl, imgProfile];
            
            connection.query(sql, params, function (err, result) {
                var resultCode = statusCode.CLIENT_ERROR;
             
                if (err) {
                    console.log(err);
                } else {
                    resultCode = statusCode.OK; //ok
                }
                console.log(result);
        
                res.json({
                    'code': resultCode,
                });
            });
          })
        })
    },
    //로그인
    homeLoginPost :  function (req, res) {
      var resultCode = statusCode.CLIENT_ERROR;
      
      if (req.user){
          resultCode = statusCode.OK;
      }

      res.json({
          'code' : resultCode,
      })
    },
    //아이디찾기
    findId : function (req, res) {
      const email = req.body.email;
      console.log(email);

      
      var sql = "select * from user where email = ?";
      connection.query(sql, email, async function (err, result) {
        if (err){
          res.json({
            'code' : statusCode.SERVER_ERROR,
          })
        } else if (result.length === 0){
          res.json({
            'code' : statusCode.CLIENT_ERROR,
          })
        } else {
          var loginIds = "";
          for(var i = 0;i < result.length; i++)
            loginIds += "아이디 : " + result[i].login_id + "\n";
          const mailOptions = {
            from:"paper.pen.smu@gmail.com",
            to : email,
            subject : "아이디 찾기.",
            text : loginIds
          }

          await smtpTransport.sendMail(mailOptions, function (err, response) {
            var resultCode;
            if (err){
              console.log(err);
              resultCode = statusCode.SERVER_ERROR;
            } else {
              resultCode = statusCode.OK;
            }
            res.json({
              'code' : resultCode,
            })
            smtpTransport.close();
          })
        }
      })
    },
    //비밀번호 찾기
    findPassword : function (req, res) {
      const email = req.body.email;
      const loginId = req.body.login_id;
      
      var sql = "select * from user where email = ? and login_id = ?";
      var params = [email, loginId];
      connection.query(sql, params, function (err, result) {
        if (err){
          res.json({
            'code' : statusCode.SERVER_ERROR,
          })
        } else if (result.length === 0){
          res.json({
            'code' : statusCode.CLIENT_ERROR,
          })
        } else {
          //임시 비밀번호 생성
          var tmpPassword = Math.random().toString(20).slice(2);
          //비밀번호 암호화
          crypto.randomBytes(64, function (err, buf) {
            crypto.pbkdf2(tmpPassword, buf.toString('base64'), 100, 64, 'sha512', function (err, key) {
              var hashedPassword = key.toString('base64');
              var salt = buf.toString('base64');
              // 삽입을 수행하는 sql문.
              var passwordSql = "update user set password = ?, salt = ? where email = ? and login_id = ?"
              var passwordParams = [hashedPassword, salt, email, loginId];
              
              connection.query(passwordSql, passwordParams, async function (err, result) {
                if (err){
                  res.json({
                    'code' : statusCode.SERVER_ERROR,
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
                      resultCode = statusCode.SERVER_ERROR;
                    } else {
                      resultCode = statusCode.OK;
                    }
                    res.json({
                      'code' : resultCode,
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