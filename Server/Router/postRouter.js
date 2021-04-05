const express = require('express');
const postController = require('../Controller/postController');
const middleWares = require('../middlewares');
const routes = require('../routes');

var postRouter = express.Router();

postRouter.get(routes.postCommunity, postController.getCommunity);
postRouter.get(routes.postFeeds, middleWares.onlyPrivate, postController.getFeeds);
postRouter.get(routes.postSearch, postController.getSearch);
postRouter.get(routes.postDetail, postController.getPostDetail);

//사용자 로그인상태 확인
postRouter.post(routes.postUpload, middleWares.multerPost, postController.postUpload);
postRouter.post(routes.postUpdate, middleWares.multerPost, postController.postUpdate);
postRouter.get(routes.postDelete, middleWares.onlyPrivate, postController.postDelete);
postRouter.get(routes.postDownload, postController.postDownload);
module.exports = postRouter;