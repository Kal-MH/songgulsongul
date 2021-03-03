/*
 * 모든 경로 기록하는 파일
 * routes라는 객체에 담아서 export
 */

//home Router
const HOME = "/";
const JOIN = "/join";
const LOGIN = "/login";
const FIND_ID = "/find/id";
const FIND_PASSWORD = "/find/password";

//user Router
const USER = "/user";
const PROFILE = "/profile";
const PROFILE_KEEP = "/profile-keep";
const PROFILE_EDIT = "/profile-edit";
const PROFILE_EDIT_IDCHECK = "/profile-edit-idcheck";

//post Router
const POST = "/post";
const POST_MAINFEED = "/main-feed";
const POST_UPLOAD = "/upload";
const POST_SEARCH_TAG = "/search/tag";
const POST_SEARCH_ID = "/search/id"

//api Router
const API = "/api";
const API_DUP_IDCHECK = "/dup-idcheck";
const API_EMAIL_AUTH = "/email-auth";
const API_EMAIL_AUTH_NUMBER = "/email-auth-number";

//img tag api temporary
const API_NAVER_ITEM_TAG = "/naver/item-tag";
const API_NAVER_ITEM_TAG_CALLBACK = "/naver/item-tag/callback";

//웹상에서 보기위한 임시 라우터
//로그인한 후, 내 프로필 보기
const ME = "/me";
const LOGOUT = "/logout";
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
    post : POST,
    postMainFeed : POST_MAINFEED,
    postUpload : POST_UPLOAD,
    postSearchTag : POST_SEARCH_TAG,
    postSearchId : POST_SEARCH_ID,
    api : API,
    apiDupIdCheck : API_DUP_IDCHECK,
    apiEmailAuth: API_EMAIL_AUTH,
    apiEmailAuthNumber : API_EMAIL_AUTH_NUMBER,
    apiNaverItemtag : API_NAVER_ITEM_TAG,
    apiNaverItemtagCallback : API_NAVER_ITEM_TAG_CALLBACK,
    me : ME,
    logout : LOGOUT
}

module.exports = routes;
