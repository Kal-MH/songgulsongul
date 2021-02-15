const express = require('express');
const helmet = require('helmet');
const logger = require('morgan');
const cookieParser = require('cookie-parser');
const bodyParser = require('body-parser');
const path = require('path');
const cors = require('cors');
const expressSession = require('express-session');
const MySQLStore = require('express-mysql-session');

const routes = require('./routes');
const homeRouter = require('./Router/homeRouter');
const apiRouter = require('./Router/apiRouter');

const dotenv = require('dotenv');
const passport = require('passport');

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

//sesssion
app.use(expressSession({
    secret: '1234DSFs@adf1234!@#$asd',
    resave:false,
    saveUninitialized: true,
    store: new MySQLStore({
        host: process.env.DB_HOST,
        user: process.env.DB_USER,
        password: process.env.DB_PASSWORD,
        database: process.env.DB_DATABASE
    })
}))

app.use(passport.initialize());
app.use(passport.session());

var configPassport = require("./config/passport");
configPassport();

app.use(function (req, res, next) {
    // sess = req.session;
    // console.log(sess.userId);
    console.log(req.user);
    next();
})

//서버구현(웹상에서)
app.use("/public", express.static(path.join(__dirname, "public")));

//라우팅
app.use(routes.home, homeRouter);
app.use(routes.api, apiRouter);

//Server listening
app.listen(PORT, function () {
    console.log('Server is listening : ', PORT);
});
