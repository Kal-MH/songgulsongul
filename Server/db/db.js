var mysql = require('mysql');

var dotenv = require('dotenv');
dotenv.config();

//DB 생성
var connection = mysql.createConnection({
    host: process.env.DB_HOST || "localhost",
    user: process.env.DB_USER || "root",
    database: process.env.DB_DATABASE || "example",
    password: process.env.DB_PASSWORD || "Capstone0!",
    port: process.env.DB_PORT || 3306,
    dateStrings : 'date',
    multipleStatements: true // 다중쿼리 사용
});

//DB connection export
module.exports = connection;
