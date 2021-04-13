package com.powinter.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@AllArgsConstructor
@NoArgsConstructor  // 아규먼트 없이 생성 가능하게 하는 Lombk 어노테이션
public class Member {
	
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder
	public Member(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	
	
	/*
	 * public Member(int id, String username, String password, String email) {
	 * this.id = id; this.username = username; this.password = password; this.email
	 * = email; }
	 * 
	 * public int getId() { return id; } public void setId(int id) { this.id = id; }
	 * public String getUsername() { return username; } public void
	 * setUsername(String username) { this.username = username; } public String
	 * getPassword() { return password; } public void setPassword(String password) {
	 * this.password = password; } public String getEmail() { return email; } public
	 * void setEmail(String email) { this.email = email; }
	 */
}
