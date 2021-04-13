package com.powinter.blog.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class EncTest {

	@Test
	void test() {
//		fail("Not yet implemented");
		String encPassword = new BCryptPasswordEncoder().encode("1234");
		System.out.println("1234 해쉬 : "+encPassword);
		}
	

}
