const nodemailer = require("nodemailer");
const dotenv = require("dotenv");

dotenv.config();

const smtpTransport = nodemailer.createTransport({
    service : process.env.EMAIL_SERVICE,
    auth:{
        user: process.env.EMAIL,
        pass: process.env.EMAIL_PASSWORD
    },
    tls:{
        rejectUnauthorized : false
    }
})

module.exports = smtpTransport;