const statusCode = require("../config/serverStatusCode");
const connection = require("../db/db");

function generateCurrentDate() {
    var date = new Date();
    var yearMonthDate = date.getFullYear() + "-" + ((date.getMonth() + 1 ) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-" + date.getDate();
    console.log(yearMonthDate);
    return (yearMonthDate);
}

const apiController_subFunc = {
    generateCurrentDate : function generateCurrentDate() {
        var date = new Date();
        var yearMonthDate = date.getFullYear() + "-" + ((date.getMonth() + 1 ) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-" + date.getDate();
        console.log(yearMonthDate);
        return (yearMonthDate);
    },
    updatePoint : function updatePoint(req, res, sql, type, userId) {
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
    },
    checkLastLogin : function (req, res, userId, type) {
        var selectUserSql = `select * from user where id = ${userId};`;

        connection.query(selectUserSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                const yearMonthDate = generateCurrentDate();
                
                var setLastLoginUpdatePointSql = `update user set last_login = ${yearMonthDate} where id = ${userId};`;
                if (result[0].last_login != yearMonthDate){
                    setLastLoginUpdatePointSql += `update user set point = point + 20 where id = ${userId};`
                }

                updatePoint(req, res, setLastLoginUpdatePointSql, type, userId);
            }
        })
    },
}

module.exports = apiController_subFunc;