package com.powinter.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
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
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob // 대용령 데이터 저장
	private String content; // 섬머노트 라이브러리는 <html>태그가 섞여서 디자인됨. 대용량 데이터 필요.
	
//	@ColumnDefault("0") // DB상에 int라 '필요없음
	private int count;
	
	// ManyToOne은 기본적으로 ManyToOne(fetch = FetchType.EAGER) 로 board 테이블 셀럭트시 user 데이터 무조건 가져오는 전략(borad 셀렉트시 user는 1명뿐으므로)
	@ManyToOne // FK와 User의 연관관계 설정. Many = Board, User = One -> 한명의 유저는 여러개의 게시글 가능.
	@JoinColumn(name="userId") // FK를 userId로 생성함
	private User user; // DB는 오브젝트를 저장할 수 없어서 Foreign Key를 사용하나 자바는 오브젝트 저장 가능.
	
	// OneToMany는 기본전략이 FetchType.LAZY로 필요할 경우만 reply에서 가져옴. 화면 UI에 따라 전략 선택.
	// 블로그에서 상세 페이지 노출시 댓글을 무조건 보여주게 설정할 예정으므로 fetch 전략을 EAGER로 변경필요.
	@OneToMany(mappedBy = "board",fetch = FetchType.EAGER) // 하나의 게시글에는 여러개의 댓글이 존재. mappedBy는 연관관계의 주인이 아니다. (난 FK가 아니다) DB에 컬럼을 만들지 않음. board는 reply 테이블에서 조인에 사용되는 필드명.
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp createDate;

}
