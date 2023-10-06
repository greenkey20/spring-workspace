<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ajaxProject의 index.jsp</title>
<!-- jQuery 라이브러리 -->
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
	<!--2022.2.23(수) 9h30 -->
	<h1>좋은 아침입니다 ^^</h1>
	<!--http://localhost:8008/ajax/ 접속 시, 내가 만들어준 jsp template에 의해 이 jsp 문서 상단에 taglib 지시어 관련 uri를 못 찾아서 500 error 발생 -> ajaxProject의 WEB-INF 아래에 springProject의 lib 폴더 복사해서 붙여넣음-->
	
	<h1>Spring에서의 AJAX 사용법</h1> <!--AJAX도 요청 보내고 응답 받으려고 사용하는 것; AJAX에서는 data(o) 새로운 페이지(x)로 응답을 받음 -> 응답 데이터를 가지고 새로 화면을 보여줄 수(? 10h40 강사님 설명 정확히 못 들음) 있음-->

	<h3>예제1. 요청 시 값 전달 -> 응답 결과 받아보기</h3>

	이름: <input type="text" id="name"><br>
	나이: <input type="number" id="age"><br>

	<!--방법1) form 태그로 감싸고 submit-->

	<!--form 태그 없이 하려면(? 맞나?) 방법2) 버튼에 id 속성 주고, 선택해서 클릭했을 때 실행할 익명함수에 AJAX-->
	<!--<button id="btn1">전송</button>-->
	
	<!--방법3) form 태그 없이 + button의 onclick 속성에 '선언적 함수(이름 있는 함수)'를 값으로 부여-->
	<button onclick="test1()">전송</button>
	
	<div id="result1"></div> <!--10h40 테스트해서 '응답 문자열 = 강미피은/는 3세입니다' 표시-->

	<script>
		/* 방법2 관련 memo
		$("#btn1").click(function() {

		})
		*/
		
		// 2022.2.23(수) 10h
		/* 방법3 = 기존에 했/알던 방식과 동일한 방법
		function test1() {
			$.ajax({
				url : "ajax1.do", // 필수로 정의해야 하는 속성 = controller의 mapping 값
				data : {
					name : $("#name").val(), // val()로 가져오면 무조건 문자열임 -> 나의 질문 = JavaScript val()의 반환 자료형이 항상 문자열이라는 의미인가?
					age : $("#age").val()
				},
				success : function(result) { // 응답으로 돌아온 데이터를 사용하고 싶다면 매개변수로 받음
					// console.log(result); // 돌아온 응답 데이터가 어떻게 왔는지 console에 찍어서 확인해본 다음, 사용하기 -> 응답 문자열 = 강판다은/는 4세입니다
					$("#result1").html(result);
				},
				error : function() {
					console.log("AJAX 통신 실패");
				}
			})
		}
		*/
		
		// 2022.2.23(수) 10h40 Spring에서만 (응답)할 수 있는 방식으로 해보기 -> 14h20 JSON 방식
		function test1() {
			$.ajax({
				url : "ajax1.do",
				data : {
					name : $("#name").val(),
					age : $("#age").val()
				},
				success : function(result) { 
					// console.log(result); // 2022.2.23(수) 14h25 '강미피3'라는 문자열로 name과 age가 붙어서 응답 데이터 옴 -> 꺼내서 사용하기 어려우므로, 이 방법은 안 쓰기로 함 -> JSON
					// $("#result1").html(result);
					
					// JSON 응답 방법1 = 응답 데이터가 배열(JSONArray)의 형태일 경우, '[인덱스]'로 인덱스에 접근 
					// $('#result1').html("이름 = " + result[0] + "<br>나이 = " + result[1]); // 필기 제대로 못함
					
					// JSON 응답 방법2 = 응답 데이터가 객체(JSONObject)의 형태일 경우, '객체명.속성명'으로 속성에 접근 가능
					$('#result1').html("이름 = " + result.name + "<br>나이 = " + result.age);
				},
				error : function() {
					console.log("AJAX 통신 실패");
				}
			})
		}
	</script>
	
	<hr>
	<br><br>
	<!--2022.2.23(수) 15h30-->
	<h3>예제2. 조회 요청 후 조회된 '한 회원 객체'를 응답 받아서 출력</h3>
	조회할 회원 번호: <input type="number" id="userNo">
	<button id="btn2">조회</button>
	<div id="result2"></div>
	
	<script>
		$(function() {
			$("#btn2").click(function() {
				$.ajax({
					url : "ajax2.do",
					data : {num : $("#userNo").val()},
					success : function(obj) {
						console.log(obj);
						
						let value = "<ul>"
									  + "<li> 이름 = " + obj.name + "</li>"
									  + "<li> ID = " + obj.userId + "</li>"
									  + "<li> 나이 = " + obj.age + "</li>"
								  + "</ul>";
						$("#result2").html(value);
					},
					error : function() {
						console.log("회원 정보 조회 실패");
					}
				})
			})
		})
	</script>
	
	<hr>
	<br><br>
	<!--2022.2.23(수) 16h15-->
	<h3>예제3. 조회 처리 후 조회된 '회원 list'를 응답 받아서 출력</h3>
	<button onclick="test3()">전체 조회</button>
	<br><br>
	
	<table border="1" id="result3">
		<thead>
			<th>이름</th>
			<th>ID</th>
			<th>나이</th>
			<th>전화번호</th>
		</thead>
		<tbody>
		</tbody>
	</table>
	
	<script>
		function test3() {
			$.ajax({
				url : "ajax3.do",
				// 전체 조회 요청인 바, 요청 시 전달할 값 없음
				success : function(list) {
					// console.log(list);
					
					let value = "";
					/* list 순환할 반복문 나의 시도; 처음에는 JavaScript 배열의 크기/길이 구하는 것을 list.size()로 잘못 썼었음
					for (var i = 1; i < list.length; i++) {
						
					}
					*/
					
					// 강사님 방식 = for in문 사용
					for (let i in list) {
						value += "<tr>"
									+ "<td>" + list[i].name + "</td>"
									+ "<td>" + list[i].userId + "</td>"
									+ "<td>" + list[i].age + "</td>"
									+ "<td>" + list[i].phone + "</td>"
								+ "</tr>"
					}
					
					$("#result3 tbody").html(value);
				},
				error : function() {
					console.log("객체배열 전체 조회 실패");
				}
			})
		}
	</script>

</body>
</html>