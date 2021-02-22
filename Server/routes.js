/*
 * 모든 경로 기록하는 파일
 * routes라는 객체에 담아서 export
 */

const HOME = "/";
const JOIN = "/join";
const LOGIN = "/login";
const FIND_ID = "/find/id";
const FIND_PASSWORD = "/find/password";

const USER = "/user";
const PROFILE = "/profile";
const PROFILE_KEEP = "/profile-keep";
const PROFILE_EDIT = "/profile-edit";
const PROFILE_EDIT_IDCHECK = "/profile-edit-idcheck";

const API = "/api";
const API_DUP_IDCHECK = "/dup-idcheck";
const API_EMAIL_AUTH = "/email-auth";
const API_EMAIL_AUTH_NUMBER = "/email-auth-number";

const routes = {
    home : HOME,
    join : JOIN,
    login : LOGIN,
    findId : FIND_ID,
    findPassword : FIND_PASSWORD,
    user : USER,
    profile : PROFILE,
    profileKeep : PROFILE_KEEP,
    profileEditIdCheck : PROFILE_EDIT_IDCHECK,
    profileEdit : PROFILE_EDIT,
    api : API,
    apiDupIdCheck : API_DUP_IDCHECK,
    apiEmailAuth : API_EMAIL_AUTH,
    apiEmailAuthNumber : API_EMAIL_AUTH_NUMBER
};

module.exports = routes;