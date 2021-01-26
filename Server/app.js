var mysql = require('mysql');
var express = require('express');
var bodyParser = require('body-parser');

//서버 객체 생성
var PORT = 3000;
var app = express();

//DB 생성
var connection = mysql.createConnection({
    host: "localhost",
    user: "root",
    database: "example",
    password: "Capstone0!",
    port: 3306
});
//Middlewares
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//라우팅
app.get('/', function(req, res) {
	res.send("Hello world!");
	connection.query('select * from Users', function(err, rows, fields) {
		if (err) throw err;
		console.log('users rows : ', rows);
	});
	connection.end();
});

app.post('/user/join', function (req, res) {
    console.log(req.body);    
});

app.post('/user/login', function (req, res) {
    console.log(req.body);
});

//Server listening
app.listen(3000, function () {
    console.log('Server is listening : ', PORT);
});

