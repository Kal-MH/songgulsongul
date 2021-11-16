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

  - 서버를 동작하려면 : npm start or node app.js

  ```
  *) 현재 nodemon 모듈이 설치되어 있기때문에, 서버가 가동되어 있는 상태라면 저장할 때마다 다시 재실행이 된다.
  *) 혹은 crtl+c를 누르고 서버를 종료했다가 다시 npm start로 재실행한다.

  ```

### Structure

- Controller

  - 사용자 요청에 따라 실행되는 파일(함수)들을 모아놓은 디렉토리
  - 컨트롤러 명명규칙(임의로 설정, 추후에 변경가능) : 분류 + 기능 + RESTful방식 ex) userJoinPost, userLoginPost

  ```
  현재 구현된 Controller
  
  *) homeController.js : 기본기능에 관련된 컨트롤러 (ex) 회원가입, 로그인 , 비밀번호, 아이디 찾기)
  *) userController.js : 사용자 기능 (ex) 프로필, 팔로우, ...)
  *) postController.js : 게시글 기능 (ex) 게시글 불러오기, 검색하기, ...)
  *) apiController.js : 위 컨트롤러 이외에 부수적인 기능들이 구현된 컨트롤러 (ex)아이디 중복체크, 좋아요, 보관하기, ...)
  *) marketContoller.js : 마켓 탭에서 필요한 기능 (ex) 작품 올리기, 작품 사기, 작품 팔기, ...)
  *) notificationController.js : 알림 기능 (ex) 좋아요 알림, 댓글 알림, 팔로우 알림

  ```

- db(dic)

  - 데이터베이스 설정 및 sql문을 모아놓은 디렉토리

  ```
  *) db.js : db설정
  *) db_config.js : 컨트롤러에서 쿼리문에 공통적으로 들어가는 설정 기록

  ```

- Router
  - 라우터 디렉토리
  - 각각의 경로에 따른 컨트롤러를 매칭해서 실행.
  - 라우터는 모듈로써 export되어 app.js에서 사용 (ex) userRouter참고)

- config
  - 서버 상태 코드, 서버 config 및 s3 스토리지의 설정값 등을 저장해 놓은 디렉토리
  ```
  설정값 보안 처리
  
  - .env 파일을 통해서 서버 구동에 필요한 설정값 환경변수 등록
  - .gitignore을 통해 .env파일을 깃헙에 등록하지 않으므로 보안성 향상
  ```

- routes.js
  - 모든 경로 기록하는 파일
  - 객체 형태로 export함
- app.js
  - 프로그램 실행의 시작점
  - 필요한 모듈, 미들웨어, 라우터설정등이 이뤄짐.
  - 마지막에 서버 가동

## Api Docs

```
서버 상태 코드

1. 200 : OK
2. 400 : client error (입력값 유효성 오류, 공백 입력, ...)
3. 500 : server error (서버 연결 오류, db 연결 오류 , ...)
```

### Api Controller

1. 아이디 중복체크

   - api : /api/dup-idcheck
   - method : POST
   - 전달받아야 하는 데이터
     - login_id
   - 응답 데이터

     ```
     json = {
      'code' // 200, 400(중복 || client error), 500(server error)
     }

     ```

