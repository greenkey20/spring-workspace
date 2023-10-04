package com.kh.cookie;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

// 2022.3.2(수) 10h45
@Controller
public class ExamController {
	
	@RequestMapping("/sign-in") // url 주소에 -(dash) 들어가도 됨
	public String signIn() { // 메소드명에 - 쓰면 회사 상사로부터 혼남
		// forwarding만 해주면 됨
		return "cookie/sign-in"; // jsp 파일명에 -(dash) 써도 됨
	}
	
	// 2022.3.2(수) 11h5
	// cookie 있으면 광고(div, 모달 창(o) 팝업창(x)) 안 보여줌
	// 요새는 브라우저에서 팝업창 차단하는 바, 팝업창 광고 잘 안 함
	@RequestMapping("/ad")
	public String ad() {
		return "cookie/ad";
	}
	
}
