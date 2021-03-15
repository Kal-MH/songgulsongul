/*
 * apiRouter
 * 홈 기능과 관련된 라우팅 파일
 * apiRouter에 달아준 각각의 경로들에 대한 실행함수는 apiController.js(Controller dic)에 정리되어 있음
 * RESTful방식에 따라서 각각의 경로와 함수(컨트롤러)를 지정한 후, apiRouter객체에 달아줌.
 */

const express = require('express');
const apiController = require('../Controller/apiController');
const middleWares = require('../middlewares');
const routes = require('../routes');

 var apiRouter = express.Router();

 apiRouter.post(routes.apiDupIdCheck, apiController.dupIdCheck);
 apiRouter.post(routes.apiEmailAuth, apiController.sendEmail);
 apiRouter.post(routes.apiEmailAuthNumber, apiController.checkEmailAuthNumber);

 apiRouter.get(routes.apiPostLike, middleWares.onlyPrivate, apiController.setPostLike);
 apiRouter.get(routes.apiPostKeep, middleWares.onlyPrivate, apiController.setPostKeep);
 
 apiRouter.post(routes.apiPostCommentInsert,apiController.insertPostComment);
 apiRouter.get(routes.apiPostCommentDelete, apiController.deletePostComment);

 //임시
 apiRouter.get(routes.apiNaverItemtag, apiController.sendNaverAPI);

 module.exports = apiRouter;