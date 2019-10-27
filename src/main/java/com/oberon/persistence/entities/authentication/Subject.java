package com.oberon.persistence.entities.authentication;

import com.oberon.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "subject", schema = "authentication")
public class Subject extends BaseEntity {

    @Column(name = "user_name")
    @Type(type = "text")
    private String userName;

    @Column(name = "alternative_user_name")
    @Type(type = "text")
    private String alternativeUserName;

    @Column(name = "password")
    @Type(type = "text")
    private String password;

    @Column(name = "salt")
    @Type(type = "text")
    private String salt;

    @Column(name = "name")
    @Type(type = "text")
    private String name;

    @Column(name = "email")
    @Type(type = "text")
    private String email;

    @Column(name = "active")
    private boolean active;

    public static Subject findByUserName(String userName) {
        Subject s = find("user_name", userName).firstResult();
        return s == null ?
                find("alternative_user_name", userName).firstResult()
                : s;
    }

}
