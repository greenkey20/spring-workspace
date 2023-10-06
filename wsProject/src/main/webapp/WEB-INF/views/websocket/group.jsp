<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>websocket_그룹</title>
<!-- jQuery 라이브러리 -->
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
	<!--2022.3.3(목) 16h15-->
	<h1>websocket_그룹</h1>
	
	<button onclick="connect();">접속</button>
	<button onclick="disconnect();">종료</button>
	
	<script>
		var socket;
	
		// 웹소켓에 접속하는 함수
		function connect() {
			var uri = "ws://localhost:8008/ws/gr";
			// var socket = new WebSocket(접속 주소);
			socket = new WebSocket(uri);
			
			// 연결이 (안)되었는지 확인할 수 있도록 예약 작업/callback을 설정
			// on = 시작 -> onopen = 소켓이 열렸으면
			socket.onopen = function() {
				console.log("서버와 연결되었습니다");
			}
			
			socket.onclose = function() {
				console.log("서버와 연결이 종료되었습니다");
			}
			
			socket.onerror = function(e) { // 15h20 강사님의 e 역할 설명 제대로 못 들음 
				console.log("에러 발생 - 오타?");
			}
			
			// 메시지가 도착하면(callback) -> 받은 메시지 표시
			socket.onmessage = function(e) {
				console.log("메시지가 도착했습니다");
				console.log(e); // 매개변수로 받은 데이터를 사용하기 전에 어떻게 생겼는지 찍어봄 = 객체 -> data 속성에 메시지 들어있음 = e.data
				console.log(e.data);
				
				// 코드 다 못 봄
				var div = $("<div></div>");
				div.text(e.data);
				
				$(".message-wrap").append(div);
			}
			
		}
		
		// 웹소켓 접속 종료 함수 = 입력된 글자를 불러와서 서버에 전송
		function disconnect() {
			socket.close();
		}
		
		// 메시지 전송 함수
		function send() {
			var text = $("#chat-input").val();
			
			if (!text) {
				return;
			}
			
			socket.send(text);
			$("#chat-input").val("");
		}
		
	</script>
	
	<hr>
	<input type="text" id="chat-input">
	<button onclick="send()";>전송</button>
	
	<!--2022.3.3(목) 16h35 수신된 메시지가 출력될 영역-->
	<div class="message-wrap"></div>
	
</body>
</html>