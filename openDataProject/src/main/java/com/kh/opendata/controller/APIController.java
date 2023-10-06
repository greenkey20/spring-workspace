package com.kh.opendata.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 2022.2.25(금) 9h30
// 2022.2.25(금) 10h5 테스트 시 'controller' annotation을 안 달았더니, 404 error = WARN : org.springframework.web.servlet.PageNotFound - No mapping found for HTTP request with URI [/opendata/air.do] in DispatcherServlet with name 'appServlet'
@Controller
public class APIController {
	
	private static final String SERVICE_KEY = "JhKf5GLOEzP4YSj9wf3SSymnHPWgof3OqdRDXs3XfHF5iWVUkqEAOn3s90YtdBlQgDkQzo4wMliH6oNuuqcgnw%3D%3D"; // 바뀌면 안 되는 값이므로 상수로 만들어둠 -> 9h40 나의 질문 = 어제 수업 Java application에서는 public 상수로 만든 것 맞나? 어떤 의미가 있을까? 
	
	/*
	@ResponseBody // 데이터 응답해 줄 것임
	@RequestMapping(value="air.do", produces="application/json; charset=utf-8")
	public String airPollution (String location) throws IOException {
		// 자료 요청 보낼 url 만들 건데, 길고 가독성 떨어지므로, 부분부분 잘라서 작성 
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		// http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty
		// B552584 = contextPath > 폴더 ArpltnInforInqireSvc(아마 의미 있게 만들었을 것임)
		
		url += "?serviceKey=" + SERVICE_KEY;
		url += "&sidoName=" + URLEncoder.encode(location, "UTF-8");
		url += "&returnType=json"; // returnType 기본적으로는 _(9h40 강사님 설명 제대로 못 들음)인데, 나는 json으로 받고자 함
		// + 응답 데이터의 상세 내용은 '참고 문서' 직접 읽어서 활용해야 함
		
//		System.out.println(url); // http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=JhKf5GLOEzP4YSj9wf3SSymnHPWgof3OqdRDXs3XfHF5iWVUkqEAOn3s90YtdBlQgDkQzo4wMliH6oNuuqcgnw%3D%3D&sidoName=%EC%84%9C%EC%9A%B8&returnType=json
		
		URL requestUrl = new URL(url); // url 객체 생성 <- 내가 요청 보낼 url 주소를 인자로 넣음
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection(); // requestUrl.openConnection()의 반환형인 URLConnection은 부모 -> 자식 HttpURLConnection으로 강제 형 변환해서 사용(?) 가능
		urlConnection.setRequestMethod("GET"); // 요청 보낼 방식을 get 방식으로 지정 -> 10h15 강사님께서는 이 줄 주석 처리하심 vs 나는 이 줄 놔둬도 데이터 응답 잘 됨 -> 나의 질문 = 강사님께서 언제/왜 주석 처리하신 거지? + 이 줄 놔둬도 괜찮은 것인가? >.< -> 10h25 강사님께서도 주석 푸심 + 별도 설명은 안 해주신 듯
		
//		BufferedReader br = new BufferedReader(urlConnection.getInputStream()); // The constructor BufferedReader(InputStream) is undefined
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // 기반 스트림 = InputStream = 1바이트씩 읽어옴 vs BufferedReader는 2바이트씩 읽어옴 -> InputStreamReader가 InputStream을 __해줌(9h45 강사님 설명 제대로 못 들음)
		
		String responseText = "";
		String line;
		while ((line = br.readLine()) != null) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
		
		return responseText; // String 형태의 자료를 json 형태로 변환해서 반환할 것임
	}
	*/
	
