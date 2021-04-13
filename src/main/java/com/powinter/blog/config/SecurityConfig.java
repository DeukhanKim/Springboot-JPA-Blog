package com.powinter.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.powinter.blog.config.auth.PrincipalDetail;
import com.powinter.blog.config.auth.PrincipalDetailService;

// 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 등록 (설정 빈으로)

// 아래 3가지 어노테이션은 세트
@Configuration // 빈등록 (IoC관리)
@EnableWebSecurity // 시큐리티에 필터 추가. 모든 컨트롤러 호출 전에 필터링.
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean // IoC 가 됨.
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder(); // Security 에 있는 함수. @Bean으로 IoC 하면 스프링에서 관리해줌
	}
	
	// 시큐리티가 대신 로그인해주는데 password를 가로채기 하는데
	// 해당 password가 뭘로 해쉬가 되어 회원가입이 되었는지 알아야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // csrf 토큰 비활성화 (테스트시 걸어두는게 좋음)
								// 스프링 시큐리티를 사용하면 csrf token을 가지고 요청하는 
								// 경우만 요청 수락하므로 테스트시엔 비활성화 필요.
			.authorizeRequests()
				.antMatchers("/","/auth/**","/js/**","/css/**","/image/**")	// /auth/로 시작하는 요청은
				.permitAll()				// 모두 허용
				.anyRequest()				// 나머지 요청은
				.authenticated()			// 인증되어야 함.
			.and()
				.formLogin()
				.loginPage("/auth/loginForm")
				.loginProcessingUrl("/auth/loginProc")  // 해당 주소가 호출될 경우 스프링 시큐리티가 해당 요청을 가로채서 대신 로그인해줌
				.defaultSuccessUrl("/"); // 로그인 성공시 호출될 URI
	}
}
