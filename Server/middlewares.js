const multer = require("multer");

const multerProfile = multer({dest:"upload/profile/"});

var middleWares = {
    multerProfile : multerProfile.single("img_profile"),
    localMiddlewares : function (req, res, next) {
        res.locals.appName = "Calligraphy";
        res.locals.loggedUser = req.user || null ;
        next();
    }
}

module.exports = middleWares;