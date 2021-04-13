package com.powinter.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest : ";
	
	@GetMapping("/http/lombok")
	public String lombokTest() {
//		Member m1 = new Member(1,"pow","1234","a@b.com");
		Member m1 = Member.builder().username("aaaa").password("1234").email("a@b.com").build(); // 생성시 순서가 필요없음
//		Member m2 = new Member();
		
		System.out.println(TAG+"getter : "+m1.getId());
		m1.setId(5000);
		System.out.println(TAG+"getter : "+m1.getId());
		
		return "lombok test 완료";
	}
	
	@GetMapping("/http/get")
	public String getTest(Member m) {
		return "get 요청 : "+m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
	}

	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) {
		return "post 요청 : "+m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
	}

	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청 : "+m.getId()+", "+m.getUsername()+", "+m.getPassword()+", "+m.getEmail();
	}
	
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}
