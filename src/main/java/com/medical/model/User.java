package com.medical.model;

import com.medical.configs.CustomGrantedAuthority;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private Boolean sex;

    private int age;

    @Column(name="phone_number")
    private String phoneNumber;

    private Boolean status;

    private String image;

    private String token;

    @ManyToOne
    private Role role;

    @OneToOne(mappedBy = "user")
    private Doctor doctor;

    @OneToOne(mappedBy = "user")
    private Patient patient;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "sender")
    private List<Message> messageSend;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "receiver")
    private List<Message> messageReceive;

    public static List<GrantedAuthority> getGrantedAuthorities(Role role){
        List<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
        List<String> permissions=new ArrayList<String>();

        for (Permission per : role.getPermissions()) {
            permissions.add(per.getName());
        }

        authorities.add(new CustomGrantedAuthority(role.getName(),permissions));

        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authList=getGrantedAuthorities(this.getRole());
        return authList;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status;
    }


}
