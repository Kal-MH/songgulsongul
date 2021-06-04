const multer = require("multer");

/*
 ** 
 ** 이미지 프로필을 저장할 때 사용되는 multer.
 ** 현재는 profile, post 이미지를 저장하는 2개의 multer함수가 존재한다.
 ** 
 ** 이후, S3로 확장될 예정 
 */

var profileStorage = multer.diskStorage({
    destination : function (req, file, cb) {
        cb(null, "upload/profile/")
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
        cb(null, 'profile' + '_' + Date.now() + mimeType);
    }
})

var postStorage = multer.diskStorage({
    destination : function (req, file, cb) {
        cb(null, "upload/post/")
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

const multerProfile = multer({storage : profileStorage});
const multerPost = multer({storage : postStorage});

var middleWares = {
    multerProfile : multerProfile.single("img_profile"),
    multerPost : multerPost.single("img_post"),
}

module.exports = middleWares;