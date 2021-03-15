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
    },
    onlyPrivate : function (req, res, next) {
        if (req.user){
            next();
        } else {
            res.json({
                'code' : 204,
                'message' : 'login please'
            })
        }
    },
    onlyPublic : function (req, res, next) {
        if (req.user){
            res.json({
                'code' : 204,
                'message' : 'only public'
            })
        } else {
            next();
        }
    }
}

module.exports = middleWares;