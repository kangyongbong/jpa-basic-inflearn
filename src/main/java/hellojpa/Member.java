package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
//@Table(name="MBR")
public class Member {

    @Id
    private Long id;

//    @Column(length=100)
    @Column(name="name", nullable = false)
    private String userName;
    private int age;

    @Enumerated(EnumType.STRING) // enum type 사용을 위한 어노테이션
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP) // java - 날짜 시간 포함 , db는 별도로 관리하여 지정해줘야 한다
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    @Lob // varchar를 넘어서는 길이의 문자를 사용하고자 할떄
    private String description;

    public Member() {
    }

//    public Member(Long id, String name) {
//        this.id = id;
//        this.name = name;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public int getAge() {
//        return age;
//    }
//
//    public void setAge(int age) {
//        this.age = age;
//    }
}
