const express = require('express');
const postController = require('../Controller/postController');
const middleWares = require('../middlewares');
const routes = require('../routes');

var postRouter = express.Router();

postRouter.get(routes.postHomeFeed, postController.getHomeFeed);
postRouter.post(routes.postUpload, middleWares.multerPost, postController.postUpload);

module.exports = postRouter;