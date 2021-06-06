const express = require('express');
const postController = require('../Controller/postController');
const middleWares = require('../middlewares');
const routes = require('../routes');

var postRouter = express.Router();

postRouter.get(routes.postDelete, postController.postDelete);

postRouter.get(routes.postCommunity, postController.getCommunity);
postRouter.get(routes.postFeeds, postController.getFeeds);
postRouter.get(routes.postSearch, postController.getSearch);
postRouter.get(routes.postSearchTag, postController.getSearchTag);
postRouter.get(routes.postSearchId, postController.getSearchId);
postRouter.get(routes.postDetail, postController.getPostDetail);

postRouter.post(routes.postUpload, middleWares.multerPost, postController.postUpload);
postRouter.post(routes.postUpdate, middleWares.multerPost, postController.postUpdate);
module.exports = postRouter;