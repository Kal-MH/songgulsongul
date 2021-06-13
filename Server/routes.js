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
const USER_PROFILE = "/profile";
const USER_KEEP = "/keep";
const USER_ID_CHANGE = "/id-change"
const USER_PW_CHANGE = "/pw-change"
const USER_PROFILE_EDIT = "/profile-edit";
const USER_FOLLOW = "/follow";
const USER_UNFOLLOW = "/unfollow";
const USER_FOLLOW_LIST = "/follow-list";
const USER_FOLLOWER_LIST = "/follower-list";
const USER_DATA_DELETE = "/data-delete";
const USER_LFOLLOW_LIST = "/lfollow-list";

//notification Router
const NOTIFICATION = "/notification";
const PUSHALARM = "/pushalarm";
const SENDNOTI = "/sendNotification";
const SETTOKEN = "/setToken";
const DELETETOKEN = "/deleteToken"

//market Router
const MARKET = "/market";
const MARKET_MAIN = "/main";
const MARKET_DETAIL = "/detail";
const MARKET_BUY = "/buy";
const MARKET_STICKER_SEARCH = "/sticker-search";
const MARKET_SEARCH_PRICE = "/search-price";
const MARKET_SEARCH_DATE = "/search-date";
const MARKET_UPLOAD = "/upload";

//post Router
const POST = "/post";
const POST_COMMUNITY = "/community";
const POST_FEEDS = "/feeds";
const POST_DETAIL = "/:id";
const POST_SEARCH = "/search";
const POST_SEARCH_TAG = "/search/tag";
const POST_SEARCH_ID = "/search/id";
const POST_UPLOAD = "/upload";
const POST_UPDATE = "/update";
const POST_DELETE = "/delete";
const POST_DOWNLOAD = "/download";

//api Router
const API = "/api";
const API_DUP_IDCHECK = "/dup-idcheck";
const API_EMAIL_AUTH = "/email-auth";
const API_EMAIL_AUTH_NUMBER = "/email-auth-number";

const API_POST_LIKE = "/like";
const API_POST_KEEP = "/keep";
const API_POST_COMMENT_INSERT = "/comment";
const API_POST_COMMENT_DELETE = "/comment/delete";

const API_DAILY_ATTENDANCE = "/point/attendance";

const API_CHECK_PASSWORD = "/check/password"

const routes = {
    home : HOME,
    join : JOIN,
    login : LOGIN,
    findId : FIND_ID,
    findPassword: FIND_PASSWORD,

    user : USER,
    userProfile : USER_PROFILE,
    userKeep : USER_KEEP,
    userIdChange : USER_ID_CHANGE,
    userPwChange : USER_PW_CHANGE,
    userProfileEdit : USER_PROFILE_EDIT,
    userFollow : USER_FOLLOW,
    userUnfollow : USER_UNFOLLOW,
    userFollowList : USER_FOLLOW_LIST,
    userFollowerList : USER_FOLLOWER_LIST,
    userDataDelete : USER_DATA_DELETE,
    userLFollowList : USER_LFOLLOW_LIST,

    notification: NOTIFICATION,
    sendNotification: SENDNOTI,
    setToken: SETTOKEN,
    deleteToken: DELETETOKEN,

    market : MARKET,
    marketMain : MARKET_MAIN,
    marketDetail : MARKET_DETAIL,
    marketBuy : MARKET_BUY,
    marketStickerSearch : MARKET_STICKER_SEARCH,
    marketSearchPrice : MARKET_SEARCH_PRICE,
    marketSearchDate : MARKET_SEARCH_DATE,
    marketUpload : MARKET_UPLOAD,

    post : POST,
    postCommunity : POST_COMMUNITY,
    postFeeds : POST_FEEDS,
    postUpload : POST_UPLOAD,
    postSearch: POST_SEARCH,
    postSearchTag : POST_SEARCH_TAG,
    postSearchId :POST_SEARCH_ID,
    postDetail : POST_DETAIL,
    postUpdate : POST_UPDATE,
    postDelete : POST_DELETE,
    postDownload : POST_DOWNLOAD,

    api : API,
    apiDupIdCheck : API_DUP_IDCHECK,
    apiEmailAuth: API_EMAIL_AUTH,
    apiEmailAuthNumber : API_EMAIL_AUTH_NUMBER,
    apiPostLike : API_POST_LIKE,
    apiPostKeep : API_POST_KEEP,
    apiPostCommentInsert : API_POST_COMMENT_INSERT,
    apiPostCommentDelete : API_POST_COMMENT_DELETE,
    apiDailyAttendance: API_DAILY_ATTENDANCE,
    apiCheckPassword : API_CHECK_PASSWORD
}

module.exports = routes;
