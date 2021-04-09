## Server Docs

### 동작

- git clone하고 처음 실행 시

  - 콘솔에 npm install 타이핑

  ```
  npm install이란, 
  - pakage.json에 지정한 모듈을 다운받는 명령어
  - 완료되면 node_modules 폴더 생성
  ```

- 프로젝트 진행과정에서

  - 서버를 동작하려면  : npm start or node app.js

  ```
  *) 현재 nodemon 모듈이 설치되어 있기때문에, 서버가 가동되어 있는 상태라면 저장할 때마다 다시 재실행이 된다.
  *) 혹은 crtl+c를 누르고 서버를 종료했다가 다시 npm start로 재실행한다.
  ```

### Structure

- Controller(dic)

  - 사용자 요청에 따라 실행되는 파일(함수)들을 모아놓은 폴더

  - 컨트롤러 명명규칙(임의로 설정, 추후에 변경가능) : 분류 + 기능 + RESTful방식 ex) userJoinPost, userLoginPost

  ```
  현재 구현된 Controller
  *) homeController.js : 기본기능에 관련된 컨트롤러 (ex) 회원가입, 로그인 , 비밀번호, 아이디 찾기)
  *) userController.js : 사용자 기능 (ex) 프로필, 팔로우, ...)
  *) postController.js : 게시글 기능 (ex) 게시글 불러오기, 검색하기, ...)
  *) apiController.js : 위 컨트롤러 이외에 부수적인 기능들이 구현된 컨트롤러 (ex)아이디 중복체크, 좋아요, 보관하기,...)
  ```

- db(dic)

  - 데이터베이스 설정 및 sql문을 모아놓은 폴더

  ```
  *) db.js : db설정
  *) db_config.js : 컨트롤러에서 쿼리문에 공통적으로 들어가는 설정 기록
  ```

- Router(dic)

  - 라우터 폴더

  - 각각의 경로에 따른 컨트롤러를 매칭해서 라우터에 달아줌.

  - 라우터는 모듈로써 export되어 app.js에서 사용 (userRouter참고)

- routes.js

  - 모든 경로 기록하는 파일

  - 객체 형태로 export함

- app.js

  - 프로그램 실행의 시작점

  - 필요한 모듈, 미들웨어, 라우터설정등이 이뤄짐.

  - 마지막에 서버 가동

## Api Controller

1. 아이디 중복체크

   - api : /api/dup-idcheck
   - method : POST
   - 전달받아야 하는 데이터
     - login_id
   - 응답 데이터

     ```java
     json = {
     	'code' // 200 or 204(중복 존재) or 500(database connection error)
     }
     ```

2. 인증 이메일 보내기

   - api : /api/email-auth
   - method : POST
   - 전달받아야 하는 데이터
     - email
   - 응답 데이터

     ```java
     json = {
     	'code' //200 or 500(서버 에러)
     	'authNumber' // 이메일 인증 번호
     }
     ```

3. 좋아요

   - api : “/api/like?userid=?&postid=?”

     - api 예시 : /api/like?userid=4&postid=17, /api/like?userid=4&postid=44, …
     - method : GET
     - 전달받아야 하는 데이터
       - user id (url query)
       - post id (url query)
     - 응답데이터

       ```java
       json = {
       	'code' //200 or 204, 500(좋아요 처리 실패)
       }
       ```

   - 고려사항
     - 좋아요 api를 보냈을 때, 데이터베이스 likes 테이블을 검색.
     - 해당 게시글에 대한 좋아요를 누른 기록이 없으면(레코드가 저장되어 있지 않으면) **사용자가 좋아요를 누른 것으로 간주하고 레코드 추가**
     - 해당 게시글에 대한 레코드가 저장되어 있으면 **좋아요 취소로 간주하고 레코드 삭제**

4. 보관하기

   - api : “/api/keep?userid=?&postid=?”
     - api 예시 : /api/like?userid=4&postid=17, /api/like?userid=4&postid=44, …
   - method : GET
   - 전달받아야 하는 데이터
     - user id (url query)
     - post id (url query)
   - 응답데이터

     ```java
     json = {
     	'code' //200 or 204, 500(보관하기 처리 실패)
     }
     ```

   - 고려사항
     - 보관하기 api를 보냈을 때, 데이터베이스 keep 테이블을 검색.
     - 해당 게시글에 보관한 기록이 없으면(레코드가 저장되어 있지 않으면) **사용자가 보관하기를 누른 것으로 간주하고 레코드 추가**
     - 해당 게시글에 대한 레코드가 저장되어 있으면 **보관하기 취소로 간주하고 레코드 삭제**

