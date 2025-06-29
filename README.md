# <div align="center">🚏 사용자 선호 기반 길 찾기 & 장소 추천 서비스 : 내길만</div>  
<div align="center">**“복잡한 경로는 이제 그만, 나에게 맞춘 길을 내길만이 안내합니다.”**</div>  

<br/>

<div align="center">
  <img src="https://github.com/user-attachments/assets/27fa0dea-245f-401f-8e7e-f490130bf5e4" width="200" />
  <img src="https://github.com/user-attachments/assets/05d6f397-8996-4f12-ab4a-c45a5cbdda8b" width="200" />
  <img src="https://github.com/user-attachments/assets/e9cb5f02-cebd-4bef-b901-0287706c19ca" width="200" />
  <img width="200" alt="스크린샷 2025-06-25 오후 6 39 24" src="https://github.com/user-attachments/assets/67a4aa03-3eda-47e5-949e-4c6a9a1feb41" />
  
  


</div>
<br/>

## 목차
1. [프로젝트 개요](#1)
2. [기술 스택](#2)
3. [주요 기능](#3)
4. [상세 기능](#4)
5. [프로젝트 팀원](#5)

---

## <span id="1">📌 프로젝트 개요</span>

### 프로젝트명  
> **내길만** – 사용자 선호 기반 맞춤형 경로 탐색 & 장소 추천 서비스  

### 개발 기간  
> 2025.03.31 ~ 2025.06.11  

### 개발 배경  
> 기존 길찾기 서비스는 사용자의 경로 선택 성향을 고려하지 않은 일방적인 정보 제공 방식이었습니다.  
> **‘내길만’**은 사용자의 실제 선택 이력을 기반으로 한 **개인화된 경로 추천**과  
> 음식 키워드 입력 시 **유사도 기반 장소 추천**을 통해 사용자 경험을 향상시킵니다.

---

## <span id="2">🛠 기술 스택</span>

<div align="center">
  <img src="https://img.shields.io/badge/react-61DAFB?style=for-the-badge&logo=react&logoColor=white">
  <img src="https://img.shields.io/badge/typescript-3178C6?style=for-the-badge&logo=typescript&logoColor=white">
  <img src="https://img.shields.io/badge/tailwindcss-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white">
  <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
  <img src="https://img.shields.io/badge/openai-412991?style=for-the-badge&logo=openai&logoColor=white">
  <img src="https://img.shields.io/badge/tmapapi-005BAC?style=for-the-badge&logo=tele5&logoColor=white">
  <img src="https://img.shields.io/badge/kakaoapi-FFCD00?style=for-the-badge&logo=kakaotalk&logoColor=black">
</div>

> 🖥 **Frontend**: React + TypeScript 기반 SPA, Tailwind CSS로 반응형 UI 구현  
> ⚙️ **Backend**: Spring Boot로 REST API 서버 구현, JWT 인증 및 OpenAI 연동 포함  
> 🗄 **Database**: MySQL  
> ☁️ **Infra**: AWS EC2 + Nginx 배포  
> 🗺 **외부 API**: Tmap API (길찾기), Kakao Local API (장소 검색)  
> 🤖 **AI 연동**: OpenAI Embedding API를 활용한 음식 키워드 유사도 추천

---

## <span id="3">🚀 주요 기능</span>

| 길찾기 기능 | 장소 추천 기능 |
|:--:|:--:|
| <img src="https://github.com/user-attachments/assets/ee654617-6d47-46f0-b09d-3b092bb8254e" width="300" /> | <img src="https://github.com/user-attachments/assets/9796a6f2-93fc-4e56-b6de-b4aa46a1a4e8" width="400" /> |
| **선호 경로 자동 추천** <br/> Tmap 기반 실시간 경로 탐색 + 사용자 선택 이력 기반 추천 | **유사 키워드 음식점 추천** <br/> 입력 키워드와 유사도 높은 장소 추천 (OpenAI Embedding + Kakao Local API) |
---

## <span id="4">🔍 상세 기능</span>

### 🧭 길찾기 기능
- 실시간 경로 탐색 (Tmap API 기반)
- 사용자 경로 선택 이력 저장 및 학습
- 선호 경로 자동 추천
- 교통수단 별 클릭 선호도 기반 자동 필터링

### 🍜 장소 추천 기능
- 음식 키워드 → 벡터화 → 유사 음식 추출
- 반복 검색 시 유사도 정밀도 향상
- 카카오 Local API 연동 결과 제공

### 🧠 시스템 구조
- 백엔드: Spring Boot + MySQL
- 프론트엔드: React + TailwindCSS + Zustand
- 서버 배포: Nginx + EC2
- GPT API: OpenAI Embedding 활용 (Python → Java 연동)
- 외부 API: Kakao API + TMAP API

---

## <span id="5">👨‍👩‍👧‍👦 프로젝트 팀원</span>

| <img src="" width="130"/> | <img src="https://github.com/kimryewon" width="130"/> | <img src="https://github.com/김연-github" width="130"/> | <img src="https://avatars.githubusercontent.com/maeng555" width="130"/> |
|:--:|:--:|:--:|:--:|
| [**PM 김연**](https://github.com/김연-github) | [**FE 현용찬**](https://github.com/현용찬-github) | [**BE 김려원**](https://github.com/kimryewon) | [**BE 맹진영**](https://github.com/maeng555) | 
> 🙌 본 프로젝트는 상명대학교 캡스톤디자인(2025) 과제로 진행되었습니다.

---

<br/>

🌟 **내길만**은 사용자의 선택을 데이터로 분석해  
🚶‍♂️ **더 빠르고 효율적인 길**, 🍱 **더 만족스러운 장소**를 안내합니다.
