const http = require("http");
const axios = require('axios');

const smtpTransport = require("../config/email");
const connection = require("../db/db");

var generateRandom = function (min, max) {
    var randomNum = Math.floor(Math.random() * (max - min + 1)) + min;
    console.log(randomNum);
    return randomNum;
}

const auth = {
    //아이디 중복체크
    dupIdCheck : function (req, res) {
        const id = req.body.id;
        console.log(id);

        var sql = "SELECT login_id from user;";
        connection.query(sql, function (err, result) {
            var resultCode = 200;
            if (err){
                console.log(err);
                resultCode = 404; //error
            } else {
                for(var i = 0;i < result.length;i++){
                    if (id == result[i].login_id){
                        resultCode = 204;
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

        console.log(req.body)
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
            var resultCode = 404;
            if (err){
               console.log(err);
            } else {
                resultCode = 200;
            }
            res.json({
                'code' : resultCode,
            })
            smtpTransport.close();
        })
    },
    checkEmailAuthNumber : function (req, res) {
        const authNumber = req.body.authNumber;
        console.log(authNumber);

        const generatedNumber = req.app.get("authNumber");
        console.log(generatedNumber);
        var resultCode = 404;

        if (authNumber == generatedNumber){
            resultCode = 200;
        }
        res.json({
            'code' : resultCode
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

        // const option = {
        //     host : "openapi.naver.com",
        //     path : `/v1/search/shop.json?query=${itemName}&start=1&display=10&sort=sim`,
        //     headers : {
        //         "X-Naver-Client-Id" : CLIENTID,
        //         "X-Naver-Client-Secret" : cLIENTPASSWORD
        //     }
        // }
        // try {      
        //     result = await http.get(option, function (res) {
        //         console.log(res.statusCode);
        //         res.on('data', function (chunk) {
        //             console.log('chunk');
        //             console.log(chunk);
        //         })
        //     })
        // } catch (error) {
        //     console.log(error);
        // }
        // res.end()
        // res.json({
        //     'json' : result
        // })
    }
}

module.exports = auth;