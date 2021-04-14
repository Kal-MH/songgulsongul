const statusCode = require("../config/serverStatusCode");
const connection = require("../db/db");

function generateCurrentDate(type) {
    var date = new Date();
    var yearMonthDate = date.getFullYear() + "-" + ((date.getMonth() + 1 ) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-" + date.getDate();
    console.log(yearMonthDate);
    return (yearMonthDate);
}

function updatePoint(req, res, sql, type, userId) {
    connection.query(sql, function (err, result) {
        if (err){
            console.log(err);
            res.json({
                'code' : statusCode.SERVER_ERROR
            })
        } else {
            if (type == 0){
                res.json({
                    'code' : statusCode.OK,
                    'id' : userId
                })    
            } else {
                res.json({
                    'code' : statusCode.OK
                })
            }
        }
    })
}

const apiController_subFunc = {
    checkLastLogin : function (req, res, userId, type) {
        var selectUserSql = `select * from user where id = ${userId};`;

        connection.query(selectUserSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                const lastYearMonthDate = generateCurrentDate();

                var setLastLoginUpdatePointSql = `update user set last_login = NOW() where id = ${userId};`;
                if (result[0].last_login != lastYearMonthDate){
                    setLastLoginUpdatePointSql += `update user set point = point + 20 where id = ${userId};`
                }
                
                console.log(setLastLoginUpdatePointSql);
                updatePoint(req, res, setLastLoginUpdatePointSql, type, userId);
            }
        })
    },
}

module.exports = apiController_subFunc;