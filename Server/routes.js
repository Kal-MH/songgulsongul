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
const POST_COMMUNITY = "/community";
const POST_FEEDS = "/feeds";
const POST_DETAIL = "/:id";
const POST_SEARCH = "/search";
//----------------------delete---------------
const POST_SEARCH_TAG = "/search/tag";
const POST_SEARCH_ID = "/search/id"
//----------------------delete---------------
const POST_UPLOAD = "/upload";
const POST_UPDATE = "/update/:id"

//api Router
const API = "/api";
const API_DUP_IDCHECK = "/dup-idcheck";
const API_EMAIL_AUTH = "/email-auth";
const API_EMAIL_AUTH_NUMBER = "/email-auth-number";

const API_POST_LIKE = "/like/:id";
const API_POST_KEEP = "/keep/:id";
const API_POST_COMMENT_INSERT = "/comment/:postid/:userid";
const API_POST_COMMENT_DELETE = "/comment/:postid/:userid";

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
    postCommunity : POST_COMMUNITY,
    postFeeds : POST_FEEDS,
    postUpload : POST_UPLOAD,
    postSearch : POST_SEARCH,
    postSearchTag : POST_SEARCH_TAG,
    postSearchId : POST_SEARCH_ID,
    postDetail : POST_DETAIL,
    postUpdate : POST_UPDATE,
    api : API,
    apiDupIdCheck : API_DUP_IDCHECK,
    apiEmailAuth: API_EMAIL_AUTH,
    apiEmailAuthNumber : API_EMAIL_AUTH_NUMBER,
    apiPostLike : API_POST_LIKE,
    apiPostKeep : API_POST_KEEP,
    apiPostCommentInsert : API_POST_COMMENT_INSERT,
    apiPostCommentDelete : API_POST_COMMENT_DELETE,
    apiNaverItemtag : API_NAVER_ITEM_TAG,
    apiNaverItemtagCallback : API_NAVER_ITEM_TAG_CALLBACK,
    me : ME,
    logout : LOGOUT
}

module.exports = routes;
