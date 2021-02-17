var LocalStrategy = require('passport-local').Strategy;
var crypto = require('crypto');

module.exports = new LocalStrategy({
    usernameField: "login_id",
    passwordField: "password",
    passReqToCallback: true
}, function (req, login_id, password, done) {
    console.log('local_login')
    
    var connection = require("../../db/db");
    var sql = "select * from user where login_id = ?";

    connection.query(sql, login_id, function (err, result) {
        console.log('local strategy')
          if(err){
            console.log(err);
            done(err, null);
          }else{
            if(result.length === 0){
              done(err, null);
            } 
            else {
              crypto.pbkdf2(password, result[0].salt, 100, 64, 'sha512', function (err, key) {
                if (key.toString('base64') !== result[0].password){
                  done(err, null);
                } else{
                  console.log(result);
                  var send_result = {
                      'id' : result[0].id,
                      'email' : result[0].email,
                      'login_id' : result[0].login_id,
                      'img_profile' : result[0].img_profile
                  }
                  done(null, send_result);
                }
              })
            }
            // else if(password !== result[0].password) {
            //   done(err, null);
            // }
            // else{
            //     console.log(result);
            //     var send_result = {
            //         'id' : result[0].id,
            //         'email' : result[0].email,
            //         'login_id' : result[0].login_id,
            //         'img_profile' : result[0].img_profile
            //     }
            //   done(null, send_result);
            // }
          }
    })
})