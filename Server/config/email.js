const nodemailer = require("nodemailer");

const smtpTransport = nodemailer.createTransport({
    service : "gmail",
    auth:{
        user: "paper.pen.smu@gmail.com",
        pass: "smu!cs2021"
    },
    tls:{
        rejectUnauthorized : false
    }
})

module.exports = smtpTransport;