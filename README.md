# [MUSINSA] Backend Engineer 과제

<br>

## 구현 범위에 대한 설명

### 패키지 구성

`common` 패키지

- 글로벌하게 사용 될 수 있는 공용 Dto, 설정 관련 클래스, 익셉션 핸들러, 유틸성 클래스 등을 각 패키지로 분리하여 포함해 놓았습니다.
- [구현 2] 단일 브랜드로 묶음구매시 합산금액 최저가 브랜드 조회 API의 성능을 위한 브랜드별 집계 배치 처리용 스케줄러가 포함되어 있습니다.

<br>

`domain` 패키지

- 패키지 구조를 설계할 때 개인간 선호도에 따라 조금씩 다르겠지만, 저의 경우에는 크게 도메인 단위로 묶어 패키지를 구성하고, 그 하위에 각 레이어 (Presentation, Service, Persistence Layer) 단위로 세부 패키지를 나누는 방식이 응집도 면에서 좋았기에 해당 구조로 설계 하였습니다.
- 도메인은 크게 브랜드(brand), 상품(product), 카테고리(category)로 나누었으며, 구현 1~4번 요구사항 API 들은 상품과 브랜드 패키지 하위에 포함되어 있습니다.

<br>

`resources` 패키지

- application.yml

  - 과제이기에 local, dev, test, prod 등 환경별 분리없이 하나의 설정파일만을 사용하였습니다.

- schema.sql

  - 데이터베이스의 테이블 생성 쿼리가 포함되어 있습니다.

- data.sql

  - 제공된 초기 데이터 insert 쿼리가 포함되어 있습니다.

<br>

### 요구사항 구현 API

`상품(product)`

- 구현 1번

  - [GET] /products/aggregation/category/lowest-price

- 구현 2번

  - [GET] /products/aggregation/brand/lowest-bundle-price
    
    - 스케줄러 배치처리를 통해 브랜드별 집계된 데이터를 조회하는 방식이어서 갱신 텀이 존재합니다.<br>(빠른 테스트를 위해 20초 주기로 짧게 설정해 놓았습니다)
    - 집계 대상은 모든 카테고리에 최소 1상품 이상 등록된 브랜드

- 구현 3번

  - [GET] /products/aggregation/category/lowest-highest-price?categoryName={categoryName}

- 구현 4번 (상품 추가, 업데이트, 삭제)

  - [POST] /products
  - [PUT] /products/{id}
  - [DELETE] /products/{id}

<br>

`브랜드(brand)`

- 구현 4번 (브랜드 추가, 업데이트, 삭제)

  - [POST] /brands
  - [PUT] /brands/{id}
  - [DELETE] /brands/{id}

<br>

> 애플리케이션 실행 후 아래 명시된 Swagger API Docs URL로 접속하시면 편리한 테스트가 가능합니다.

<br>


## 개발환경

- Backend

  - Java 17
  - Spring Boot 3.3.5
  - Gradle
  - JPA, QueryDsl

- DB

  - H2 In-Memory Database

- API Docs

  - Swagger

<br>


## 코드빌드 및 애플리케이션 실행 방법

### 코드 빌드
```bash
./gradlew clean build
```

### 테스트
```bash
./gradlew test
```

### 애플리케이션 실행
```bash
java -jar build/libs/*.jar
```

### Swagger API Docs 접속 URL
```bash
http://localhost:8080/swagger-ui/index.html
```

### H2 Database Console 접속 URL
```bash 
http://localhost:8080/h2-console
```

<br>
