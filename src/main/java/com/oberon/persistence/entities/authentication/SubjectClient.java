package com.oberon.persistence.entities.authentication;

import com.oberon.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "subject_client", schema = "authentication")
public class SubjectClient extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "subject_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "subject_client_subject_fk"))
    private Subject subject;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "client_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "subject_client_client_fk"))
    private Client client;

    public static SubjectClient findBySubjectIdAndClientId(Long subjectId, Long clientId) {
        return SubjectClient.find("subject_id = ?1 and client_id = ?2", subjectId, clientId).firstResult();
    }
}
