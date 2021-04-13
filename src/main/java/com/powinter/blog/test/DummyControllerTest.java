package com.powinter.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.powinter.blog.model.RoleType;
import com.powinter.blog.model.User;
import com.powinter.blog.repository.UserRepository;

// html 파일이 아니라 data를 리턴해주는 controller = RestController
@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성 주입(DI) 
					  // 이 어노테이션을 설정해야 DummyControllerTest 가 메모리에 로드될때 userRepository가 같이 로드됨. 비 설정시 null임.
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) { // 에러메시지를 보고 catch할 exception 선택. 세밀하게 처리 필요.
			return "삭제에 실패하였습니다. 해당 id가 존재하지 않습니다.";
		}
		
		return "삭제되었습니다. id : "+id;
	}
	
	// email, password만 업데이트
	@Transactional // 함수 호출 시 트랜잭션 시작되고 종료시 commit이 됨
	@PutMapping("/dummy/user/{id}") // 다른 HTTP 메소드와 동일한 URI 사용 가능
	public User udpateUser(@PathVariable int id, @RequestBody User requestUser) { // json 바디를 받기 위해 @RequestBody를 사용
																												  // json 데이터의 요청을 스프링이 MessageConverter의 Jackson 라이브러리를 이용해서 변환해서 받아줌.
		System.out.println("id : "+id);
		System.out.println("password : "+requestUser.getPassword());
		System.out.println("email : "+requestUser.getEmail());
		
		// save()로 업데이트를 할 경우엔 요청된 필드 이외는 null로 변경됨. 
		// 그래서 save()를 사용할 경우 DB에서 해당 user의 정보를 미리 가져와서 
		// 변경이 요청된 부분만 setter로 수정한 후 user를 다시 save해야됨
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다."); // user가 없을 경우
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		// save함수는 id를 전달하지 않으면 insert를 해주고
		// save함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 updaet를 해주고
		// save함수는 id를 전달하면 해당 id에 대한 데이터가 없으면 insert를 수행.
		// userRepository.save(user); 
		
		// 더티 체킹 : 업데이트 시 save함수를 호출하지 않고 DB에서 해당 user의 정보만 가지고와서 값만 요청된 값으로
		// 설정하고 @Transactinoal 을 붙이면 업데이트가 수행됨.
		// 영속화 된 데이터의 변동 사항을 체크하여 update를 수행하는 방식
		// 이 함수에서는 user가 호출되었을때 영속화 컨텍스트의 1차 캐시에 들어가면서 영속화 되고
		// 요청된 값으로 영속화된 user의 값을 변경하였으므로 더티 체킹을 이용한 업데이트가 수행되는것임.
		return user;		
	}
	
	// User 전체 목록 조회
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	// 페이징 기능. URI에 쿼리 스트링으로 페이징 변경 가능. '?page=0'과 같이 사용. 0부터 시작 
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
		// Page의 기능을 사용하고 싶을때 Page로 받아서 List로 결과를 넘겨주는게 좋음
		Page<User> pagingUser =  userRepository.findAll(pageable);
		List<User> users = pagingUser.getContent();
		
		if(pagingUser.isEmpty()) {
			
		}
		
		return users;
	}
	// pageable의 전체 정보를 리턴하지 말고 데이터만 리턴할 경우 아래의 방법으로 처리
//	@GetMapping("/dummy/user")
//	public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
//		List<User> users =  userRepository.findAll(pageable).getContent();
//		return users;
//	}
	
	// {id} 주소를 파라미터로 전달 받을 수 있음.
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		// user/4를 찾는데 데이터베이스에서 못찾게되면 user가 null이 될것 아냐?
		// 그럼 return null이 되는데, 그럼 프로그램에 문제가 있을 수 있지?
		// 그래서 findById에서는 Optional로 너의 User 객체를 감싸서 가져올테니 null인지 아닌지 판단해서 return 해!!
		//User user = userRepository.findById(id).get(); // null 리턴은 없으니 그냥 Optional에서 뽑아서 씀
		
		// DB에 없는 user가 요청되었을떄 처리 방안 1
		// 자바에서 인터페이스는 new 안됨 인터페이스를 new하려면 익명 객체(클래스)를 생성해야 됨.
		// Supplier 인터페이스를 new하면서 익명객체로 생성한 후 Supplier에 있는 get을 오버라이드하면 객체 생성이 가능
