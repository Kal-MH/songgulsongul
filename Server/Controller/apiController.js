const http = require("http");
const axios = require('axios');

const smtpTransport = require("../config/email");
const statusCode = require("../config/serverStatusCode");
const connection = require("../db/db");

var generateRandom = function (min, max) {
    var randomNum = Math.floor(Math.random() * (max - min + 1)) + min;
    console.log(randomNum);
    return randomNum;
}

const apiController = {
    //아이디 중복체크
    dupIdCheck : function (req, res) {
        const id = req.body.login_id;
        console.log(id);

        var sql = "SELECT login_id from user;";
        connection.query(sql, function (err, result) {
            var resultCode = statusCode.OK;
            if (err){
                console.log(err);
                resultCode = statusCode.SERVER_ERROR;
            } else {
                for(var i = 0;i < result.length;i++){
                    if (id == result[i].login_id){
                        resultCode = statusCode.CLIENT_ERROR;
                        break;
                    }
                }
            }
            console.log(resultCode);
            res.json({
                'code' : resultCode
            })
        })


    },
    //이메일인증보내기
    sendEmail : async function (req, res) {
        const number = generateRandom(111111, 999999);
        const queryEmail = req.body.email;
        console.log(queryEmail)

        const mailOptions = {
            from:"paper.pen.smu@gmail.com",
            to : queryEmail,
            subject : "회원가입 인증메일입니다.",
            text : "오른쪽 숫자 6자리를 입력해주세요" + number
        }

        console.log(number);
        req.app.set('authNumber', number);

        const result = await smtpTransport.sendMail(mailOptions, function (err, responses) {
            var resultCode = statusCode.SERVER_ERROR;
            if (err){
               console.log(err);
            } else {
                resultCode = statusCode.OK;
            }
            res.json({
                'code' : resultCode,
                'authNumber' : number
            })
            smtpTransport.close();
        })
    },
    //authNumber를 클라이언트에게 보내주기 때문에 추후에 삭제될 수 있음.
    checkEmailAuthNumber : function (req, res) {
        const authNumber = req.body.authNumber;
        console.log(authNumber);

        const generatedNumber = req.app.get("authNumber");
        console.log(generatedNumber);
        var resultCode = statusCode.CLIENT_ERROR;

        if (authNumber == generatedNumber){
            resultCode = statusCode.OK;
        }
        res.json({
            'code' : resultCode
        })
    },
    //좋아요 api
    setPostLike : function (req, res) {
        const postId = req.params.id;

        //일단은 사용자가 로그인되었다는 가정하에, 클라이언트에서 거른다는 가정하에 진행
        const loggedUser = res.locals.loggedUser;
        const apiLikeCheckSql = `select * from likes where post_id=${postId} and user_id=${loggedUser.id};`;

        connection.query(apiLikeCheckSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                if (result.length == 0){
                    //사용자가 좋아요를 누른 상황
                    var apiLikeSql = `insert into likes (post_id, user_id) values (${postId}, ${loggedUser.id});`;
                    connection.query(apiLikeSql, function (err, result) {
                        var code = statusCode.OK;
                        if (err){
                            console.log(err);
                            code = statusCode.SERVER_ERROR;
                        }
                        res.redirect(`/post/${postId}`)
                        // res.json({
                        //     'code' : code
                        // })
                    })
                } else {
                    //사용자가 좋아요 취소를 누른 상황
                    var apiLikeDeleteSql = `delete from likes where post_id=${postId} and user_id=${loggedUser.id};`;
                    connection.query(apiLikeDeleteSql, function (err, result) {
                        var code = statusCode.OK;
                        if (err){
                            console.log(err);
                            code = statusCode.SERVER_ERROR;
                        }
                        res.redirect(`/post/${postId}`)
                        // res.json({
                        //     'code' : code
                        // })
                    })
                }
            }
        })
        
    },
    setPostKeep : function (req, res) {
        const postId = req.params.id;

        //일단은 사용자가 로그인되었다는 가정하에, 클라이언트에서 거른다는 가정하에 진행
        const loggedUser = res.locals.loggedUser;
        const apiKeepCheckSql = `select * from keep where post_id=${postId} and user_id=${loggedUser.id};`;

        connection.query(apiKeepCheckSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                if (result.length == 0){
                    //사용자가 보관하기를 누른 상황
                    var apiKeepSql = `insert into keep (post_id, user_id) values (${postId}, ${loggedUser.id});`;
                    connection.query(apiKeepSql, function (err, result) {
                        var code = statusCode.OK;
                        if (err){
                            console.log(err);
                            code = statusCode.SERVER_ERROR;
                        }
                        res.redirect(`/post/${postId}`)
                        // res.json({
                        //     'code' : code
                        // })
                    })
                } else {
                    //사용자가 보관하기 취소를 누른 상황
                    var apiKeepDeleteSql = `delete from keep where post_id=${postId} and user_id=${loggedUser.id};`;
                    connection.query(apiKeepDeleteSql, function (err, result) {
                        var code = statusCode.OK;
                        if (err){
                            console.log(err);
                            code = statusCode.SERVER_ERROR;
                        }
                        res.redirect(`/post/${postId}`)
                        // res.json({
                        //     'code' : code
                        // })
                    })
                }
            }
        })
        
    },
    insertPostComment : function (req, res) {
        var postId = req.params.postid;
        var userId = req.params.userid;
        var text = req.body.comment; //공백으로 오는 것을 어디서 걸러줄 것인가.

        if (text == ''){
            res.json({
                'code' : statusCode.CLIENT_ERROR
            })
        } else {
            var commentInsertSql = `insert into comment(user_id, post_id, text, c_time, c_date) values(?, ?, ?, curtime(), curdate());`
            var commentParams = [userId, postId, text];
            connection.query(commentInsertSql, commentParams, function (err, result) {
                var code = statusCode.OK;
                if (err){
                    console.log(err);
                    code = statusCode.CLIENT_ERROR;
                }
                res.redirect(`/post/${postId}`)
                // res.json({
                //     'code' : code
                // })
            })
        }

    },
    deletePostComment : function (req, res) {
        var postId = req.params.postid;
        var userId = req.params.userid;

        var commentDeleteSql = `delete from comment where user_id=? and post_id=?;`
        var commentParams = [userId, postId];
        connection.query(commentDeleteSql, commentParams, function (err, result) {
            var code = statusCode.OK;
            if (err){
                console.log(err);
                code = statusCode.CLIENT_ERROR;
            }
            res.redirect(`/post/${postId}`)
            // res.json({
            //     'code' : code
            // })
        })
    },
    //itemTag naver api 사용하기
    sendNaverAPI : async function (req, res) {
        const itemName = encodeURI(req.query.item);

        const CLIENTID = "JEvadN6iDuJaQLpKAkUf";
        const cLIENTPASSWORD = "PzQWeURFmU";

        await axios.get(`https://openapi.naver.com/v1/search/shop.json?query=${itemName}&start=1&display=10&sort=sim`,{
            headers:{
                "X-Naver-Client-Id" : CLIENTID,
                "X-Naver-Client-Secret" : cLIENTPASSWORD
            }
        })
        .then(function (response) {
            console.log(response.data.items[0]);
            res.json({
                result : response.data.items
            })
        }).catch(function (error) {
            console.log(error);
        })
    }
}

module.exports = apiController;