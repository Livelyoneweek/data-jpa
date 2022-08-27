package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass //Member 테이블에 속성 내려서 테이블에 컬럼으로 생성되도록 함
public class JpaBaseEntity {

    @Column(updatable = false) //createdDate는 혹시나 업데이트 안되게끔 설정
    private LocalDateTime createdDate;
    
    private LocalDateTime updatedDate;

    @PrePersist // 생성등록 하기 전에 호출
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate //업데이트 하기 전에 호출
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}
