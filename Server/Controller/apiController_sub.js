const statusCode = require("../config/serverStatusCode");
const connection = require("../db/db");


const apiController_subFunc = {

    //checkLastLogin: function (req, res, userId) {

    //    var sql = `select last_login from user where id = ${userId}`

    //    connection.query(sql, function (err, result) {

    //        if (err) {
    //            console.log(err);
    //            res.json({
    //                'code': statusCode.SERVER_ERROR
    //            })
    //            return;
    //        }

    //        const lastYearMonthDate = generateCurrentDate();
    //        if (result[0].last_login != lastYearMonthDate) {
    //            if (attendanceCheck(req, res, userId) == false) { //업데이트 실패함 
    //                res.json({
    //                    'code': statusCode.SERVER_ERROR
    //                })
    //                return;
    //            }
    //            else { //출석체크 성공         
    //                res.json({
    //                    'code': statusCode.OK,
    //                    'id': userId
    //                })
    //            }
    //        }
    //        else { //첫출석 아님
    //            res.json({
    //                'code': statusCode.NO,
    //                'id': userId
    //            })
    //        }
    //    })
    //},
    
}

module.exports = apiController_subFunc;