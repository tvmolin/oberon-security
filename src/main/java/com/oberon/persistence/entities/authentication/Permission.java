package com.oberon.persistence.entities.authentication;

import com.oberon.persistence.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "permission", schema = "authentication")
public class Permission extends BaseEntity {

    @Column(name = "name")
    private String name;

}
