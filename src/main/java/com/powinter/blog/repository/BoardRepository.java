package com.powinter.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.powinter.blog.model.Board;

// DAO
// 자동으로 bean등록이 된다.
// @Repository 생략가능
public interface BoardRepository extends JpaRepository<Board, Integer>{ // 해당 JpaRepository는 User 테이블을 관리하는 repository이고 User 테이블의 PK는 정수형이다.

}
