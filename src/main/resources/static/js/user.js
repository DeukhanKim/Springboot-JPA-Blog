let index = {
	init: function() {
		$("#btn-save").on("click",()=>{ // function(){}을 써야 되는데 ()=>{}를 쓴이유는 this를 바인딩하기 위해서!!! 
			this.save();						  // function(){}를 쓰게되면 this값이 이 자체가 아니라 윈도우 객체를 바인딩함.
		});
		
//		$("#btn-login").on("click",()=>{ 
//			this.login();
//		});		
	},
	
	save: function() {
		// alert('user의 save함수');
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};
		
		//console.log(data);
		
		// ajax 호출시 default가 비동기 호출
		$.ajax({
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=utf-8", // body 데이터 타입(MIME) 설정
			dataType: "json" //요청을 해서 서버로부터 온 응답이 json이라면 javascript 오브젝트로 변환해서 받음. 삭제 가능
		}).done(function(resp){
			alert("회원가입이 완료되었습니다.");
			//console.log(resp);
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},

	login: function() {
		// alert('user의 save함수');
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
		};
		
		//console.log(data);
		
		// ajax 호출시 default가 비동기 호출
//		$.ajax({
//			type: "POST",
//			url: "/api/user/login",
//			data: JSON.stringify(data), // http body 데이터
//			contentType: "application/json; charset=utf-8", // body 데이터 타입(MIME) 설정
//			dataType: "json" //요청을 해서 서버로부터 온 응답이 json이라면 javascript 오브젝트로 변환해서 받음. 삭제 가능
//		}).done(function(resp){
//			alert("로그인이 완료되었습니다.");
//			//console.log(resp);
//			location.href="/";
//		}).fail(function(error){
//			alert(JSON.stringify(error));
//		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	}	
}


index.init();