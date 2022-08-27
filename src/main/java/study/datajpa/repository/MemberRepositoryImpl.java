package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
// MemberRepository + Impl 이름 맞춰줘야함!!!!!!!!!!!!!!!!!!
    //그래야 스프링 data jpa가 기능 연결시켜줌
    
    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }
}
