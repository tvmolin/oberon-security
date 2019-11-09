package com.oberon.persistence.entities.authentication;

import com.oberon.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "client_role", schema = "authentication")
public class ClientRole extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "client_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "client_role_client_fk"))
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "role_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "client_role_role_fk"))
    private Role role;

    public static List<ClientRole> findByClientId(Long clientId) {
        return Role.list("client_id", clientId);
    }
}