5. 댓글쓰기

   - api : "/api/comment?userid=?&postid=?"
   - method : GET
   - 전달받아야 하는 데이터
     - user id (url query)
     - post id (url query)
   - 응답데이터

     ```java
     json = {
     	'code' //200, 204(댓글 저장 실패)
     }
     ```

   - 고려사항
     - 공백으로 오는 코멘트도 현재 거르지 않고 저장하고 있음.

6. 댓글 지우기

   - api : "/api/comment/delete?postid=?&commentid=?"
   - method : GET
   - 전달받아야 하는 데이터
     - post id(url query), comment id(url query)
   - 응답데이터

     ```java
     json = {
     	'code' //200, 204(댓글 지우기 실패)
     }
     ```

7. 출석 포인트 반영하기

   - api : "/api/point/daily?id=?"
   - method : GET
   - 전달받아야 하는 데이터
     - user id(url query)
   - 응답데이터

     ```java
     json = {
     	'code' //200, 204, 500
     }
     ```

## Home Controller

1. 회원가입

   - api : /join
   - method : POST
   - 전달받아야 하는 데이터
     - email
     - password
     - login_id
     - **sns_url // 데이터베이스에 적혀져 있는 명명에 따라 일관성을 위해 바꿈.**
     - img_profile
   - 응답 데이터

     ```java
     json = {
     	'code' : 200(ok) or 204(client error)
     	// 에러 메세지를 좀 더 세부적으로 나눌 필요가 있다면 204(client error), 500(server error)로 나눌 수 있음.**
     }
     ```

2. 로그인

   - api : /login
   - method : POST
   - 전달받아야 하는 데이터
     - login_id
     - password
   - 응답 데이터

     ```java
     json = {
     	'code' : 200 or 204(error),
       'id' : userid
     }
     ```
  - 고려사항
    - id의 경우, 로그인 성공(200) 시에만 값이 반환된다.

3. 아이디 찾기

   - api : /find/id
   - method : POST
   - 전달받아야 하는 데이터
     - email
   - 응답 데이터

     ```java
     json = {
     	'code' : 200 or 204(일치하는 이메일 없음) or 500(서버에러),
     }
     ```

4. 비밀번호 찾기

   - api : /find/password
   - method : POST
   - 전달받아야 하는 데이터
     - email
     - login_id
   - 응답 데이터

     ```java
     json = {
     	'code' : 200 or 204(일치하는 이메일, login_id 없음) or 500(서버에러),
     }
     ```

## User Router

1. 프로필

   - api: /user/profile
   - method : POST
   - 전달받아야 하는 데이터
     - id
     - status
   - 응답 데이터

     ```java
     json = {
     	'code', //응답코드
     	'followerCnt',  //팔로워 수
     	'followCnt',  //팔로우 수
     	'postInfo', //게시글 정보 -> image, postId
     	'profileInfo'  //프로필 정보 -> profile_image, intro, sns
     }
     ```

2. 팔로우 하기

   - api: /user/follow
   - method : POST
   - 전달받아야 하는 데이터
     - loginId(로그인한 사용자)
     - userId(팔로우 대상자)
   - 응답 데이터

     ```java
     json = {
     	'code'
     }
     ```

3. 언팔로우 하기

   - api: /user/unfollow
   - method : POST
   - 전달받아야 하는 데이터
     - loginId
     - userId
   - 응답 데이터

     ```java
     json = {
     	'code'
     }
     ```

4. 로그인한 사용자의 팔로우 리스트 받아오기

   - api: /user/lfollow-list
   - method : POST
   - 전달받아야 하는 데이터
     - id(로그인한 사용자의 id)
   - 응답 데이터

     ```java
     json = {
       'code',
     	'followinfo' //팔로우 정보 -> image, userId
     }
     ```

