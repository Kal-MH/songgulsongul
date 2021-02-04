var express = require('express');
var helmet = require('helmet');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

var dotenv = require('dotenv');
const routes = require('./routes');
const userRouter = require('./Router/userRouter');
dotenv.config();


//서버 객체 생성
var PORT = process.env.SERVER_PORT || 3000;
var app = express();

//Middlewares
app.use(helmet());
app.use(logger('dev'));
app.use(cookieParser());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));

//라우팅
app.get('/', function(req, res) {
	res.send("Hello world!");
});

app.use(routes.user, userRouter);

//Server listening
app.listen(PORT, function () {
    console.log('Server is listening : ', PORT);
});
