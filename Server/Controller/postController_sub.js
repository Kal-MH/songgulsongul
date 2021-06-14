const db_config = require("../db/db_config");
const connection = require("../db/db");
const statusCode = require("../config/serverStatusCode");

const postController_subFunc = {
    getSearchTag : function (req, res) {
        var searchKeyword = req.query.keyword;
        var offset = req.query.offset;

        //해쉬태그의 경우, 한 게시글에 여러 개가 중복되어 달릴 수 있다.
        // 1. 게시글의 배수를 곱해서 레코드 가져오기
        // 2. 같은 게시글 아이디를 갖는 해시태그는 제외하기
        var sql;
        if(offset == undefined || offset == 0){
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
                    'code' : statusCode.SERVER_ERROR,
                    'data' : null
                })
            } else {
                var data = [];

                if (result.length > 0){
                    var post_id = result[0].post_id;
                    for (var i = 0; i < result.length; i++){

                        if (i == 0 || post_id != result[i].post_id){
                            post_id = result[i].post_id;
                            var post_data = {
                                'id': result[i].post_id,
                                'text': result[i].text,
                                'image': result[i].image,
                                'post_time': result[i].post_time,
                                'post_date': result[i].post_date
                            };

                            data.push(post_data);
                        }
                        if (data.length == db_config.limitation)
                            break ;
                    }
                }
                console.log(data)
                res.json({
                    'code' : statusCode.OK,
                    'data' : data
                })
            }
        })
    },
    getSearchId : function (req, res) {
        var searchKeyword = req.query.keyword;
        var offset = req.query.offset;

        var sql;
        if (offset == undefined || offset == 0){
            sql = `select id, login_id, img_profile, intro from user where login_id like'${searchKeyword}%' limit ${db_config.limitation};`;
        } else {
            sql = `select id, login_id, img_profile, intro from user where login_id like'${searchKeyword}%' and id > ${offset} limit ${db_config.limitation};`;
        }
        connection.query(sql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR,
                    'data' : null
                })
            } else {
                console.log(result);
                res.json({
                    'code' : statusCode.OK,
                    'data' : result
                })
            }
        })
    },
    getPostSendData : function (req, res, statusCode, postData, likeKeep, type) {
        //getPostDetail
        if (type == 0) {
            var data = {
                post : { //게시글 정보
                    id : postData[0][0].id,
                    image : postData[0][0].image,
                    text : postData[0][0].text,
                    post_time : postData[0][0].post_time,
                    post_date : postData[0][0].post_date,
                    user_id : postData[0][0].user_id,
                    ccl : {
                        ccl_cc : postData[0][0].ccl_cc,
                        ccl_a : postData[0][0].ccl_a,
                        ccl_nc : postData[0][0].ccl_nc,
                        ccl_nd : postData[0][0].ccl_nd,
                        ccl_sa : postData[0][0].ccl_sa,
                    }
                },
                user : { //작성자 정보
                    login_id : postData[0][0].login_id,
                    img_profile : postData[0][0].img_profile
                },
                hashTags : postData[1],
                itemTags : postData[2],
                likeNum : postData[3].length,
                comments : postData[4],
                likeOnset : (likeKeep && likeKeep.likeOnset == 1) ? 1 : 0, //현재 작성자가 좋아요 눌렀는 지에 대한 여부
                keepOnset :  (likeKeep && likeKeep.keepOnset == 1) ? 1 : 0 //현재 작성자가 보관하기 눌렀는 지에 대한 여부
            }
            console.log(data);
            res.json({
                'code' : statusCode,
                'data' : data
            })
        } else if (type == 1) { //getFeeds
            var data = [];
            for(var i = 0;i < postData.length; i += 5){
                var info = {
                    post : {
                        id : postData[i][0].id,
                        image : postData[i][0].image,
                        text : postData[i][0].text,
                        post_time : postData[i][0].post_time,
                        post_date : postData[i][0].post_date
                    },
                    user : {
                        user_id : postData[i][0].user_id,
                        login_id : postData[i][0].login_id,
                        img_profile : postData[i][0].img_profile
                    },
                    commentsNum : (postData[i + 1]) ? postData[i + 1].length : 0,
                    likeNum : (postData[i + 2]) ? postData[i + 2].length : 0,
                    likeOnset : (postData[i + 3] && postData[i + 3].length != 0) ? 1 : 0,
                    keepOnset : (postData[i + 4] && postData[i + 4].length != 0) ? 1 : 0
                }
                data.push(info);
            }
            //console.log(data);
            res.json({
                'code' : statusCode,
                'data' : data
            })
        }
    },
    updatePointInsertHashItem : function (res, postId, hashTags, items, sql) {
        var hashTagsSplitItemParams = [];
        var insertItemSql = "";
        var itemForIndex = 0;

        if (typeof hashTags == 'string'){
            hashTagsSplitItemParams.push(hashTags);
        } else {
            hashTagsSplitItemParams = hashTags;
        }
        for (var i = 0; i < hashTagsSplitItemParams.length; i++) {
          if(hashTagsSplitItemParams[i] != '')
            sql += `insert into hash_tag (post_id, text) values (${postId}, ?);`
          else{
            hashTagsSplitItemParams.splice(i,1);
            i--;
          }
        }
        
        if (typeof items.name == 'string' && items.name != ''){
            insertItemSql += `insert into item_tag (post_id, name, lprice, hprice, brand, category1, category2, url, picture)
            values(${postId}, '${items.name}', ${items.lowprice ? items.lowprice : -1}, ${items.highprice ? items.highprice : -1}, ?, ?, ?, ?, ?);`;

            hashTagsSplitItemParams.push(items.brand);
            hashTagsSplitItemParams.push(items.category1);
            hashTagsSplitItemParams.push(items.category2);
            hashTagsSplitItemParams.push(items.itemLink);
            if (items.itemImg) {
                hashTagsSplitItemParams.push(items.itemImg)
            } else {
                hashTagsSplitItemParams.push(serverConfig.defaultImg);
            }
        } else if (typeof items.name != 'string'){
            itemForIndex = items.name.length;
            for (var i = 0; i < itemForIndex; i++) {
                insertItemSql += `insert into item_tag (post_id, name, lprice, hprice, brand, category1, category2, url, picture)
                    values(${postId}, '${items.name[i]}', ${items.lowprice[i] ? items.lowprice[i] : -1}, ${items.highprice[i] ? items.highprice[i] : -1}, ?, ?, ?, ?, ?);`;
    
                hashTagsSplitItemParams.push(items.brand[i]);
                hashTagsSplitItemParams.push(items.category1[i]);
                hashTagsSplitItemParams.push(items.category2[i]);
                hashTagsSplitItemParams.push(items.itemLink[i]);
                if (items.itemImg[i]) {
                    hashTagsSplitItemParams.push(items.itemImg[i])
                } else {
                    hashTagsSplitItemParams.push(serverConfig.defaultImg);
                }
            }
        }
        connection.query(sql + insertItemSql, hashTagsSplitItemParams, function (err, result) {
            if (err) {
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR
                })
            } else {
                res.json({
                    'code' : statusCode.OK,
                    'post_id' : postId
                })
            }
        })
    }
}

module.exports = postController_subFunc;
