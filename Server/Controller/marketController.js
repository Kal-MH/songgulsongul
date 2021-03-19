 var connection = require("../db/db");
 var statusCode = require("../config/serverStatusCode");

 const marketController = {
   // 마켓 메인
   marketMain : function(req, res){
     var sql = 'SELECT * FROM market ORDER BY id DESC'; // 최신순으로 전송
     var market_item = [];

     connection.query(sql, function(err, rows){
      var resultCode = statusCode.SERVER_ERROR;
       if(err){
         console.log(err);
       }
       else{
         resultCode = statusCode.OK;
         for(let i = 0; i < rows.length(); i++){
           var data = {
             'id' : rows[i].id,
             'image' : rows[i].image,
             'name' : rows[i].name,
             'price' : rows[i].price
           };
           market_item.push(data);
         }
       }

       res.json({
         'code': resultCode,
         'marketItem': market_item
       })
     })
   },

   // 마켓 아이템 상세보기
   getStickerDetail : function(req, res){
     const sticker_id = req.params.id;
     var item_detail = [];
     var seller_info = [];

     var sql = 'SELECT * FROM market JOIN user ON market.user_id = user.id WHERE market.id = ?';
     connection.query(sql, [sticker_id], function(err, rows){
       var resultCode = statusCode.CLIENT_ERROR;
       if(err){
         console.log(err);
       }
       else{
         resultCode = statusCode.OK;
         // 마켓 스티커 data
         var data = {
           'image': rows[0].image,
           'name': rows[0].name,
           'price': rows[0].price,
           'text': rows[0].text
         };
         item_detail.push(data);

         // 판매자 data
         var seller = {
           'profileImage': rows[0].img_profile,
           'userId': rows[0].login_id
         };
         seller_info.push(seller);
       }

       res.json({
         'code': resultCode,
         'itemDetail': item_detail,
         'seller': seller_info
       })
     })
  },

  // 마켓 스티커 구매 --> 이미지 저장 보류
  stickerBuy : function(req, res){
    const sticker_id = req.body.stickerId;
    const user_id = req.body.userId;
    var params = [sticker_id, user_id];

    var sql = 'UPDATE user SET point = point - (SELECT price FROM market WHERE id = ?)AS mpoint WHERE login_id = ? AND point >= mpoint' // 구매자의 포인트 차감
    connection.query(sql, params, function(err, rows){
      var resultCode = statusCode.CLIENT_ERROR;
      if(err){
        console.log(err);
      }
      else{
        resultCode = statusCode.OK;
      }

      res.json({
        'code': resultCode
      })
    })
  },

  // 마켓 스티커 검색(기본)
  getStickerSearch : function(req, res){
    const search_word = req.query.searchWord;
    var market_item = [];

    var sql = `SELECT * FROM market WHERE name like '%${search_word}%';`;
    connection.query(sql, function(err, rows){
      var resultCode = statusCode.CLIENT_ERROR;
      if(err){
        console.log(err);
      }
      else{
        resultCode = statusCode.OK;

        for(let i = 0; i < rows.length(); i++){
          var data = {
            'id': rows[i].id,
            'image': rows[i].image,
            'name': rows[i].name,
            'price': rows[i].price
          };
          market_item.push(data);
        }
      }

      res.json({
        'code': resultCode,
        'marketItem': market_item
      })
    })
  },

  // 마켓 스티커 검색(낮은 가격순)
  getSearchPrice : function(req, res){
    const search_word = req.query.searchWord;
    var market_item = [];

    var sql = `SELECT * FROM market WHERE name like '%${search_word}%' ORDER BY price ASC;`;
    connection.query(sql, function(err, rows){
      var resultCode = statusCode.CLIENT_ERROR;
      if(err){
        console.log(err);
      }
      else{
        resultCode = statusCode.OK;

        for(let i = 0; i < rows.length(); i++){
          var data = {
            'id': rows[i].id,
            'image': rows[i].image,
            'name': rows[i].name,
            'price': rows[i].price
          };
          market_item.push(data);
        }
      }

      res.json({
        'code': resultCode,
        'marketItem': market_item
      })
    })
  },

  // 마켓 스티커 검색(최신순)
  getSearchDate : function(req, res){
    const search_word = req.query.searchWord;
    var market_item = [];

    var sql = `SELECT * FROM market WHERE name like '%${search_word}%' ORDER BY id DESC;`;
    connection.query(sql, function(err, rows){
      var resultCode = statusCode.CLIENT_ERROR;
      if(err){
        console.log(err);
      }
      else{
        resultCode = statusCode.OK;

        for(let i = 0; i < rows.length(); i++){
          var data = {
            'id': rows[i].id,
            'image': rows[i].image,
            'name': rows[i].name,
            'price': rows[i].price
          };
          market_item.push(data);
        }
      }

      res.json({
        'code': resultCode,
        'marketItem': market_item
      })
    })
  }
 }

module.exports = marketController;
