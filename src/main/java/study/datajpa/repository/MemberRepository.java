package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    //jpql 바로쓰는 방법 , 많이쓰임
    @Query("select m from Member m where m.username =:username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    //jqpl-> 값 받아오는법
    @Query("select m.username from Member m")
    List<String> findUsernameList();

    //jqpl 에서 dto로 값 받아오는 법
    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username,t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    //jpql in절 사용하여 컬렉션으로 조건줘서 엔티티 받기, 많이쓰임
    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    //반환타입에 대해서
    //List는 없어도 null 아님 그냥 list로 받을 것
    List<Member> findListByUsername(String username); //컬렉션
    //단건은 없으면 null임
    Member findMemberByUsername(String  username);//단건
    Optional<Member> findOptionalByUsername(String username); //단건 Opional

    //Page 반환, Page 반환하면 count 쿼리가 자동으로 나가는데 여기서 조인이 많은 경우 카운트쿼리가 복잡하게 나가니깐 따로 지정하는 것이 유리
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
}
