package com.powinter.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// RestController는 데이터를 리턴
// Controller는 기본경로의 파일을 리턴
@Controller
public class TempControllerTest {
	
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome");
		// 컨트롤러는 파일을 리턴함
		// 파일리턴 기본경로는 src/main/resources/static
		// 이 기본경로는 웹브라우저에서 인식하는 파일만 배치가능. html, 이미지, css 등
		// 파일리턴명은 앞에 /를 추가해야됨. 아니면 static에 연결되어 파일명을 찾음.
		return "/home.html";
	}
	
	@GetMapping("/temp/img")
	public String tempImg( ) {
		return "/a.png";
	}

	@GetMapping("/temp/jsp")
	public String tempJsp( ) {
		// application.yml 에서 mvc  관련 설정을 기반으로
		// prefix: /WEB-INF/views/
		// suffix: .jsp
		// 풀경로는 /WEB-INF/views/ + 리턴파일명 + .jsp 가 됨.
		// 따라서 return 파일명에 차리가 있음.
		return "test";
	}
	
	@GetMapping("/naverlogin")
	public String naverLogin() {
		System.out.println("naverlogin");
		// 컨트롤러는 파일을 리턴함
		// 파일리턴 기본경로는 src/main/resources/static
		// 이 기본경로는 웹브라우저에서 인식하는 파일만 배치가능. html, 이미지, css 등
		// 파일리턴명은 앞에 /를 추가해야됨. 아니면 static에 연결되어 파일명을 찾음.
		return "/naverlogin";
	}	

	@GetMapping("/naverloginjs")
	public String naverLoginJS() {
		System.out.println("naverloginjs");
		// 컨트롤러는 파일을 리턴함
		// 파일리턴 기본경로는 src/main/resources/static
		// 이 기본경로는 웹브라우저에서 인식하는 파일만 배치가능. html, 이미지, css 등
		// 파일리턴명은 앞에 /를 추가해야됨. 아니면 static에 연결되어 파일명을 찾음.
		return "/naverloginjs.html";
	}	
}

