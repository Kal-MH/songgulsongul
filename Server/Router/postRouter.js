const express = require('express');
const postController = require('../Controller/postController');
const routes = require('../routes');

var postRouter = express.Router();

postRouter.get(routes.postHomeFeed, postController.getHomeFeed)

module.exports = postRouter;