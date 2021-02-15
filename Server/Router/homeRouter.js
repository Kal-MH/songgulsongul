/*
 * HomeRouter
 * 사용자 기능과 관련된 라우팅 파일
 * HomeRouter에 달아준 각각의 경로들에 대한 실행함수는 homeController.js(Controller dic)에 정리되어 있음
 * RESTful방식에 따라서 각각의 경로와 함수(컨트롤러)를 지정한 후, homeRouter객체에 달아줌.
 */

 var express = require('express');
const passport = require('passport');
const homeController = require('../Controller/homeController');
 var routes = require('../routes');

 var homeRouter = express.Router();

 homeRouter.post(routes.join,homeController.homeJoinPost);
 homeRouter.post(routes.login, passport.authenticate("local_login", {
     failureFlash: false
 }), homeController.homeLoginPost);
 homeRouter.post(routes.findId, homeController.findId)

 module.exports = homeRouter;