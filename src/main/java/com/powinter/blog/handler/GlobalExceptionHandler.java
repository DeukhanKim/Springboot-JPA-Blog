package com.powinter.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.powinter.blog.dto.ResponseDto;

@ControllerAdvice // 모든 exception이 발생되면 이 객체로 들어오게 설정
@RestController
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value=Exception.class) // Exception 별로 처리가 좋음.
	public ResponseDto<String> handleException(Exception e) {
		System.out.println("handleException+++++++++++++++");
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
	}

}
