package com.gabriel.donation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private long balance;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false)
    private boolean isDeleted = false;

    @OneToMany(mappedBy = "user")
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserDonated> userDonateds = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<DonationPost> donationPosts = new ArrayList<>();


    @Override
    public String getUsername(){
        return phone;
    }

    @OneToOne(mappedBy = "user")
    private ForgotPassword forgotPassword;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
