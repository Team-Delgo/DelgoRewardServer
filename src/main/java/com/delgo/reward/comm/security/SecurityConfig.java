package com.delgo.reward.comm.security;


import com.delgo.reward.comm.security.jwt.filter.JwtAuthenticationFilter;
import com.delgo.reward.comm.security.jwt.filter.JwtAuthorizationFilter;
import com.delgo.reward.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final UserRepository userRepository;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.formLogin().disable()
				.httpBasic().disable()

				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(),userRepository))
				.authorizeRequests()
//				.antMatchers("/wish/**").authenticated()
//				.antMatchers("/booking/**").authenticated()
//				.antMatchers("/photo/**").authenticated()
//				.antMatchers("/coupon/**").authenticated()
//				.antMatchers("/review/**").authenticated()
//				.antMatchers("/changePassword").authenticated()
				.anyRequest().permitAll();
	}
}






