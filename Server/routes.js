/*
 * 모든 경로 기록하는 파일
 * routes라는 객체에 담아서 export 
 */

const HOME = "/";

const USER = "/user";
const USER_JOIN = "/join";
const USER_LOGIN = "/login";

const routes = {
    home : HOME,
    user : USER,
    userJoin : USER_JOIN,
    userLogin : USER_LOGIN
}

module.exports = routes;