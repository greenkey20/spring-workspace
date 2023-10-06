package com.kh.ajax.controller;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.kh.ajax.model.vo.Member;

//2022.2.23(수) 10h10
//controller 클래스 생성 후, 바로'Controller'라고 annotation 달기 = bean으로 등록 -> servlet-context.xml에서 읽으며 beans 인식(10h10 강사님 설명 다 적지는 못함)
@Controller
public class AjaxController {
	
	// AJAX 요청 받으면, 데이터(o) view page(x) 응답 보내므로, AJAX 요청 처리하는 메소드명 앞에 ajax 기재하기
	
	// 방법1) HttpServletResponse 객체로 응답 데이터 응답 = 기존의 jsp/servlet 때 했던 Stream 응답 방식 = 기존에 알/했던 방식과 동일한 방법
	/*
	@RequestMapping("ajax1.do")
	public void ajaxMethod1(String name, int age, HttpServletResponse response) throws IOException { // int라고 적었을 때 숫자 값이 들어가면, 문자열로 들어온 값을 알아서 숫자로 parsing해줌 + 단, 반드시 값이 들어와야 함/빈 값이 들어오면 안 됨
		// 요청 시 전달 값이 잘 넘어오는지 확인 -> 10h20 테스트 시 잘 넘어옴
//		System.out.println(name); // 강판다
//		System.out.println(age); // 4
		
		// 요청 처리를 위해 service 호출
//		Member m = memberService.처리할메소드명1(name, age);
		
		// 요청 처리 다 했다는 가정 하에, 요청한 페이지에 응답할 데이터가 있을 경우
		String responseData = "응답 문자열 = " + name + " 님은 " + age + "세입니다";
		
		response.setContentType("text/html; charset=UTF-8"); // 보내고자 하는 데이터의 encoding 설정
		response.getWriter().print(responseData); // Stream 만드는 과정에서 io exception 발생할 수 있기 때문에 (예외가 발생 안 할 수도 있지만)예외 처리해줌 -> throws = 이 메소드를 호출한 주체(여기서는 Spring)에게 예외 처리 떠넘김  -> 예외 발생 시 Spring이 처리함
		// servlet 클래스 생성 시 doGet() 메소드 선언부(?)에 throws 구문이 자동으로 만들어져 있었음
	}
	*/
	
	/* 2022.2.23(수) 10h45
	 * 방법2) 응답할 데이터가 문자열(String)로 return될 경우 -> HttpServletResponse 필요 없음/안 쓸 수 있음
	 * 문자열 return = forwarding 방식 -> return 문자열을 응답 view로 인식해서, 해당 view page를 찾음 vs 내가 반환/return하는 문자열이 응답 페이지/view(x) 응답 데이터(o)임을 알려줘야 함 = annotation 'ResponseBody'
	 */
	/*
	@ResponseBody
	@RequestMapping(value="ajax1.do", produces="text/html; charset=utf-8") // "ajax1.do" = value 속성의 값 -> 1개 속성만 있을 때는 'value=' 표기 안 해도 됨 vs 속성 추가 시에는 기재해야 함
	public String ajaxMethod1(String name, int age) throws IOException {
		// 아래 return문 = 해당 페이지로 forwarding -> 브라우저 console에 404 error(페이지를 찾을 수 없음) 발생
		return "응답 문자열 = " + name + " 님은 " + age + "세입니다";
		
		// AJAX 등 JavaScript 작업 시에는 꼭 브라우저 개발자 도구/console 켜서 처리 결과 등 내용 확인하며 작업하기
		// 11h40 사용자가 입력한 name '홍길동'이 어떻게 요청 페이지로부터 한글 안 깨지고 잘 넘어오는 이유 = encoding filter는 동기식 통신일 때만 적용됨(요청 페이지로부터 '홍길동' 한글 써서 넘겼을 때, filter 등 encoding 설정 안 해주면 controller에서 받을 때 이미 깨져있음) vs AJAX는 알아서 잘 넘어옴
		// produces = 응답해줄 때 응답 데이터의 encoding 지정 != 요청 받을 때 데이터의 encoding
	}
	*/
	
	// 2022.2.23(수) 14h15 다수의 응답 데이터가 있을 경우 -> response 객체 사용하는 방법 = response 객체 받아오고 __ 등(15h20 강사님 설명 제대로 못 들음) 사용 번거로움
	// 현재 'ajax1.do'라는 요청을 여러 가지 방법으로 처리하고 있는데, url mapping 값이 중복되면 안 되므로, 앞의 다른 메소드들 주석 처리 잘 해두기 vs otherwise 서버 안 켜짐
	/*
	@RequestMapping("ajax1.do")
	public void ajaxMethod1(String name, int age, HttpServletResponse response) throws IOException {
		// 요청 처리가 다 되었다는 가정 하에, 데이터 응답
		
		// 값 하나씩 넣어서 getWriter().print() 보내는 방법
//		response.setContentType("text/html; charset=utf-8");
//		response.getWriter().print(name);
//		response.getWriter().print(age);
		// 위와 같이 응답하면 '강미피3'라는 문자열로 name과 age가 붙어서 응답 데이터 감 -> 꺼내서 사용하기 어려우므로, 이 방법은 안 쓰기로 함 vs JSON(JaveScript Object Notation) 형태로 담아서 응답
		// JSONArray = [값, 값, 값, ..] = Java에서의 ArrayList와 유사
		// JSONObject = {key:값, key:값, key:값, ..} = Java에서의 HashMap과 유사 = key+value set; 바구니/주머니 개념; 순서 없음; 동일한 key 값에 대해 value 입력하면 덮어쓰기됨
		
		// https://mvnrepository.com에서 json 검색 -> JSON.simple 1.1.1의 Maven <dependency> 태그 가져와서 pom.xml에 추가 vs 이거 없으면 JSONArray import 안 되고 빨간줄 뜸
		// JSON 응답 방법1 = JSONArray; JSON 배열 사용하려면 특정 정보가 몇 번 인덱스에 있는지 알아야 함
		 JSONArray jArr = new JSONArray();
		 jArr.add(name); // ["강미피"]
		 jArr.add(age); // ["강미피", 3]
		 
		 response.setContentType("application/json; charset=utf-8");
//		 response.getWriter().print(jArr); // 응답 데이터 = JavaScript의 배열 -> ['강미피', 3]
		 
		 // JSON 응답 방법2 = JSONObject
		 JSONObject jObj = new JSONObject();
		 jObj.put("age", age);
		 jObj.put("name", name);
		 
		 response.getWriter().print(jObj); // 응답 데이터 = JavaScript의 객체 -> {name: '강곰돌', age: 6}
	}
	*/
	
