package by.questionary.domain;

import javax.persistence.Table;

@Table(name = "user_role")
public enum Role{
    ROLE_ADMIN,
    ROLE_USER;

}
