package com.kh.spring.member.controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller; // Spring에서 제공하는, Maven에서 받아온, library
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.entity.CertVo;
import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

// 2022.2.15(화) 12h45 jdbc 배울 때 했듯이 일반 Java 클래스로 만든 controller에 여러 메소드 모아서 쓸 것임
// Spring에 servlet은 존재하긴 함: view단으로부터 사용자가 요청함 -> servlet(Spring에서는 DispatcherServlet)이 모든 요청을 받아줌 -> 요청별로 실행/처리할 내용은 다른 바, DispatcherServlet으로부터 어찌저찌 이 MemberController(나만의 controller)로 받고, 그 안에서 각기 다른 메소드로 처리
// 2022.2.17(목) 9h15 사용자가 어떤 요청을 하든지 DispatcherServlet이 요청받아줌(new MemberDao()(bean 등록 필요).insertMember() 등과 같이 내가 객체 생성할 일 없고, Spring이 알아서 해줌 등등 강사님 설명 제대로 못 들음) + logic 처리는 내가 만든 controller 클래스에서 각 기능 담당하는 메소드가 함 + handler mapping(새로운 개념)
// ___하는(2022.3.11(금) 15h 나의 생각 = 이 클래스의 생성을 Spring이 알아서 하도록 하는? + 2022.3.15(화) 22h15 나의 생각 = Spring이 이 클래스를 controller로 인식할 수 있게 하는? 의존성 주입하는? 방법1) 이 컨트롤러를 bean으로 등록해두기
// 방법2) Controller 타입의 annotation('@Controller')을 달아두/붙여주면 bean scanning을 통해 자동으로 bean 등록됨 -> servlet-context.xml 설정 파일의 context:component-scan이 내 프로젝트에 있는 모든 파일을 읽는데, 'controller' annotation이 달려있으면 Spring이 이게 컨트롤러라고 알아서 인식함
@Controller
public class MemberController {
	
	// private MemberService memberService = new MemberServiceImpl(); // 앞으로 이렇게 new로 객체 생성 내가 직접 안 할 것임 -> 그러려면 MemberServiceImpl 클래스도 bean으로 등록되어 있어야 함 -> service 클래스 가서 annotation 붙이고 옴
	// 기존의 객체 생성 방식 = 객체 간의 결합도가 높아짐(-> 소스코드의 수정이 일어날 경우, 하나하나 전부 다 바꿔줘야 함) + 동시에 매우 많은 횟수로 service가 요청될 경우, 그만큼 객체가 생성됨(e.g. 2백만명이 동시에 로그인 요청한 경우 이 객체가 2백만개 생성) = 좋지 않은 방법
	// vs Spring의 DI(dependency injection)를 이용한 방식 = 객체를 생성해서 주입해줌 + new 키워드 없이 선언만 함 + @Autowired라는  annotation을 반드시 사용해야 함 -> 객체 간의 결합도 낮춰줌; Spring이 알아서 객체 생성하고, 내가 달라고 할 때 줌
	// Autowired annotation을 붙여서 의존성 주입 = 밑에서 사용할 객체들을 Spring에게 맡김 -> 해당 타입의 기본생성자를 통해 객체를 만들어줌
	@Autowired
	private MemberService memberService; // memberService = interface = 객체 생성 불가능 -> interface를 상속/구현한 자식 클래스는 interface 타입의 생성자로 객체 생성 가능함 -> 이 interface(? 2022.3.11(금) 16h15 나의 생각 = 뭘 쓰다 만거지? ㅠ.ㅠ)
	// Spring이 이 타입에 맞는 것을 찾아서 나에게 줌 = 이렇게 객체 생성을 Spring에게 맡김 = 의존성 주입
	// interface를 상속받은 자식 클래스들이 많은 경우, 해당 메소드 signature에 맞는 메소드를 가진 객체를 Spring이 찾아서 줌
	// 2022.2.17(목) 12h 왜 MemberServiceImpl로 안 하/가는가? -> 추후 MemberServiceImpl2 등으로 바꿨을 때 소스코드 일일이 수정해야 함 = 유지/보수 불편 vs 모듈화/객체화/객체지향프로그래밍(oop)/다형성 -> 부품/객체만 갈아끼움 등 강사님 설명 제대로 이해 못 함 ㅠ.ㅠ -> 2023.10.8(일) 15H25 i think that now i know what the teacher meant..
	
