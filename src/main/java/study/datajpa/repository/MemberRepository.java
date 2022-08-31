package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.Entity;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom, JpaSpecificationExecutor<Member> {
//JpaSpecificationExecutor는 Specification임 실무에 안씀

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


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //반환타입에 대해서
    //List는 없어도 null 아님 그냥 list로 받을 것
    List<Member> findListByUsername(String username); //컬렉션

    //단건은 없으면 null임
    Member findMemberByUsername(String username);//단건

    Optional<Member> findOptionalByUsername(String username); //단건 Opional
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //Page 반환, Page 반환하면 count 쿼리가 자동으로 나가는데 여기서 조인이 많은 경우 카운트쿼리가 복잡하게 나가니깐 따로 지정하는 것이 유리
    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //벌크성 업데이트 쿼리, 주의점!! 영속성컨텍스트 무시하고 디비에 업데이트 떄려박음!!
    @Modifying(clearAutomatically = true)
    //이거 넣어줘야 순수 jpa .executeUpdate(); 같은 효과나옴 꼭 넣어줄 것, clearAutomatically true 해줘야 업데이트 후 영속성 컨텍스트 클리어 해줌
    @Query("update Member m set m.age = m.age +1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //fetch join 사용하여 값 조회
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"true"})
        //jqpl 없이도 fetch join 하는방법 !!
    List<Member> findAll();

    @EntityGraph(attributePaths = {"true"}) //jqpl 짜논 것에 entityGraph만 살짝 얹는 것도 가능
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = ("true"))
        //메소드 함수에 entityGraph만 살짝 얹는 것도 가능
//    @EntityGraph("Member.all") // Entity 에 NamedEntityGraph 지정해서도 사용가능 잘 안씀
    List<Member> findEntityGraphByUsername(@Param("username") String username);
    ////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////////
    //변경감지 안하게끔 ReadOnly 속성 지정하는거, 근데 최적화 크게 안됌, 성능테스트 해봐야함
    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
        //락기능이라함..
    List<Member> findLockByUsername(String username);
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    //find와 By사이에는 아무거나 들어가도됌
    List<UsernameOnlyDto> findProjectionsByUsername(@Param("username") String username);

    //네이티브 쿼리////////////////////////////////////////////////////////////////////////////////////
    @Query(value = "select * from Member where username = ?", nativeQuery = true)
    Member findByNativeQuery(String username);

    @Query(value = "select m.member_id as id, m.username, t.name as teanName from member m left join team t",
            countQuery = "select count(*) from member" ,
            nativeQuery = true)
    Page<MemberProjection> findByNativeProjection(Pageable pageable);
}