5. 선택한 사용자의 팔로우 리스트 받아오기

   - api: /user/follow-list
   - method : POST
   - 전달받아야 하는 데이터
     - userId(선택한 사용자의 id)
   - 응답 데이터

     ```java
     json = {
       'code',
     	'userfollowinfo'  //팔로우 정보 -> image, userId
     }
     ```

6. 팔로워 리스트 받아오기

   - api: /user/follower-list
   - method : POST
   - 전달받아야 하는 데이터
     - id
   - 응답 데이터

     ```java
     json = {
       'code',
     	'followerinfo'  //팔로워 정보 -> image, userId
     }
     ```

7. 보관함 기능

   - api: /user/keep
   - method : POST
   - 전달받아야 하는 데이터
     - id(로그인한 사용자)
   - 응답 데이터

     ```java
     json = {
       'code',
     	'keepinfo', //보관정보 -> image, postId
     	'keepcnt' //보관개수
     }
     ```

8. 프로필 수정 – 아이디 중복 확인

   - api: /user/profile-edit-idcheck

     ```java
     json = {
     	'code'
     }
     ```

9. 프로필 수정

   - api: /user/profile-edit
   - method : POST
   - 전달받아야 하는 데이터
     - id(기존 아이디)
     - newId(변경된 아이디)
     - newIntro(변경된 소개글)
     - newSNS(변경된 sns주소)
     - profileImage
   - 응답 데이터

     ```java
     json = {
     	'code'
     }
     ```

10. 회원 탈퇴

    - api: /user/data-delete
    - method : POST
    - 전달받아야 하는 데이터
      - id(로그인한 사용자)
    - 응답 데이터

      ```java
      json = {
      	'code'
      }
      ```

## Post Router

