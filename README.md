## 👩🏼‍🤝‍👩🏼 개발인원 (1명)

| 이름        | 역할     | 담당 업무                                          |
|-----------|--------|------------------------------------------------|
| 권민주       | 백엔드 개발 |               전체                       |

## 🔍 프로젝트 목적 (Purpose)

**물류 관리 및 주문/배송 시스템** 
프로젝트의 주요 목적은 B2B(Business to Business) 물류 관리 시스템으로, 허브에 속한 업체들의 주문을 배송으로 연결하는 시스템입니다.


## 📋 프로젝트 상세 (Details)

프로젝트는 다음과 같은 기능을 제공합니다:

- 🔐 **회원가입 및 로그인**: 기본값인 사용자(USER), 관리자(MASTER), 업체 주인(STORE), 허브 관리자(HUB)로 나뉘어져 있으며 각각의 권한에 따라 서비스 접근이 가능합니다.
- 🔐 **사용자 관리**: 사용자 정보를 수정, 삭제, 조회 및 유저 검증을 할 수 있습니다.
- 🗂️ **허브 관리**: 허브의 위치정보는 위경도 데이터로 저장되어 있으며, 주문시 배송을 위한 날씨 정보를 가져오는데 사용됩니다.
- 🏪 **업체 관리**: 업체 주인은 다른 업체의 상품을 주문하거나, 요청받을 수 있으며 상품은 허브를 거쳐 배송됩니다.
- 📦 **상품 관리**: 가게 주인은 상품을 등록하고, 수정하며, 삭제할 수 있습니다.
- 🛒 **주문 관리**: 업체 주인은 상품을 주문, 수정, 취소할 수 있습니다.
- 🛠️ **슬랙 메시지**: 공공 포털 오픈 날씨 API와 구글 gemini API를 이용한 날씨 정보를 저장하고 slack API를 통해 슬랙 메시지로 전달됩니다.
- 🔍 **검색 기능**: 사용자는 다양한 검색 조건과 필터를 적용하여 원하는 허브, 업체, 상품을 쉽게 검색할 수 있습니다.


## 📄 서비스 구성 및 실행방법

### 필수 설치 사항

- Java 17.x : Spring Boot 애플리케이션을 실행하기 위한 JDK
- Docker : 애플리케이션을 컨테이너로 실행하기 위한 Docker
  - MySQL 8.x : 백엔드 데이터베이스로 사용할 MySQL 서버

### 설치 및 실행 방법
1. 데이터베이스 실행
```bash
HeidiSQL을 설치하여 로컬에서 실행
```
2. 리포지토리 클론
```bash
git clone https://github.com/tmsSystem/eureka-server.git
git clone https://github.com/tmsSystem/gateway.git
git clone https://github.com/tmsSystem/auth.git
git clone https://github.com/tmsSystem/tms.git
```
3. Eureka Server 실행
```bash
cd eureka-server
./gradlew clean build  # 프로젝트 빌드
./gradlew bootRun      # Eureka 서 실행
```
4. Gateway 서비스 실행
```bash
cd gateway
./gradlew clean build  # 프로젝트 빌드
./gradlew bootRun      # Gateway 서비스 실행
```
5. Auth 서비스 실행
```bash
cd quth
./gradlew clean build  # 프로젝트 빌드
./gradlew bootRun      # Auth 서비스 실행
```
6. Tms 서비스 실행
  - ${GEMINI_KEY}, ${PUBLIC_PORTAL_KEY}, ${SLACK_TOKEN} 값 설정
```
cd tms
./gradlew clean build  # 프로젝트 빌드
./gradlew bootRun      # Tms 서비스 실행
```


## ERD 다이어그램
![image](https://github.com/user-attachments/assets/e1caf88b-2f79-4871-8735-19706bbe51ac)


## ⚙ 기술 스택
- <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> : 주 언어로 사용되었습니다.
- <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> : 프로젝트의 백엔드 개발에 사용되었습니다
- - <img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> : 프로젝트의 메인 프레임워크로 사용되었습니다.
- - <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring Security&logoColor=white"> : 보안 기능을 구현하는 데 사용되었습니다.
- - <img src="https://img.shields.io/badge/Spring Cloud-6DB33F?style=for-the-badge&logo=Spring cloud&logoColor=white"> : 클라우드 환경에서의 마이크로서비스 아키텍처를 구현하는 데 사용되었습니다.
- <img src="https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white"> : 데이터베이스 ORM(Object-Relational Mapping) 프레임워크로 사용되었습니다.
- <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"> : 관계형 데이터베이스로 사용되었습니다.
- <img src="https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white"> : MSA 방식의 서비스들을 한번에 띄우기 위한 도커 컴포즈를 사용하였습니다.

## 트러블 슈팅

- JPA 양방향 연관관계에서 순환참조 문제 -> responseDto에서 엔티티가 아닌 dto를 반환하도록 변경하였다.
  - https://github.com/chrysan5/tmsSystem/commit/664ccde5dadae19a28fd61c826e4079cc62adab7#diff-951fa22d343c74f8435612a30b1df4fa944eacd9bd47dff4ff295cdedbe4b486L16

- auditing 적용시 인증 객체가 있어야 하지만 인증 객체는 Auth 서비스만 가지고 있었다. -> jwt 토큰에 들어있는 정보로 인증객체 만드는 필터를 적용하여 사용하였다
  - https://github.com/chrysan5/tmsSystem/commit/b696356aa7163dfae28bb37106cd49b08e2091da
  
- @SQLRestriction("is_delete = false")로 논리적 삭제를 구현하였으나, is_delete=true까지 모두 조회해야하는 경우 발생 - nativeQuery = true 조건을 사용하여 해결하였다.
  - https://github.com/chrysan5/tmsSystem/commit/59cf9851dcbdbea528c512366061fa12346a5bdc#diff-46485dc7ed2739c7a2ddd978e96a8a6a32a96ff4e3f5ece63cea394073420b7e
  
- 스케줄러를 tms 서비스에서 실행하려 했더니 403 forbidden 에러가 뜨며 권한을 확인하였다(로그인을 해야하는 상황) -> 원인은 slack 메시지를 저장하는 과정에서 auditing시 createBy 값이 인증객체가 없어서 null로 들어갔기 때문이다. 따라서 인증객체가 없을 경우도 insert 가능하도록 로직을 수정하고 스케줄러를 서비스단 로직만 가져오게 하여 해결하였다.
  - https://github.com/chrysan5/tmsSystem/commit/de6070490c07ef451dc3f2bc7dca08b3b1bd67ba#diff-1c1c1a2466ef5f33a32b5fa5f9ac8e10e72c32b211a1e9b56fe79d9e7c246d62
  - https://github.com/chrysan5/tmsSystem/commit/c035cd81d664603db5baeaa12a8864fdc7e79362
