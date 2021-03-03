const connection = require("../db/db");

const postController = {
    getMainFeed : function (req, res) {
        const appName = res.locals.appName;
        const user = res.locals.loggedUser;

        var sql = "select * from post order by post_time desc, post_date desc;"

        connection.query(sql, function (err, result) {
            if (err){
                res.json({
                    'code' : 204
                })
            } else {
                //이후에 res.json()으로 바꿔야 한다.
                res.render("mainfeed.ejs", {appName : appName, user : user, posts : result});
            }
        })

    },
    postSearchTag : function (req, res) {
        var searchKeyword = req.body.searchKeyword;

        var sql = `select h.post_id, h.text, p.image, p.post_time, p.post_date from hash_tag as h join Post as p on h.post_id = p.id and h.text like "${searchKeyword}%" order by post_time desc, post_date desc;`;
        connection.query(sql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : 204
                })
            } else {
                //이후에 res.json으로 수정
                res.render("search.ejs", {searchKeyword : searchKeyword, posts : result})
            }
        })
    },
    postSearchId : function (req, res) {
        var searchKeyword = req.body.searchKeyword;

        var sql = `select * from user where login_id="${searchKeyword}";`;
        connection.query(sql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : 204
                })
            } else {
                //이후에 res.json으로 수정
                res.render("searchid.ejs", {searchKeyword : searchKeyword, posts : result})
            }
        })
    },
    postUpload : function (req, res) {
        var loggedUser = res.locals.loggedUser;
        var text = req.body.text;
        var hashTags = req.body.hashTag;
        var item = {
            name : req.body.itemName,
            lowprice : req.body.itemLowprice,
            highprice : req.body.itemHighprice,
            itemLink : req.body.itemLink,
            itemImg : function () {
                if (req.body.itemImg == '')
                    return "/public/default/to-do-list.png";
                else 
                    return req.body.itemImg;
            }
        }

        // post이미지를 여러 개 받을 수 있다는 경우의 수를 고려해서 일단은 multer.array & files로 받는다.
        var postImages = req.files[0].path;

        //post
        var sqlPost = "insert into post (image, text, post_time, post_date, user_id) values (?, ?, now(), now(), ?);";
        var postParams = [postImages, text, loggedUser.id];

        connection.query(sqlPost, postParams, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : 204
                })
            } else {
                var postId = result.insertId;
                
                //hashTag
                var sqlHash = "";
                for(var i = 0;i < hashTags.length; i++){
                    sqlHash += `insert into hash_tag (post_id, text) values (${postId}, ?);`
                }
                connection.query(sqlHash, hashTags, function (err, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : 204
                        })
                    }
                    else{
                         //itemTag
                        var itemSql = "insert into item_tag(post_id, name, lowprice, highprice, url, picture) values(?, ?, ?, ?, ?, ?);";
                        var itemParams = [postId, item.name, item.lowprice, item.highprice, item.itemLink, item.itemImg()]
    
                        connection.query(itemSql, itemParams, function (err, result) {
                            if (err){
                                console.log(err);
                                res.json({
                                    'code' : 204
                                })
                            } else {
                                res.json({
                                    'code' : 200
                                })
                            }
                        })                                                     
                    }
                })
            }
        })
    }
}

module.exports = postController;