1. 게시판 게시글 불러오기

   - api : "/post/community?offset=?";
     - 처음 게시글을 불러오는 경우,
       - /post/community
       - 가장 최신 게시글을 20개씩 불러온다.
     - 이후에 게시글을 계속해서 불러오는 경우
       - /post/community?offset=?
       - 게시글을 20개씩 불러오기 위해서는 이전 data의 마지막 게시글 id(offset)값이 필요
       - 쿼리문으로 offset값이 넘어오면 해당 offset을 기준으로 20개의 게시글을 긁어온다.
   - method : GET
   - 전달받아야 하는 데이터
     - 쿼리 offset값
   - 응답데이터

     ```java
     json = {
     	'code', //상태코드
     				  // 상태코드는 ok(200), server error(500)으로 나뉜다.
     	'data'  = {
     		'id',  //post id
     		'image',
     		'text',
     		'post_time',
     		'post_data',
     		'user_id'
     	}
     }
     **//data의 경우, 오류가 발생했을 때는 빈배열 혹은 undefined(null)로 넘어올 수 있다.**
     ```

   - 예시

     ![postCommunity](https://user-images.githubusercontent.com/59648372/112145078-41c06480-8c1d-11eb-8303-68bbcdc80f26.png)

2. 팔로우 게시글 불러오기

   - api : “/post/feeds?userid=?&offset=?”
     - 처음 게시글을 불러오는 경우,
       - /post/feeds?userid=2
       - 가장 최신 게시글을 20개씩 불러온다.
     - 이후에 게시글을 계속해서 불러오는 경우
       - /post/feeds?userid=2&offset=44
       - 게시글을 20개씩 불러오기 위해서는 이전 data의 마지막 게시글 id(offset)값이 필요
       - 쿼리문으로 offset값이 넘어오면 해당 offset을 기준으로 20개의 게시글을 긁어온다.
   - method : GET
   - 전달받아야 하는 데이터
     - 쿼리 offset값
   - 응답 데이터

     ```java
     json = {
     	'code', //상태코드
     				  // 상태코드는 ok(200), server error(500)으로 나뉜다.
     	'data'  = {
     		'post' : {
     			'id',  //post id
     			'image',
     			'text',
     			'post_time',
     			'post_data'
     		},
     		'user' : {
     			'user_id',
     			'login_id',
     			'img_profile'
     		},
     		'commentsNum',
     		'likeNum',
     		'likeOnset',
     		'keepOnset'
     	}
     }
     **//data의 경우, 오류가 발생했을 때는 null(혹은 undefined)로 넘어올 수 있다.**
     ```

   - 예시

     ![postfeeds](https://user-images.githubusercontent.com/59648372/112144953-1a699780-8c1d-11eb-8463-b9ef1bbc9191.png)

3. 검색하기

   - api : “/post/search?method=?&keyword=?&offset=?"
     - 총 3가지의 쿼리문을 받는다.
       - method : 검색 종류를 지정한다.
         - method=tag //태그 검색
         - method=id // 계정 검색
       - keyword : 검색할 키워드
       - offset
         - tag검색의 경우, 게시판 게시글, 피드 게시글과 마찬가지로 이전 data의 마지막 게시글 id를 offset 값으로 받는다.
           - 처음 검색결과를 불러오는 경우 : /post/search?method=tag&keyword=calli
           - 이후에 계속해서 검색결과를 불러오는 경우 : /post/search?method=tag&keyword=calli&offset=44
         - id검색의 경우, 이전 data의 마지막 user id를 offset값으로 받는다.
           - 처음 검색결과를 불러오는 경우 : /post/search?method=id&keyword=aaa
           - 이후에 계속해서 검색결과를 불러오는 경우 : /post/search?method=id&keyword=aaa&offset=10
   - method : GET
   - 전달받아야 하는 데이터
     - 쿼리 method, keyword, offset값
   - 응답데이터

     ```java
     json = {
     	'code', //상태코드
     					// 상태코드는 ok(200), server error(500)으로 나뉜다.
     	'data'  = { … }
     }
     ```

   - 고려사항
     - **data의 경우, 오류가 발생했을 때는 null(혹은 undefined)로 넘어올 수 있다.**
     - **태그 검색인지, 계정검색인 지에 따라 data에 담겨오는 정보들이 달라진다. (아래 예시 참조)**
     - 검색 키워드를 **포함하는** 레코드를 데이터 베이스에서 가져온다.
     - 포함한다는 것의 의미는 다음과 같다.
       - 해당 키워드와 일치한다.
       - 검색하고자 하는 값의 앞부분이 해당 키워드와 일치한다.
       - 키워드가 중간에 위치하거나, 뒤에 위치하는 경우는 제외한다.
     - keyword%
   - 예시

     - <search tag>

       ```java
       data : [
       	{
       		'post_id', // 게시글 id
       		'text', //해시태그 text
       		'image', // 게시글 이미지
       		'post_time',
       		'post_date'
       	}
       ]
       ```

       ![postSearchTag](https://user-images.githubusercontent.com/59648372/112144995-281f1d00-8c1d-11eb-8b30-d5bad38f3e5f.png)

     - <search Id>

       ```java
       data : [
       	{
       		'id', // user id
       		'login_id', //user login_id
       		'img_profile' // user profile
       	}
       ]
       ```

       ![postSearchId](https://user-images.githubusercontent.com/59648372/112145026-340adf00-8c1d-11eb-8af4-70810bc376aa.png)

4. **특정 게시글 불러오기**

   - api : “/post/:id?userid=?”
   - method : GET
   - 전달받아야 하는 데이터
     - post id (url param)
     - user id (url query)
   - 응답데이터

     ```java
     json = {
     	'code', //상태코드
     					// 상태코드는 ok(200), server error(500)으로 나뉜다.
     	'data' : {
     		'post', //게시글 정보,
     		'user', //게시글 작성자 정보,
     		'hashTags',
     		'itemTags',
     		'likeNum', //좋아요 갯수,
     		'comments', //해당 게시글에 달린 댓글,
     		'likeOnset',
     		'keepOnset'
     	}
     }
     ```

   - 고려사항
     - likeOnset :
       - 로그인한 사용자가 해당 게시글의 좋아요를 눌렀는 지 표시해주는 Onset 값.
       - 1이면 이전에 좋아요를 누른 게시글이고, 0이면 누르지 않은 게시글
     - keepOnset
       - 로그인한 사용자가 해당 게시글을 보관했는지 표시해주는 Onset값
       - 1이면 보관하고 있는 게시글, 0이면 보관하지 않은 게시글

   //자세한 객체 구조는 예시 참조

   **// 하나의 게시글만 가져오도록 변경할 예정,**

   **// data는 배열이 아니라 객체로 넘어올 가능성 있음**

- 예시

  ![postDetail](https://user-images.githubusercontent.com/59648372/112144589-c5c61c80-8c1c-11eb-8dbc-2e50f8b86b2d.png)

5. 게시글 업로드

   - api : “/post/upload?userid=?”
   - method : POST
   - 전달받아야 하는 데이터
     - user id (url query)
     - 게시글 정보
       - image
       - text
     - 해시태그 정보
       - text
     - 아이템 태그 정보
       - name
       - lprice
       - hprice
       - url
       - picture
     - ccl 정보
       - ccl_cc
       - ccl_a
       - ccl_nc
       - ccl_nd
       - ccl_sa
         현재는 int형 5개짜리 ccl 배열이 넘어오는 것을 가정해서 구현되어 있다. 추후에 넘어오는 데이터 구조에 따라 수정 가능.
   - 응답데이터

     ```java
     json = {
     	'code', //상태코드
     					// 상태코드는 ok(200), client error(500)으로 나뉜다.
     }
     ```

6. 게시글 업데이트

   - api : “/post/update?userid=?&postid=?”
   - method : POST
   - 전달받아야 하는 데이터
     - user id (url query)
     - post id (url query)
     - 게시글 정보
       - image
       - text
     - 해시태그 정보
       - text
     - 아이템 태그 정보
       - name
       - lprice
       - hprice
       - url
       - picture
   - 응답데이터

     ```java
     json = {
     	'code', //상태코드
     					// 상태코드는 ok(200), client error(204)으로 나뉜다.
     }
     ```

7. 게시글 삭제

   - api : “/post/delete?userid=?&postid=?”
   - method : GET
   - 전달받아야 하는 데이터
     - user id (url query)
     - post id (url query)
   - 응답데이터

     ```java
     json = {
     	'code', //상태코드
     					// 상태코드는 ok(200), server error(500)으로 나뉜다.
     }
     ```

8. 게시글 다운로드

   - api : "/post/download?postid=?"
   - method : GET
   - 전달받아야 하는 데이터
     - post id (url query)
   - 응답데이터
     ```java
     json = {
     'code', //상태코드
     					// 상태코드는 ok(200), server error(500)으로 나뉜다.
     	'data' : {
     		'img_path' : file
     	}
     ```

## Market Router

1. 마켓 메인화면

   - api : “/market/main”
   - 응답데이터

     ```java
     json = {
     	'code',
      'marketItem' // 마켓 sticker 정보 --> id, image, name, price
     }
     ```

2. 마켓 스티커 상세보기

   - api : “/market/detail/:stickerId/:userId”
   - method : GET
   - 전달받아야 하는 데이터
     - stickerId(스티커id)
     - userId(로그인한 사용자 id -- 보유 포인트 전송에 필요)
   - 응답데이터

     ```java
     json = {
     	'code',
      'stickerDetail', // sticker 상세 정보 --> image, name, price, text
      'sellerInfo', // 판매자 정보 --> profileImage, userId
      'userPoint' // 사용자 포인트
     }
     ```

3. 마켓 스티커 구매

   - api : “/market/buy/:stickerId/:userId”
   - method : GET
   - 전달받아야 하는 데이터
     - stickerId(스티커id)
     - userId(로그인한 사용자 id -- 구매자 포인트 차감)
   - 응답데이터

     ```java
     json = {
     	'code'
     }
     ```

4. 마켓 스티커 검색시(기본)

   - api : “/market/sticker-search?searchWord=?”
   - method : GET
   - 전달받아야 하는 데이터
     - searchWord(검색어)
   - 응답데이터

     ```java
     json = {
     	'code',
      'marketItem' // 검색한 sticker --> id, image, name, price
     }
     ```

5. 마켓 스티커 검색(낮은 가격순)

   - api : “/market/search-price?searchWord=?”
   - method : GET
   - 전달받아야 하는 데이터
     - searchWord(검색어)
   - 응답데이터

     ```java
     json = {
     	'code',
      'marketItem' // 검색한 sticker --> id, image, name, price
     }
     ```

6. 마켓 스티커 검색(최신순)

   - api : “/market/search-date?searchWord=?”
   - method : GET
   - 전달받아야 하는 데이터
     - searchWord(검색어)
   - 응답데이터

     ```java
     json = {
     	'code',
      'marketItem' // 검색한 sticker --> id, image, name, price
     }
     ```
