package com.powinter.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.powinter.blog.dto.ResponseDto;
import com.powinter.blog.model.RoleType;
import com.powinter.blog.model.User;
import com.powinter.blog.service.UserService;


// 서비스 레이어의 사용은 트랜잭션을 처리 하기 위함.
@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
//	@Autowired
//	private HttpSession session;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) {
		System.out.println("UserApiController : save 호출됨");
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	// Spring Security 사용으로 삭제
//	@PostMapping("/api/user/login")
////	public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) { // session을 매개변수로 호출해서 사용해도 되고 DI해서 사용해도 동일함.
//	public ResponseDto<Integer> login(@RequestBody User user) {
//		System.out.println("UserApiController : login 호출됨");
//		User principal = userService.로그인(user); // principal(접근주체)
//		
//		if (principal != null) {
//			session.setAttribute("principal", principal);
//		}
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}

}