	// 2022.2.18(금) 11h5 추가 -> 아래 클래스? bean 등록해두었으니까, 이렇게 의존성 주입 가능함
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	// 기존 방식 = 필요할 때마다 객체 생성하는 것은 자원의 낭비 심함 + 코드 작성도 복잡함 vs 자원 준비(?), 객체 생성 및 반납을 Spring에게 맡김 = 의존성 주입 -> 선언된 타입에 맞는 객체 생성(기본 생성자 찾아서/이용해서)을 Spring이 알아서 함; 메모리 상에 빈 객체?/기본 생성자 만들어놨다가
	// autowired 쓰고 싶으면/Spring이 해당 객체를 관리할 수 있기 위해서는 bean으로 등록해 두어야 함 <- bean 등록 방법1 = annotations 붙임 ou 방법2 = 내가 만들지 않은/다른 데서 받은 것은 내가 수정할/annotations 붙일 수 없으니까(+수정하면 안 되니까) Spring bean config file.xml에 beans 등록
	// bean으로 등록되어있지 않으면 Spring이 인식/관리할 수 없음
	
	// 2022.3.15(화) 11h45 무엇을 wiring하셨는지 못 봄 ㅠ.ㅠ -> 추후 수업 진행에 따라 sender wiring해 두었는데, 수업 영상 확인 필요함
	@Autowired
	private JavaMailSender sender;
	
	// 2022.2.17(목) 10h20 강사님께서 왜 아래 메소드를 주석 처리하셨는지 못 들음 ㅠ.ㅠ
	// RequestMapping 타입의 annotation을 붙여줌으로써 HandlerMapping 등록
	/*
	@RequestMapping(value="login.me")
	public void loginMember() {
//		int result = new MemberServiceImpl().loginMember(); // 앞으로 이렇게 new로 객체 생성 내가 직접 안 할 것임 -> 그러려면 MemberServiceImpl 클래스도 bean으로 등록되어 있어야 함 -> service 클래스 가서 annotation 붙이고 옴
	}
	*/
	// login 기능 = 사용자로부터 id, pwd(요청 시 전달받은 값) 받아서, db 가서 select/조회 -> Member 객체 받아옴
	
	// Spring에서 parameter(요청 시 전달(받는) 값)를 받는 방법
	// 방법1) HttpServletRequest를 이용해서 전달받기 = 기존의 jsp/servlet 때의 방식 = 해당 메소드의 매개변수로 HttpServletRequest를 작성해두면, Spring container(이게 뭐지? >.<)가 해당 메소드를 호출 시 자동으로 해당 객체를 생성해서 매개변수로 주입해줌
	/*
	@RequestMapping("login.me")
	public String loginMember(HttpServletRequest request) {
		String userId = request.getParameter("id");
		String userPwd = request.getParameter("pwd");
		
		System.out.println("userId = " + userId);
		System.out.println("userPwd = " + userPwd);
		
		return "main"; // main으로 이동
	}
	*/
	
	// 방법2) 'RequestParam'('request.getParameter'를 줄인 것; 기능도 동일 -> request.getParameter("key 값")로 value를 뽑아오는 역할을 대신(수행)해주는 annotation) annotation 이용 -> value 속성의 value로 jsp로부터 작성한 name 속성 값을 담으면, 알아서 해당 매개변수로 받아올 수 있음
	// 2022.2.17(목) 10h50 나의 질문 = RequestParam은 request.getParameterValues() 기능도 수행이 가능한가? -> 11h 강사님 답변 = 강사님께서도 안 해보셔서 모르겠다고 하심
	// request 객체의 특정 key 값에 해당하는 value가 비어있는 경우 null(x) 빈 값/문자열 ""(o)이 옴 -> 넘어온 값이 비어있는 형태라면 대신 넘겨줄 값을 defaultValue 속성(생략 가능)을 통해 기본 값을 지정해 줄 수 있음
	/*
	@RequestMapping("login.me")
	public String loginMember(@RequestParam(value = "id", defaultValue="xx") String userId, @RequestParam(value = "pwd") String userPwd) {
		System.out.println("userId = " + userId);
		System.out.println("userPwd = " + userPwd);
		
		return "main";
	}
	*/
	
	// 방법3) @RequestParam annotation 생략 + 매개변수명을 jsp의 'name 속성 값/요청 시 전달하는 값의 key 값'과 동일하게 setting해둬야 자동으로 값이 주입됨 + defaultValue 추가 속성은 사용할 수 없음
	/*
	@RequestMapping("login.me")
	public String loginMember(String userId, String userPwd) {
		// userId, userPwd와 같은 값이 아예 전달이 안 됨 -> String 자료형의 기본 값인 null이 전달됨
		System.out.println("userId = " + userId);
		System.out.println("userPwd = " + userPwd);
		
		return "main";
	}
	*/
	
