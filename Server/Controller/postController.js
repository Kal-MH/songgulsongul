const connection = require("../db/db");
const postController_subFunc = require("./postController_sub");
const db_config = require("../db/db_config");
const statusCode = require("../config/serverStatusCode");
const serverConfig = require("../config/serverConfig");

const s3 = require("../config/s3");

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
         * req.body =  {
            user_id: '2',
            text: '',
            hash_tag: '',
            ccl: [ '1', '1', '1', '1', '1' ],
            item_name: [
                '갤럭시 A51 SM-A516N 잇item 모던 기본 폰케이스',
                '[BEST ITEM] 김혜수 핸드폰줄 아르노폰스트랩 노트 갤럭시 카드수납 지갑 목걸이핸드폰'
            ],
            item_lowprice: [ '6550', '10800' ],
            item_highprice: [ '', '' ],
            item_link: [
                'https://search.shopping.naver.com/gate.nhn?id=27701808406',
                'https://search.shopping.naver.com/gate.nhn?id=82462722047'
            ],
            item_img: [
                'https://shopping-phinf.pstatic.net/main_2770180/27701808406.jpg',
                'https://shopping-phinf.pstatic.net/main_8246272/82462722047.8.jpg'
            ],
            item_brand: [ '', '' ],
            item_category1: [ '휴대폰케이스', '휴대폰케이스' ],
            item_category2: [ '기타케이스', '기타케이스' ]
            }
         */
        var loggedUser = req.body.user_id * 1;
        var text = req.body.text;
        var ccl = req.body.ccl;
        var hashTags = req.body.hash_tag;
        var items = {
            name: req.body.item_name,
            lowprice: req.body.item_lowprice,
            highprice: req.body.item_highprice,
            itemLink: req.body.item_link,
            itemImg: req.body.item_img,
            brand : req.body.item_brand,
            category1 : req.body.item_category1,
            category2 : req.body.item_category2
        }

        console.log(req.file);
        var postImages = req.file.location;


        if (req.file == undefined || ccl.length != 5)
        {
            res.json({
                'code' : statusCode.CLIENT_ERROR
            })
        } else {
            for(var i = 0; i < ccl.length; i++)
                ccl[i] = ccl[i] * 1;

            var updatePointinsertPostSql = `insert into post (image, text, post_time, post_date, user_id, ccl_cc, ccl_a, ccl_nc, ccl_nd, ccl_sa)
            values (?, '${text}', curtime(), curdate(), ${loggedUser}, ?);`;
            var insertPostParams = [postImages, req.body.ccl];

            //current date 계산
            var date = new Date();
            var yearMonthDate = date.getFullYear() + "-" + ((date.getMonth() + 1) < 10 ? '0' + (date.getMonth() + 1) : (date.getMonth() + 1)) + "-" + date.getDate();
            updatePointinsertPostSql += `select post_time, post_date from post where post_date = '${yearMonthDate}' and user_id = ${loggedUser} limit ${db_config.limitation};`;

            connection.query(updatePointinsertPostSql, insertPostParams, function (err, result) {
                if (err) {
                    console.log(err);
                    res.json({
                        'code': statusCode.SERVER_ERROR
                    })
                } else {
                    var postId = result[0].insertId;

                    // 오늘 날짜의 게시글이 5개 이하이면 포인트 100을 추가로 반영한다.
                    var updatePointinsertHashItemsSql = "";
                    if (result[1].length <= 5)
                        updatePointinsertHashItemsSql += `update user set point = point + 100 where id = ${loggedUser};`
                    console.log(result[1]);
                    console.log(updatePointinsertHashItemsSql);

                    postController_subFunc.updatePointInsertHashItem(res, postId, hashTags, items, updatePointinsertHashItemsSql);
                }
            })
        }

    },
    postUpdate: function (req, res) {
        var postId = req.body.post_id;
        var loggedUser = req.body.user_id;

        if (postId == undefined){
            res.json({
                'code' : statusCode.CLIENT_ERROR
            })
        } else {

            console.log(req.body);

            var updatePostSql = `update post set text='${req.body.text}', ccl_cc=?, ccl_a=?, ccl_nc=?, ccl_nd=?, ccl_sa=? where id = ${postId};`;
            connection.query(updatePostSql, req.body.ccl, function (err, result) {
                if (err) {
                    console.log(err);
                    res.json({
                        'code': statusCode.SERVER_ERROR
                    })
                } else {
                    //hashTag
                    var deleteHashTagsSql = `delete from hash_tag where post_id=${postId};`;
                    var insertHashSql = "";
                    var hashTags = req.body.hash_tag;
                    if (hashTags.length > 0) {
                        for (var i = 0; i < hashTags.length; i++) {
                            if (hashTags[i] != '')
                                insertHashSql += `insert into hash_tag (post_id, text) values (${postId}, "${hashTags[i]}");`
                        }
                    }
                    connection.query(deleteHashTagsSql + insertHashSql, function (err, result) {
                        if (err) {
                            console.log(err);
                            res.json({
                                'code': statusCode.SERVER_ERROR
                            })
                        } else {
                            var deleteItemTagSql = `delete from item_tag where post_id=${postId};`;
                            var insertItemSql = "";
                            var insertItemParams = [];

                            /*
                             * req.body.item_tag: [
                                    {
                                    brand: '',
                                    category1: '휴대폰케이스',
                                    category2: '기타케이스',
                                    hprice: '',
                                    id: 0,
                                    lprice: '6550',
                                    name: '갤럭시 A51 SM-A516N 잇item 모던 기본 폰케이스',
                                    picture: 'https://shopping-phinf.pstatic.net/main_2770180/27701808406.jpg',
                                    post_id: 0,
                                    url: 'https://search.shopping.naver.com/gate.nhn?id=27701808406'
                                    }
                                ]
                             */

                            if (req.body.item_tag.length > 0) {
                                for (var i = 0; i < req.body.item_tag.length; i++) {
                                    var itemImg;
                                    if (req.body.item_tag[i].picture == '') {
                                        itemImg = "/public/default/to-do-list.png";
                                    } else {
                                        itemImg = req.body.item_tag[i].picture;
                                    }

                                    insertItemSql += `insert into item_tag (post_id, name, lprice, hprice, url, picture, brand, category1, category2) values(${postId}, ?, ?, ?, ?, ?, ?, ?, ?);`;
                                    insertItemParams.push(req.body.item_tag[i].name);
                                    insertItemParams.push(req.body.item_tag[i].lprice ? Number(req.body.item_tag[i].lprice) : -1);
                                    insertItemParams.push(req.body.item_tag[i].hprice ? Number(req.body.item_tag[i].hprice) : -1);
                                    insertItemParams.push(req.body.item_tag[i].url);
                                    insertItemParams.push(itemImg);
                                    insertItemParams.push(req.body.item_tag[i].brand);
                                    insertItemParams.push(req.body.item_tag[i].category1);
                                    insertItemParams.push(req.body.item_tag[i].category2);
                                }
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
        }
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
                var fileName = result[0].image.split('/')[3];
                console.log(fileName);
                var deletePostSql = `delete from post where image = ?;`;
                connection.query(deletePostSql, fileImage, function (err, result) {
                    if (err) {
                        console.log(err);
                        res.json({
                            'code': statusCode.SERVER_ERROR
                        })
                    } else {
                        s3.deleteObject({
                            Bucket : serverConfig.s3BuckerName,
                            Key: fileName
                        }, function (err, data) {
                            if (err) {
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
}

module.exports = postController;
