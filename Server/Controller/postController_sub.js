const db_config = require("../db/db_config");
const connection = require("../db/db");
const statusCode = require("../config/serverStatusCode");

const postController_subFunc = {
    getSearchTag : function (req, res) {
        var searchKeyword = req.query.keyword;
        var offset = req.query.offset;

        var sql;
        if(offset == undefined){
            sql = `select h.post_id, h.text, p.image, p.post_time, p.post_date from hash_tag as h join post as p 
            on h.post_id = p.id and h.text like "${searchKeyword}%" order by post_date desc, post_time desc limit ${db_config.limitation * 1000};`;
        } else {
            sql = `select h.post_id, h.text, p.image, p.post_time, p.post_date from hash_tag as h join post as p 
            on h.post_id = p.id and h.text like "${searchKeyword}%" and h.post_id < ${offset} order by post_date desc, post_time desc limit ${db_config.limitation * 1000};`;
        }
        connection.query(sql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                var data = [];

                if (result.length > 0){
                    var post_id = result[0].post_id;
                    for(var i = 0;i < result.length ; i++){
                        if (i == 0 || post_id != result[i].post_id){
                            post_id = result[i].post_id;
                            data.push(result[i]);
                        }
                        if (data.length == db_config.limitation)
                            break ;
                    }
                } 
                console.log(result)

                //이후에 res.json으로 수정
                res.json({
                    'code' : statusCode.OK,
                    'data' : data
                })
                //res.render("search.ejs", {searchKeyword : searchKeyword, posts : result})
            }
        })
    },
    getSearchId : function (req, res) {
        var searchKeyword = req.query.keyword;
        var offset = req.query.offset;

        var sql;
        if (offset == undefined){
            sql = `select id, login_id, img_profile from user where login_id like'${searchKeyword}%' limit ${db_config.limitation};`;
        } else {
            sql = `select id, login_id, img_profile from user where login_id like'${searchKeyword}%' and id > ${offset} limit ${db_config.limitation};`;
        }
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
                    'data' : result
                })
                //res.render("searchid.ejs", {searchKeyword : searchKeyword, posts : result})
            }
        })
    },
    getPostDetailSendData : function (req, res, statusCode, postData, likeKeep) {
        var data = {
            post : {
                id : postData[0][0].id,
                image : postData[0][0].image,
                text : postData[0][0].text,
                post_time : postData[0][0].post_time,
                post_date : postData[0][0].post_date,
                user_id : postData[0][0].user_id
            },
            user : {
                login_id : postData[0][0].login_id,
                img_profile : postData[0][0].img_profile
            },
            hashTags : postData[1],
            itemTags : postData[2],
            likeNum : postData[3].length,
            comments : postData[4],
            likeOnset : (likeKeep && likeKeep.likeOnset == 1) ? 1 : 0,
            keepOnset :  (likeKeep && likeKeep.keepOnset == 1) ? 1 : 0
        }
        res.json({
            'code' : statusCode,
            'data' : data
        })
    }
}

module.exports = postController_subFunc;