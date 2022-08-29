package study.datajpa.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.criteria.*;

public class MemberSpec {

    //코드완성 x, 크리트리아? 실무에서 안씀
    public static Specification<Member> teamName(final String teamName) {
        return new Specification<Member>() {
            @Override
            public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join<Member, Team> t = root.join("team", JoinType.INNER);//회원과 조인

                if (StringUtils.isEmpty(teamName)) {
                    return null;
                }

                return criteriaBuilder.equal(t.get("name"), teamName);
            }
        };
    }
}
