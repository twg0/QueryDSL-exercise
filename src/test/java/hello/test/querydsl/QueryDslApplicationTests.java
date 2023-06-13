package hello.test.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.test.querydsl.domain.Member;
import hello.test.querydsl.domain.Team;
import hello.test.querydsl.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class QueryDslApplicationTests {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    JPAQueryFactory jpaQueryFactory;

    @Test
    void dataTest() throws Exception {
        // given

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        // when

        List<Member> memberList = em.createQuery("select m from Member m", Member.class).getResultList();

        // then

        for (Member member : memberList) {
            System.out.println("member = " + member);
            System.out.println("member.getTeam() = " + member.getTeam());
        }
    }

    @Test
    public void testMember() throws Exception {
        // given


        // when

        // then

    }
}
