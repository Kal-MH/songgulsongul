/*
 * HomeRouter
 * 사용자 기능과 관련된 라우팅 파일
 * HomeRouter에 달아준 각각의 경로들에 대한 실행함수는 homeController.js(Controller dic)에 정리되어 있음
 * RESTful방식에 따라서 각각의 경로와 함수(컨트롤러)를 지정한 후, homeRouter객체에 달아줌.
 */

var express = require('express');
const passport = require('passport');
var routes = require('../routes');

const homeController = require('../Controller/homeController');
const middleWares = require("../middlewares");

var homeRouter = express.Router();

homeRouter.post(routes.join,middleWares.multerProfile, homeController.homeJoinPost);
homeRouter.post(routes.login, function(req, res, next) {
    passport.authenticate("local_login", function(err, user) {
        console.log("passport authenticate")
        if (err){
            console.log(err);
        }

        if (user){
            req.login(user, function (err) {
                if (err){
                    console.log(err);
                }
            })
            console.log("passport", req.user);
        }
        return next();
    })(req, res, next);
}, homeController.homeLoginPost);
homeRouter.post(routes.findId, homeController.findId)
homeRouter.post(routes.findPassword, homeController.findPassword);

//임시 라우터
homeRouter.get(routes.me, homeController.tmpgetMeProfile)
homeRouter.get(routes.logout, homeController.logout)

module.exports = homeRouter;