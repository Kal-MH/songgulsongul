const express = require('express');
const marketController = require('../Controller/marketController');
const routes = require('../routes');

var marketRouter = express.Router();

marketRouter.use(routes.marketMain, marketController.marketMain);
marketRouter.get(routes.marketDetail, marketController.getStickerDetail);
marketRouter.get(routes.marketBuy, marketController.stickerBuy);
marketRouter.get(routes.marketStickerSearch, marketController.getStickerSearch);
marketRouter.get(routes.marketSearchPrice, marketController.getSearchPrice);
marketRouter.get(routes.marketSearchDate, marketController.getSearchDate);
