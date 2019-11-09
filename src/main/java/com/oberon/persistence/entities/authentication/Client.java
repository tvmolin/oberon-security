package com.oberon.persistence.entities.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oberon.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "client", schema = "authentication")
public class Client extends BaseEntity {

    @Column(name = "name", nullable = false)
    @Type(type = "text")
    private String name;

    @Column(name = "allowed_attempts", nullable = false)
    private Long allowedAttempts;

    @JsonIgnore
    @OneToMany
    @JoinTable(name = "client_role",
            schema = "authentication",
            joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> clientRoles;

    public static Client findByName(String clientName) {
        return Client.find("name", clientName).firstResult();
    }
}
