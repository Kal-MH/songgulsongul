const express = require('express');
const helmet = require('helmet');
const logger = require('morgan');
const cookieParser = require('cookie-parser');
const path = require('path');
const cors = require('cors');

const routes = require('./routes');
const userRouter = require('./Router/userRouter');
const homeRouter = require('./Router/homeRouter');
const apiRouter = require('./Router/apiRouter');
const postRouter = require('./Router/postRouter');
const marketRouter = require('./Router/marketRouter');
const dotenv = require('dotenv');

dotenv.config();


//서버 객체 생성
var PORT = process.env.SERVER_PORT || 3000;
var app = express();


//Middlewares
app.use(helmet());
app.use(logger('dev'));
app.use(cookieParser());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors());


//서버구현(웹상에서)
app.use("/public", express.static(path.join(__dirname, "public")));

//서버에서 저장한 이미지 불러오기 -> 이후, S3로 확장 가능성 있음
app.use("/upload", express.static(path.join(__dirname, "upload/")))

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
