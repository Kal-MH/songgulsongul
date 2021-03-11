const statusCode = require("../config/serverStatusCode");
const connection = require("../db/db");
const postController_subFunc = require("./postController_sub");
const db_config = require("../db/db_config");

const postController = {
    /*
     * 특정피드를 포함해서 20개 가져오기
     * - 특정피드를 첫번째로 해서 20개를 가져오자
     * api : /post/:id?offset=0
     * 1안 : select * from post order by field(post_id, 1) desc, post_time desc //특정 피드를 제일 먼저 올리고 나머지는 최신순으로
     *       SELECT * FROM 테이블 order by project_name asc LIMIT 10 OFFSET 300000;
     * 2안 : post_id를 첫번째 레코드로 row_num테이블을 만들어서 20개 뽑아오기
     */
    getPostDetail : function (req, res) {
        const postId = req.params.id;
        const appName = res.locals.appName;
        const loggedUser = res.locals.loggedUser;

        var postAndUserSql = `select p.id, p.image, p.text, p.post_date, p.user_id, u.login_id, u.img_profile from post as p join user as u on p.id = ${postId} and p.user_id = u.id;`;
        var hashSql = `select text from hash_tag where post_id=${postId};`;
        var itemSql = `select * from item_tag where post_id=${postId};`;
        connection.query(postAndUserSql + hashSql + itemSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                var postUser = result[0][0];
                var hashs = result[1];
                var items = result[2];

                var likesSql = `select * from likes where post_id=${postId};`;
                var commentsSql = `select c.user_id, c.text, u.img_profile, u.login_id from comment as c join user as u on c.post_id=${postId} and c.user_id=u.id;`;
                connection.query(likesSql + commentsSql, function (err, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : statusCode.CLIENT_ERROR
                        })
                    } else {
                        var options = {
                            appName : appName,
                            user : loggedUser,
                            postUser : postUser,
                            hashs : hashs,
                            items : items, 
                            likeNum : result[0].length,
                            comments : result[1]
                        }

                        if (loggedUser){
                            var meLikesSql = `select * from likes where post_id=${postId} and user_id=${(loggedUser) ? loggedUser.id : -1};`;
                            var meKeepsSql = `select * from keep where post_id=${postId} and user_id=${(loggedUser) ? loggedUser.id : -1};`;
                            connection.query(meLikesSql + meKeepsSql, function (err, result) {
                                if (err){
                                    console.log(err);
                                    res.json({
                                        'code' : statusCode.CLIENT_ERROR
                                    })
                                } else {
                                    var likeOnset = (result[0].length == 0) ? 0 : 1 ;
                                    var keepOnset = (result[1].length == 0) ? 0 : 1 ;
                                    
                                    res.render("postDetail.ejs", {options : options, likeOnset : likeOnset, keepOnset : keepOnset});
                                }
                            })
                        }
                        res.render("postDetail.ejs", {options : options, likeOnset : 0, keepOnset : 0});
                    }
                })
                
                
            }
        })

    },
    /*
     * api plane
     * mainFeed : /post/feed?category=main&offset=20
     * homeFeed(follow feed) : /post/feed?category=home&offset=20
     * keepFeed : /post/feed?category=keep&offset=20
     * 
     * const category = req.query.category
     * const offset = req.query.offset
     * 
     * <로그인 정보 불필요>
     * mainFeed sql : select * from post order by post_time desc, post_date desc limit 20 offset 20
     * 
     * <로그인 정보 필요>
     * homeFeed sql : follow table과 post table 조인해서 넘겨줌
     * keepFeed sql : keep table, post table 조인해서 넘겨줌
     * 
     * 넘겨줄 데이터
     *  - postid, image, text, user_id(작성자 아이디)
     */
    getHomeFeeds : function (req, res) {
        const loggedUser = res.locals.loggedUser;
        const offset = req.query.offset;

        const selectFollowSql = `select follow_target_id from follower where follower_id = ${loggedUser.id} limit ${db_config.limitation} offset ${offset};`;
        connection.query(selectFollowSql, function (err, result) {
            if (err){
                //set error handling
            } else {
                var selectPostJoinSql = `select * from post where `;
                for(var i = 0;i < sql.length - 1;i++){
                    selectPostJoinSql += `id = ${result[i]} or`
                }
                selectPostJoinSql += `id = ${result[result.length - 1]} order by post_time desc, post_date desc;`;

                connection.query(selectPostJoinSql, function (req, result) {
                    if (err){
                        //set error handling
                    } else {
                        res.json({
                            'data' : result
                        })
                    }
                })
            }
        })
    },
    getMainFeeds : function (req, res) {
        const appName = res.locals.appName;
        const user = res.locals.loggedUser;
        const offset = req.query.offset;

        //20개만 받아오기
        var sql = `select * from post order by post_time desc, post_date desc limit ${db_config.limitation} offset ${offset};`

        connection.query(sql, function (err, result) {
            if (err){
                res.json({
                    'code' : statusCode.CLIENT_ERROR
                })
            } else {
                //이후에 res.json()으로 바꿔야 한다.
                res.render("mainfeed.ejs", {appName : appName, user : user, posts : result});
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
    postSearch : function (req, res) {
        const method = req.query.method;

        if (method == 'tag'){
            postController_subFunc.postSearchTag(req, res);
        } else if (method == 'id'){
            postController_subFunc.postSearchId(req, res);
        }
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
        var insertPostSql = "insert into post (image, text, post_time, post_date, user_id) values (?, ?, curtime(), curdate(), ?);";
        var insertPostParams = [postImages, text, loggedUser.id];

        connection.query(insertPostSql, insertPostParams, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR
                })
            } else {
                var postId = result.insertId;
                
                //hashTag
                var insertHashSql = "";
                for(var i = 0;i < hashTags.length; i++){
                    insertHashSql += `insert into hash_tag (post_id, text) values (${postId}, ?);`
                }
                connection.query(insertHashSql, hashTags, function (err, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : statusCode.SERVER_ERROR
                        })
                    }
                    else{
                         //itemTag - 현재는 단수로 되어 있지만, 이후에 액티비티랑 연동할 때, 복수로 바꿔야 한다.
                        var itemSql = "insert into item_tag (post_id, name, lowprice, highprice, url, picture) values(?, ?, ?, ?, ?, ?);";
                        var itemParams = [postId, item.name, item.lowprice, item.highprice, item.itemLink, item.itemImg()]
    
                        connection.query(itemSql, itemParams, function (err, result) {
                            if (err){
                                console.log(err);
                                res.json({
                                    'code' : statusCode.SERVER_ERROR
                                })
                            } else {
                                res.json({
                                    'code' : statusCode.OK
                                })
                            }
                        })                                                     
                    }
                })
            }
        })
    },
    postUpdate : function (req, res) {
        var postId = req.params.id;
        var loggedUser = res.locals.loggedUser;
        var files = req.files;

        console.log(req.body);

        //안드로이드에서 넘어오는 값에 따라서 수정이 필요한 부분
        //또한, 지금은 이미지를 하나만 받고 있기 때문에 반복문으로 처리해야 할 수도 있다.
        var image = (files.length == 0) ? req.body.img_post_link : req.files[0].path; 
        var updatePostSql = `update post set image=?, text=?, post_time=curtime(), post_date=curdate(), user_id=? where id=${postId};`;
        var updatePostParams = [image, req.body.text, loggedUser.id];

        connection.query(updatePostSql, updatePostParams, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR
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
                            'code' : statusCode.SERVER_ERROR
                        })
                    } else {
                        //itemTag - 현재는 단수로 되어 있지만, 이후에 액티비티랑 연동할 때, 복수로 바꿔야 한다.
                        var item = {
                            name : req.body.itemName,
                            lowprice : Number(req.body.itemLowprice),
                            highprice : Number(req.body.itemHighprice),
                            itemLink : req.body.itemLink,
                            itemImg : function () {
                                if (req.body.itemImg == '')
                                    return "/public/default/to-do-list.png";
                                else 
                                    return req.body.itemImg;
                            }
                        }
                        var deleteItemTagSql = `delete from item_tag where post_id=${postId};`;
                        var insertItemSql = "insert into item_tag (post_id, name, lowprice, highprice, url, picture) values(?, ?, ?, ?, ?, ?);";
                        var itemParams = [postId, item.name, item.lowprice, item.highprice, item.itemLink, item.itemImg()]
                        connection.query(deleteItemTagSql + insertItemSql, itemParams, function (err, result) {
                            if (err){
                                console.log(err);
                                res.json({
                                    'code' : statusCode.SERVER_ERROR
                                })
                            } else {
                                res.redirect(`/post/${postId}`);
                            }
                        })
                    }
                })
            }
        })
    }
}

module.exports = postController;