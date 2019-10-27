package com.oberon.persistence.entities.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oberon.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "token", schema = "authentication")
@JsonIgnoreProperties({"client", "subject"})
public class Token extends BaseEntity {

    @Column(name = "token_string")
    @Type(type = "text")
    private String tokenString;

    @Column(name = "type")
    @Type(type = "text")
    private String type;

    @Column(name = "expires_in")
    private Long expiresIn;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "client_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "token_client_fk"))
    private Client client;

    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "subject_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "token_subject_fk"))
    private Subject subject;

}
