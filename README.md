
## Overview
Guess Me는 가족 구성원 간의 심리 상담 및 소통을 돕기 위한 서비스입니다. 가족 구성원들이 각자의 관심사와 고민거리를 입력하면, AI(ChatGPT)가 하루에 1번씩 맞춤형 상담 질문을 생성하고, 이에 대한 답변을 통해 추가적인 상담 질문 및 대화가 이어집니다.  

<img width="340" alt="대화가필요해 포스터" src="https://github.com/user-attachments/assets/4bfaacd9-04af-455d-ab01-5dec14ff66c7">

## Main Features
- **유저 정보 및 사전 정보 등록**
  - 기본 정보 및 관심사/걱정거리 등록
  - 유저 코드 발급 및 검증

- **가족 연결 및 관리**
  - 가족 코드 기반으로 가족 구성원을 연결 
  - 가족 정보 조회 및 구성원 추가 기능

- **질문 생성 및 답변**
  - 가족의 관심사/걱정거리 기반으로 ChatGPT가 상담 질문 자동 생성
  - 질문에 대한 답변 등록 및 조회

- **댓글 질문 및 답변**
  - 질문에 대한 추가 상담 질문(댓글 질문) 자동 생성 및 답변 
  - 가족 전체의 댓글 질문/답변 조회

- **실시간 채팅방**
  - 상담 종료 후 가족별 채팅방 자동 생성 및 24시간 채팅
  - 채팅 메시지 MongoDB 저장 및 조회 

- **OAuth2 로그인**
  - 카카오 로그인 연동 


## Flow Chart

1. **가족 연결**
   - 가족 코드로 구성원 연결 → 가족 정보 DB 저장

2. **유저 정보 등록**
   - 각 유저가 기본 정보 및 관심사/걱정거리 입력

3. **질문 생성**
   - 가족의 관심사/걱정거리 기반으로 ChatGPT API 호출  
     → 상담 질문 5개 자동 생성 및 DB 저장

4. **질문 답변**
   - 각 가족 구성원이 질문에 답변  
     → 답변 수가 가족 수와 같아지면 추가 상담 질문(댓글 질문) 생성

5. **댓글 질문/답변**
   - ChatGPT가 각 답변을 바탕으로 추가 상담 질문 생성  
     → 각 유저가 댓글 질문에 답변

6. **실시간 채팅방**
   - 모든 상담이 끝나면 가족별 채팅방 자동 생성  
     → 24시간 동안 실시간 채팅 가능, 이후 자동 종료 메시지 발송


## Tech Stack
- **Backend**: Spring Boot (JDK 17)
- **Database**: MySQL, MongoDB
- **ORM**: JPA, JPQL
- **Security**: Spring Security, JWT
- **AI Integration**: OpenAI ChatGPT API
- **OAuth2**: Kakao
- **CI/CD**: Github Actions 

## Demo
[데모 영상](https://drive.google.com/file/d/1VTF4lBi0Xfi0YqIOyqz54-mtM6hTSrMg/view )