	/*
	@RequestMapping("login.me")
	public String loginMember(String id, String pwd) {
		System.out.println("userId = " + id);
		System.out.println("userPwd = " + pwd);
		
		// MyBatis에는 1개의 값만 넘길 수 있음 -> 요청 시 전달받은 값들을 객체나  map에 담음
		Member m = new Member();
		m.setUserId(id);
		m.setUserPwd(pwd);
		
		// service단 메소드에 객체 m을 전달하면서 db 조회
		
		return "main";
	}
	*/
	
	// 방법4) command 객체 방식 = 해당 메소드의 매개변수로 요청 시, 전달 값을 담고자 하는 vo 클래스의 타입을 세팅 후, 요청 시 전달 값의 key 값(jsp의 name 속성 값)을 vo 클래스에 담고자 하는 필드명으로 작성 -> 내부적인 원리 = Spring container가 해당 객체를 기본 생성자로 생성 후, 내부적으로 setter 메소드를 찾아서, 요청 시 전달 값을 해당 필드에 담아줌
	// 기본 생성자가 꼭 필요함 + 반드시 name 속성 값과 담고자 하는 필드명이 동일해야 함
	// 2022.2.17(목) 11h20~25 vo 클래스 생성자(?) 관련해서 학우님 질문 관련해서 강사님께서 부가 설명해주셨는데, 제대로 못 듣고 이해 못 함 ㅠ.ㅠ
	/*
	@RequestMapping("login.me")
	public String loginMember(Member m) {
		// 나의 질문 = 강사님께서 오전에 설명하실 때, 방법1에서 request 객체가 알아서/자동으로 생기는 것 언급하셨었는데, 여기와 어떤 관련이 있는지 제대로 못 들음 ㅠ.ㅠ
//		System.out.println("userId = " + m.getUserId());
//		System.out.println("userPwd = " + m.getUserPwd());
		
		// 이 Member 객체 m(사용자가 입력한 id 및 비밀번호 들어있음)을 바로 service단 메소드에 전달하면서 db 조회하면 됨
//		Member loginUser = new MemberServiceImpl().loginMember(m); // 앞으로는 new로 객체 생성 안 할 것임
		Member loginUser = memberService.loginMember(m);
		
		// 2022.2.17(목) 12h35 db 조회 결과가 없으면 dao로부터 null 반환됨
		if (loginUser == null) { // 로그인 실패 -> error 문구를 requestScope에 담고, error page로 forwarding
			System.out.println("로그인 실패");
		} else { // 로그인 성공 -> loginUSer를  sessionScope에 담고, main page url로 재요청
			System.out.println("로그인 성공");
		}
		
		return "main"; // 2022.2.17(목) 14h40 servlet-context.xml 파일에서 설명해주심 -> 나의 질문 = 현재 Spring 프로젝트에서 main, index, welcome page 개념 등 헷갈림
	}
	*/
	
	/* 2022.2.17(목) 14h45
	 * (request가 없는데)요청 처리 후 응답 데이터를 담고 응답 페이지로 forwarding 또는 url 재요청하는 방법; 아래 2가지 방법 중 내가 더 편한 방법 사용하면 됨
	 * 방법1) request 대신 Spring에서 제공하는 Model 객체를 사용 -> Model = forwarding할 응답 view로 전달하고자 하는 데이터를 map 형식(key-value 세트)으로 담을 수 있는 영역/공간; Model 객체는 requestScope.
	 * request 객체의 역할을 Model 객체가 수행 + 단, Model의 attribute 영역에 값을 담기 위해서는 setAttribute(x) addAttribute(o) 메소드 이용
	 * Model도 Spring으로부터 받아다 씀 + session도 Spring으로부터 받아다 씀
	 */
	/*
	@RequestMapping("login.me")
	public String loginMember(Member m, Model model, HttpSession session) { // 내가 이 메소드에서 이러한 객체/자료형들이 필요하니까 Spring에게 달라고 하려면(+받아서 쓰려면) 매개변수로 받음 -> Spring이 알아서 만들어서 줌
		Member loginUser = memberService.loginMember(m);
		
		if (loginUser == null) { // 로그인 실패 -> error 문구를 requestScope에 담고, error page로 forwarding
			model.addAttribute("errorMsg", "로그인 실패 - error가 발생했습니다 >.<");
			
			// forwarding 방식 = 파일 경로를 포함한 파일명 제시
			// return "xx"; // 이렇게 기재하면 servlet-context.xml에서 주소 자동 완성 등록해주는 bean(도구):viewResolver -> 접두어 = /WEB-INF/views/, 중간 = 파일명 xx, 접미어 = .jsp -> /WEB-INF/views/파일명.jsp
			return "common/errorPage";
		} else { // 로그인 성공 -> loginUser를 sessionScope에 담고, main page url로 재요청(sendRedirect)
			session.setAttribute("loginUser", loginUser);
			
			// url 재요청 방식 = sendRedirect 방식 = url 경로 제시
			// redirect:요청할url(=contextPath 뒤에 붙일 주소)
			return "redirect:/"; // 이렇게 쓰면 localhost:8008/spring + /로 url 재요청; 최상위 폴더 = webapp -> 이 폴더에 있는 welcome page
			// www.naver.com이라고 주소 창에 입력해서 가면, 브라우저가 알아서 내가 입력한 주소 뒤에 / 붙여줌; /는 '폴더' 의미
			
			// cf. 여기에서도 forwarding 방식으로 응답해도 괜찮음
		}
		
	} // loginMember() 종료 15h30
	*/
	