2. 인증 이메일 보내기

   - api : /api/email-auth
   - method : POST
   - 전달받아야 하는 데이터
     - email
   - 응답 데이터

     ```
     json = {
      'code' // 200, 400(client error), 500(server error)
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

       ```
       json = {
        'code' //200, 400(client error),  500(server error)
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

     ```
     json = {
      'code' //200, 400(client error),  500(server error)
     }

     ```

   - 고려사항
     - 보관하기 api를 보냈을 때, 데이터베이스 keep 테이블을 검색.
     - 해당 게시글에 보관한 기록이 없으면(레코드가 저장되어 있지 않으면) **사용자가 보관하기를 누른 것으로 간주하고 레코드 추가**
     - 해당 게시글에 대한 레코드가 저장되어 있으면 **보관하기 취소로 간주하고 레코드 삭제**

5. 댓글쓰기

   - api : "/api/comment"
   - method : POST
   - 전달받아야 하는 데이터
     - user id (url body)
     - post id (url body)
   - 응답데이터

     ```
     json = {
      'code' //200, 400(client error),  500(server error)
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

     ```
     json = {
      'code' //200, 204(댓글 지우기 실패) -> 수정 예정
     }

     ```

7. 출석 포인트 반영하기

   - api : "/api/point/daily?id=?"
   - method : GET
   - 전달받아야 하는 데이터
     - user id(url query)
   - 응답데이터

     ```
     json = {
      'code' //200(첫 출석 & 포인트 반영), 201(출석 & 포인트 반영x), 400(client error), 500(server error)
     }

     ```

8. 비밀번호 확인하기

    - api : "/api/check/password/:userid
    - method : POST
    - 전달받아야 하는 데이터
      - userid (url param)
      - password (req body)
    - 응답데이터

      ```
      json = {
        'code' //200(password 일치), 400(client error), 500(server error)
      }
      ```

### Home Controller

1. 회원가입

   - api : /join
   - method : POST
   - 전달받아야 하는 데이터
     - email
     - password
     - login_id
     - sns_url
   - 응답 데이터

     ```
     json = {
      'code' : 200(ok), 400(client error), 500(server error)
     }

     ```

2. 로그인

   - api : /login
   - method : POST
   - 전달받아야 하는 데이터
     - login_id
     - password
   - 응답 데이터

     ```
     json = {
      'code' : 200, 201(첫출석 아님), 400(client error), 500(server error)
      'id'
     }

     ```

    - 고려사항
      - id의 경우, 로그인 성공(200, 201) 시에만 값이 반환된다.

1. 아이디 찾기

   - api : /find/id
   - method : POST
   - 전달받아야 하는 데이터
     - email
   - 응답 데이터

     ```
     json = {
      'code' : 200 or 400(빈 값, 일치하는 이메일 x) or 500(서버에러)
     }

     ```

2. 비밀번호 찾기

   - api : /find/password
   - method : POST
   - 전달받아야 하는 데이터
     - email
     - login_id
   - 응답 데이터

     ```
     json = {
      'code' : 200 or 400(일치하는 이메일x, login_id 없음) or 500(서버에러)
     }

     ```

### User Router

1. 프로필

   - api: /user/profile
   - method : POST
   - 전달받아야 하는 데이터
     - id
     - status
   - 응답 데이터

     ```
     json = {
      'code', //응답코드
      'followerCnt',  //팔로워 수
      'followCnt',  //팔로우 수
      'postInfo', //게시글 정보 -> image, postId
      'profileInfo'  //프로필 정보 -> profile_image, intro, sns, userId
     }

     ```

2. 팔로우 하기

   - api: /user/follow
   - method : POST
   - 전달받아야 하는 데이터
     - loginId(로그인한 사용자)
     - userId(팔로우 대상자)
   - 응답 데이터

     ```
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

     ```
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

     ```
     json = {
      'code',
      'followinfo' //팔로우 정보 -> image, userId
     }

     ```

5. 선택한 사용자의 팔로우 리스트 받아오기

   - api: /user/follow-list
   - method : POST
   - 전달받아야 하는 데이터
     - user_id(선택한 사용자의 id)
     - id(로그인한 사용자의 id)
   - 응답 데이터

     ```
     json = {
      'code',
      'loginFollowInfo', -> userId
      'userFollowInfo'  //팔로우 정보 -> image, userId
     }

     ```

6. 팔로워 리스트 받아오기

   - api: /user/follower-list
   - method : POST
   - 전달받아야 하는 데이터
     - id
     - status
   - 응답 데이터

     ```
     json = {
      'code',
      'followingInfo', -> userId
      'followerInfo'  //팔로워 정보 -> image, userId
     }

     ```

7. 보관함 기능

   - api: /user/keep
   - method : POST
   - 전달받아야 하는 데이터
     - login_id(로그인한 사용자)
   - 응답 데이터

     ```
     json = {
      'code',
      'keepInfo', //보관정보 -> image, postId
      'keepCnt', //보관개수
      'profileImg'
     }

     ```
     

8. 아이디 변경

   - api: /user/id-change
   - method: POST
   - 전달받아야 하는 데이터
     - login_id
     - new_id
   - 응답 데이터
   
     ```
     json = {
      'code'
     }

     ```
     
9. 비밀번호 변경

   - api: /user/pw-change
   - method: POST
   - 전달받아야 하는 데이터
     - login_id
     - password
   - 응답 데이터
   
     ```
     json = {
      'code'
     }

     ```

10. 프로필 수정

    - api: /user/profile-edit
    - method : POST
    - 전달받아야 하는 데이터
      - sns_check_flag
      - img_check_flag
      - login_id
      - new_intro
      - new_SNS
      - new_image
    - 응답 데이터

      ```
      json = {
        'code'
      }

      ```

11. 회원 탈퇴

    - api: /user/data-delete
    - method : POST
    - 전달받아야 하는 데이터
      - id(로그인한 사용자)
    - 응답 데이터

      ```
      json = {
        'code'
      }

      ```

### Post Router

1. 게시판 게시글 불러오기

   - api : "/post/community?offset=?";
     - 처음 게시글을 불러오는 경우,
       - api : /post/community
       - 가장 최신 게시글을 10개씩 불러온다.
     - 이후에 게시글을 계속해서 불러오는 경우
       - /post/community?offset=?
       - 게시글을 10개씩 불러오기 위해서는 이전 data의 마지막 게시글 id(offset, db에서의 primary key)값이 필요
       - 쿼리문으로 offset값이 넘어오면 해당 offset을 기준으로 10개의 게시글을 긁어온다.
       - db limitation 설정값에 따라 10개 혹은 20개로 변경 가능.
   - method : GET
   - 전달받아야 하는 데이터
     - offset
   - 응답데이터

     ```
     json = {
      'code', //상태코드는 ok(200), server error(500)으로 나뉜다.
      'data'  = {
        'id',  //post id
        'image',
        'text',
        'post_time',
        'post_data',
        'user_id',
        'ccl_cc',
        'ccl_a',
        'ccl_nc',
        'ccl_nd',
        'ccl_sa'
       }
     }
     **//data의 경우, 오류가 발생했을 때, undefined(null)로 넘어온다.**

     ```

2. 팔로우 게시글 불러오기

   - api : “/post/feeds?userid=?&offset=?”
     - 처음 게시글을 불러오는 경우,
       - /post/feeds?userid=2
       - 가장 최신 게시글을 20개씩 불러온다.
     - 이후에 게시글을 계속해서 불러오는 경우
       - /post/feeds?userid=2&offset=44
       - 게시글을 10개씩 불러오기 위해서는 이전 data의 마지막 게시글 id(offset)값이 필요
       - 쿼리문으로 offset값이 넘어오면 해당 offset을 기준으로 10개의 게시글을 긁어온다.
   - method : GET
   - 전달받아야 하는 데이터
     - userid (로그인한 본인의 아이디)
     - offset
   - 응답 데이터

     ```
     json = {
      'code',// 상태코드는 ok(200), client error(400), server error(500)으로 나뉜다.
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
3. 검색하기

   - api : “/post/search?method=?&keyword=?&offset=?"
     - 총 3가지의 데이터를 받는다.
       - method : 검색 방법을 지정한다.
         - method=tag //태그 검색
         - method=id // 계정 검색
       - keyword : 검색할 키워드
       - offset
         - tag검색의 경우, 게시판 게시글, 피드 게시글과 마찬가지로 이전 data의 마지막 게시글 id(primary key)를 offset 값으로 받는다.
           - 처음 검색결과를 불러오는 경우 : /post/search?method=tag&keyword=calli
           - 이후에 계속해서 검색결과를 불러오는 경우 : /post/search?method=tag&keyword=calli&offset=44
         - id검색의 경우, 이전 data의 마지막 user id(primary key)를 offset값으로 받는다.
           - 처음 검색결과를 불러오는 경우 : /post/search?method=id&keyword=aaa
           - 이후에 계속해서 검색결과를 불러오는 경우 : /post/search?method=id&keyword=aaa&offset=10
   - method : GET
   - 전달받아야 하는 데이터
     - method, keyword, offset값 (query)
   - 응답데이터

     ```
     json = {
     	'code', //상태코드
     					// 상태코드는 ok(200), client error(400), server error(500)으로 나뉜다.
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

     - search tag

       ```
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

       ![https://user-images.githubusercontent.com/59648372/117537120-f67ddc00-b039-11eb-8985-ddd03f762996.png](https://user-images.githubusercontent.com/59648372/117537120-f67ddc00-b039-11eb-8985-ddd03f762996.png)

     - search Id

       ```
       data : [
       	{
       		'id', // user id
       		'login_id', //user login_id
       		'img_profile' // user profile
       	}
       ]

       ```

       ![https://user-images.githubusercontent.com/59648372/117537188-68562580-b03a-11eb-9356-495a8b75879e.png](https://user-images.githubusercontent.com/59648372/117537188-68562580-b03a-11eb-9356-495a8b75879e.png)

     - search error

       ![https://user-images.githubusercontent.com/59648372/117537122-f978cc80-b039-11eb-85cd-195696cded0e.png](https://user-images.githubusercontent.com/59648372/117537122-f978cc80-b039-11eb-85cd-195696cded0e.png)

4. 특정 게시글 불러오기

   - api : “/post/:id?userid=?”
   - method : GET
   - 전달받아야 하는 데이터
     - post id (url param)
     - user id (url query)
   - 응답데이터

     ```
     json = {
     	'code', // 200, 400(client error), 500(server error)
     	'data' : {
     		'post', //게시글 정보(id, image, text, time, ccl, ...), 객체로 반환
     		'user', //게시글 작성자 정보(id, img_profile), 객체로 반환
     		'hashTags', //배열
     		'itemTags', //배열
     		'likeNum', //좋아요 갯수,
     		'comments', // 배열, 해당 게시글에 달린 댓글,
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

- 예시

  ![postDetail](https://user-images.githubusercontent.com/59648372/117741315-234e1100-b23d-11eb-9277-e777e27f9216.png)

- 에러 발생

  ![https://user-images.githubusercontent.com/59648372/117537320-03e79600-b03b-11eb-9dd4-abbf1e880703.png](https://user-images.githubusercontent.com/59648372/117537320-03e79600-b03b-11eb-9dd4-abbf1e880703.png)

1. 게시글 업로드

   - api : “/post/upload”
   - method : POST
   - 전달받아야 하는 데이터
     - user_id
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
       - brand
       - category1
       - category2
     - ccl 정보(현재는 int형 5개짜리 ccl 배열이 넘어오는 것을 가정해서 구현되어 있다. 추후에 넘어오는 데이터 구조에 따라 수정 가능.)
       - ccl_cc
       - ccl_a
       - ccl_nc
       - ccl_nd
       - ccl_sa
   - 응답데이터

     ```
     json = {
      'code', //200, 400, 500
     }

     ```

2. 게시글 업데이트

   - api : “/post/update”
   - method : POST
   - 전달받아야 하는 데이터
     - user_id
     - post_id
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

     ```
     json = {
      'code', //200, 400, 500
     }

     ```

3. 게시글 삭제

   - api : “/post/delete?userid=?&postid=?”
   - method : GET
   - 전달받아야 하는 데이터
     - user id (url query)
     - post id (url query)
   - 응답데이터

     ```
     json = {
      'code', //200, 400, 500
     }

     ```

4. 게시글 다운로드

   - api : "/post/download?postid=?"
   - method : GET
   - 전달받아야 하는 데이터
     - post id (url query)
   - 응답데이터

     ```
     json = {
      'code', // 상태코드는 ok(200), server error(500)으로 나뉜다.
      'data' : {
        'img_path' : file
       }
      }

     ```

### Market Router

1. 마켓 메인화면

   - api : “/market/main”
   - 응답데이터

     ```
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

     ```
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

     ```
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

     ```
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

     ```
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

     ```
     json = {
      'code',
      'marketItem' // 검색한 sticker --> id, image, name, price
     }

     ```
