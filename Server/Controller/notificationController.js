
var statusCode = require("../config/serverStatusCode");

var admin = require("firebase-admin");
var serviceAccount = require("./serviceAccountKey.json");
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});



var notificationController = {
    pushAlarm: function (req, res) {
        console.log("send push alarm")

        var target_token = req.query.token;
            //target_token은 푸시 메시지를 받을 디바이스의 토큰값입니다
      
        let message = {
            data: {
                title: 'Test',
                message: 'sending message'
            },
            token: target_token,
        }

        admin
            .messaging()
            .send(message)
            .then(function (response) {
                console.log('Successfully sent message: : ', response)

            })
            .catch(function (err) {
                console.log('Error Sending message!!! : ', err)
            })
    }
    
}

 module.exports = notificationController;
