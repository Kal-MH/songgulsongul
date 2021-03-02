const multer = require("multer");

const multerProfile = multer({dest:"upload/profile/"});

var middleWares = {
    multerProfile : multerProfile.single("img_profile")
}

module.exports = middleWares;