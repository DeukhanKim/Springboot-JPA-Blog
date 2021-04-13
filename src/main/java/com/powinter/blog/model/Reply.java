package com.powinter.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CollectionType;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity  //  ORM 객체라는 것을 가까이 표기해주는게 좋음
public class Reply {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	
	@Column(nullable = false, length = 200)
	private String content;
	
	@ManyToOne // 하나의 게시글에는 여러개의 답변이 가능
	@JoinColumn(name="boardId")
	private Board board;
	
	@ManyToOne // 하나의 유저는 여러개의 답변이 가능
	@JoinColumn(name="userId") // FK 생성
	private User user;
	
	@CreationTimestamp
	private Timestamp createDate;

}