	/* 방법2) Spring에서 제공하는 ModelAndView 객체 사용
	 * Model = 데이터를 key-value 세트로 담을 수 있는 공간
	 * View = 응답 view에 대한 정보를 담을 수 있는 공간
	 * ModelAndView = Model과 View가 결합된 형태의 객체; 단, Model 객체는 단독 존재 가능 vs View 객체는 단독 존재 불가능한 바, Model 객체와 함께 사용되어야 함
	 * addObject() 메소드가 편한 바, 사용하다보면 방법2가 편할 것임(강사님 의견)
	 */
	@RequestMapping("login.me")
	public ModelAndView loginMember(Member m, HttpSession session, ModelAndView mv) {
		// 비밀번호 암호화 작업 전 로그인 logic
		/*
		Member loginUser = memberService.loginMember(m);
		
		if (loginUser == null) {
			mv.addObject("errorMsg", "로그인에 실패했습니다");
			mv.setViewName("common/errorPage");
		} else {
			session.setAttribute("loginUser", loginUser);
			mv.setViewName("redirect:/");
		}
		*/
		
		// 2022.2.18(금) 14h10 비밀번호 암호화 작업 후 로그인 logic
		// 매개변수로 받은 Member 객체 m의 userId 필드 값 = 사용자가 입력한 id 
		// 						  userPwd 필드 값 = 사용자가 입력한 비밀번호 평문
		Member loginUser = memberService.loginMember(m);
		// loginUser = id만 가지고 조회된 회원 객체 -> loginUser의 userPwd 필드 값 = db에 기록된, 암호화된, 비밀번호
		
		// BCryptPasswordEncoder 객체의 matches(평문/rawPassword, 암호문/encodedPassword) 메소드 -> 내부적으로 복호화 등의 작업이 이루어지고, 두 구문이 일치하는지 비교 -> 일치한다면 true 반환
		if (loginUser != null && bcryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) { // 로그인에 성공한 경우
			// 실행해야만 알 수 있는 예외 = runtime exception e.g. 컴파일러는 loginUser가 null일지 미리 알 수 없음 -> loginUser에 null이 반환되었는데, null의 getUserPwd()에 접근하려고 하면 null pointer exception 발생
			// loginUser가 null이라서 조건이 false인 경우, 논리연산의 short-cut 연산에 따라 두번째 조건 체크 안 하고 바로 else절로 넘어감 -> 두번째 조건에서 null pointer exception 발생할 일 없음
			session.setAttribute("loginUser", loginUser);
			mv.setViewName("redirect:/"); // url 방식
		} else { // 로그인 실패 시
			mv.addObject("errorMsg", "로그인에 실패했습니다").setViewName("common/errorPage");
		}
		
		return mv;
	} // 15h40 이 로그인 메소드 테스트 시작하며 session 만료되어서 로그아웃 되어 있을 거라고 강사님께서 설명해주심 -> 나의 질문 = 서버 재구동 시 session이 만료되는 것인가?
	
	// 2022.2.17(목) 15h45 실습 -> 16h 강사님 설명
	/*
	@RequestMapping("logout.me")
	public ModelAndView logoutMember(HttpSession session, ModelAndView mv) {
		session.removeAttribute("loginUser");
		mv.setViewName("redirect:/");
		
		return mv;
	} // logoutMember() 종료
	*/

	// JSP project MemberDeleteController에서 배운 내용 = // invalidate() 메소드를 사용하면 session이 만료되어 alert가 작동하지 않을 것임 -> session.removeAttribute("key 값")를 활용(해당 key 값에 대한 value만 없어짐)해서 로그아웃 시키기
	
