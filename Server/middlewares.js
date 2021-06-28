const multer = require("multer");
const s3 = require("./config/s3");
const multerS3 = require("multer-s3")

/*
 **
 ** 이미지 프로필을 저장할 때 사용되는 multer.
 ** 현재는 profile, post 이미지를 저장하는 2개의 multer함수가 존재한다.
 **
 ** 이후, S3로 확장.
 */

var profileStorage = multerS3({
    s3: s3,
    bucket: "songgulsongul",
    key: function (req, file, cb) {
        var mimeType = file.mimetype.split('/')[1];
        if (!['png', 'jpg', 'jpeg', 'gif', 'bmp'].includes(mimeType)) {
            return cb(new Error('Only images are allowed.'));
        }
        cb(null, 'profile' + '_' + Date.now() + '.'+ mimeType);
    },
    acl : 'public-read'
})

var postStorage = multerS3({
    s3: s3,
    bucket: "songgulsongul",
    key: function (req, file, cb) {
        var mimeType = file.mimetype.split('/')[1];
        if (!['png', 'jpg', 'jpeg', 'gif', 'bmp'].includes(mimeType)) {
            return cb(new Error('Only images are allowed.'));
        }
        cb(null, 'post' + '_' + Date.now() + '.'+ mimeType);
    },
    acl : 'public-read'
})

var marketStorage = multer.diskStorage({
    s3: s3,
    bucket: "songgulsongul",
    key: function (req, file, cb) {
        var mimeType = file.mimetype.split('/')[1];
        if (!['png', 'jpg', 'jpeg', 'gif', 'bmp'].includes(mimeType)) {
            return cb(new Error('Only images are allowed.'));
        }
        cb(null, 'market' + '_' + Date.now() + '.'+ mimeType);
    },
    acl : 'public-read'
})

const multerProfile = multer({
    dest : "upload/profile/",
    storage : profileStorage
});
const multerPost = multer({
    dest : "upload/post/",
    storage : postStorage
});
const multerMarket = multer({
    dest : "upload/market",
    storage : marketStorage
});

var middleWares = {
    multerProfile : multerProfile.single("img_profile"),
    multerPost : multerPost.single("img_post"),
    multerMarket : multerMarket.single("img_market"),
}

module.exports = middleWares;
