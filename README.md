
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
    - 네트워크를 여러번 타야하는 성능이슈가 있을 수 있음
      - allocationSize 옵션을 사용하면 미리 seq를 지정된 개수만큼 가져와서 메모리에서 사용가능
      - 동시성 이슈를 해결할 수 있음
      - insert를 여러번 하는 경우에 최초에 1회 실행( 확인용 ) 후 1회 추가 실행 ( 메모리 확보 )
      - 그래서 최종 메모리상 seq 수는 allocationSize + 1 개
      - 많은 수를 세팅하게 되면 application shutdown시 메모리 증발로 seq가 갑자기 뻥튀기 되는 문제발생
      - 너무 많은 size를 세팅하면 안됨
- TABLE ( 키 생성용 테이블 사용 , 모든 db사용 / TabelGenerator 필요 )
- AUTO ( 방언에 따라 자동 지정 )
#### @Table
- 여러 옵션이 있지만 실무에서 사용하지 적합하지 않음

>##연관관계 매핑 기초
- 객체지향과 관계형db간의 차이로 인해 발생
- 객체의 참조와 테이블의 외래 키 매핑
- 용어 이해가 필요
  - 연관관계의 주인(owner) : 객체 양방향 연관관계는 관리주인이 필요
  - 방향 (direction) : 단방향,양방향
  - 다중성 (muliplicity) : 1:N, N:1, 1:1 , N:N 등
- 엔티티를 객체지향스럽게 디자인하는것이 어려움 ( 관계형db에 지식이 필요 )

###단방향 연관관계
- 연관관계가 있는 Entity를 join할 Entity에 세팅 
- @ManyToOne, @ManyToMany로 다중성 세팅
- @JoinToColumn(name="외래키명")으로 외래키 매핑
- 기본적으로 join을 걸어 quary를 실행하지만 분리하여 각각 실행도 가능

>##양방향 연관관계와 연관관계의 주인
- jpa에서 가장어려운 내용 중 하나

###양방향 연관관계
- 단방향이나 양방향이나 db table은 변화가 없음 (외래키 이용)
- 조인할 객체에 변수선언
- mappedBy 옵션을 사용

#### mappedBy
- 객체와 테이블간의 연관관계 차이 이해필요
- 객체 : 팀->맴버 , 맴버->팀 연관관계 ( 단반향 연관관계가 2개 )
- 테이블 : 팀 <-> 맴버 ( 외래키 하나로 연관관계 설명 )
- 객체에서는 참조하는 객체 중 외래키를 관리하는 객체가 있어야한다 ( 연관관계 주인 )

####연관관계의 주인
- 객체 주우 하나를 연관관계 주인으로 지정
- 연관관계의 주인만 외래 키 관리 ( 등록, 수정)
- 주인이 아닌쪽은 읽기만 가능
- 주인은 mappedBy 속성 사용 하지 않는다
- 주인이 아니면 mappedBy 속성으로 주인 지정
- 주인은 어떻게 정해야 하는가
  - 외래 키가 있는 곳을 주인으로 정해라
  - pk가 있는곳을 주인으로 정하면 참조된 다른 객체의 데이터가 변하면 엉뚱한 테이블에 업데이트가 될 수 있다.
  - 성능적으로 pk가 있는 곳을 주인으로 하면 손해

###양방향 매핑시 가장 많이 하는 싨
- 연관관게의 주인에 값을 입력하지 않음
- mappedBy는 읽기 전용 ( jpa에서 update, insert시 사용하지 않음 )
- 양쪽 모두에 값을 세팅
  - jpa입장에선 주인에만 세팅하면 맞음
  - 참조된 member를 조회하는 시점에 select문을 날림
  - flush,clear를 하지 않으면 영속성 컨택스트에만 가지고 있음 ( 순수한 객체상태 / 키가 없음으로 member조회 불가 )
  - 양방향시엔 양쪽 다 세팅하는게 문제가 없음
  - 무한루프에 빠질 수 있다.(toStrign(),lombok,JSON 등)
  - setter의 이름을 바꾼 후 연관관계 편의메소드를 생성 ( 참조하는 객체에 add )

>##다양한 연관관계 매핑
###연관관계 매핑 시 고려사항 3가지
- 다중성
  - 다대일 : @ManyToOne
  - 일대다 : @OneToMany
  - 일대일 : @OneToOne
  - 다대다 : @ManyToMany ( 실무에서 사용하면 안된다 )
- 단방향,양방향
  - 테이블
    - 외래키 하나로 양쪽 조인 가능
    - 사실 방향이라는 개념이 없음
  - 객체
    - 참조용 필드가 있는 쪽으로만 참조 가능
    - 한쪽만 참조하면 단방향
    - 양쪽이 서로 참조하면 양방향 ( 사실 양방향은 없다. 객체 입장에서는 단방향 2개 )
- 연관관계 주인
  - 테이블은 외래 키 하나로 두 테이블이 연관관계를 맺음
  - 객체 양방향 관계는 참조가 2군데
  - 둥줄 테이블의 외래 키를 관리할 곳을 지정해야함
  - 외래 키는 관리하는 참조
  - 주인의 반대편은 외래 키에 영향을 주지 않음, 단순 조회만 가능

#### 다대일 ( N : 1 )
- 다(N)에 외래 키가 있음
- 다대일 단방향이 가장 많이 사용되는 관계
- 양방향은 외래 키가 있는쪽이 주인
- 양쪽다 참조하도록 개발
#### 일대다 ( 1 : N )
- 해당 모델은 권장하지 않음 ( 실무에서는 거의 가져가지 않음)
- 알(1)이 주인
- 다쪽에 외래 키가 있음
- 일 쪽에서 @joinnColumn을 이용하여 매핑
- 쿼리가 외래 키를 가진 테이블때문에 더 실행해야 된다.
- @joinnColumn을 사용하지 않으면 joinTable방식을 사용 ( 중간 테이블을 하나 생성함 )
- 일대다 단반향 매핑보다는 다대일 양방향 매핑을 사용
####일대일 ( 1 : 1 )
- 주, 대상 테이블 중에 외래 키 선택가능
- 외래 키에 데이터베이스 유니크 제약조건 추가
- 다대일 매핑과 유사
- 외래 키가 있는 곳이 주인
####다대다 ( N : M )
- 실무에서 사용하지 않음
- 연결테이블을 추가하여 일대다,다대일 관계로 풀어야함
- 객체로는 다대다가 가능하나 관계형db는 안됨