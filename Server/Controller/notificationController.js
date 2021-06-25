
var statusCode = require("../config/serverStatusCode");
const connection = require("../db/db");

var admin = require("firebase-admin");
var serviceAccount = require("./serviceAccountKey.json");
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});


function sendNotiPostid (sender, postid, mode, title, msg , res) {  
    // postid로 포스트 작성한 작성자 id 찾기
    var findIdSql = `SELECT user_id FROM post WHERE id=${postid};`
    connection.query(findIdSql, function (err, result) {
        if (err) {
            console.log(err);
            res.json({
                'code': statusCode.SERVER_ERROR
            })
        }
        else {
            var id = result[0].user_id;            

       
            //targetToken 체크
            var getTokenSql = `select token_key from token where userid=${id}`
            connection.query(getTokenSql, mode, function (err, result) {
                if (err) {
                    console.log(err);
                    res.json({
                        'code': statusCode.SERVER_ERROR
                    })
                }
                else {
                    console.log("mode " + mode);
                    console.log("postid " + String(postid));
                    console.log("userid " + String(id));
                    console.log(title)
                    console.log(msg);
                    for (var i = 0; i < result.length; i++) {
                        var target_token = result[i].token_key;
                        let message = {
                            data: {
                                title: title,
                                message: msg,
                                mode: String(mode),
                                postid: String(postid), //이동할 게시글의 id
                                userid: String(id) //알림받는 id
                                ,sender : String(sender)
                            },
                            token: target_token,
                        }

                        admin
                            .messaging()
                            .send(message)
                            .then(function (response) {
                                console.log('Successfully sent message: : ', response)

                            })
                            .catch(function (err) {
                                console.log('Error Sending message!!! : ', err)
                            })
                    }
                }
            });
        }
    });
}
function sendNotiUserid (sender, loginid, mode, title, msg , res) {
    // loginid 로 id 찾기
    var findIdSql = `SELECT id FROM user WHERE login_id='${loginid}';`
    connection.query(findIdSql, function (err, result) {
        if (err) {
            console.log(err);
            res.json({
                'code': statusCode.SERVER_ERROR
            })
        }
        else {
            var id = result[0].id;
            
  

            //targetToken 체크
            var getTokenSql = `select token_key from token where userid=${id}`
            connection.query(getTokenSql, mode, function (err, result) {
                if (err) {
                    console.log(err);
                    res.json({
                        'code': statusCode.SERVER_ERROR
                    })
                }
                else {
                    for (var i = 0; i < result.length; i++) {

                        console.log("mode " + mode);
                        console.log(title)
                        console.log(msg);


                        var target_token = result[i].token_key;
                        let message = {
                            data: {
                                title: title,
                                message: msg,
                                mode: String(mode),
                                sender: String(sender)
                            },
                            token: target_token,
                        }

                        admin
                            .messaging()
                            .send(message)
                            .then(function (response) {
                                console.log('Successfully sent message: : ', response)

                            })
                            .catch(function (err) {
                                console.log('Error Sending message!!! : ', err)
                            })

                    }
                }
            });
        }
    });
}

var notificationController = {
    
    sendNotification: function (req, res) {
        /* mode 설명 
           0 : 푸쉬알람 설정 userid(int), notification
           1 : 팔로우 알림   loginid(string), notification
           2 : 좋아요 알림   postid
           3 : 댓글 알림     postid, notification
       */
        
        var mode = req.body.mode;
        var sender = req.body.sender;

        if (mode == 3 || mode == 2) { //좋아요, 댓글
            var postid = req.body.postid;
            var title = req.body.notification.title;
            var msg = req.body.notification.message;

            sendNotiPostid(sender,postid, mode, title, msg, res);
        }
        else if (mode == 1) { // 팔로우

            var loginid = req.body.loginid;
            var title = req.body.notification.title;
            var msg = req.body.notification.message;

            sendNotiUserid(sender,loginid, mode, title, msg, res);
        }
    },
    setToken: function (req, res) { //데이터베이스에 토큰 등록 
        var userid = req.body.userid;
        var token = req.body.token;

        console.log(userid);
        console.log(token)

        const checkTokenSql = `select * from token where userid=${userid} and token_key='${token}';`;

        connection.query(checkTokenSql, function (err, result) {
            if (err) {
                console.log(err);
                res.json({
                    'code': statusCode.SERVER_ERROR
                })
            }
            else {
                if (result.length == 0) {
                    //토큰 추가
                    var addTokenSql = `insert into token (userid, token_key) values (${userid}, '${token}');`;
                    connection.query(addTokenSql, function (err, result) {
                        var code = statusCode.OK;
                        if (err) {
                            console.log(err);
                            code = statusCode.SERVER_ERROR;
                        }
                        res.json({
                            'code': code
                        })
                    })
                }
            }
        })
    },
    deleteToken: function (req, res) {

        var userid = req.body.userid;
        var token = req.body.token;

        console.log(userid);
        console.log(token);

        const deleteTokenSql = `delete from token where userid=${userid} and token_key='${token}';`;

        connection.query(deleteTokenSql, function (err, result) {
            if (err) {
                console.log(err);
                res.json({
                    'code': statusCode.SERVER_ERROR
                })
            }
            else {
                res.json({
                    'code': statusCode.OK
                })
            }


        })
    }
}

 module.exports = notificationController;
