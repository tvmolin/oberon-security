package com.oberon.persistence.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue
    public Long id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt = LocalDateTime.now();

    @Column(name = "created_by", nullable = false)
    @Type(type = "text")
    private String createdBy = "System";

    @Column(name = "modified_by", nullable = false)
    @Type(type = "text")
    private String modifiedBy = "System";

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "<" + id + ">";
    }

}
