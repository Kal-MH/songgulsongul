const express = require('express');
const helmet = require('helmet');
const logger = require('morgan');
const cookieParser = require('cookie-parser');
const bodyParser = require('body-parser');
const path = require('path');
const cors = require('cors');

const routes = require('./routes');
const homeRouter = require('./Router/homeRouter');
const apiRouter = require('./Router/apiRouter');

const dotenv = require('dotenv');
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
app.use(cors())

//서버구현(웹상에서)
app.use("/public", express.static(path.join(__dirname, "public")));

//라우팅
app.use(routes.home, homeRouter);
app.use(routes.api, apiRouter);

//Server listening
app.listen(PORT, function () {
    console.log('Server is listening : ', PORT);
});