	// 공공 데이터 사용하다보면 xml로만 자료 응답해주는 경우 있음 -> 2022.2.25(금) 10h45
	@ResponseBody // 데이터 응답해 줄 것임
	@RequestMapping(value="air.do", produces="text/xml; charset=utf-8")
	public String airPollution (String location) throws IOException {
		// 자료 요청 보낼 url 만들 건데, 길고 가독성 떨어지므로, 부분부분 잘라서 작성 
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty";
		// http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty
		// B552584 = contextPath > 폴더 ArpltnInforInqireSvc(아마 의미 있게 만들었을 것임)
		
		url += "?serviceKey=" + SERVICE_KEY;
		url += "&sidoName=" + URLEncoder.encode(location, "UTF-8");
		url += "&returnType=xml"; // xml로 응답받고 produces에서 "application/json"으로 기재해두면, client 페이지로 json으로 감 -> 브라우저 개발자 도구 network > status code = 200, response에는 데이터 잘 넘어감 vs 'AJAX 통신 실패'(error로 들어감) -> 10h50 나의 질문 = 왜?
		
		// 2022.2.25(금) 11h40 자료가 기본적으로 10건씩 밖에 조회가 안 되어서, 아래와 같이 url로 요청 내용 추가
		url += "&numOfRows=30";
		
//		System.out.println(url); // http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=JhKf5GLOEzP4YSj9wf3SSymnHPWgof3OqdRDXs3XfHF5iWVUkqEAOn3s90YtdBlQgDkQzo4wMliH6oNuuqcgnw%3D%3D&sidoName=%EC%84%9C%EC%9A%B8&returnType=json
		
		URL requestUrl = new URL(url); // url 객체 생성 <- 내가 요청 보낼 url 주소를 인자로 넣음
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection(); // requestUrl.openConnection()의 반환형인 URLConnection은 부모 -> 자식 HttpURLConnection으로 강제 형 변환해서 사용(?) 가능
		urlConnection.setRequestMethod("GET"); // 요청 보낼 방식을 get 방식으로 지정 -> 10h15 강사님께서는 이 줄 주석 처리하심 vs 나는 이 줄 놔둬도 데이터 응답 잘 됨 -> 나의 질문 = 강사님께서 언제/왜 주석 처리하신 거지? + 이 줄 놔둬도 괜찮은 것인가? >.< -> 10h25 강사님께서도 주석 푸심 + 별도 설명은 안 해주신 듯
		
//		BufferedReader br = new BufferedReader(urlConnection.getInputStream()); // The constructor BufferedReader(InputStream) is undefined
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // 기반 스트림 = InputStream = 1바이트씩 읽어옴 vs BufferedReader는 2바이트씩 읽어옴 -> InputStreamReader가 InputStream을 __해줌(9h45 강사님 설명 제대로 못 들음)
		
		String responseText = "";
		String line;
		while ((line = br.readLine()) != null) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
		
		return responseText; // String 형태의 자료를 json 형태로 변환해서 반환할 것임
	}
	
	// 2022.2.25(금) 12h15 지진/해일 대피소 실습 <- xml 데이터 받기
	@ResponseBody
	@RequestMapping(value="shelter.do", produces="text/xml; charset=utf-8")
	public String tsunamiShelter () throws IOException {
		String url = "http://apis.data.go.kr/1741000/TsunamiShelter3/getTsunamiShelter1List";
		url += "?serviceKey=" + SERVICE_KEY;
		url += "&numOfRows=50"; // 12h30 나의 질문 = 1000개 받으면 paging 처리는 어떻게 할까? -> 12h40 강사님 답변 = script 영역 내에서 (JavaScript 코드로) 페이징 처리해줘야 함
		url += "&type=xml";
		
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET"); // 12h30 나의 질문 = get 방식으로 요청받는 특별한 이유가 있을까? 문서에 그렇게 나와있나?
		
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String responseText = "";
		String line;
		while ((line = br.readLine()) != null) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
		
		return responseText;
	}
	
	// 2022.2.25(금) 14h20 강사님 설명
	@ResponseBody
	@RequestMapping(value="disaster.do", produces="text/xml; charset=utf-8")
	public String disasterShelter() throws IOException {
		String url = "http://apis.data.go.kr/1741000/TsunamiShelter3/getTsunamiShelter1List";
		url += "?serviceKey=" + SERVICE_KEY; // 필수 입력 항목
		url += "&type=xml"; // 필수 입력 항목
		url += "&pageNo=1"; // default 값이 1이라고 하나, 필수 입력 항목이라고 되어있기도 함 -> 14h30 나의 생각 = 이 정보를 가지고 view단에서 paging 처리할 수 있을 것 같다..?!
		url += "&numOfRows=50"; // default 값이 10이라고 하나, 필수 입력 항목이라고 되어있기도 함
		
		// 아래 처리 내용은 공통적으로 적용되는 바, 강사님께서도 air.do 처리 메소드에서 복사해서 붙여넣기 하심
		URL requestUrl = new URL(url);
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection();
		urlConnection.setRequestMethod("GET");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		
		String responseText = "";
		String line;
		while ((line = br.readLine()) != null) {
			responseText += line;
		}
		
		br.close();
		urlConnection.disconnect();
		
		return responseText;
	}
	
}