//		User user = userRepository.findById(id).orElseGet(new Supplier<User>() {
//			@Override
//			public User get() {
//				return new User(); // 정상 검색이 될 경우 이 메소드 실행이 안되지만, user가 없을 경우 빈 객체를 user에 넣어줌. 빈 객체는 null은 아님
//			}
//		});
		
		// DB에 없는 user가 요청되었을떄 처리 방안 2
		// 선호하는 방법은 Exception을 throw해서 메시지로 처리
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 존재하지 않습니다. id : "+id);
			}
		});
		// 위와 동일하나 JDK 1.8부터는 람다식으로 표현 가능
//		User user = userRepository.findById(id).orElseThrow(()-> { // 메소드에 들어가는 타입 신경 안쓰고 익명으로 처리 가능. 
//			return new IllegalArgumentException("해당 유저는 존재하지 않습니다. id : "+id);
//		});
		
		// 요청은 웹브라우저에서 한것이라 user 객체를 리턴하려면 웹 브라우저가 이해할 수 있는 데이터로 변환해야 됨.
		// 가장 좋은게 json 포맷. 예전엔 Gson 라이브러리로 자바 오브젝트를 json으로 변경해서 던져줬음
		// 스프링부트는 MessageConverter라는 애가 응답시에 자동 작동
		// 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		// user 오브젝트를 json으로 변환해서 브라우저에게 던져줌.
		// user = 자바 오브젝트
		return user;
	}
		
	@PostMapping("/dummy/join2")
	// public String join(@RequestParam("username") String u, String password, String email) @RequestParam을 사용하면 변수명을 u 같이 임의로 설정 가능
	// 아래 방식으로 사용할 경우 요청시 http body에 username, passsword, email을 맞춰서 요청하면 됨.
	public String join2(String username, String password, String email) { //x-www-form-urlencoded MIME type인 key=value&key=value의 형태로 스프링이 받아줌.

		System.out.println("username : "+username);
		System.out.println("password : "+password);
		System.out.println("email : "+email);
		return "회원가입2이 완료되었습니다.";
	}
	
	// 아래의 방식으로도 요청 처리 가능
	@PostMapping("/dummy/join")
	public String join(User user) {

		System.out.println("id: : "+user.getId());
		System.out.println("username : "+user.getUsername());
		System.out.println("password : "+user.getPassword());
		System.out.println("emai : "+user.getEmail());
		System.out.println("role : "+user.getRole());
		System.out.println("createDate : "+user.getCreateDate());
		
		user.setRole(RoleType.USER); // User의 role을 설정해줌. enum으로 도메인을 설정해주면 실수데이터 입력을 방지가능.
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
	

	
	@PostMapping("/test/v1.1") // 다른 HTTP 메소드와 동일한 URI 사용 가능
	public Void test(
			@RequestHeader(value="Accept") String accept,
			@RequestHeader(value="User-Agent") String userAgent,
			@RequestHeader(value="Authorization") String authorization,
			@RequestHeader(value="Host") String host,
			@RequestBody String body) { // json 바디를 받기 위해 @RequestBody를 사용
													
		

		

		
        System.out.println("Accept: " + accept);
        System.out.println("User-Agent: " + userAgent);
        System.out.println("Authorization: " + authorization);
        System.out.println("Host: " + host);
		System.out.println("body :\n"+body.toString());		
		
		return null;
	}
}
