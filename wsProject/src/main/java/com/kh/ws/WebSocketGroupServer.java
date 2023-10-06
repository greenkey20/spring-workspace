package com.kh.ws;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// 2022.3.3(목) 16h group server(접속한 사용자를 기억해두고, 모두에 대한 처리를 수행하는 서버) 만들고자 함 -> 이 클래스 만들고, bean 등록
// websockets 여러 개 등록 가능하지만, 사용자가 많을수록 performance 저하
public class WebSocketGroupServer extends TextWebSocketHandler {
	
	/* 사용자를 기억하기 위한 저장소 = 중복 불가 + 동기화 지원 -> CopyOnWriteArraySet 사용; 내가 어떤 문제 해결을 위해, 어떤 과정을 통해, 이 저장소를 사용하게 되었는지, 설명할 수 있어야 함
	 * 
	 */
	private Set<WebSocketSession> users = new CopyOnWriteArraySet<WebSocketSession>(); // 제네릭 생략은 jdk 1.8 이후부터 지원됨

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//		super.afterConnectionEstablished(session);
		users.add(session);
		System.out.println("사용자 접속! -> 현재 접속한 사용자 = " + users.size() + "명");
		// 현재 실습 예제에서는 jsp 페이지에서 '접속' 버튼 클릭 시 접속
		// 프로젝트 구현 시에는 해당 페이지에 들어올 때 접속
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//		super.handleTextMessage(session, message);
		
		TextMessage newMessage = new TextMessage(message.getPayload());
		
		// 향상된 for문 = collection 안에 담긴 요소들에 접근하는 반복문
		for (WebSocketSession ws : users) { // 메시지를 모든 사용자(메시지 발신자 포함)에게 전송 <- 사용자 수만큼 반복해서 전송 
			ws.sendMessage(newMessage);
		}
		// 이 반복문의 단점 = 사용자가 많아질수록 반복 횟수 증가 -> 점점 속도 느려짐
		
//		session.sendMessage(message); // session에 메시지 발신자 정보가 들어있는 바, 메시지 발신자에게 다시 메시지를 보냄
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//		super.afterConnectionClosed(session, status);
		
		users.remove(session);
		System.out.println("사용자 종료! -> 현재 접속한 사용자 = " + users.size() + "명");
		
	}
	
	
	
}
