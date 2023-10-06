package com.kh.spring.common.run;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

// 2022.3.15(화) 10h40
@Controller
public class Test03 {
	
	@Autowired
	private JavaMailSender sender; // 전송 도구는 Spring이 만들어준 것 받음
	
	@RequestMapping("hypermail")
	public String mail() throws MessagingException {
		// Mime Message
		MimeMessage message = sender.createMimeMessage(); // sender의 속성으로써 mime msg 생성
		
		// mime msg는 복잡하기 때문에 도우미가 있음
		MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8"); // 두번째 매개변수(multipart?) false = 첨부파일 안 넣음; 강사님께서 10h43 설명하실 때 MimeMessageHelper의 여러 생성자 보여주셨는데, 그 목록 어떻게 보는지 잘 모르겠음 
		
		String[] to = {"20key@hanmail.net"};
		helper.setTo(to);
		
		helper.setSubject("mime msg를 보냅니다");
//		helper.setText("<h1>안녕하세요!</h1>", true); // 10h45 true에 대한 강사님 설명 제대로 못 들음 -> 평문(x) hypertext?(o)로 전송됨; 2022.3.16(수) 0h5 메소드 설명 = 첫번째 매개변수 String text(the text for the message) + 두번째 매개변수 boolean html(whether to apply content type "text/html" for an HTML mail, using default content type "text/plain" else)
		
		// 이 사이트의 대문으로 들어오는 링크 발송
		// 접속 주소 표현 방법1)
//		helper.setText("<a href='http://localhost:8008/spring/'>여기로 이동~</a>", true); // 실제 서비스에서는 localhost(x) domain(o)이 들어감 <- localhost 대신 ip주소 + 배포 시에는 contextRoot 'spring'을 다른 것으로 바꿈
		
		// 접속 주소 표현 방법2) 왜 이런 방법을 쓰는지, 이 방법의 특장점?은 무엇인지, 11h5 강사님 설명 제대로 못 들음 ㅠ.ㅠ
		String url = ServletUriComponentsBuilder
					.fromCurrentContextPath()
					.port(8008)
					.path("/")
					.queryParam("come", "hello")
					.queryParam("go", "bye")
					.toUriString();
//		helper.setText(url, true); // 강사님 필기 제대로 못 따라적고, 내가 임의로 적어서 테스트해본 것 -> http://localhost:8008/spring/?come=hello&go=bye
		helper.setText("<a href='" + url + "'>여기로 이동~</a>", true);
		
		sender.send(message);
		return "redirect:/";
	}

}
