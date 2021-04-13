package com.powinter.blog.test;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogControllerTest {
	
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello spring boot</h1>";
	}

	
	@GetMapping("/auth/naver/redirect")
	public void  apiNaverTest(
			@PathParam("code") String code,
			@PathParam("state") String state,
			@PathParam("error") String error,
			@PathParam("error_description") String error_description
			) {
		
		System.out.println("=== {} : "+code);
		System.out.println("=== {} : "+state);
		System.out.println("=== {} : "+error);
		System.out.println("=== {} : "+error_description);
		
	}
}
