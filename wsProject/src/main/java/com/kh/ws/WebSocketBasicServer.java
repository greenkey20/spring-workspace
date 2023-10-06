package com.kh.ws;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// 2022.3.3(목) 14h10
/* web socket = web에서 수행하는 socket 통신
 * 기본적으로 web(http)은 비연결형(서버와 연결되어 있지 않음) 통신이라는 특징을 가지고 있음 -> 내가 요청해야 응답 받음; 14h15 웹툰 볼 때.. 등 강사님 설명 제대로 못 들음/이해 못함; 내가 필요할 때만 왔다갔다/통신함
 * socket 통신 = 연결형 통신 -> 계속 통신 중임 -> 신뢰성  -> 채팅, 주식, coins 등 순간순간 바뀌는 데이터의 신뢰성 중요할 때 web socket(o) AJAX(x) 사용; 내가 요청한 게 아니라, i/o stream 항상 연결되어있음 vs AJAX도 요청을 해야 응답이 옴
 * 
 * 기존/http 통신은 쓰레기인 것이 아님 + http 통신이 기본으로 깔려있어야 함
 * 
 * web socket은 통신 방식이 다름 -> url 맨앞에 ws(o) http(x) 붙음
 * 
 * 기본 통신은 http로 진행하고, 필요 시 WebSocket 사용
 * 
 * Spring version4 이후부터는 Spring에서 WebSocket 제공해줌 vs 
 * 
 * 
 */

/**
 * 웹소켓의 기본적인 이해를 돕기 위해 만든 서버
 * WebSocketServer를 만들기 위해서는 특정 클래스의 상속 또는 인터페이스의 구현 필요
 * 인터페이스 구현(implements) 시, 모든 것을 다 구현해야 함 vs 클래스 상속 받으면(extends), 내가 필요한 것만 구현(overriding)해서 쓰면 됨
 * 
 * 그룹채팅 = webSocket vs 카카오톡 = http?(14h30 강사님 설명 제대로 들었는지 모르겠음)
 * 
 * @author greenpianorabbit
 *
 */
//public class WebSocketBasicServer implements WebSocketHandler {
public class WebSocketBasicServer extends TextWebSocketHandler { // text를 주고 받을 것인 바(? 14h30 강사님 설명 제대로 들었나?)
	
	/* 14h40 접속 시 실행되는 메소드 = 특정 상황에 실행되는 코드 = callback(JavaScript 수업 때 배운, 중요한 개념)
	 * session = 접속한 사용자의 웹소켓 정보 != HttpSession이 아님 -> 사용자가 로그인했는지 등 알 수 없음 vs 알려면 인터셉터 써야 함(개별적으로 공부하기)
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//		super.afterConnectionEstablished(session); // 자동 완성 구문
		
		System.out.println("접속");
		System.out.println("session = " + session); // session = StandardWebSocketSession[id=d328517b-e7dd-8300-d64d-9896822ccf5b, uri=ws://localhost:8008/ws/sc]

	}
	
	/* 메시지 수신 시 실행될 메소드
	 * session = 접속한 사용자의 웹소켓 정보 != HttpSession
	 * message = 사용자가 전송한 메시지 정보 = payload(실제 보낸 내용) + byteCount(보낸 메시지 크기; 한글 1글자 = 3bytes) + last(메시지의 종료 여부; 끊어서 온 것 중의 마지막)
	 * 통신에는 1번에 보낼 수 있는 양이 한정되어 있음; packet 끊어서 감 + 누가 먼저 도착할지 모름 -> 수신자는 기다렸다가 모두 다 오면 순서대로 맞춰서 받음
	 */
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("수신");
		System.out.println("session = " + session); // session = StandardWebSocketSession[id=4b5609e3-075f-dbe5-62c1-484a1c69d5ba, uri=ws://localhost:8008/ws/sc]
		System.out.println("message = " + message); // message = TextMessage payload=[hello], byteCount=5, last=true]
	}

	/* 사용자 접속 종료 시 실행되는 메소드
	 * session = 접속한 사용자의 웹소켓 정보 != HttpSession 
	 * status = 접속이 종료된 원인과 관련된 정보
	 * 
	 * 15h20 테스트 시, 나는 왜 이 메소드가 호출 안 되지? ㅠ.ㅠ -> 15h40 나의 관찰 = JavaScript 'socket' 변수 선언을 잘 못 해놨음
	 */
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//		super.afterConnectionClosed(session, status);
		
		System.out.println("종료");
		System.out.println("session = " + session); // session = StandardWebSocketSession[id=aec0f062-5f26-282c-4492-ecf7a48a5ba4, uri=ws://localhost:8008/ws/sc]
		System.out.println("status = " + status); // status = CloseStatus[code=1000, reason=null]
	}

}