	// 2022.2.23(수) 15h25
	@ResponseBody
	@RequestMapping(value="ajax1.do", produces="application/json; charset=UTF-8")
	public String ajaxMethod1(String name, int age) {
		JSONObject jObj = new JSONObject();
		jObj.put("name", name); // {name:'강토미'}
		jObj.put("age", age); // {name:'강토미', age:5}
		
//		return jObj.toString(); // toString() 써도, 어차피 문자열로 반환되는 거라, 잘 됨
		return jObj.toJSONString(); // toJSONString() -> JSONObject를 문자열로 반환 -> 2022.2.23(수) 18h45 나의 질문 = 왜 jObj를 그냥 반환 안 하고, String으로 변환해서? 응답하는 것인가? Spring에서 AJAX 응답하는 특별한 방법으로써 이런 건가?
	}
	
	// 2022.2.23(수) 15h35
	/*
	@ResponseBody
	@RequestMapping(value="ajax2.do", produces="application/json; charset=utf-8")
	public String ajaxMethod2(int num) {
		// 요청 처리 잘 되었다고 치기; 수제 Member 객체 하나 생성
//		Member m = mService.selectMember(num);
		Member m = new Member("user01", "pass01", "강스노크메이든", 4, "01012345678");
		
		// Java 객체를 JSON 형태로 만들어서(=번거로운/귀찮은 작업) 응답; 회원 정보 조회 결과인 바, 비밀번호는 안 넘겨줌 -> 객체의 모든 필드를 다 넘길 필요 없고, 응답 페이지에서도 객체의 모든 속성 뽑아 쓸 필요 없음
		JSONObject jObj = new JSONObject();
		jObj.put("userId", m.getUserId());
		jObj.put("name", m.getName());
		jObj.put("age", m.getAge());
		jObj.put("phone", m.getPhone());
		
		return jObj.toJSONString(); // 응답 데이터 type 및 encoding 지정 안 하고 보내면, 한글 깨짐 + 일반 문자열 데이터 넘어감 -> {"phone":"01012345678","name":"???????","userId":"user01","age":4}
		// @RequestMapping에 produces 속성 부여 -> {phone: '01012345678', name: '강스노크메이든', userId: 'user01', age: 4}
	}
	*/
	
	// 2022.2.23(수) 16h10 GSON 이용해서 Java 객체를 JSON 객체로 변환해서 응답 
	@ResponseBody
	@RequestMapping(value="ajax2.do", produces="application/json; charset=utf-8")
	public String ajaxMethod2(int num) {
		// 요청 처리 잘 되었다고 치기; 수제 Member 객체 하나 생성
//			Member m = mService.selectMember(num);
		Member m = new Member("user01", "pass01", "강스노크메이든", 4, "01012345678");

		return new Gson().toJson(m); // key 값은 GSON 내부적으로 Member 객체의 필드명으로 알아서 잡힘 -> {userId: 'user01', userPwd: 'pass01', name: '강스노크메이든', age: 4, phone: '01012345678'}
	}
	
	// 2022.2.23(수) 16h20
	@ResponseBody
	@RequestMapping(value="ajax3.do", produces="application/json; charset=utf-8")
	public String ajaxMethod3() {
		// 아래와 같은 요청 처리 잘 되었다고 치기; 수제 list 하나 생성
//		ArrayList<Member> list = mService.selectList();
		
		ArrayList<Member> list = new ArrayList<Member>(); // 현재 프로젝트의 Java 버전이 1.6으로 되어있는데(프로젝트 properties > project facets에서 맞나? 어디서 확인하는지 16h25 강사님 설명 제대로 못 봄 ㅠ.ㅠ), 1.6 버전까지는 문법상 제네릭에 자료형 꼭 기재했어야 했음 -> 귀찮기 때문에 version up하며 문법 수정
		// Java version up -> 속도 느려짐 + 성능 향상 e.g. Java 10~ 타입 추론 가능 -> 컴파일 속도 느려짐
		list.add(new Member("user01", "pass01", "강스노크메이든", 4, "01012345678"));
		list.add(new Member("user02", "pass02", "강무민", 4, "01012345679"));
		list.add(new Member("user03", "pass03", "강해피", 4, "01012345680"));
		
		// JavaScript에는 ArrayList라는/같은 자료형 없음 -> JSONArray(배열) 형태로 담아서(=GSON 사용하면 편하게/쉽게 할 수 있음) 응답
		return new Gson().toJson(list); // 반환 타입 지정 안 하기 전에는 문자열("")로 넘어감 -> "[{필드명1:값, 필드명2:값, 필드명3:값, ..}, {필드명1:값, 필드명2:값, 필드명3:값, ..}, {필드명1:값, 필드명2:값, 필드명3:값, ..}]" = 객체 배열의 형태 = 객체(들)를 요소로 갖는 배열
	}

}
