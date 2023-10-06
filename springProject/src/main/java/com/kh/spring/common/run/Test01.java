package com.kh.spring.common.run;

import java.util.Properties;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

// 2022.3.15(화) 9h10
/* 메일 전송 시 적용?(사용?) protocol = smtp != http
 * 메일 서버..(연결?설정? 뭘 쓰다 만 거지? >.<) != was
 * 
 * 메시지 보내는 방법 = SMS(유료?), mail(무료?), app push(notifications)(무료?).. <- 강사님 강의 녹화 다시 보면서, (개괄)설명 다시 듣기
 * 
 * 직접 도구 생성해서 이메일 전송하는 예제
 * 필요한 의존성 목록 <- 'Maven repository' website
 * 1. spring-context-support
 * 2. java mail-api
 * 필요한 외부 도구 = Gmail 계정 -> https://myaccount.google.com/lesssecureapps '보안 수준이 낮은 앱의 access 허용' 설정 필요
 */

// 그냥 클래스(o) 웹 통신 요청 받아 처리하는 controller(x)
public class Test01 {
	
	public static JavaMailSenderImpl sender; // 전송 도구
	
	public static void main(String[] args) { // Java prgm -> run as Java application
		// 2022.3.16(수) 1h10 나의 질문 = impl이 정확히 무엇인지, 수업 시간 강사님 설명 제대로 못 들음
		JavaMailSenderImpl impl = new JavaMailSenderImpl();
		
		// impl에 필요한 설정을 수행 -> '나는 이제 Gmail을 사용할거야'
		// 계정 설정
		impl.setHost("smtp.gmail.com"); // Gmail이 설정 이렇게 쓰라고 해 둔 것
		impl.setPort(587); // Gmail이 설정 이렇게 쓰라고 해 둔 것
		impl.setUsername("greenkey20@gmail.com"); // 메일을 발송할 Gmail 주소
		impl.setPassword("02Purple14gEgE*");
		
		// 옵션 설정 = Map<Object, Object> 형태 cf. JDBC Connection 객체 생성 시? 사용했던 방법
		Properties prop = new Properties();
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", true);
		
		impl.setJavaMailProperties(prop);
		
		// sender에 대입
		sender = impl;
		// 이상, 보내는 전송 도구까지 준비함 9h40
		
		// 메시지 생성
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setSubject("메일을 보내봅니다2"); // 제목
		message.setText("메일의 본문입니다2");
		String[] to = {"greenkey20@naver.com", "greenkey20art@naver.com"};
		message.setTo(to);
		
		// CC(carbon copy)
		String[] cc = {"greenkey20art@gmail.com"};
		message.setCc(cc);
		
		// BCC(blind carbon copy)
		String[] bcc = {"20key@hanmail.net"};
		message.setBcc(bcc);
		
		sender.send(message); // 메일 전송 버튼
	}
	// 이상 Java codes 짜서 메일 전송한 것임; dispatcherServlet/Spring과 관계 없음; 요청 받아서 메일 전송하고 그런 것 아님
	// 10h15 마음에 안 드는 부분 = new JavaMailSenderImpl() 객체 생성 = Spring이 이런 방식 안 좋아함 vs root-context.xml에 이렇게 작성해둔 내용 등록해두기/외부 modules(?제대로 못 들음) bean 등록 -> 필요할 때마다 Spring에게 달라고해서 받아올(wiring, 당겨올) 것임

}