	// 강사님 방식
	@RequestMapping("logout.me")
	public String logoutMember(HttpSession session) {
		// 이 메소드가 처리할 일 = 로그아웃 처리 = session 만료시키기/비우기/초기화/지우기
		session.invalidate();
		
		return "redirect:/";
	} // logoutMember() 종료 16h10
	
	// 2022.2.17(목) 16h25
	@RequestMapping("enrollForm.me")
	public String enrollForm() {
		// "WEB-INF/views/(접두어) + member/memberEnrollForm + .jsp(접미어)"로 forwarding
		return "member/memberEnrollForm";
	}
	
	// 2022.2.17(목) 16h35 RequestMapping이라는 annotation = 컨트롤러의 어느 메소드가 어느 요청을 처리할지 알려줌(내가 이해한 것이 맞나? 이 컨트롤러는 어디에, 언제, 어떻게 등록했었지? >.<)
	@RequestMapping("insert.me")
	public String insertMember(Member m, Model model, HttpSession session) { // command 객체 방식으로 요청 데이터를 받고자/담고자 함 -> 'name 속성의 값 = 담고자 하는 vo 클래스의 필드명'이어야 함 -> 16h40 나의 질문 = Jsp 페이지에서 name 값 나열되는 순서가 중요한가요?
		// MyBatis에 1개의 값만 전달할 수 있는 바, 어딘가에 주섬주섬 담아야 함 + 회원(정보) 관련 vo 클래스를 만들어놨음
//		System.out.println(m); // 회원 가입 양식 jsp로부터 사용자가 요청 시 보낸 데이터 잘 전달되어 들어오는지 확인
		// 문제1 = 한글 깨짐 -> 16h45 Member [userId=user03, userPwd=pass03, userName=ì¬ì©ì3, email=user03@abc.de, gender=F, age=38, phone=01234567890, address=ìì¸ì ì¢ë¡êµ¬, enrollDate=null, modifyDate=null, status=null] 찍힘
		// vo 클래스에 만들어둔 toString()에 의해 위와 같이 출력됨; enrollDate, modifyDate, status는 요청 페이지에서 입력 안 했기 때문에 null로 출력됨(나의 질문 = 왜 빈 문자열 ""이 아닌, null로 찍힐까?) + DB 테이블에 default 값 지정되어 있음(null인 컬럼에 대해 INSERT 시 db가 알아서 default 값이 있다면 default 값으로 INSERT할 것임); encoding 이슈로 인해 한글 제대로 표시 안 됨
		// 2022.2.17(목) 17h15 Spring에서 제공하는 encoding 필터를 web.xml에 등록한 후에는 Member [userId=user03, userPwd=pass03, userName=사용자3, email=user03@abc.de, gender=F, age=38, phone=012-3456-7890, address=서울시 종로구, enrollDate=null, modifyDate=null, status=null]와 같이 한글 정상 출력됨
		
		// 문제2 = 나이(선택적 입력 사항)를 입력하지 않고 회원 가입하려고 한 경우, int 자료형의 필드에 빈 문자열이 넘어와 자료형이 맞지 않음 -> Member 객체의 setAge()의 매개변수로 빈 문자열 ""을 넣으려고 해서, 400 bad request error 발생 -> 해결 방법 = Member 클래스 age 필드의 자료형을 int로부터 String으로 바꿈; Member 객체로 요청 받기로 했기 때문에 default 값 설정은 불가능함
		// 2022.2.18(금) 10h15 테스트 시, 나이 입력하지 않아도 문제 없이 동작함; Member(userId=user04, userPwd=pass04, userName=사용자4, email=, gender=F, age=, phone=, address=, enrollDate=null, modifyDate=null, status=null)
		
		// 2022.2.18(금) 10h_
		// 문제3 = 사용자가 입력한 있는 그대로의 비밀번호 plain text(평문)가 보임 = 개인정보보호법에 위배 -> 해결 방법 = Spring security module에서 제공하는 부품인 Bcrypt 방식 사용 = pom.xml에 library 추가 -> BCryptPasswordEncoder 클래스를 xml파일에 bean으로 등록 -> web.xml에 spring-security.xml 파일을 로딩할 수 있도록 작성
		// 암호화 방법은 여러 가지가 있음 -> 암호화 방법으로써의 hashing의 문제점 = 같은 비밀번호에 대해 같은 hashing algorithm이 적용되어 같은 결과가 나옴 + 복호화 가능(rainbow bridge; 10h25 강사님 설명 제대로 못 들음)
		// 요즘에는 복호화가 불가능한 암호 algorithm 사용 + '비밀번호 찾기' 요청하면 '임시 비밀번호'를 보내줌
		// 평문 + salt 값(보통 nano second 단위까지의 시간) + 암호화 algorithm 돌림 = 암호
		
		// 2022.2.18(금) 11h5
//		System.out.println("평문 = " + m.getUserPwd());
		
		// 암호화 작업 = 암호문을 만들어내는 과정
		String encPwd = bcryptPasswordEncoder.encode(m.getUserPwd());
//		System.out.println("암호문 = " + encPwd);
		/* 평문 = 1234 vs 암호문 = $2a$10$xVj9aGgRdo4RWV1lQc6FlONpIGHub.zMVxnZrmYicvGQom426L3LW
		 * 평문 = 1234 vs 암호문 = $2a$10$GPkEMqg6eAjF0ZlhX7SV.u02CCP.L4vimuBdNdr4C7b8LEtd5cgtO -> 같은 평문이라도 salt 값이 다르기 때문에 다른 암호문이 나옴; 강사님 의견으로는 같은 암호문 나올 확률 < lotto 2번 당첨될 확률
		 * 평문 = pass01 vs 암호문 = $2a$10$2/VK4NS1ZIjlGLDSNsNNCOI002CLzpxVF7BsdlQCEPzI6vMFh8sOG
		 * 평문 = pass02 vs 암호문 = $2a$10$b.hbtyPmpWZSTtUd2npTn.Pd67lMAUKVgL0SsK596j4JgIyNiQuLe
		 * db에 직접 컬럼 값 수정 + 트랜잭션 처리(commit)
		 */
		
		// 회원 가입 처리를 위해 회원 정보를 db에 INSERT 시 암호화된 비밀번호를 INSERT해야 함
		m.setUserPwd(encPwd); // Member 객체 m의 userPwd 필드의 값을 평문이 아닌, 암호문으로 변경
		
		int result = memberService.insertMember(m); // INSERT/dml 구문 실행 -> 반환형 = int = 0 또는 1
		
		if (result > 0) { // 회원 가입 성공 시 -> main page url 재요청
			session.setAttribute("alertMsg", "성공적으로 회원 가입이 되었습니다"); // 2022.2.18(금) 11h40 나의 질문 = controller 클래스에서 session, model도 자주 쓸 것 같으면, Autowired해두면 좋을까?
			return "redirect:/";
		} else { // 회원 가입 실패 시 -> error 문구를 담아서 errorPage(/WEB-INF/views/(접두어) + common/errorPage + .jsp(접미어))로 forwarding; result 값이 0이 반환되는/회원 가입 실패할 일은 사실 존재할 수 없는데, 그냥 만들어둔 것으로, 안 만들어도 됨 != id 중복 시에는 dao에서 sql exception/500 error 발생; id 중복 시에는 아예 요청이 못 가게 앞단/jsp 페이지에서 막는 것이 올바른 처리방법임
			model.addAttribute("errorMsg", "회원 가입에 실패했습니다");
			return "common/errorPage";
		}
		
//		return "main"; // 16h45 강사님 테스트 시, return 문구 없었더니, (반환 자료형 void임에도 불구하고? 맞나? 나 스스로 실험해보자) 오류 발생했음
	} // insertMember() 종료
	
