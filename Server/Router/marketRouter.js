const express = require('express');
const marketController = require('../Controller/marketController');
const routes = require('../routes');
const middleWares = require('../middlewares');

var marketRouter = express.Router();

marketRouter.get(routes.marketMain, marketController.marketMain);
marketRouter.get(routes.marketDetail, marketController.getStickerDetail);
marketRouter.get(routes.marketBuy, marketController.stickerBuy);
marketRouter.get(routes.marketStickerSearch, marketController.getStickerSearch);
marketRouter.get(routes.marketSearchPrice, marketController.getSearchPrice);
marketRouter.get(routes.marketSearchDate, marketController.getSearchDate);

marketRouter.post(routes.marketUpload, middleWares.multerMarket, marketController.marketUpload);

module.exports = marketRouter;
