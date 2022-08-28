package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    //Web 확장 도메인 클래스 컨버터 기능 위랑 같은효과를냄, 쓴다해도 조회용으로만 쓸 것 영속성컨텍스트를 타지않음
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    //Web 확장
    // url에 localhost:8080/members?page=0
    // url에 localhost:8080/members?page=0&size=3&sort=id,desc 등 다 사용가능
    @GetMapping("members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);

        Page<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        return map;
    }

    //의존성 주입이 이루어진 후 초기화를 수행하는 메서드
    @PostConstruct
    public void init() {
        for (int i = 0 ; i<100; i++) {
            memberRepository.save(new Member("user"+i,i));
        }

    }
}