	// 2022.2.18(금) 15h5
	@RequestMapping("myPage.me")
	public String myPage() {
		// 'my page' jsp 화면(/WEB-INF/views/(접두어) + member/myPage + .jsp(접미어)) 띄워주는 메소드
		return "member/myPage";
	} // myPage() 종료
	
	// 2022.2.18(금) 15h35
	@RequestMapping("update.me")
	public String updateMember(Member m, Model model, HttpSession session) { // 요청 시 전달 값 많은 바, name 속성도 Member 클래스의 필드명과 동일하게 기재해 두었고, Member 객체로 받고자 함
		// dml 구문의 db 처리 결과 = int 자료형
		int result = memberService.updateMember(m); // 로그인된 사용자만 수정 요청 보낼 수 있음 + MEMBER 테이블에 이 회원의 userId는 반드시 존재 -> where절 조건에서 조회가 안 될 경우는 없음
		
		// 처리 결과에 따라 응답 view/page 지정
		if (result > 0) { // 회원 정보 수정 성공 시
			// db로부터 수정된 회원 정보를 다시 조회해서(이 기능은 위 loginMember() 메소드로 만들어 두었음) session의 'loginUser' key 값에 대한 value 값을 덮어써야 함 + 이 때, 기존의 loginMember() 메소드 재활용해서 조회
			Member updateMem = memberService.loginMember(m);
			session.setAttribute("loginUser", updateMem);
			
			// session에 1회성 알람/alert 문구 담기
			session.setAttribute("alertMsg", "성공적으로 회원 정보가 변경되었습니다");
			
			// 'my page' url 재요청 = sendRedirect 방식
			return "redirect:myPage.me"; // Spring(x) 사용자(o)가 이 요청 값을 찾는 것임; redirect 방식 = 사용자의 요청에 대한 응답으로써, 응답을 해줄 다른 주소, 또는 사용자가 다시 요청해야 하는 곳으로/값을 보내줌
		} else { // 회원 정보 수정 실패 시(e.g. 테이블 제약 조건에 위배되는 값으로 UPDATE하려고 한 경우 등) -> error 문구 담아서, error page로 forwarding
			model.addAttribute("errorMsg", "회원 정보 변경에 실패했습니다");
			return "common/errorPage"; // forwarding 방식 = 사용자의 요청에 대한 응답으로써 응답 page를 보내줌
		}
		
	} // updateMember() 종료
	
