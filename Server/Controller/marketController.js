const connection = require("../db/db");
const statusCode = require("../config/serverStatusCode");
const db_config = require("../db/db_config");

const fs = require("fs");
const path = require("path");
const mime = require("mime");

const marketController = {
   // 마켓 메인
   marketMain : function(req, res){
     const offset = req.query.offset;
     var market_item = [];
     var sql = [];

     if(offset == undefined || offset == 0){
       sql += `SELECT * FROM market ORDER BY id ASC limit ${db_config.limitation};`; // 저장순으로 전송
     }
     else{
       sql +=  `SELECT * FROM market WHERE id > ${offset} ORDER BY id ASC limit ${db_config.limitation};`;
     }

     connection.query(sql, function(err, rows){
      var resultCode = statusCode.SERVER_ERROR;
       if(err){
         console.log(err);
         res.json({
           'code': resultCode
         })
       }
       else{
         resultCode = statusCode.OK;
         for(let i = 0; i < rows.length; i++){
           var data = {
             'id' : rows[i].id,
             'image' : rows[i].image,
             'name' : rows[i].name,
             'price' : rows[i].price
           };
           market_item.push(data);
         }

         res.json({
           'code': resultCode,
           'marketItem': market_item
         })
       }
     })
   },

   // 마켓 아이템 상세보기
   // 사용자의 현재 보유 포인트도 함께 전송한다. --> front에서 스티커 가격과 비교 후 buy버튼 클릭 시 반영
   getStickerDetail : function(req, res){
     const sticker_id = req.query.sticker_id;
     const user_id = req.query.user_id;

     var sticker_detail = [];
     var seller_info = [];
     var user_point;
     var params = [sticker_id, user_id];

     var sql1 = 'SELECT * FROM market JOIN user ON market.user_id = user.id WHERE market.id = ?;';
     var sql2 = 'SELECT point FROM user WHERE id = ?;';
     connection.query(sql1 + sql2, params, function(err, rows){
       var resultCode = statusCode.SERVER_ERROR;
       if(err){
         console.log(err);
         res.json({
           'code': resultCode
         })
       }
       else{
         resultCode = statusCode.OK;

         // 스티커 data
         var data = {
           'image': rows[0][0].image,
           'name': rows[0][0].name,
           'price': rows[0][0].price,
           'text': rows[0][0].text
         };
         sticker_detail.push(data);

         // 판매자 data
         var seller = {
           'img_profile': rows[0][0].img_profile,
           'login_id': rows[0][0].login_id
         };
         seller_info.push(seller);

         // 사용자의 보유 point
         user_point = rows[1][0].point;

         res.json({
           'code': resultCode,
           'stickerDetail': sticker_detail,
           'sellerInfo': seller_info,
           'userPoint': user_point
         })
       }
     })
  },

  // 마켓 스티커 구매
  stickerBuy : function(req, res){
    const sticker_id = req.query.sticker_id;
    const user_id = req.query.user_id;
    var params = [sticker_id, user_id, sticker_id];

    var sql = 'UPDATE user SET point = point - (SELECT price FROM market WHERE id = ?) WHERE id = ?;'; // 구매자의 포인트 차감
    var sql2 = 'SELECT image FROM market WHERE id = ?;';
    connection.query(sql + sql2, params, function(err, rows){
      var resultCode = statusCode.SERVER_ERROR;
      if(err){
        console.log(err);
        res.json({
          'code': resultCode
        })
      }
      else{
        resultCode = statusCode.OK;
        res.json({
          'code': resultCode,
          'image': rows[1][0].image
        })
      }
    })
  },

  // 마켓 스티커 검색(기본-저장순)
  getStickerSearch : function(req, res){
    const search_word = req.query.search_word;
    const offset = req.query.offset;
    var market_item = [];
    var sql =[];

    if(offset === undefined || offset === 0){
      sql += `SELECT * FROM market WHERE name like '%${search_word}%' ORDER BY id ASC limit ${db_config.limitation};`;
    }
    else{
      sql += `SELECT * FROM market WHERE name like '%${search_word}%' ORDER BY id ASC limit ${offset}, ${db_config.limitation};`;
    }

    connection.query(sql, function(err, rows){
      var resultCode = statusCode.SERVER_ERROR;
      if(err){
        console.log(err);
        res.json({
          'code': resultCode
        })
      }
      else{
        resultCode = statusCode.OK;

        for(let i = 0; i < rows.length; i++){
          var data = {
            'id': rows[i].id,
            'image': rows[i].image,
            'name': rows[i].name,
            'price': rows[i].price
          };
          market_item.push(data);
        }
        console.log(market_item);
        res.json({
          'code': resultCode,
          'marketItem': market_item
        })
      }
    })
  },

  // 마켓 스티커 검색(낮은 가격순)
  getSearchPrice : function(req, res){
    const search_word = req.query.search_word;
    const offset = req.query.offset;
    var market_item = [];
    var sql = [];

    if(offset === undefined || offset === 0)
      sql += `SELECT * FROM market WHERE name like '%${search_word}%' ORDER BY price ASC limit ${db_config.limitation};`;
    else
      sql += `SELECT * FROM market WHERE name like '%${search_word}%' ORDER BY price ASC limit ${offset}, ${db_config.limitation};`;
    connection.query(sql, function(err, rows){
      var resultCode = statusCode.SERVER_ERROR;
      if(err){
        console.log(err);
        res.json({
          'code': resultCode
        })
      }
      else{
        resultCode = statusCode.OK;

        for(let i = 0; i < rows.length; i++){
          var data = {
            'id': rows[i].id,
            'image': rows[i].image,
            'name': rows[i].name,
            'price': rows[i].price
          };
          market_item.push(data);
        }

        res.json({
          'code': resultCode,
          'marketItem': market_item
        })
      }
    })
  },

  // 마켓 스티커 검색(최신순)
  getSearchDate : function(req, res){
    const search_word = req.query.search_word;
    const offset = req.query.offset;
    var market_item = [];
    var sql = [];

    if(offset === undefined || offset === 0)
      sql += `SELECT * FROM market WHERE name like '%${search_word}%' ORDER BY id DESC limit ${db_config.limitation};`;
    else
      sql += `SELECT * FROM market WHERE name like '%${search_word}%' ORDER BY id DESC limit ${offset}, ${db_config.limitation};`;
    connection.query(sql, function(err, rows){
      var resultCode = statusCode.SERVER_ERROR;
      if(err){
        console.log(err);
        res.json({
          'code': resultCode
        })
      }
      else{
        resultCode = statusCode.OK;

        for(let i = 0; i < rows.length; i++){
          var data = {
            'id': rows[i].id,
            'image': rows[i].image,
            'name': rows[i].name,
            'price': rows[i].price
          };
          market_item.push(data);
        }

        res.json({
          'code': resultCode,
          'marketItem': market_item
        })
      }
    })
  },

  // 마켓 이미지 업로드
  marketUpload : function(req, res){
    var name = req.body.name;
    var text = req.body.text;
    var price = Number(req.body.price);
    var user_id = Number(req.body.user_id);
    var image = req.file.path;
    image = "/"+image.replace(/\\/g, '/');

    var params = [image, name, text, price, user_id];
    var sql = 'INSERT INTO market(image, name, text, price, user_id) values(?, ?, ?, ?, ?);';
    sql += 'SELECT id FROM market ORDER BY id DESC limit 1;';
    connection.query(sql, params, function(err, result){
      var resultCode = statusCode.SERVER_ERROR;
      if(err){
        console.log(err);
        res.json({
          'code': resultCode,
        })
      }
      else{
        resultCode = statusCode.OK;
        var market_id = result[1][0].id;
        res.json({
          'code': resultCode,
          'market_id': market_id
        })
      }
    })
  }
 }

module.exports = marketController;
