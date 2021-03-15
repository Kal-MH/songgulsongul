const db_config = require("../db/db_config");
const connection = require("../db/db");
const statusCode = require("../config/serverStatusCode");

const postController_subFunc = {
    getSearchTag : function (req, res) {
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
    getSearchId : function (req, res) {
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
    getPostDetailSendData : function (req, res, statusCode, posts, hashItemLikeComments, likeArray, keepArray) {
        var data = [];

        for(var i = 0;i < posts.length;i++){
            var post = {
                post : posts[i],
                user : hashItemLikeComments[i * 5],
                hashTags : hashItemLikeComments[(i * 5) + 1],
                itemTags : hashItemLikeComments[(i * 5) + 2],
                likeNum : hashItemLikeComments[(i * 5) + 3].length,
                comments : hashItemLikeComments[(i * 5) + 4],
                likeOnset : (likeArray && (likeArray[i] != undefined && likeArray[i].length > 0) ? 1 : 0),
                keepOnset : (keepArray && (keepArray[i] != undefined && keepArray[i].length > 0) ? 1 : 0)
            }
            data.push(post);
        }
        res.json({
            'code' : statusCode,
            'data' : data
        })
    }
}

module.exports = postController_subFunc;