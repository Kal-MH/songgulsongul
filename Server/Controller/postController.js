const connection = require("../db/db");
const postController_subFunc = require("./postController_sub");
const db_config = require("../db/db_config");
const statusCode = require("../config/serverStatusCode");
const serverConfig = require("../config/serverConfig");

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
        const offset = req.query.offset ? req.query.offset : 0;

        var selectPostSql = `select * from post where id >= ${postId} order by post_time desc, post_date desc limit ${db_config.limitation} offset ${offset};`;
        connection.query(selectPostSql, function (err, result) {
            var code = statusCode.SERVER_ERROR
            if (err){
               console.log(err)
            } else {
                var posts = result;

                var selectUserSql = "";
                for(var i = 0; i < result.length; i++) {
                    selectUserSql += `select login_id, img_profile from user where id = ${result[i].user_id}; `;
                    selectUserSql += `select text from hash_tag where post_id=${result[i].id};`;
                    selectUserSql += `select * from item_tag where post_id=${result[i].id};`;
                    selectUserSql += `select * from likes where post_id=${result[i].id};`;
                    selectUserSql += `select c.user_id, c.text, u.img_profile, u.login_id from comment as c join user as u on c.post_id=${result[i].id} and c.user_id=u.id;`;
                }
                connection.query(selectUserSql, function (err, result) {
                    if (err){
                        console.log(err);
                    } else {
                        /*
                         * To-do
                         * 1. likeOnset, keepOnset  포스트 마다 각각
                         * 2. 보내지는 데이터 확인하기
                         * 3. api문서 마무리 하기
                         * 4. db수정사항과 고려사항 정리하기
                         */
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
                        var data = [];
                        for(var i = 0;i < posts.length;i++){
                            var post = {
                                post : posts[i],
                                user : result[i * 5],
                                hashTags : result[(i * 5) + 1],
                                itemTags : result[(i * 5) + 2],
                                likeNum : result[(i * 5) + 3].length,
                                comments : result[(i * 5) + 4]
                            }
                            data.push(post);
                        }
                        code = statusCode.OK;
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
                        res.render("postDetail.ejs", {options : options, likeOnset : 0, keepOnset : 0});
                    }
                    // res.json({
                    //     'code' : code,
                    //     'data' : data
                    // })
                })
            }
        })

        // var postAndUserSql = `select p.id, p.image, p.text, p.post_date, p.user_id, u.login_id, u.img_profile from post as p join user as u 
        // on p.id = ${postId} and p.user_id = u.id;`;
        // var hashSql = `select text from hash_tag where post_id=${postId};`;
        // var itemSql = `select * from item_tag where post_id=${postId};`;
        // connection.query(postAndUserSql + hashSql + itemSql, function (err, result) {
        //     if (err){
        //         console.log(err);
        //         res.json({
        //             'code' : statusCode.CLIENT_ERROR
        //         })
        //     } else {
        //         var postUser = result[0][0];
        //         var hashs = result[1];
        //         var items = result[2];

        //         var likesSql = `select * from likes where post_id=${postId};`;
        //         var commentsSql = `select c.user_id, c.text, u.img_profile, u.login_id from comment as c join user as u on c.post_id=${postId} and c.user_id=u.id;`;
        //         connection.query(likesSql + commentsSql, function (err, result) {
        //             if (err){
        //                 console.log(err);
        //                 res.json({
        //                     'code' : statusCode.CLIENT_ERROR
        //                 })
        //             } else {
        //                 var options = {
        //                     appName : appName,
        //                     user : loggedUser,
        //                     postUser : postUser,
        //                     hashs : hashs,
        //                     items : items, 
        //                     likeNum : result[0].length,
        //                     comments : result[1]
        //                 }

        //                 if (loggedUser){
        //                     var meLikesSql = `select * from likes where post_id=${postId} and user_id=${(loggedUser) ? loggedUser.id : -1};`;
        //                     var meKeepsSql = `select * from keep where post_id=${postId} and user_id=${(loggedUser) ? loggedUser.id : -1};`;
        //                     connection.query(meLikesSql + meKeepsSql, function (err, result) {
        //                         if (err){
        //                             console.log(err);
        //                             res.json({
        //                                 'code' : statusCode.CLIENT_ERROR
        //                             })
        //                         } else {
        //                             var likeOnset = (result[0].length == 0) ? 0 : 1 ;
        //                             var keepOnset = (result[1].length == 0) ? 0 : 1 ;
                                    
        //                             res.render("postDetail.ejs", {options : options, likeOnset : likeOnset, keepOnset : keepOnset});
        //                         }
        //                     })
        //                 }
        //                 res.render("postDetail.ejs", {options : options, likeOnset : 0, keepOnset : 0});
        //             }
        //         })
                
                
        //     }
        // })

    },
    /*
     * api plane
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
     *  - 혹은 전체 데이터
     */
    getFeeds : function (req, res) {
        const loggedUser = res.locals.loggedUser;
        const offset = req.query.offset;

        const selectFollowSql = `select follow_target_id from follow where follower_id = ${loggedUser.id};`;
        connection.query(selectFollowSql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR
                })
            } else {
                var selectPostSql = `select * from post where `;
                for(var i = 0;i < result.length - 1;i++){
                    selectPostSql += `user_id = ${result[i].follow_target_id} or `
                }
                selectPostSql += `user_id = ${result[result.length - 1].follow_target_id} 
                order by post_time desc, post_date desc limit ${db_config.limitation} offset ${offset};`;
                connection.query(selectPostSql, function (req, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : statusCode.SERVER_ERROR
                        })
                    } else {
                        res.json({
                            'code' : statusCode.OK,
                            'data' : result
                        })
                    }
                })
            }
        })
    },
    getCommunity : function (req, res) {
        const appName = res.locals.appName;
        const user = res.locals.loggedUser;
        const offset = req.query.offset;

        //20개만 받아오기
        var sql = `select * from post order by post_time desc, post_date desc limit ${db_config.limitation} offset ${offset};`

        connection.query(sql, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR
                })
            } else {
                //이후에 res.json()으로 바꿔야 한다.
                res.render("mainfeed.ejs", {appName : appName, user : user, posts : result});
                // res.json({
                //     'code' : statusCode.OK,
                //     'data' : result
                // })
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
        var loggedUser = res.locals.loggedUser;
        var text = req.body.text;
        var hashTags = req.body.hash_tag;
        var items = {
            name : req.body.item_name,
            lowprice : req.body.item_lowprice,
            highprice : req.body.item_highprice,
            itemLink : req.body.item_link,
            itemImg : req.body.item_img
        }

        // post이미지를 여러 개 받을 수 있다는 경우의 수를 고려해서 일단은 multer.array & files로 받는다.
        var postImages = req.files;
        console.log(postImages[0].path);
        
        var insertPostSql = "";
        var insertPostParams = [];
        for(var i = 0;i < postImages.length; i++)
        {
            // 문장에 포함해서 쿼리를 보내게 되면 image path의 구분자가 사라지는 문제 발생
            // 배열을 만들어서 인자를 넘겨주는 방법으로 우회
            
            // 혹은 image url을 ,(콤마)로 연결한 다음, 데이터베이스에서 꺼내올 때, split하는 방법
            // 그럴 경우, 데이터베이스 메모리 할당을 고려해야 한다.
            // 결론, 업로드하는 이미지 갯수 제한을 두어야 한다.
            
            insertPostSql += `insert into post (image, text, post_time, post_date, user_id) values (?, '${text}', curtime(), curdate(), ${loggedUser.id});`;
            insertPostParams.push(postImages[i].path);


        }
        connection.query(insertPostSql, insertPostParams, function (err, result) {
            if (err){
                console.log(err);
                res.json({
                    'code' : statusCode.SERVER_ERROR
                })
            } else {
                var postId;
                if (result.length > 1) 
                    postId = result[0].insertId;
                else 
                    postId = result.insertId
                
                //hashTag
                var insertHashSql = "";
                for(var i = 0;i < hashTags.length; i++){
                    insertHashSql += `insert into hash_tag (post_id, text) values (${postId}, ?);`
                }
                 var insertItemSql = "";
                 var insertItemParams = [];
                 for(var i = 0;i < items.name.length; i++) {
                     insertItemSql += `insert into item_tag (post_id, name, lprice, hprice, url, picture) 
                     values(${postId}, '${items.name[i]}', ${items.lowprice[i]}, ${items.highprice[i]}, '${items.itemLink[i]}', ?);`;
                     
                     if (items.itemImg[i]){
                         insertItemParams.push(items.itemImg[i])
                     } else {
                         insertItemParams.push(serverConfig.defaultImg);
                     }

                 }
                
                 connection.query(insertHashSql + insertItemSql, hashTags + insertItemParams, function (err, result) {
                    if (err){
                        console.log(err);
                        res.json({
                            'code' : statusCode.SERVER_ERROR
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