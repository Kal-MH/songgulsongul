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
        const id = req.query.id;
        console.log(id);

        var sql = "SELECT login_id from user;";
        connection.query(sql, function (err, result) {
            console.log(result);
            var resultCode = 200;
            if (err){
                throw err;
            } else {
                for(db_id in result){
                    if (id == db_id){
                        resultCode = 204;
                        break;
                    }
                }
            }
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
            if (err){
               throw err;
            } else {
               res.json({
                   'code' : 200,
               })
            }
            smtpTransport.close();
        })
    },
    checkEmailAuthNumber : function (req, res) {
        const authNumber = req.body.authNumber;
        console.log(authNumber);

        const generatedNumber = req.app.get("authNumber");
        console.log(generatedNumber);
        var code = 400;

        if (authNumber == generatedNumber){
            code = 200;
        }
        res.json({
            'code' : code
        })
    }
}

module.exports = auth;