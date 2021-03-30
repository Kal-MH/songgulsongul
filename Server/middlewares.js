const multer = require("multer");

/*
 ** 현재는 post 업로드에만 확장자를 지정해서 저장하고 있다.
 ** 이미지 프로필을 저장할 때도 확장자를 지정해야 하는 지는 아직 정해지지 않았다. 
 */

// var profileStorage = multer.diskStorage({
//     destination : function (req, file, cb) {
//         cb(null, "upload/profile")
//     }, 
//     filename : function (req, file, cb) {
//         var mimeType;

//         switch (file.mimetype) {
//         case "image/jpeg":
//             mimeType = ".jpg";
//         break;
//         case "image/png":
//             mimeType = ".png";
//         break;
//         case "image/gif":
//             mimeType = ".gif";
//         break;
//         case "image/bmp":
//             mimeType = ".bmp";
//         break;
//         default:
//             mimeType = ".png";
//         break;
//         }
//         cb(null, 'profile' + '_' + Date.now() + mimeType);
//     }
// })

var postStorage = multer.diskStorage({
    destination : function (req, file, cb) {
        cb(null, "upload/post")
    }, 
    filename : function (req, file, cb) {
        var mimeType;

        switch (file.mimetype) {
        case "image/jpeg":
            mimeType = ".jpg";
        break;
        case "image/png":
            mimeType = ".png";
        break;
        case "image/gif":
            mimeType = ".gif";
        break;
        case "image/bmp":
            mimeType = ".bmp";
        break;
        default:
            mimeType = ".png";
        break;
        }
        cb(null, 'calligraphy' + '_' + Date.now() + mimeType);
    }
})

const multerProfile = multer({dest:"upload/profile/"});
const multerPost = multer({storage : postStorage});

var middleWares = {
    multerProfile : multerProfile.single("img_profile"),
    multerPost : multerPost.single("img_post"),
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