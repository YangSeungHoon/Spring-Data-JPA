package sh.practiceJPA.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class JpaBaseEntity {

    @Column(updatable= false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    @PrePersist//저장하기전에 이 어노테이션이 붙어있으면 이것부터 실행한다.
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate // 업데이트 될 때 이것을 실행한다.
    public void preUpdate() {
        updatedDate = LocalDateTime.now();
    }
}
