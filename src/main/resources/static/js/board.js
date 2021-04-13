let index = {
	init: function() {
		$("#btn-save").on("click",()=>{ // function(){}을 써야 되는데 ()=>{}를 쓴이유는 this를 바인딩하기 위해서!!! 
			this.save();						  // function(){}를 쓰게되면 this값이 이 자체가 아니라 윈도우 객체를 바인딩함.
		});
		
		$("#btn-delete").on("click",()=>{ 
			this.deleteById();				
		});
		
		$("#btn-update").on("click",()=>{ 
			this.update();				
		});
	},
	
	save: function() {
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};
		
		$.ajax({
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=utf-8", // body 데이터 타입(MIME) 설정
			dataType: "json" //요청을 해서 서버로부터 온 응답이 json이라면 javascript 오브젝트로 변환해서 받음. 삭제 가능
		}).done(function(resp){
			alert("글쓰기가 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},
	
	deleteById: function() {
		let id = $("#id").text();
		
		$.ajax({
			type: "DELETE",
			url: "/api/board/"+id
		}).done(function(resp){
			alert("삭제가 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},
	
	update: function() {
		let	id = $("#id").val();
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};
		
		$.ajax({
			type: "PUT",
			url: "/api/board/"+id,
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=utf-8", // body 데이터 타입(MIME) 설정
			dataType: "json" //요청을 해서 서버로부터 온 응답이 json이라면 javascript 오브젝트로 변환해서 받음. 삭제 가능
		}).done(function(resp){
			alert("글수정이 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	}
}


index.init();