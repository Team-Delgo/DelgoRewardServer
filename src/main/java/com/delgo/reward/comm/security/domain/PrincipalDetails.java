package com.delgo.reward.comm.security.domain;


import com.delgo.reward.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public record PrincipalDetails(User user) implements UserDetails {

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
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
        return true;
    }
    
	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//        user.getRoleList().forEach(r -> {
//        	authorities.add(()->{ return r;});
//        });
//        return authorities;
        return null;
    }
}
