package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;

    //여기 파라미터명을 기준으로 실행함 OpenProjections
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
