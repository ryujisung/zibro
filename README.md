# zibro

AI와 대화하는 심리상담 채팅에 커뮤니티 게시판을 붙인 안드로이드 앱입니다. 2024년 5월부터 7월까지 팀 프로젝트로 만들었습니다.

## 기능

- 이메일 회원가입과 로그인
- AI 상담 채팅. 앱이 텍스트 생성 서버의 `/generate`로 질문을 보내고 답을 받아 보여 줍니다.
- 커뮤니티 게시판. 인기글과 자유글 보기, 글쓰기, 댓글
- 마이 페이지

## 기술

Kotlin, XML 레이아웃, Retrofit, OkHttp, Room, Navigation, Firebase

## 실행

Android Studio로 열어서 실행합니다. minSdk 24입니다.

AI 상담은 개발할 때 임시로 띄운 서버 주소를 쓰고 있어서 지금은 응답이 오지 않습니다. 써 보려면 `ui/chat/ChatActivity.kt`의 `baseUrl`을 직접 띄운 서버 주소로 바꿔야 합니다.
