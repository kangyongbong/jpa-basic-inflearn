
# 자바 ORM 표준 jpa 프로그래밍 - 기본편 ( 김영한 )
## 인프런
### 영속성 컨텍스트
- jpa를 이해하는데 가장 중요한 용어
- entity manager를 이용하여 관리
- 논리적인 개념
- jpa를 이용하여 insert 하려 해도 실제 query가 동작은 transaction이 commit되는 시점에 실행
- transaction commit전에는 entity manager에 키 / entity로 저장 ( 실제 db에 저장되는것이 아님 )
- select 후 같은 entity를 조회하면 케시에서 조회하여 출력 ( db에서 조회하는건 최초 조회시 혹은 transaction 종료 후 다시 조회 시)
- update 없이 entity를 수정하는 행위로 update query를 실 ( 변경을 감지한다 )

### 플러시
- 영속성 컨텍스트 변경내역을 db에 반영
- em.flush() <- 수동 호출
- transaction commit <- 자동 호출
- jpql 쿼리실행 <- 자동 호출
- flush 실행 모드를 설정할 수 있다
- 영속성 컨텍스트를 비우지 않음
- 영속성 컨텍스트를 db에 반영
- transaction 단위가 중요

### 엔티티 매핑
#### @Entity
- 객체와 테이블 매핑
- @Entity가 붙은 클래스는 jpa가 관리
- jpa를 사용해서 테이블과 매핑할 클래스는 @Entity필수
- 기본 생성자 필수 (파라미터가 없는 public 또는 protected 생성자)
- final, enum, interface, inner 사용 안됨
- 저장할 필드에 final사용 안됨
- name속성이 있지만 기본은 class name (기본값 사용 권장)
#### @Table
- 엔티티와 메핑할 테이블 지정
- name속성을 사용하여 테이블명 변경 가능
