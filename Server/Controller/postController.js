const connection = require("../db/db");
const postController_subFunc = require("./postController_sub");
const db_config = require("../db/db_config");
const statusCode = require("../config/serverStatusCode");
const serverConfig = require("../config/serverConfig");

const fs = require("fs");
const path = require("path");
const mime = require("mime");

const postController = {
    getPostDetail : function (req, res) {
        const loggedUser = req.query.userid;
        const postId = req.params.id;

        // 게시글과 관련된 정보를 가져오는 쿼리문
        // 게시글, 작성자, 해시태그, 아이템 태그, 좋아요, 코멘트를 차례로 가져오고 있다.
         var selectPostDetailSql = `select p.id, p.image, p.text, p.post_time, p.post_date, p.user_id, u.login_id, u.img_profile from post as p join user as u 
        on p.id = ${postId} and p.user_id = u.id;`;
        selectPostDetailSql += `select text from hash_tag where post_id=${postId};`;
        selectPostDetailSql += `select * from item_tag where post_id=${postId};`;
        selectPostDetailSql += `select * from likes where post_id=${postId};`;
        selectPostDetailSql += `select c.id, c.user_id, c.text, u.img_profile, u.login_id from comment as c join user as u on c.post_id=${postId} and c.user_id=u.id;`;
        connection.query(selectPostDetailSql, function (err, result) {
            if (err){
                console.log(err);
                postController_subFunc.getPostDetailSendData(req, res, statusCode.SERVER_ERROR, null, null);
            } else {
                var postData = result;

                if (loggedUser){
                    var meLikesSql = `select * from likes where post_id=${postId} and user_id=${loggedUser};`;
                    var meKeepsSql = `select * from keep where post_id=${postId} and user_id=${loggedUser};`;
                    connection.query(meLikesSql + meKeepsSql, function (err, result) {
                        if (err){
                            console.log(err);
                            postController_subFunc.getPostDetailSendData(req, res, statusCode.SERVER_ERROR, null, null);
                        } else {
                            var likeKeep = {
                                likeOnset : (result[0].length == 0) ? 0 : 1,
                                keepOnset : (result[1].length == 0) ? 0 : 1

                            }

                            // ------------------------- data  확인을 위한 ejs 넘겨주는 변수값들 --------------------
                            var data = [{
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
                            }];
            
                            var options = {
                                appName : "Caligraphy",
                                user : res.locals.loggedUser,
                                post : data[0].post,
                                postUser : data[0].user,
                                hashTags : data[0].hashTags,
                                itemTags : data[0].itemTags, 
                                likeNum : data[0].likeNum,
                                comments : data[0].comments
                            }

                            res.render("postDetail.ejs", {options : options, likeOnset : data[0].likeOnset, keepOnset : data[0].keepOnset});
                            // --------------------------------------------------------------------------------------
                            //postController_subFunc.getPostDetailSendData(req, res, statusCode.OK, postData, likeKeep);  
                           
                        }
                    })
                } else {
                    postController_subFunc.getPostDetailSendData(req, res, statusCode.OK, postData, null);
                }


               
                
            }
        })
    },
    getFeeds : function (req, res) {
        const loggedUser = req.query.userid;
        const offset = req.query.offset;

        const selectFollowSql = `select follow_target_id from follow where follower_id = ${loggedUser};`;
        connection.query(selectFollowSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR,
                    'data' : null
                })
            } else if (result.length == 0) { // 팔로우하는 사람이 없는 경우
                res.json({
                    'code' : statusCode.OK,
                    'data' : result
                })
            } else {
                var selectPostSql = `select id from post where (`;
                for(var i = 0;i < result.length - 1;i++){
                    selectPostSql += `user_id = ${result[i].follow_target_id} or `
                }
                if(offset == undefined){
                    selectPostSql += `user_id = ${result[result.length - 1].follow_target_id} )
                    order by post_date desc, post_time desc limit ${db_config.limitation};`;
                } else {
                    selectPostSql += `user_id = ${result[result.length - 1].follow_target_id} ) and id < ${offset}
                    order by post_date desc, post_time desc limit ${db_config.limitation};`;
                }
                connection.query(selectPostSql, function (req, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : statusCode.SERVER_ERROR,
                            'data' : null
                        })
                    } else {
                        if (result.length == 0) {
                            res.json({
                                'code' : statusCode.OK,
                                'data' : result
                            })
                        } else {
                            var selectPostFeedSql = "";
                        
                            for(var i = 0;i < result.length; i++){
                                selectPostFeedSql += `select p.id, p.image, p.text, p.post_date, p.post_time, p.user_id, u.login_id, u.img_profile from post as p join user as u 
                                on p.id = ${result[i].id} and p.user_id = u.id;`;
                                selectPostFeedSql += `select id from comment where post_id = ${result[i].id};`;
                                selectPostFeedSql += `select * from likes where post_id = ${result[i].id};`;
                                selectPostFeedSql += `select * from likes where post_id = ${result[i].id} and user_id = ${loggedUser};`;
                                selectPostFeedSql += `select id from keep where post_id = ${result[i].id} and user_id = ${loggedUser};`;
                            }
                            connection.query(selectPostFeedSql, function (req, result) {
                                if (err){
                                    console.log(err);
                                    res.json({
                                        'code' : statusCode.SERVER_ERROR,
                                        'data' : null
                                    })
                                } else {
                                    var data = [];
                                    for(var i = 0;i < result.length; i += 5){
                                        var info = {
                                            post : {
                                                id : result[i][0].id,
                                                image : result[i][0].image,
                                                text : result[i][0].text,
                                                post_time : result[i][0].post_time,
                                                post_date : result[i][0].post_date
                                            },
                                            user : {
                                                user_id : result[i][0].user_id,
                                                login_id : result[i][0].login_id,
                                                img_profile : result[i][0].img_profile
                                            },
                                            commentsNum : (result[i + 1]) ? result[i + 1].length : 0,
                                            likeNum : (result[i + 2]) ? result[i + 2].length : 0,
                                            likeOnset : (result[i + 3] && result[i + 3].length != 0) ? 1 : 0,
                                            keepOnset : (result[i + 4] && result[i + 4].length != 0) ? 1 : 0
                                        }
                                        data.push(info);
                                    }
                                    console.log(data);
                                    res.json({
                                        'code' : statusCode.OK,
                                        'data' : data
                                    })
                                }
                            })
                        }
                    }
                })
            }
        })
    },
    getCommunity : function (req, res) {
        const appName = res.locals.appName;
        //const user = req.query.userid;
        const offset = req.query.offset;

        // //20개만 받아오기
        // var sql = `select * from post order by post_time desc, post_date desc limit ${db_config.limitation} offset ${offset};`

        var selectPostSql;
        if (offset == undefined)
            selectPostSql = `select * from post order by post_date desc, post_time desc limit ${db_config.limitation};`
        else
            selectPostSql = `select * from post where id < ${offset} order by post_date desc, post_time desc limit ${db_config.limitation};`

        connection.query(selectPostSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR,
                    'data' : null
                })
            } else {
                //이후에 res.json()으로 바꿔야 한다.
                //res.render("mainfeed.ejs", {appName : appName, user : user, posts : result});
                res.json({
                    'code' : statusCode.OK,
                    'data' : result
                })
            }
        })

    },
    /*
     * api plane
     * 
     * 1. 그냥 이대로 라우터를 태그검색과 아이디 검색을 분리해서 관리할 지
     * 2. api 하나에 쿼리값을 달리해서 구분할 지
     *      - api : /post/search?method=tag&keyword=searchKeyword (GET) 
     *              or /post/search?method=tag (POST)
     */
    getSearch : function (req, res) {
        const method = req.query.method;

        if (method == 'tag'){
            postController_subFunc.getSearchTag(req, res);
        } else if (method == 'id'){
            postController_subFunc.getSearchId(req, res);
        }
    },
    postUpload : function (req, res) {
        /*
         * 현재 넘겨받는 데이터의 구조
         * 추후에 안드로이드와 연결하는 부분에서 수정사항 발생할 수 있다.
         * hashTags = [ 'hash1', 'hash2', 'hash3' ]
           items = { 
                    name: [ 'item1', 'item2' ],
                    lowprice: [ '1000', '20000' ],
                    highprice: [ '2000', '30000' ],
                    itemLink:
                    [ 'https://search.shopping.naver.com/gate.nhn?id=82726822549',
                        'https://search.shopping.naver.com/gate.nhn?id=82726822549' ],
                    itemImg: 
                    [ 'https://shopping-phinf.pstatic.net/main_8272682/82726822549.1.jpg',
                      '' ]
                }                           
         */
        var loggedUser = req.query.userid;
        var text = req.body.text;
        var hashTagsAndItemTags = req.body.hash_tag;
        var items = {
            name : req.body.item_name,
            lowprice : req.body.item_lowprice,
            highprice : req.body.item_highprice,
            itemLink : req.body.item_link,
            itemImg : req.body.item_img
        }

        var postImages = req.file.path;

        var updatePointinsertPostSql = `insert into post (image, text, post_time, post_date, user_id, ccl_cc, ccl_a, ccl_nc, ccl_nd, ccl_sa) 
        values (?, '${text}', curtime(), curdate(), ${loggedUser}, ?);`;
        var insertPostParams = [postImages, req.body.ccl];
        
        //current date 계산
        var date = new Date();
        var yearMonthDate = date.getFullYear() + "-" + ((date.getMonth() + 1 ) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-" + date.getDate();
        console.log(yearMonthDate);
        updatePointinsertPostSql += `select post_time, post_date from post where post_date = '${yearMonthDate}';`;    
        
        connection.query(updatePointinsertPostSql, insertPostParams, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                console.log(result[1]);
                console.log(result[1].length);
                var postId = result[0].insertId;
                
                //update Point if post-count under 5 && insert hashTag 
                var updatePointinsertHashSql = "";

                // 오늘 날짜의 게시글이 5개 이하이면 포인트 100을 추가로 반영한다.
                if (result[1].length <= 5)
                    updatePointinsertHashSql += `update user set point = point + 100 where id = ${loggedUser};`

                for(var i = 0;i < hashTagsAndItemTags.length; i++){
                    updatePointinsertHashSql += `insert into hash_tag (post_id, text) values (${postId}, ?);`
                }
                //itemTag
                //배열로 넘겨주기 위해 해시태그와 item태그를 한 배열로 넣는다.(해시태그 먼저, 아이템 태그는 그 뒤에)
                 var insertItemSql = "";
                 for(var i = 0;i < items.name.length; i++) {
                     insertItemSql += `insert into item_tag (post_id, name, lprice, hprice, url, picture) 
                     values(${postId}, '${items.name[i]}', ${items.lowprice[i]}, ${items.highprice[i]}, ?, ?);`;
                     
                     hashTagsAndItemTags.push(items.itemLink[i]);
                     if (items.itemImg[i]){
                         hashTagsAndItemTags.push(items.itemImg[i])
                     } else {
                         hashTagsAndItemTags.push(serverConfig.defaultImg);
                     }

                 }
                 connection.query(updatePointinsertHashSql + insertItemSql, hashTagsAndItemTags, function (err, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : statusCode.CLIENT_ERROR
                        })
                    }
                    else{
                        res.json({
                            'code' : statusCode.OK
                        })                                                  
                    }
                })
            }
        })
    },
    postUpdate : function (req, res) {
        var postId = req.query.postid;
        var loggedUser = req.query.userid;
        var file = req.file;

        console.log(req.body);

        //안드로이드에서 넘어오는 값에 따라서 수정이 필요한 부분
        var image = (file) ? req.file.path : req.body.img_post_link; 

        var updatePostSql = `update post set image=?, text=?, post_time=curtime(), post_date=curdate(), user_id=? where id=${postId};`;
        var updatePostParams = [image, req.body.text, loggedUser];

        connection.query(updatePostSql, updatePostParams, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                //hashTag
                var deleteHashTagsSql = `delete from hash_tag where post_id=${postId};`;
                var insertHashSql = "";
                var hashTags = req.body.hashTags;
                for(var i = 0;i < hashTags.length; i++){
                    //현재는 input을 동적으로 조정할 수 없기 때문에 추가된 if문 
                    //이후에는 삭제되면 클라이언트에서 넘어온 배열을 기준으로 sql문을 작성하게 된다.
                    if (hashTags[i] != '')
                        insertHashSql += `insert into hash_tag (post_id, text) values (${postId}, ?);`
                }
                connection.query(deleteHashTagsSql + insertHashSql, hashTags, function (err, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : statusCode.CLIENT_ERROR
                        })
                    } else {                      
                        var deleteItemTagSql = `delete from item_tag where post_id=${postId};`;
                        var insertItemSql = "";
                        var insertItemParams = [];
                        for(var i = 0;i < req.body.itemName.length;i++){
                            var itemImg;
                            if (req.body.itemImg[i] == '') {
                                itemImg = "/public/default/to-do-list.png";
                            } else {   
                                itemImg = req.body.itemImg[i];
                            }

                            insertItemSql += `insert into item_tag (post_id, name, lprice, hprice, url, picture) values(${postId}, ?, ?, ?, ?, ?);`;
                            insertItemParams.push(req.body.itemName[i]);
                            insertItemParams.push(Number(req.body.itemLowprice[i]));
                            insertItemParams.push(Number(req.body.itemHighprice[i]));
                            insertItemParams.push(req.body.itemLink[i]);
                            insertItemParams.push(itemImg);
                        }
                        connection.query(deleteItemTagSql + insertItemSql, insertItemParams, function (err, result) {
                            if (err){
                                console.log(err);
                                res.json({
                                    'code' : statusCode.CLIENT_ERROR
                                })
                            } else {
                                res.redirect(`/post/${postId}`);
                            }
                        })
                    }
                })
            }
        })
    },
    postDelete : function (req, res) {
        const postId = req.query.postid;
        const loggedUser = req.query.userid;

        var selectPostSql = `select image from post where id = ${postId} and user_id = ${loggedUser};`;
        connection.query(selectPostSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR,
                })
            } else {
                var fileImage = result[0].image;
                var filePath = `./${fileImage}`;
                var deletePostSql = `delete from post where image = ?;`;
                connection.query(deletePostSql, fileImage, function (err, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : statusCode.SERVER_ERROR
                        })
                    } else {
                        if (fs.existsSync(filePath)){
                            fs.unlink(filePath, function (err) {
                                if (err){
                                    console.log(err);
                                    res.json({
                                        'code' : statusCode.SERVER_ERROR,
                                    })
                                } else {
                                    res.redirect("/post/community?offset=0")
                                    // res.json({
                                    //     'code' : statusCode.OK
                                    // })
                                }               
                            })
                        } else {
                            res.json({
                                'code' : statusCode.OK,
                            })
                        }
                    }
                })
            }
        })
    },
    postDownload : function (req, res) {
        var postId = req.query.postid;

        var sql = `select image from post where id = ${postId};`;
        connection.query(sql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR
                })
            } else {
                //s3로 확장하게 되면 경로설정 수정할 필요성 있음
                var file = `${result[0].image}`;
                console.log(file);
                
                res.json({
                    'code' : statusCode.OK,
                    'data' : {
                        'imgPath' : file
                    }
                })
            }

            //이미지 확장자를 체크하기 위한 다운로드 코드
            // try{
            //     if (fs.existsSync(file)){
            //         var fileName = path.basename(file);
            //         var mimeType = mime.getType(file);

            //         console.log(fileName);
            //         console.log(mimeType);

            //         console.log(fileName);
            //         res.setHeader('Content-disposition', 'attachment; filename=' + fileName);
            //         res.setHeader('Content-type', mimeType);

            //         var fileStream = fs.createReadStream(file);
            //         fileStream.pipe(res);
            //     } else {
            //         res.send('no file exists');
            //         return ;
            //     }
            // } catch(e){
            //     console.log(e);
            //     res.send('error occurs');
            //     return ;
            // }
        })
    }
}

module.exports = postController;