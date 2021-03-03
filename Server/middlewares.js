const multer = require("multer");

const multerProfile = multer({dest:"upload/profile/"});
const multerPost = multer({dest : "upload/post"});

var middleWares = {
    multerProfile : multerProfile.single("img_profile"),
    multerPost : multerPost.array("img_post"),
    localMiddlewares : function (req, res, next) {
        res.locals.appName = "Calligraphy";
        res.locals.loggedUser = req.user || null ;
        next();
    }
}

module.exports = middleWares;