	// 2022.2.18(금) 16h50 실습
	@RequestMapping("delete.me")
	public ModelAndView deleteMember(Member m, ModelAndView mv, HttpSession session) {
		Member loginUser = memberService.loginMember(m);
		// loginUser = id만 가지고 조회된 회원 객체 -> loginUser의 userPwd 필드 값 = db에 기록된, 암호화된, 비밀번호
		
		if (loginUser != null && bcryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) { // 비밀번호가 일치하는 경우
			int result = memberService.deleteMember(m.getUserId());
			
			if (result > 0) {
				// mv.addObject("alertMsg", "회원 탈퇴에 성공했습니다");
				session.setAttribute("alertMsg", "회원 탈퇴에 성공했습니다");
				session.removeAttribute("loginUser");
				mv.setViewName("redirect:/"); // 17h20 나의 궁금증 = forwarding 방식으로 mv.setViewName("main") return 할까? 
			} else {
				mv.addObject("errorMsg", "회원 탈퇴에 실패했습니다").setViewName("common/errorPage");
			}
			
		} else { // 비밀번호가 일치하지 않는 경우
			mv.addObject("errorMsg", "회원 탈퇴에 실패했습니다").setViewName("common/errorPage");
		}
		
		return mv;
		// 17h25 초안 작성 및 테스트 마무리 -> 회원 탈퇴 처리(status 변경) 및 화면 전환은 됨 vs 구동상 문제점 = alert창이 안 뜨고, url 창에 뜸 -> 17h45 나의 생각 = alertMsg는 model/request(x) session(o) scope에 있는 attribute이기 때문에, 위와 같이 작성했을 때 alertMsg 안 뜸
	} // deleteMember() 종료
	
	// 2022.2.18(금) 17h20 강사님 설명
	/*
	@RequestMapping("delete.me")
	public String deleteMember(String userId, String userPwd, HttpSession session, Model model) { // jsp 페이지로부터 정확히 userId, userPwd로 name 값을 보내야 여기서 이 이름으로 뽑아서 쓸 수 있음
		// userPwd = 회원 탈퇴 요청 시 사용자가 입력한 비밀번호 평문
		// session의 loginUser(Member 객체)의 userPwd 필드 값 = db에 기록된, 암호화된, 비밀번호
		String encPwd = ((Member)session.getAttribute("loginUser")).getUserPwd(); // getAttribute()의 반환형 = Object -> Member(자식 자료형)로 강제 형 변환해야 자식의 것에 접근 가능함
		
		if (bcryptPasswordEncoder.matches(userPwd, encPwd)) { // 사용자가 입력한 비밀번호가 맞는 경우 -> 탈퇴 처리
			int result = memberService.deleteMember(userId);
			
			if (result > 0) { // 탈퇴 처리 성공한 경우 -> session의 loginUser 지움 + alert 문구 담기 -> 'main page' url 요청 -> 17h45 나의 궁금증 = 여전히 redirect, forward 각각 언제 무엇을 쓰는지 헷갈린다.. ㅠ.ㅠ
				session.removeAttribute("loginUser");
				session.setAttribute("alertMsg", "회원 탈퇴에 성공했습니다");
				return "redirect:/";
			} else { // 탈퇴 처리 실패한 경우(이런 경우는 발생하지 않겠지만, 혹시나 만들어둠)
				model.addAttribute("errorMsg", "회원 탈퇴에 실패했습니다");
				return "common/errorPage";
			}
			
		} else { // 사용자가 입력한 비밀번호가 틀린 경우 -> 탈퇴 처리 실패
			session.setAttribute("alertMsg", "비밀번호가 맞지 않습니다");
			return "redirect:myPage.me"; // 17h40 나의 궁금증 = return "member/myPage"할 때와 다른 점은 무엇일까?
		}
		
	} // deleteMember() 종료
	*/
	
