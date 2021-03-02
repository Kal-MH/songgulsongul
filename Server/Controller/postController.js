const postController = {
    getHomeFeed : function (req, res) {
        const appName = res.locals.appName;
        const user = res.locals.loggedUser;

        res.render("homefeed.ejs", {appName : appName, user : user});
    }
}

module.exports = postController;