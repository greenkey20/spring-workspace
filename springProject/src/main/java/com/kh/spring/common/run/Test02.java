package com.kh.spring.common.run;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 2022.3.15(화) 10h25
// 'sender' bean 등록 = 전송 도구는 Spring이 알아서 만들어줌 -> 이 클래스는 요청 받아 처리하는 controller로 만들어줌 + 나는 전송할 메시지만 만들면 됨

@Controller
public class Test02 {
	
	@Autowired
	private JavaMailSender sender; // 전송 도구
	
	@RequestMapping("mail") // http://localhost:8008/spring/mail로 요청 보냄
	public String mail() {
		SimpleMailMessage message = new SimpleMailMessage();
		
		message.setSubject("Spring으로 메일을 보내봅니다"); // 제목
		message.setText("<h1>Spring에서 보낸 메일의 본문입니다</h1>"); // text/plain(평(plain)문(text)으로 보냄) vs text/html(Test03.java mime msg 예제 참고)
		String[] to = {"greenkey20@naver.com", "greenkey20art@naver.com"};
		message.setTo(to);
		
		String[] cc = {"greenkey20art@gmail.com"};
		message.setCc(cc);
		
		String[] bcc = {"20key@hanmail.net"};
		message.setBcc(bcc);
		
		sender.send(message);
		
		return "redirect:/"; // 응답 페이지를 /(webapp, welcome/main page)로 재요청하게 만듦
	}
	

}
