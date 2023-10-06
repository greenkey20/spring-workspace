package com.kh.spring.common.run;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 2022.3.15(화) 11h15
@Controller
public class Test04 {
	
	@Autowired
	private JavaMailSender sender;
	
	@RequestMapping("sendfile")
	public String mail() throws MessagingException {
		// MimeMessage를 이용한 파일 첨부
		// javax.activation.DataSource(파일 정보) 사용 != javax.sql의 DataSource는 db 관련
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		
		String[] to = {"20key@hanmail.net", "greenkey20art@naver.com"};
		helper.setTo(to);
		helper.setSubject("파일 테스트");
		helper.setText("메일에 파일 잘 첨부되어 전송되는지 확인해 보고 있어요");
		
		// 첨부 파일 추가 <- 첨부할 파일 경로 = C:\frontend_workspace\2_css_workspace\resources\image
		DataSource source = new FileDataSource("C:/frontend_workspace/2_css_workspace/resources/image/tower1.PNG");
		helper.addAttachment(source.getName(), source);
		
		sender.send(message);
		return "redirect:/";
	}

}
