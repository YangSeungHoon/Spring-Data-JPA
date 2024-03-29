package sh.practiceJPA.datajpa.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class) //이벤트를 기반으로 동작한다는 것을 의미.
@MappedSuperclass
@Getter
public class BaseEntity extends BaseTimeEntity {

    //등록자 관련해서 값이 들어가는 것은 Application 실행부분에서 처리했음.(DataJpaApplication)
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;


}
