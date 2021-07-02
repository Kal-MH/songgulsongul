const aws = require('aws-sdk');

const dotenv = require('dotenv');
dotenv.config();

const s3 = new aws.S3({
    credentials: {
        accessKeyId : process.env.S3_ID,
        secretAccessKey : process.env.S3_PASSWORD,
        region : process.env.S3_REGION 
    }
});

module.exports = s3;