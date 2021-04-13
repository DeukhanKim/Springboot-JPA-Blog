package com.powinter.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.powinter.blog.model.User;
import com.powinter.blog.repository.UserRepository;

@Service // 빈등록
public class PrincipalDetailService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	// 스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
	// password 부분 처리는 알아서 함.
	// 개발자는 username 이 DB에 있는지만 확인해주면 됨. 확인은 아래 함수에서 함.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findByUsername(username) // findByUsername() 메소드가 없으면 생성해야 됨.
			.orElseThrow(()->{
				return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. :"+username);
			});
		return new PrincipalDetail(principal); // 리턴시 시큐리티 세션에 UserDetail 타입으로 세션이 저장됨
												// 이 메소드를 오버라이드 안하면 기본 시큐리티 user만 사용가능.
												// principal 을 위하여 객체 생성자 필요
	}
}
