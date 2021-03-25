const express = require('express');
const helmet = require('helmet');
const logger = require('morgan');
const cookieParser = require('cookie-parser');
//const bodyParser = require('body-parser');
const path = require('path');
const cors = require('cors');
const expressSession = require('express-session');
const MySQLStore = require('express-mysql-session');

const passport = require('passport');
const configPassport = require("./config/passport");

const routes = require('./routes');
const userRouter = require('./Router/userRouter');
const homeRouter = require('./Router/homeRouter');
const apiRouter = require('./Router/apiRouter');
const postRouter = require('./Router/postRouter');
const marketRouter = require('./Router/marketRouter');
const { localMiddlewares } = require('./middlewares');
const dotenv = require('dotenv');

dotenv.config();


//서버 객체 생성
var PORT = process.env.SERVER_PORT || 3000;
var app = express();

//뷰 엔진 설정 - 웹상에서 확인하기 위해 설치, 이후에 삭제 예정
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');

//Middlewares
app.use(helmet());
app.use(logger('dev'));
app.use(cookieParser());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors());

//sesssion & passport
app.use(expressSession({
    secret: '1234DSFs@adf1234!@#$asd',
    resave: false,
    saveUninitialized: true,
    // store: new MySQLStore({
    //     host: process.env.DB_HOST,
    //     user: process.env.DB_USER,
    //     password: process.env.DB_PASSWORD,
    //     database: process.env.DB_DATABASE,
    //     port : process.env.DB_PORT
    // })
}))
app.use(passport.initialize());
app.use(passport.session());
configPassport();

//localMiddlewares to remember app name
app.use(localMiddlewares);

app.use(function (req, res, next) {
    console.log(req.user);
    next();
})

//서버구현(웹상에서)
app.use("/public", express.static(path.join(__dirname, "public")));

//서버에서 저장한 이미지 불러오기 -> 이후, S3로 확장 가능성 있음
app.use("/upload", express.static("upload"))

//라우팅
app.use(routes.home, homeRouter);
app.use(routes.api, apiRouter);
app.use(routes.user, userRouter);
app.use(routes.post, postRouter);
app.use(routes.market, marketRouter);

//Server listening
app.listen(PORT, function () {
    console.log('Server is listening : ', PORT);
}).on('error', function (err) {
    console.log(err)
});
