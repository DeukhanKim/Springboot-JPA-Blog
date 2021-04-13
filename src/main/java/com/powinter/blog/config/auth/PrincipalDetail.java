package com.powinter.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.powinter.blog.model.User;

import lombok.Getter;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetail 타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다. UserDetail 타입의 PrincipalDetail이 세션저장소에 저장되는것
@Getter
public class PrincipalDetail implements UserDetails{ // DI해서 사용
	private User user; // 콤포지션 : 객체를 품고 있는것
	
	// 리턴을 위한 생성자
	public PrincipalDetail(User user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정 만료 여부 리턴 (true: 만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 잠김 여부 리턴 (true: 안잠김)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호 만료 여부 리턴 (true : 만료안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	// 계정 활성화 여부 리턴 (true: 활성화상태)
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// 계정이 가지고 있는 권한 목록을 리턴한다. 권한이 여러개일 경우 루프/현재는 1개만 이용
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collectors = new ArrayList<>();
//		collectors.add(new GrantedAuthority() {
//			
//			@Override
//			public String getAuthority() {
//				return "ROLE_"+user.getRole(); // "ROLE_" : 스프링에서 롤을 받을때 규칙.
//			}
//		});
		
		// 자바에서는 메소드 안에서 메소드 바로 호출 불가하고 객체로 감싸야 함.
		// 인터페이스를 new 하면 임의 객체가 생성되고 생성된 임의 객체는 안에서 선언된 메소드를 오버라이드 해줘야함
		// add()안에 타입은 1개 뿐이고 이 타입인 GrantedAuthoriey 객체는 1개의 메소드만 존재하므로 아래처럼 람다식으로 표현 가능.
		collectors.add(()->{return "ROLE_"+user.getRole();});
		return collectors;
	}
	
}
