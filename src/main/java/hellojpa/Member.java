package hellojpa;

import javax.persistence.*;
import java.util.Date;

@Entity
//@Table(name="MBR")
public class Member {

    @Id // 기본키 매핑 수동매핑
    private Long id;
//    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 매핑 위임
//    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
