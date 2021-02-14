/*
 * UserRouter
 * 사용자 기능(프로필)과 관련된 라우팅 파일
 * UserRouter에 달아준 각각의 경로들에 대한 실행함수는 userController.js(Controller dic)에 정리되어 있음
 * RESTful방식에 따라서 각각의 경로와 함수(컨트롤러)를 지정한 후, userRouter객체에 달아줌.
 */

 var express = require('express');
const userController = require('../Controller/userController');
 var routes = require('../routes');

 var userRouter = express.Router();

userRouter.post(routes.user, userController.userProfilePost);

 module.exports = userRouter;
