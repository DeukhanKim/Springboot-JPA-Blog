package com.powinter.blog.controller;

import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.powinter.blog.model.Board;
import com.powinter.blog.service.BoardService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;

	@GetMapping({"","/"})
//	public String index(@AuthenticationPrincipal PrincipalDetail principal) { // 컨트롤러에서 세션 존재 여부 확인
	public String index(Model model, @PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable) { // 스프링에서 데이터로 응답값을 가져갈때 Model 객체가 필요
//		System.out.println("로그인 사용자 아이디 :"+principal.getUsername());
		// /WEB-INF/views/index.jsp
		model.addAttribute("boards",boardService.글목록(pageable));
		return "index"; // 리턴시 viewResover 작동.
	}
	
	@GetMapping("/board/{id}")
	public String findById(Model model, @PathVariable int id) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/detail";
	}
	
	@GetMapping("/board/{id}/updateForm")
	public String updateForm(Model model, @PathVariable int id) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/updateForm";
	}
	
	// USER 권한이 필요
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "board/saveForm";
	}
	

}
