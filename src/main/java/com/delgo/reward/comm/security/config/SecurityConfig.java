package com.delgo.reward.comm.security.config;


import com.delgo.reward.comm.security.filter.JwtAuthenticationFilter;
import com.delgo.reward.comm.security.filter.JwtAuthorizationFilter;
import com.delgo.reward.comm.security.filter.CustomAuthenticationEntryPoint;
import com.delgo.reward.user.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	private final UserQueryService userQueryService;

	@Value("${config.cors-allow-url}")
	String CORS_ALLOW_URL;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		List<String> allowedOrigins = Arrays.asList(CORS_ALLOW_URL.split(",\\s*"));
		configuration.setAllowedOrigins(allowedOrigins);
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.addExposedHeader("Authorization_Access, Authorization_Refresh");
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}


	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.formLogin().disable()
				.httpBasic().disable()
				.cors().configurationSource(corsConfigurationSource())
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(new CustomAuthenticationEntryPoint())
				.and()
				.addFilter(new JwtAuthenticationFilter(authenticationManager()))
				.addFilter(new JwtAuthorizationFilter(authenticationManager(), userQueryService))
				.authorizeRequests()
				.antMatchers("/api/oauth/**").permitAll()
				.antMatchers("/api/auth/**").permitAll()
				.antMatchers("/api/user/**").permitAll()
				.antMatchers("/api/code/**").permitAll()
				.antMatchers("/api/fcm/**").permitAll()
				.antMatchers("/api/token/reissue").permitAll()
				.antMatchers("/api/account/logout/**").permitAll()

				// certification
				.antMatchers("/api/certification/all").permitAll()
				.antMatchers("/api/certification/id").permitAll()
				.antMatchers("/api/certification/my").permitAll()
				.antMatchers("/api/certification/other").permitAll()
				.antMatchers("/api/certification/mungple").permitAll()

				// delgo-map
				.antMatchers("/api/map/**").permitAll()
				.antMatchers("/api/map/mungple").permitAll()
				.antMatchers("/api/photo/webp").permitAll()
				.antMatchers("/api/photo/mungplenote/*").permitAll()
				.antMatchers("/api/photo/mungple/*").permitAll()
				.antMatchers("/api/photo/achievements/*").permitAll()

				.antMatchers("/api/mungple").permitAll()
				.antMatchers("/api/mungple/bookmark").permitAll()
				.antMatchers("/api/mungple/detail").permitAll()
				.antMatchers("/api/mungple/parsing").permitAll()
				.antMatchers("/api/mungple/category").permitAll()
				.antMatchers("/api/mungple/cache").permitAll()

				.antMatchers("/health-check").permitAll()
				.antMatchers("/kafka/**").permitAll()

				// version
				.antMatchers("/api/version").permitAll()

				// swagger
				.antMatchers("/swagger-ui/**").permitAll()
				.antMatchers("/v3/**").permitAll()

//				.anyRequest().authenticated();

//				 TEST
				.antMatchers("/**").permitAll();
	}
}
