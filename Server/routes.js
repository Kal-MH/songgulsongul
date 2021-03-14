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
const USER_FOLLOW = "/user-follow";
const USER_UNFOLLOW = "/user-unfollow";
const USER_FOLLOW_LIST = "/user-follow-list";
const USER_FOLLOWER_LIST = "/user-follower-list";
const USER_DATA_DELETE = "/user-data-delete";
const USER_LFOLLOW_LIST = "/user-lfollow-list";

//웹상에서 보기위한 임시 라우터
//로그인한 후, 내 프로필 보기
const ME = "/me";
const LOGOUT = "/logout";

const API = "/api";
const API_DUP_IDCHECK = "/dup-idcheck";
const API_EMAIL_AUTH = "/email-auth";
const API_EMAIL_AUTH_NUMBER = "/email-auth-number";

const API_NAVER_ITEM_TAG = "/naver/item-tag";
const API_NAVER_ITEM_TAG_CALLBACK = "/naver/item-tag/callback";

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
    userFollow : USER_FOLLOW,
    userUnfollow : USER_UNFOLLOW,
    userFollowList : USER_FOLLOW_LIST,
    userFollowerList : USER_FOLLOWER_LIST,
    userDataDelete : USER_DATA_DELETE,
    userLFollowList : USER_LFOLLOW_LIST,
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
