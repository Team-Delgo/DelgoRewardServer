package com.delgo.reward.comm.security.service;


import com.delgo.reward.comm.security.domain.PrincipalDetails;
import com.delgo.reward.user.domain.User;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    private final UserQueryService userQueryService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userQueryService.getOneByEmail(email);
        return new PrincipalDetails(user);
    }
}
