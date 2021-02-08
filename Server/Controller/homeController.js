/*
 * 경로에 따라서 실행될 함수들
 * 명명규칙 : 분류 + 기능 + restful방식
 */

const connection = require("../db/db");
const smtpTransport = require("../config/email");

 var homeController = {
     //회원가입
     homeJoinPost : function (req, res) {
        var email = req.body.email;
        var password = req.body.password;
        var login_id = req.body.loginId;

        // 삽입을 수행하는 sql문.
        var sql = 'INSERT INTO user (email, login_id, password) VALUES (?, ?, ?)';
        var params = [email, login_id, password];
        connection.query(sql, params, function (err, result) {
            var resultCode = 404;
            var message = '에러가 발생했습니다';
    
            if (err) {
                console.log(err);
            } else {
                resultCode = 200;
                message = '회원가입에 성공했습니다.';
            }
    
            res.json({
                'code': resultCode,
                'message': message
            });
        });
    },
    //로그인
    homeLoginPost :  function (req, res) {
        var login_id = req.body.login_id;
        var password = req.body.password;
        var sql = 'select * from user where login_id = ?';

        console.log(login_id, password);
    
        var params = [login_id];
        connection.query(sql, params, function(err, result) {
          var resultCode = 404;
          var message = '에러가 발생했습니다.';
    
          if(err){
            console.log(err);
          }else{
            if(result.length === 0){
              resultCode = 204;
              message = '존재하지 않는 계정입니다!';
            }
            else if(password !== result[0].password) {
              resultCode = 204;
              message = '비밀번호가 틀렸습니다!';
            }
            else{
              resultCode = 200;
              message = '로그인 성공! ' + result[0].login_id + '님 환영합니다!';
            }
          }
    
          res.json({
            'code': resultCode,
            'message': message
          });
        });
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
    }  
 }

 module.exports = homeController;