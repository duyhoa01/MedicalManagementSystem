package com.medical.configs;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class CustomGrantedAuthority implements GrantedAuthority {

    private String role;
    private List<String> permissions;

    public CustomGrantedAuthority() {
        super();
    }

    public CustomGrantedAuthority(String role) {
        super();
        this.role = role;
    }

    public CustomGrantedAuthority(String role, List<String> permissions) {
        super();
        this.role = role;
        this.permissions = permissions;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public List<String> getPermissions() {
        return permissions;
    }
    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }

}
