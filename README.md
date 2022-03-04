
# 자바 ORM 표준 jpa 프로그래밍 - 기본편 ( 김영한 )
>## 영속성 컨텍스트
- jpa를 이해하는데 가장 중요한 용어
- entity manager를 이용하여 관리
- 논리적인 개념
- jpa를 이용하여 insert 하려 해도 실제 query가 동작은 transaction이 commit되는 시점에 실행
- transaction commit전에는 entity manager에 키 / entity로 저장 ( 실제 db에 저장되는것이 아님 )
- select 후 같은 entity를 조회하면 케시에서 조회하여 출력 ( db에서 조회하는건 최초 조회시 혹은 transaction 종료 후 다시 조회 시)
- update 없이 entity를 수정하는 행위로 update query를 실 ( 변경을 감지한다 )
***
>## 플러시
- 영속성 컨텍스트 변경내역을 db에 반영
- em.flush() <- 수동 호출
- transaction commit <- 자동 호출
- jpql 쿼리실행 <- 자동 호출
- flush 실행 모드를 설정할 수 있다
- 영속성 컨텍스트를 비우지 않음
- 영속성 컨텍스트를 db에 반영
- transaction 단위가 중요
***
>## 엔티티 매핑
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
***
>## 데이터베이스 스키마 자동 생성
- DDL을 애플리케이션 실행 시정에 자동 생성
- 운영보단 개발단계에서 사용 추천
- create ( drop + create )
- create-drop (create와 같지만 종료 시점에 drop)
- update ( 변경분만 반영 alter / add colume만 가능 )
- validate ( 엔티티와 테이블이 정상 메핑인지 확인 )  
- none ( 사용하지 않음 )
- 데이터베이스 방언별로 자동생성
- 운영 장비에는 절대 create, create-drop, update 사용하면 안됨
- 테스트 서버 및 운영서버에서 create, create-drop사용 시 데이터가 삭제됨
- @Colume의 속성으로 length, unique 등 DDL생성 제약조건을 생성할 수 있음 
***
>## 필드와 컬럼 매핑
- create 어노테이션을 이용하여 타입을 지정할 수 있음
- @Transient ( 생성 하지 않음 )
- @Temporal ( 날짜 타입 )
- @Enumerated ( enum 타입 )
- @Lob ( BLOB, CLOB 타입 )
- @Column ( 컬럼 )
#### @Column ( 가장 많이 쓰임 )
- name ( 필드와 매핑할 테이블 컬럼 )
- updateble, insetble ( 컬럼을 변경 혹은 생성할 지 여부 )
- nullable ( not null 제약 여부 )
- unique ( unique 제약조건, 잘 사용안함 / 알수없는 임의의 데이터 세팅 / @Table에서 이름까지 세팅하여 설정 가능)
- columnDefinition ( db 컬럼 정보를 직접 세팅 / 세팅한 문자가 ddl에 그대로 사용됨 / db별로 방언을 확인해야함 )
- precision, scale ( 아주큰 숫자나 소수점에 사용 / BigDecimal 등 )
#### @Enumerated
- enum 사용 시 주의사항 ( default ORDINAL / ORDINAL = enum의 순서로 저장, integer로 생성 , STRING = 이름으로 저장, varchar로 생성)
- ORDINAL사용하면 enum의 순서가 바뀌면 데이터가 꼬일 수 있음 ( STRING사용 필수 )
#### @Temporal ( java 8 이후는 거의 필요없다 )
- 하이터네이트 최신에선 LocalData(년월), LocalDateTime(년월일시) 지원 ( 속성에 따라 db 컬럼 타입도 변경 )
- 최신버전이 아닐 경우 value 속성을 이용하여 타입 지정
#### @Lob
- 속성을 지정할 수 없다
- java 타입이 String이름 CLOB, 나머지는 BLOB으로 자동매핑
***
>## 기본키 매핑
- 기본키 매핑에 사용되는 어노테이션은 크게 두가지 ( @id, @GeneratedValue )
- id는 세팅하면 안됨
- 기본 키 제약 조건 ( null 아님, 유일, 변하면 안된다. )
- 미래까지 만족하는 조건, 대리키를 사용하자 ( 주민번호 사용 X )
- 권장 ( Long타입 + 대체키 + 키 생성전략 사용 )
- auto_increment, sequence, random 조합 등
#### @id
- 직접할당
#### @GeneratedValue
- 자동할당
- IDENTITY ( db에 위임, mysql )
    - id값은 db에 insert되어야 확인 가능
    - 영속성 컨텐스트는 id가 필요한데 키가 없음
    - 영속성 관리를 하기위해 persist할때 insert해버림 ( 원래는 transaction commit시에 날림 )
    - commit전에 영속성 컨택스트에 키를 포함하여 관리
    - 쿼리를 모아서 한번에 실행하는게 불가능
- SEQUENCE ( db 시퀀스 오브젝트 사용, oracle / SequenceGenerator 필요 )
    - seq는 db에서 관리
    - 영속성 컨텍스트에 저장할때 pk가 필요 ( seq를 먼저 가져와야함 )
    - 쿼리를 실행하기 전 seq를 먼저 호출하여 키를 가져옴
    - commit시 실제 쿼리 실행
    - buffer 사용 가능
    - 
- TABLE ( 키 생성용 테이블 사용 , 모든 db사용 / TabelGenerator 필요 )
- AUTO ( 방언에 따라 자동 지정 )
#### @Table
- 여러 옵션이 있지만 실무에서 사용하지 적합하지 않음
