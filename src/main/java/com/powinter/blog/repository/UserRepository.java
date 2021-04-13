package com.powinter.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.powinter.blog.model.User;

// DAO
// 자동으로 bean등록이 된다.
// @Repository 생략가능
public interface UserRepository extends JpaRepository<User, Integer>{ // 해당 JpaRepository는 User 테이블을 관리하는 repository이고 User 테이블의 PK는 정수형이다.
	// SELECT * FROM user WHERE username = 1?;
	Optional<User> findByUsername(String username); // JPA 네이밍 규칙
	
	// 빈 상태로 두면 기본 CRUD 기능 사용 가능.
	
	// JPA Naming 쿼리 전략. 아래와 같이 메소드 네이밍을 하면 아래와 같은 쿼리 문이 수행됨
	// SELECT * FROM user WHERE username = ?1 AND password =?2;
//	User findByUsernameAndPassword(String username, String password); // Spring Security 사용으로 삭제
	
	// Native 쿼리를 사용하기 위해서 아래와 같이도 사용 가능.
//	@Query(value = "SELECT * FROM user WHERE username = ?1 AND password =?2", nativeQuery = true)
//	User login(String username, String password);
}
