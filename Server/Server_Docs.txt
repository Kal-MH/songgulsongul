Server

git clone하고 처음 실행 시
    - 콘솔에 npm install 타이핑
        *) pakage.json에 지정한 모듈을 다운받는 명령어
        *) 완료되면 node_modules 폴더 생성

프로젝트 진행과정에서
    - 처음 서버 동작 시 : npm start or node app.js
    - 이후 작업 혹은 수정작업
        *) 현재 nodemon 모듈을 설치 사용하고 있기때문에 서버가 가동되어 있는 상태라면 저장할 때마다 다시 재실행이 된다.
        *) 혹은 crtl+c를 누르고 서버를 종료했다가 다시 재실행한다.

Structure
    Controller(dic)
        - 사용자 요청에 따라 실행되는 파일(함수)들을 모아놓은 폴더
        - 컨트롤러 명명규칙(임의로 설정, 추후에 변경가능) : 분류 + 기능 + RESTful방식 ex) userJoinPost, userLoginPost
        *) userController.js : 사용자기능에 관련된 컨트롤러 (ex) 회원가입, 로그인 , ...)

    db(dic)
        - 데이터베이스 설정 및 sql문을 모아놓은 폴더
        *) db.js : db설정
        *) sql.js

    Router(dic)
        - 라우터 폴더
        - 각각의 경로에 따른 컨트롤러를 매칭해서 라우터에 달아줌.
        - 라우터는 모듈로써 export되어 app.js에서 사용 (userRouter참고)

    routes.js
        - 모든 경로 기록하는 파일
        - 객체 형태로 export함

    app.js
        - 프로그램 실행의 시작점
        - 필요한 모듈, 미들웨어, 라우터설정등이 이뤄짐.
        - 마지막에 서버 가동