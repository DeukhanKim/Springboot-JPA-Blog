package com.powinter.blog.model;


import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.type.TrueFalseType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data // Getter + Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity // User 클래스가 MySQL에 실행시 자동으로 테이블 생성 // ORM이란 Java 포함 모든 언에의 Object를 테이블로 매핑해주는 기술
// @DynamicInsert // insert시에 null인 필드를 제외시켜주는 기능
public class User {
	
	@Id //Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; // 시퀀스, auto_increament
	
	@Column(nullable = false, length = 20, unique = true) // 데이터 중복 방지
	private String username;
	
	@Column(nullable = false, length = 100) // 패스워드 hash로 변경 후 암호화 예정
	private String password;
	
	@Column(nullable = false, length = 50)
	private String email;

	// @ColumnDefault("'user'") // 기본 입력값 설정. 문자라는것을 알려줘야되서 양쪽에 ' 필요. 입력값 'user'임 @DynamicInsert 사용 필요
	// private String role; // Enum을 쓰는게 좋다.  Enum 사용 시 admin, user, manager 의 세가지 도메인으로 설정 가능
	@Enumerated(EnumType.STRING) // 아래 방식의 경우 DB는 RoleType이라는게 없으므로 데이터 타입 설정이 필요
	private RoleType role; // Enum을 쓰는게 좋다.  Enum 사용 시 admin, user, manager 의 세가지 도메인으로 설정 가능
	
//	private Enum role2;
	
	@CreationTimestamp // 시간이 자동으로 입력됨
	private Timestamp createDate;
	
}
