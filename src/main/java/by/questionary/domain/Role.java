package by.questionary.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Table;

@Table(name = "user_role")
public enum Role implements GrantedAuthority {
    ADMIN,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }

}
