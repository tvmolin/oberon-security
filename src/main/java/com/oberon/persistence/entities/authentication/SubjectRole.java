package com.oberon.persistence.entities.authentication;

import com.oberon.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "subject_role", schema = "authentication")
public class SubjectRole extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "role_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "subject_role_role_fk"))
    private Role role;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "subject_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "subject_role_subject_fk"))
    private Subject subject;

    public static List<SubjectRole> findBySubjectId(Long subjectId) {
        return SubjectRole.list("subject_id", subjectId);
    }
}
