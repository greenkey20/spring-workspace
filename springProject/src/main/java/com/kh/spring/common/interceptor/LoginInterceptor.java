package com.kh.spring.common.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

// 2022.2.24(목) 14h20
public class LoginInterceptor extends HandlerInterceptorAdapter {

	/* (Handler)Interceptor = 해당 controller가 실행되기 전 + 실행된 후에 낚아채서 실행할 내용을 작성 가능
	 * e.g. 로그인 유무 판단, 회원의 권한 체크 등 -> Spring에서 사용자의 권한 체크/관리하기 위해 사용하는 도구(? 강사님의 설명/용어 정확히 필기 못 함) = interceptor
	 * 
	 * preHandle(전 처리) = DispatcherServlet이 controller를 호출하기 전에 낚아채는 영역 -> preHandle() 메소드 overriding 구현
	 * postHandle(후 처리) = controller에서 요청 처리 후 DispatcherServlet으로 view 정보가 return되는 순간 낚아채는 영역 -> postHandle() 메소드 overriding 구현
	 * 
	 * 권한 처리하는 거니까 preHandle을 많이 씀
	 * 14h40 후처리 사례는 강사님께서도 잘 모르겠다고 하셨음 + 검색해봐도 별다른 사례 안 나왔음
	 * 
	 * 내가 필요한 서비스에 대해 servlet-context.xml에 interceptor 태그 등록 + url mapping 값 등록해둠
	 */
	
	@Override // 나는 override annotation 자동으로 안 뜬 것 같은데, 맞나..? >.<
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		// true return 시, 기존 요청 흐름대로 해당 controller 정상 실행 vs false return 시, controller 실행되지 않음
		
		HttpSession session = request.getSession();
		// 현재 요청을 보낸 사람이 로그인 되어있을 경우 = session에 loginUser 객체가 있는 경우 -> controller 실행
		if (session.getAttribute("loginUser") != null) {
			return true;
		} else { // 로그인 되어있지  않은 경우 -> controller 실행x
			session.setAttribute("alertMsg", "로그인 후 이용 가능한 서비스입니다");
			response.sendRedirect(request.getContextPath());
			return false;
		}
		
	}
	
}
