/*
 *  200 : 요청 처리 완료
 *  204 : client error
 *      - 중복, 잘못된 입력, 원하는 데이터를 찾을 수 없음 등과 같은 상태를 나타냄
 *      - 사용자 입력의 parameter가 필요한 쿼리문을 사용했을 때의 발생하는 오류는 204코드를 보냄
 *  500 : server error
 *      - server error, database connection error 등의 상태를 나타냄
 *      - parameter가 필요하지 않는 쿼리문 혹은 update문등에서 발생하는 오류는 500코드를 보냄  
 */

const statusCode = {
    OK : 200,
    CLIENT_ERROR : 204,
    SERVER_ERROR : 500
}

module.exports = statusCode;