const multer = require("multer");
const s3 = require("./config/s3");
const multerS3 = require("multer-s3");
const serverConfig = require("./config/serverConfig");

/*
 **
 ** 이미지 프로필을 저장할 때 사용되는 multer.
 ** 현재는 profile, post 이미지를 저장하는 2개의 multer함수가 존재한다.
 **
 ** 이후, S3로 확장.
 */

var profileStorage = multerS3({
    s3: s3,
    bucket: serverConfig.s3BucketName,
    key: function (req, file, cb) {
        var mimeType = file.mimetype.split('/')[1];
        if (!['png', 'jpg', 'jpeg', 'gif', 'bmp'].includes(mimeType)) {
            return cb(new Error('Only images are allowed.'));
        }
        cb(null, serverConfig.s3BucketProfileFolderName + 'profile' + '_' + Date.now() + '.'+ mimeType);
    },
    acl : 'public-read'
})

var postStorage = multerS3({
    s3: s3,
    bucket: serverConfig.s3BucketName,
    key: function (req, file, cb) {
        var mimeType = file.mimetype.split('/')[1];
        if (!['png', 'jpg', 'jpeg', 'gif', 'bmp'].includes(mimeType)) {
            return cb(new Error('Only images are allowed.'));
        }
        cb(null, serverConfig.s3BucketPostFolderName + 'post' + '_' + Date.now() + '.'+ mimeType);
    },
    acl : 'public-read'
})

var marketStorage = multerS3({
    s3: s3,
    bucket: serverConfig.s3BucketName,
    key: function (req, file, cb) {
        var mimeType = file.mimetype.split('/')[1];
        if (!['png', 'jpg', 'jpeg', 'gif', 'bmp'].includes(mimeType)) {
            return cb(new Error('Only images are allowed.'));
        }
        cb(null, serverConfig.s3BucketMarketFolderName + 'market' + '_' + Date.now() + '.'+ mimeType);
    },
    acl : 'public-read'
})

const multerProfile = multer({storage : profileStorage});
const multerPost = multer({storage : postStorage});
const multerMarket = multer({storage : marketStorage});

var middleWares = {
    multerProfile : multerProfile.single("img_profile"),
    multerPost : multerPost.single("img_post"),
    multerMarket : multerMarket.single("img_market"),
}

module.exports = middleWares;