	// 2022.2.23(수) 12h15
	@ResponseBody
	@RequestMapping("idCheck.me")
	public String idCheck(String checkId) {
		int count = memberService.idCheck(checkId);
		
		// 방법1) if문 사용
		/*
		if (count > 0) { // 이미 존재하는 id -> 사용 불가능
			return "NNNNN"; // 그냥 이 문자열 반환하면 이런 jsp 응답 페이지를 찾음 -> 'ResponseBody' annotation을 붙여 응답 페이지(x) 데이터(o)임을 알려줌
		} else { // 현재 db에 존재하지 않는 id -> 사용 가능
			return "NNNNY";
		}
		*/
		
		// 방법2) 3항 연산자 사용
//		return count > 0 ? "NNNNN" : "NNNNY";
		
		// 방법3) count는 1번 쓰고 말 거니까, 굳이 변수 선언 안 하기로 함
		return memberService.idCheck(checkId) > 0 ? "NNNNN" : "NNNNY";
		
		// 어떤 것이 더 좋은 코드인가? = 상황에 따라 if문의 가독성이 더 필요할 수도 있고, 방법3과 같이 line이 짧은 코드가 선호될 수도 있음; line이 짧다고 무조건 좋은 것은 아님
		// 3항 연산자 안의 3항 연산자 안의 3항 연산자 안의.. 이런 경우에는 가독성 떨어질 수 있지만, 위의 코드는 간결한 편임
	}
	
	// 2022.3.15(화) 11h45
	// get 방식으로 'input' 요청받으면 처리할 메소드
	@GetMapping("input")
	public String input() {
		return "member/input";
	}
	
	// 사용자가 입력.. 이 메소드의 기능에 대한 강사님의 설명 정확히 이해 못함; post 방식으로 'input' 요청받으면 처리할 메소드
	@PostMapping("input")
	public String input(String email, HttpServletRequest request) throws MessagingException { // 위 메소드와 메소드명 동일해서 빨간줄 뜸 -> 매개변수 1개 추가해서 오버로딩
		String ip = request.getRemoteAddr(); // 인증 요청한 사용자의 ip 주소는 request 객체를 사용해서 얻을 수 있음 -> MAC주소
		String secret = memberService.sendMail(ip); // 인증 요청한 사용자의 ip 주소 관련해서 생성한 인증 번호는 현재 db에도 있지만, service단 sendMail() 메소드에도 있는 바, 굳이 db 1번 더 다녀오지 않고, service단으로부터 받아오기로 함
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
		helper.setTo(email); // 사용자가 입력한 이메일 주소로 메일을 전송하고자 함
		helper.setSubject("[Spring프로젝트] 인증을 위한 이메일입니다");
		helper.setText("인증 번호 : " + secret + " (인증 요청 후 3분 간만 유효합니다)");
		
		sender.send(message); // '메일 전송하기' 버튼 누르는 것
		
//		return "member/check"; // forwarding하는 방식(이렇게 응답하면, 브라우저 주소창의 url이 input으로 남아있음) vs 대신 아래와 같이 redirect 보내서 'check'라는 요청 다시 하도록 만듦
		return "redirect:check"; // 
	}
	
	@GetMapping("check")
	public String check() {
		return "member/check"; // 이 경로에 있는 check.jsp 페이지를 응답해줌
	}
	
	// 2022.3.15(화) 12h30
	@ResponseBody // 페이지 이동x, http://localhost:8008/spring/check 페이지에 이 행의 반환 값이 찍힘(=그 페이지에 있던 내용이 이 행의 반환 값으로 대체됨)
	@PostMapping("check")
	public String check(String secret, HttpServletRequest request) {
		// 인증 성공/실패 여부만 확인하기 위해 boolean형 자료를 반환받기로 함
		boolean result = memberService.validate(CertVo.builder()
													.who(request.getRemoteAddr())
													.secret(secret)
													.build());
		
		// 2022.3.15(화) 14h5
		return "result = " + result; // 2022.3.15(화) 18h40 나의 질문 = String과의 + 피연산자로써 boolean 값도 String으로 자동 형 변환되는 거 맞나?
	}

}
