package com.oberon.persistence.entities.authentication;

import com.oberon.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "role_permission", schema = "authentication")
public class RolePermission extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "role_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "role_permission_role_fk"))
    private Role role;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false,
            name = "permission_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "role_permission_permission_fk"))
    private Permission permission;

}
