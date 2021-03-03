const express = require('express');
const postController = require('../Controller/postController');
const middleWares = require('../middlewares');
const routes = require('../routes');

var postRouter = express.Router();

postRouter.get(routes.postMainFeed, postController.getMainFeed);
postRouter.post(routes.postUpload, middleWares.multerPost, postController.postUpload);
postRouter.post(routes.postSearchTag, postController.postSearchTag);
postRouter.post(routes.postSearchId, postController.postSearchId);

module.exports = postRouter;