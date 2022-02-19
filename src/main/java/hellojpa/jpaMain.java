package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class jpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        // code
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*insert*/
//            Member member = new Member();
//            member.setId(2L);
//            member.setName("HelloB");
//            em.persist(member)

            /*select*/
//            Member findMember = em.find(Member.class, 1L);
//            System.out.println("findMember.getName() = " + findMember.getName());
//            System.out.println("findMember.getId() = " + findMember.getId());

            /*update*/
//            Member findMember = em.find(Member.class, 1L);
//            findMember.setName("HelloJPA");

            /*영속성*/
//            Member member = new Member();
//            member.setId(3L);
//            member.setName("HelloJpa");
//            System.out.println("==== BEFORE =====");
//            em.persist(member);
//            System.out.println("==== AFTER =====");

//            Member findMemeber1 = em.find(Member.class, 3L);
//            Member findMember2 = em.find(Member.class, 3L);
//
//            System.out.println("findMemeber1 = " + findMemeber1.getId());
//            System.out.println("findMember2 = " + findMember2.getId());



            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();
     }
}
