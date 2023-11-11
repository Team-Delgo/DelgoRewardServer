package com.delgo.reward.comm.security.services;


import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.infrastructure.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new NullPointerException("NOT FOUND USER")).toModel();

        return new PrincipalDetails(user);
    }
}
