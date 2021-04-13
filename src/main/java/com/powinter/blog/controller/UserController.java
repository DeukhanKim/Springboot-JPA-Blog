package com.powinter.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


// 인증이 안된 사용자들이 출입하는 경로를 /auth/** 로 설정
// 그냥 주소가 /  이면 index.jsp 허용
// resources/static 이하에 있는 폴더들도 접근 허용 설정

@Controller
public class UserController {
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		
		return "user/joinForm";
	}

	@GetMapping("/auth/loginForm")
	public String loginForm() {

		return "user/loginForm";
	}
}
