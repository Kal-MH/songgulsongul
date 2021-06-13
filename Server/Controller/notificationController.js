
var statusCode = require("../config/serverStatusCode");
const connection = require("../db/db");

var admin = require("firebase-admin");
var serviceAccount = require("./serviceAccountKey.json");
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});


function sendNotiPostid (postid, mode, title, msg , res) {  
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

            // 작성자 id의 알림설정 체크
            var getNotiset = `SELECT * FROM notification where userid = ${id};`
            connection.query(getNotiset, mode, function (err, result) {
                if (err) {
                    console.log(err);
                    res.json({
                        'code': statusCode.SERVER_ERROR
                    })
                }
                else {
                    if (mode == 1) {
                        if (result[0].comment == 0)
                            return;
                    }
                    else if (mode == 2) {
                        if (result[0].like == 0)
                            return;
                    }
                }

            })

            // 알림 보내기        
            console.log(title);
            console.log(msg);

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
                    console.log(String(postid));
                    for (var i = 0; i < result.length; i++) {
                        var target_token = result[i].token_key;
                        let message = {
                            data: {
                                title: title,
                                message: msg,
                                mode: String(mode),
                                postid: String(postid), //이동할 게시글의 id
                                userid: String(id) //알림받는 id
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
function sendNotiUserid (loginid, mode, title, msg , res) {
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

            // 작성자 id의 알림설정 체크
            var getNotiset = `SELECT * FROM notification WHERE userid='${id}' ;`
            
            connection.query(getNotiset, function (err, result) {
                if (err) {
                    console.log(err);
                    res.json({
                        'code': statusCode.SERVER_ERROR
                    })
                }
                else {
                    console.log(result);
                    if (mode == 3) {
                        if (result[0].follow == 0)
                            return;
                    }
                }

            })


            // 알림 보내기    
            console.log(title);
            console.log(msg);


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

                        var target_token = result[i].token_key;
                        let message = {
                            data: {
                                title: title,
                                message: msg,
                                mode: String(mode)
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
           1 : 댓글 알림     postid, notification 
           2 : 좋아요 알림   postid
           3 : 팔로우 알림   loginid(string), notification
       */
        
        var mode = req.body.mode;

        if (mode == 1 || mode == 2) {
            var postid = req.body.postid;
            var title = req.body.notification.title;
            var msg = req.body.notification.message;

            sendNotiPostid(postid, mode, title, msg, res);
        }
        else if (mode == 3) {

            var loginid = req.body.loginid;
            var title = req.body.notification.title;
            var msg = req.body.notification.message;

            sendNotiUserid(loginid, mode, title, msg, res);
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
