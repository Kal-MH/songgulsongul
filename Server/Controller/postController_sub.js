const db_config = require("../db/db_config");
const connection = require("../db/db");
const statusCode = require("../config/serverStatusCode");

const postController_subFunc = {
    postSearchTag : function (req, res) {
        var searchKeyword = req.query.keyword;
        var offset = req.query.offset;

        var sql = `select h.post_id, h.text, p.image, p.post_time, p.post_date from hash_tag as h join post as p 
        on h.post_id = p.id and h.text like "${searchKeyword}%" order by post_time desc, post_date desc limit ${db_config.limitation} offset ${offset};`;
        connection.query(sql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                //이후에 res.json으로 수정
                res.json({
                    'code' : statusCode.OK,
                    'posts' : result
                })
                //res.render("search.ejs", {searchKeyword : searchKeyword, posts : result})
            }
        })
    },
    postSearchId : function (req, res) {
        var searchKeyword = req.query.keyword;
        var offset = req.query.offset;

        var sql = `select * from user where login_id like'${searchKeyword}%' limit ${db_config.limitation} offset ${offset};`;
        connection.query(sql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                //이후에 res.json으로 수정
                res.json({
                    'code' : statusCode.OK,
                    'posts' : result
                })
                //res.render("searchid.ejs", {searchKeyword : searchKeyword, posts : result})
            }
        })
    },
}

module.exports = postController_subFunc;