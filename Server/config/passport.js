const passport = require('passport');

const local_login = require("./passport/local_login");

module.exports = function () {
    passport.serializeUser(function (user, done) {
        console.log("serializeUSer");
        done(null, user);
    })
    passport.deserializeUser(function (user, done) {
        console.log("deserializeUser");
        done(null, user);
    })
    
    passport.use('local_login', local_login);
}
