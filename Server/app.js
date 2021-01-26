var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');

//서버 객체 생성
var PORT = 3000;
var app = express();

//DB 생성
var connection = mysql.createConnection({
    host: "",
    user: "",
    database: "",
    password: "",
    port: 3306
});
//Middlewares
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//라우팅
app.post('/user/join', function (req, res) {
    console.log(req.body);    
});

app.post('/user/login', function (req, res) {
    console.log(req.body)
});

//Server listening
app.listen(3000, function () {
    console.log('Server is listening : ', PORT);
});

