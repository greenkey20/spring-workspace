package com.kh.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 2022.3.2(수) 9h25
@Controller
public class CookieController {
	
	// 9h40 나의 질문 = 'create' 호출한 곳이 없는데, 어떻게 아래 메소드에서 만든 쿠키가 브라우저에 생성되었지? 브라우저 주소 창에 http://localhost:8008/cookie/create로 들어가서 그런가? -> 나의 생각 = 브라우저 주소 창에 http://localhost:8008/cookie/create
	@RequestMapping("create")
	public String create(HttpServletResponse response) {
		// 브라우저의 db는 5Mbytes 정도의 아주 작은 크기
		
		// cookie는 객체를 생성한 다음, 응답 정보에 첨부해야(response 객체를 통해) 완전히 생성됨
		// name 및 value 속성을 필수로(필기 다 못함)
		
		// 한글 쓰고 싶다면, 공공 데이터 수업 때 사용한 unicode encoder? 사용(=번거로움)
		
		Cookie ck = new Cookie("test", "khacademy");
		
		// setMaxAge = 만료 시간(단위 = 초) -> utc(세계 표준시) 기준
//		ck.setMaxAge(10);
		
		// 하루 동안 안 보기
//		ck.setMaxAge(86400); // 60초 * 60분 * 24시간 = 1일 = 86400초
		ck.setMaxAge(60 * 60 * 24 * 1); // 60초 * 60분 * 24시간 = 1일 = 86400초
		
		// 보통 로그인(나의 질문 = 로그인 상태 유지? 자동 로그인 정보 저장? -> 강사님 설명 = 로그인 상태 유지)은 4주 정도 줌
		// 길면 길수록 보안 안 좋음 vs otp는 보안이 엄청 좋음
		
		response.addCookie(ck);
		
		// 2022.3.2(수) 10h50 4주짜리 saveId 쿠키 생성 및 발급
		Cookie si = new Cookie("saveId", "kh");
		si.setMaxAge(60 * 60 * 24 * 7 * 4); // 4주
		response.addCookie(si);
		
		// 2022.3.2(수) 11h10 1일짜리 쿠키 생성 및 발급
		Cookie pop = new Cookie("pop", "ad");
		pop.setMaxAge(60 * 60 * 24 * 1); // 하루짜리
		response.addCookie(pop);
		
		return "cookie/create";
		
		// expires = 만료 시간; cookie를 언제까지 놔둘(?) 것인가? -> default? = session = 브라우저 종료 시까지
		// 서버 요청 시 필요한 것 = request
		// 서버에서 페이지 만들 때는 response 만들어주면서 cookie 첨부해주니까,
		// 남이 만든 cookie는 내 웹서비스에서 사용 불가능 e.g. 내 웹서비스에서 naver, google 등이 만든 cookies 사용 불가능
		// 불법 웹툰 site에서 NAVER login하면 피싱됨 -> 실제 NAVER login이 아님
	}
	
	// 2022.3.2(수) 10h_
	// 쿠키 삭제 = 별도 메소드(?)는 없음 -> 브라우저 개발자 도구에서 사용자가 삭제 ou 
	// 같은 이름으로 재발급/생성하면 덮어쓰기 됨 + 지속 시간을 0초로 발급 = 
	@RequestMapping("delete")
	public String delete(HttpServletResponse response) {
		Cookie ck = new Cookie("test", "khacademy");
		
		ck.setMaxAge(0);
		response.addCookie(ck);
		
		return "cookie/delete";
	}
	
	// 10h20 나의 생각/궁금증 = 로그인 화면에서 id 저장 체크박스 선택하면.. 처리 과정은 무엇일까?
	// memberController > loginUser() 메소드에서 
	
	// 쿠키(존재, 들어있는 값 등) 확인
	// 방법1 = jsp에서 확인
	@RequestMapping("/list1")
	public String list1() {
		return "cookie/list1";
	}
	
	// 방법2 = Spring/controller에서 확인 -> 계/연산이  필요한 경우
	// @CookieValue = 쿠키를 찾아주는 annotation -> 기본 값이 필수이므로, 필수가 아닌 경우 error 발생
	@ResponseBody // 10h35 강사님 설명 제대로 못 들음
	@RequestMapping("/list2")
	public String list2(
							@CookieValue(required=false) Cookie test
						) {
		// test라는 쿠키 발급 안 받았는데, 아래 코드 실행 시 null pointer exception 발생
//		System.out.println(test.getValue());
		
		if (test!= null) {
			System.out.println(test.getValue());
		} else {
			System.out.println("cookie 없음");
		}
		
		return "list2";
	}
	
	// 자동 로그인 = 보안에 아주 취약함 vs 사람들이 매번 로그인하기 귀찮아해서 + 스마트폰 보급되며 로그인 정보 입력하기 어려워져서(작은 화면, 터치 등) 쿠키 많이 사용하게 됨

}
