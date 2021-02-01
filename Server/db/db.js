var mysql = require('mysql');

var dotenv = require('dotenv');
dotenv.config();

//DB 생성
var connection = mysql.createConnection({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    database: process.env.DB_DATABASE,
    password: process.env.DB_PASSWORD,
    port: process.env.DB_PORT
});

//DB connection export
module.exports = connection;