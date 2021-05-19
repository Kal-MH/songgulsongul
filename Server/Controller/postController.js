const connection = require("../db/db");
const postController_subFunc = require("./postController_sub");
const db_config = require("../db/db_config");
const statusCode = require("../config/serverStatusCode");
const serverConfig = require("../config/serverConfig");

const fs = require("fs");
const path = require("path");
const mime = require("mime");

const postController = {
    getPostDetail: function (req, res) {
        const loggedUser = req.query.userid; // 현재 로그인한 사용자 id(loginId가 아님)
        const postId = req.params.id; // 게시글의 id
       // const postId = req.query.id; // 게시글의 id

        if (loggedUser == undefined || loggedUser == "" || postId == undefined || postId == "") {
            res.json({
                'code': statusCode.CLIENT_ERROR,
                'data': null
            })
        } else {
            // 게시글과 관련된 정보를 가져오는 쿼리문
            // 게시글, 작성자, 해시태그, 아이템 태그, 좋아요, 코멘트를 차례로 가져오고 있다.
            // 댓글은 댓글 레코드 id에 상관없이, 최신 댓글순으로 정렬한 후, 가져오고 있다.(20개씩 받아오도록 설정했으나, 추후 수정될 수 있음)
            var selectPostDetailSql = `select p.id, p.image, p.text, p.post_time, p.post_date, p.user_id, p.ccl_cc, p.ccl_a, p.ccl_nc, p.ccl_nd, p.ccl_sa, u.login_id, u.img_profile 
            from post as p join user as u on p.id = ${postId} and p.user_id = u.id;`;
            selectPostDetailSql += `select text from hash_tag where post_id=${postId};`;
            selectPostDetailSql += `select * from item_tag where post_id=${postId};`;
            selectPostDetailSql += `select * from likes where post_id=${postId};`;
            selectPostDetailSql += `select c.id, c.user_id, c.text, u.img_profile, u.login_id, c.c_date, c.c_time from comment as c 
            join user as u on c.post_id=${postId} and c.user_id=u.id order by c.c_date, c.c_time, c.id limit ${db_config.limitation};`;
            connection.query(selectPostDetailSql, function (err, result) {
                if (err) {
                    console.log(err);
                    res.json({
                        'code': statusCode.SERVER_ERROR,
                        'data': null
                    })
                } else if (result[0].length == 0) {//postId가 잘못 넘어온 경우 에러처리
                    res.json({
                        'code': statusCode.CLIENT_ERROR,
                        'data': null
                    })
                } else {
                    var postData = result;

                    //현재 사용자가 해당 게시글을 좋아요, 보관하기를 눌렀는 지 체크하기 위한 쿼리문
                    var meLikesSql = `select * from likes where post_id=${postId} and user_id=${loggedUser};`;
                    var meKeepsSql = `select * from keep where post_id=${postId} and user_id=${loggedUser};`;
                    connection.query(meLikesSql + meKeepsSql, function (err, result) {
                        if (err) {
                            console.log(err);
                            res.json({
                                'code': statusCode.SERVER_ERROR,
                                'data': null
                            })
                        } else {
                            var likeKeep = {
                                likeOnset: (result[0].length == 0) ? 0 : 1,
                                keepOnset: (result[1].length == 0) ? 0 : 1
                            }
                            postController_subFunc.getPostSendData(req, res, statusCode.OK, postData, likeKeep, 0);
                        }
                    })
                }
            })
        }
    },
    getFeeds: function (req, res) {
        const loggedUser = req.query.userid;
        const offset = req.query.offset;

        if (loggedUser == undefined) {
            res.json({
                'code': statusCode.CLIENT_ERROR,
                'data': null
            })
        } else {
            //현재 사용자의 팔로우 목록 불러오기
            const selectFollowSql = `select follow_target_id from follow where follower_id = ${loggedUser};`;
            connection.query(selectFollowSql, function (err, result) {
                if (err) {
                    console.log(err);
                    res.json({
                        'code': statusCode.SERVER_ERROR,
                        'data': null
                    })
                } else if (result.length == 0) { // 팔로우하는 사람이 없는 경우
                    //통신에는 성공했으므로 일단 OK로 상태코드 저장
                    //상태코드 NO가 추가됨 -> 추후에 OK에서 NO로 변경 가능함.
                    res.json({
                        'code': statusCode.OK,
                        'data': result
                    })
                } else {
                    //팔로우한 유저의 게시글 아이디 가져오기
                    //처음 불러온 것이라면 최근 게시글에서 20개를 가져옴.
                    //offset이 지정된 경우, offset값을 기준으로 20개 가져옴
                    var selectPostSql = `select id from post where (`;
                    for (var i = 0; i < result.length - 1; i++) {
                        selectPostSql += `user_id = ${result[i].follow_target_id} or `
                    }
                    if (offset == undefined) {
                        selectPostSql += `user_id = ${result[result.length - 1].follow_target_id} )
                        order by post_date desc, post_time desc, id desc limit ${db_config.limitation};`;
                    } else {
                        selectPostSql += `user_id = ${result[result.length - 1].follow_target_id} ) and id < ${offset}
                        order by post_date desc, post_time desc, id desc limit ${db_config.limitation};`;
                    }
                    connection.query(selectPostSql, function (req, result) {
                        if (err) {
                            console.log(err);
                            res.json({
                                'code': statusCode.SERVER_ERROR,
                                'data': null
                            })
                        } else {
                            if (result.length == 0) { //더 이상 가져올 게시글이 없는 경우.
                                res.json({
                                    'code': statusCode.OK,
                                    'data': result
                                })
                            } else {
                                //각 게시글 세부정보 가져오기 (image, test, post_date, post_time, user_id, login_id, img_profile)
                                var selectPostFeedSql = "";

                                for (var i = 0; i < result.length; i++) {
                                    selectPostFeedSql += `select p.id, p.image, p.text, p.post_date, p.post_time, p.user_id, u.login_id, u.img_profile from post as p join user as u 
                                    on p.id = ${result[i].id} and p.user_id = u.id;`;
                                    selectPostFeedSql += `select id from comment where post_id = ${result[i].id};`;
                                    selectPostFeedSql += `select * from likes where post_id = ${result[i].id};`;
                                    selectPostFeedSql += `select * from likes where post_id = ${result[i].id} and user_id = ${loggedUser};`;
                                    selectPostFeedSql += `select id from keep where post_id = ${result[i].id} and user_id = ${loggedUser};`;
                                }
                                connection.query(selectPostFeedSql, function (req, result) {
                                    if (err) {
                                        console.log(err);
                                        res.json({
                                            'code': statusCode.SERVER_ERROR,
                                            'data': null
                                        })
                                    } else {
                                        postController_subFunc.getPostSendData(req, res, statusCode.OK, result, null, 1)
                                    }
                                })
                            }
                        }
                    })
                }
            })
        }

    },
    getCommunity: function (req, res) {
        const offset = req.query.offset;

        //offset값을 기준으로 20개 가져옴 -> offset이 지정되어 있지 않다면 최근 게시글 기준으로 20개 가져옴
        var selectPostSql;
        if (offset == undefined || offset == 0)
            selectPostSql = `select * from post order by post_date desc, post_time desc, id desc limit ${db_config.limitation};`
        else
            selectPostSql = `select * from post where id < ${offset} order by post_date desc, post_time desc, id desc limit ${db_config.limitation};`

        connection.query(selectPostSql, function (err, result) {
            if (err) {
                console.log(err);
                res.json({
                    'code': statusCode.SERVER_ERROR,
                    'data': null
                })
            } else {
                res.json({
                    'code': statusCode.OK,
                    'data': result
                })
            }
        })

    },
    getSearch: function (req, res) {
        const method = req.query.method;

        if (method == undefined || method == "" || req.query.keyword == undefined || req.query.keyword == "") {
            res.json({
                'code': statusCode.CLIENT_ERROR,
                'data': null
            })
        } else {
            if (method == 'tag') {
                postController_subFunc.getSearchTag(req, res);
            } else if (method == 'id') {
                postController_subFunc.getSearchId(req, res);
            }
        }
    },

    getSearchTag: function (req, res) {
        if ( req.query.keyword == undefined || req.query.keyword == "") {
            res.json({
                'code': statusCode.CLIENT_ERROR,
                'data': null
            })
        } else {
            postController_subFunc.getSearchTag(req, res);
        }
    },
    getSearchId: function (req, res) {
        if (req.query.keyword == undefined || req.query.keyword == "") {
            res.json({
                'code': statusCode.CLIENT_ERROR,
                'data': null
            })
        } else {
            postController_subFunc.getSearchId(req, res);
        }
    },

    /*
        확인 사항
        1. 넘어오는 데이터 구조
        2. 데이터 필드 이름
        3. client error 에러 처리
            -> 값 유효성 에러 처리
            -> db connection 이후, result == 0 인 경우에 대한 에러 처리
     */
    postUpload: function (req, res) {
        /*
         * 현재 넘겨받는 데이터의 임시 구조
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
        var loggedUser = req.body.user_id;
        var text = req.body.text;
        var hashTagsAndItemTags = req.body.hash_tag;
        var items = {
            name: req.body.item_name,
            lowprice: req.body.item_lowprice,
            highprice: req.body.item_highprice,
            itemLink: req.body.item_link,
            itemImg: req.body.item_img
        }

        var postImages = req.file.path;

        var updatePointinsertPostSql = `insert into post (image, text, post_time, post_date, user_id, ccl_cc, ccl_a, ccl_nc, ccl_nd, ccl_sa) 
        values (?, '${text}', curtime(), curdate(), ${loggedUser}, ?);`;
        var insertPostParams = [postImages, req.body.ccl];

        //current date 계산
        var date = new Date();
        var yearMonthDate = date.getFullYear() + "-" + ((date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-" + date.getDate();
        console.log(yearMonthDate);
        updatePointinsertPostSql += `select post_time, post_date from post where post_date = '${yearMonthDate}' and user_id = ${loggedUser} limit ${db_config.limitation};`;

        connection.query(updatePointinsertPostSql, insertPostParams, function (err, result) {
            if (err) {
                console.log(err);
                res.json({
                    'code': statusCode.SERVER_ERROR
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

                for (var i = 0; i < hashTagsAndItemTags.length; i++) {
                    updatePointinsertHashSql += `insert into hash_tag (post_id, text) values (${postId}, ?);`
                }
                //itemTag
                //배열로 넘겨주기 위해 해시태그와 item태그를 한 배열로 넣는다.(해시태그 먼저, 아이템 태그는 그 뒤에)
                var insertItemSql = "";
                for (var i = 0; i < items.name.length; i++) {
                    insertItemSql += `insert into item_tag (post_id, name, lprice, hprice, url, picture) 
                     values(${postId}, '${items.name[i]}', ${items.lowprice[i]}, ${items.highprice[i]}, ?, ?);`;

                    hashTagsAndItemTags.push(items.itemLink[i]);
                    if (items.itemImg[i]) {
                        hashTagsAndItemTags.push(items.itemImg[i])
                    } else {
                        hashTagsAndItemTags.push(serverConfig.defaultImg);
                    }

                }
                connection.query(updatePointinsertHashSql + insertItemSql, hashTagsAndItemTags, function (err, result) {
                    if (err) {
                        console.log(err);
                        res.json({
                            'code': statusCode.SERVER_ERROR
                        })
                    }
                    else {
                        res.json({
                            'code': statusCode.OK
                        })
                    }
                })
            }
        })
    },
    postUpdate: function (req, res) {
        var postId = req.body.post_id;
        var loggedUser = req.body.user_id;
        var file = req.file;

        console.log(req.body);

        //안드로이드에서 넘어오는 값에 따라서 수정이 필요한 부분
        var image = (file) ? req.file.path : req.body.img_post_link;

        var updatePostSql = `update post set image=?, text=?, post_time=curtime(), post_date=curdate(), user_id=? where id=${postId};`;
        var updatePostParams = [image, req.body.text, loggedUser];

        connection.query(updatePostSql, updatePostParams, function (err, result) {
            if (err) {
                console.log(err);
                res.json({
                    'code': statusCode.SERVER_ERROR
                })
            } else {
                //hashTag
                var deleteHashTagsSql = `delete from hash_tag where post_id=${postId};`;
                var insertHashSql = "";
                var hashTags = req.body.hashTags;
                for (var i = 0; i < hashTags.length; i++) {
                    //현재는 input을 동적으로 조정할 수 없기 때문에 추가된 if문 
                    //이후에는 삭제되면 클라이언트에서 넘어온 배열을 기준으로 sql문을 작성하게 된다.
                    if (hashTags[i] != '')
                        insertHashSql += `insert into hash_tag (post_id, text) values (${postId}, ?);`
                }
                connection.query(deleteHashTagsSql + insertHashSql, hashTags, function (err, result) {
                    if (err) {
                        console.log(err);
                        res.json({
                            'code': statusCode.SERVER_ERROR
                        })
                    } else {
                        var deleteItemTagSql = `delete from item_tag where post_id=${postId};`;
                        var insertItemSql = "";
                        var insertItemParams = [];
                        for (var i = 0; i < req.body.itemName.length; i++) {
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
                            if (err) {
                                console.log(err);
                                res.json({
                                    'code': statusCode.SERVER_ERROR
                                })
                            } else {
                                res.json({
                                    'code': statusCode.OK
                                })
                            }
                        })
                    }
                })
            }
        })
    },
    postDelete: function (req, res) {
        const postId = req.query.postid;
        const loggedUser = req.query.userid;

        var selectPostSql = `select image from post where id = ${postId} and user_id = ${loggedUser};`;
        connection.query(selectPostSql, function (err, result) {
            if (err) {
                console.log(err);
                res.json({
                    'code': statusCode.SERVER_ERROR,
                })
            } else {
                var fileImage = result[0].image;
                var filePath = `./${fileImage}`;
                var deletePostSql = `delete from post where image = ?;`;
                connection.query(deletePostSql, fileImage, function (err, result) {
                    if (err) {
                        console.log(err);
                        res.json({
                            'code': statusCode.SERVER_ERROR
                        })
                    } else {
                        if (fs.existsSync(filePath)) {
                            fs.unlink(filePath, function (err) {
                                if (err) {
                                    console.log(err);
                                    res.json({
                                        'code': statusCode.SERVER_ERROR,
                                    })
                                } else {
                                    res.json({
                                        'code': statusCode.OK
                                    })
                                }
                            })
                        } else {
                            res.json({
                                'code': statusCode.OK,
                            })
                        }
                    }
                })
            }
        })
    },
    postDownload: function (req, res) {
        var postId = req.query.postid;

        var sql = `select image from post where id = ${postId};`;
        connection.query(sql, function (err, result) {
            if (err) {
                console.log(err);
                res.json({
                    'code': statusCode.SERVER_ERROR
                })
            } else {
                //s3로 확장하게 되면 경로설정 수정할 필요성 있음
                var file = `${result[0].image}`;
                console.log(file);

                res.json({
                    'code': statusCode.OK,
                    'data': {
                        'imgPath': file
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