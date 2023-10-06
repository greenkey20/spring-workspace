package com.kh.opendata.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kh.opendata.model.vo.AirVO;

//2022.2.24(목) 15h40
public class AirPollutionJavaApp {
	
	// Java application 만들기
	
	public static final String SERVICE_KEY = "JhKf5GLOEzP4YSj9wf3SSymnHPWgof3OqdRDXs3XfHF5iWVUkqEAOn3s90YtdBlQgDkQzo4wMliH6oNuuqcgnw%3D%3D"; // 바뀔 일 없는 값이므로, 상수 처리함; 마이페이지 > open api > 인증 key 발급 현황

	// HttpURLConnection 객체 활용해서 OpenAPI 서버에 요청하여 응답 데이터 받기
	public static void main(String[] args) throws IOException {
		// open API 서버로 요청하는 url 만들기
		String url = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"; // 아래 url의 물음표(?) 전까지 복사해서 붙여넣기
		// http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=JhKf5GLOEzP4YSj9wf3SSymnHPWgof3OqdRDXs3XfHF5iWVUkqEAOn3s90YtdBlQgDkQzo4wMliH6oNuuqcgnw%3D%3D&returnType=xml&numOfRows=100&pageNo=1&sidoName=%EC%84%9C%EC%9A%B8&ver=1.0
		url += "?serviceKey=" + SERVICE_KEY;
		url += "&sidoName=" + URLEncoder.encode("서울", "UTF-8"); // 요청 시 전달 값에 한글이 있는 경우 encoding 작업해줘야 함; 'UnsupportedEncodingException' 예외 처리해줘야 하는데, 앞으로도 이런저런 예외가 나올 것이라 IOException을 throws함 
//		System.out.println(url); // http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty?serviceKey=JhKf5GLOEzP4YSj9wf3SSymnHPWgof3OqdRDXs3XfHF5iWVUkqEAOn3s90YtdBlQgDkQzo4wMliH6oNuuqcgnw%3D%3D&sidoName=%EC%84%9C%EC%9A%B8
//		url += "&returnType=xml";
		url += "&returnType=json"; // 2022.2.24(목) 16h35
		
		// Java 코드로 요청해야 함 <- HttpURLConnection 객체 활용해서 OpenAPI 요청
		// 절차1) 요청하고자 하는 url을 전달하면서 java.net.URL 객체 생성
		URL requestUrl = new URL(url);
		
		// 절차2) 1번 절차로 생성된 URL 객체를 가지고 HttpURLConnection 객체 생성
		HttpURLConnection urlConnection = (HttpURLConnection)requestUrl.openConnection(); // openConnection()의 반환형은 URLConnection -> downcasting 필요
		
		// 절차3) 요청에 필요한 Header 설정 -> 16h10 나의 질문 = Header가 무엇인가?
		urlConnection.setRequestMethod("GET"); // get 방식
		
		// 절차4) 해당 OpenAPI 서버로 요청을 보낸 후, 입력 데이터로 읽어오기
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // 1줄씩 보내는 보조 스트림 = 단독으로 사용 불가능 + 기반 스트림이 있어야 기반 스트림에 붙어서 스트림 만들 수 있음
		// In/OutputStream은 1바이트씩 읽음 vs reader는 2바이트씩 읽음 -> 스트림으로 바꿔주는(?) 보조 스트림이 하나 더 필요함(16h10 강사님 설명 제대로 이해 못함)
		
		// 이렇게 출력하면 중간에 빠지고 출력 안 되는 코드들 있음
		/*
		while (br.readLine() != null) {
			System.out.println(br.readLine()); // 호출하는 순간 데이터가 읽혀버림
		}
		*/
		
		// 이렇게 출력해야 함
		String responseText = ""; // json으로 출력 시 추가
		String line;
		while ((line = br.readLine()) != null) { // 이 코드 정확히 이해 안 됨 -> 16h20 강사님 보충 설명 = 읽어올 때 다음 데이터가 있으면, 계속 읽음
//			System.out.println(line);
			responseText += line; // json으로 출력 시 추가
		}
		
		System.out.println(responseText); // JavaScript 객체 -> Java 언어가 이해할 수 있도록 parsing해야 함
		/*
		{"response":
						{"body":
								{"totalCount":40,"items":
															[
															 {"so2Grade":"1",
															  "coFlag":null,
															  "khaiValue":"58",
															  "so2Value":"0.003",
															  "coValue":"0.4",
															  "pm10Flag":null,
															  "o3Grade":"2",
															  "pm10Value":"33",
															  "khaiGrade":"2",
															  "sidoName":"서울",
															  "no2Flag":null,
															  "no2Grade":"1",
															  "o3Flag":null,
															  "so2Flag":null,
															  "dataTime":"2022-02-24 16:00",
															  "coGrade":"1",
															  "no2Value":"0.014",
															  "stationName":"강남구",
															  "pm10Grade":"1",
															  "o3Value":"0.040"},
															 {"so2Grade":"1","coFlag":null,"khaiValue":"-","so2Value":"0.003","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"37","khaiGrade":null,"sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2022-02-24 16:00","coGrade":"1","no2Value":"0.016","stationName":"서초구","pm10Grade":null,"o3Value":"0.040"},{"so2Grade":null,"coFlag":"점검및교정","khaiValue":"-","so2Value":"-","coValue":"-","pm10Flag":null,"o3Grade":null,"pm10Value":"33","khaiGrade":null,"sidoName":"서울","no2Flag":"점검및교정","no2Grade":null,"o3Flag":"점검및교정","so2Flag":"점검및교정","dataTime":"2022-02-24 16:00","coGrade":null,"no2Value":"-","stationName":"도산대로","pm10Grade":"2","o3Value":"-"},{"so2Grade":"1","coFlag":null,"khaiValue":"65","so2Value":"0.006","coValue":"1.2","pm10Flag":null,"o3Grade":"1","pm10Value":"40","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"2","o3Flag":null,"so2Flag":null,"dataTime":"2022-02-24 16:00","coGrade":"1","no2Value":"0.036","stationName":"강남대로","pm10Grade":"2","o3Value":"0.028"},{"so2Grade":"1","coFlag":null,"khaiValue":"59","so2Value":"0.004","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"37","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2022-02-24 16:00","coGrade":"1","no2Value":"0.021","stationName":"송파구","pm10Grade":"2","o3Value":"0.041"},{"so2Grade":"1","coFlag":null,"khaiValue":"56","so2Value":"0.003","coValue":"0.3","pm10Flag":null,"o3Grade":"2","pm10Value":"39","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2022-02-24 16:00","coGrade":"1","no2Value":"0.014","stationName":"강동구","pm10Grade":"2","o3Value":"0.036"},{"so2Grade":"1","coFlag":null,"khaiValue":"-","so2Value":"0.004","coValue":"0.5","pm10Flag":null,"o3Grade":"2","pm10Value":"39","khaiGrade":null,"sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2022-02-24 16:00","coGrade":"1","no2Value":"0.022","stationName":"천호대로","pm10Grade":"2","o3Value":"0.036"},{"so2Grade":"1","coFlag":null,"khaiValue":"61","so2Value":"0.004","coValue":"0.4","pm10Flag":null,"o3Grade":"2","pm10Value":"31","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2022-02-24 16:00","coGrade":"1","no2Value":"0.019","stationName":"금천구","pm10Grade":"2","o3Value":"0.043"},{"so2Grade":"1","coFlag":null,"khaiValue":"58","so2Value":"0.004","coValue":"0.3","pm10Flag":null,"o3Grade":"2","pm10Value":"29","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2022-02-24 16:00","coGrade":"1","no2Value":"0.015","stationName":"시흥대로","pm10Grade":"2","o3Value":"0.040"},{"so2Grade":"1","coFlag":null,"khaiValue":"63","so2Value":"0.003","coValue":"0.3","pm10Flag":null,"o3Grade":"2","pm10Value":"39","khaiGrade":"2","sidoName":"서울","no2Flag":null,"no2Grade":"1","o3Flag":null,"so2Flag":null,"dataTime":"2022-02-24 16:00","coGrade":"1","no2Value":"0.009","stationName":"강북구","pm10Grade":"2","o3Value":"0.045"}],"pageNo":1,"numOfRows":10},"header":{"resultMsg":"NORMAL_CODE","resultCode":"00"}}}
		*/
		
		// JSONObject, JSONArray = Json library가 제공; 아래와 다른 것들임
		// JsonObject, JsonArray, JsonElement(=GSON library가 제공)를 이용해서 JavaScript 객체를 Java 언어가 이해할 수 있도록 parsing할 수 있음 -> 각각의 item 정보를 AirVO 객체에 담고, ArrayList에 차곡차곡 담기
		JsonObject totalObj = JsonParser.parseString(responseText).getAsJsonObject(); // Json 객체 생성
		
		JsonObject responseObj = totalObj.getAsJsonObject("response"); // response 속성에 접근 -> JsonObject(Json 객체) {}
//		System.out.println(responseObj); // Json 객체 출력
		
		JsonObject bodyObj = responseObj.getAsJsonObject("body"); // body 속성에 접근 -> JsonObject(Json 객체) {}
//		System.out.println(bodyObj);
		
		int totalCount = bodyObj.get("totalCount").getAsInt(); // totalCount 속성에 접근 -> int -> 20h15 나의 질문 = get()의 반환형은 무엇이지? 왜 getAsJsonObject() 메소드로 JsonObject 얻을 때는 get() 안 거쳐도 되지?
//		System.out.println(totalCount); // 40
		
		JsonArray itemArr = bodyObj.getAsJsonArray("items"); // items 속성에 접근 -> JsonArray(Json 배열) []
//		System.out.println(itemArr);
		
		ArrayList<AirVO> list = new ArrayList<>();
		
		for (int i = 0; i < itemArr.size(); i++) { // itemArr 배열에 객체가 몇 개 있는지 정확히 모르니까, 반복문 돌림
			JsonObject item = itemArr.get(i).getAsJsonObject(); // itemArr 배열의 원/요소 하나하나인 객체를 Json 객체로 빼냄
//			System.out.println(item);
			
			AirVO air = new AirVO();
			air.setStationName(item.get("stationName").getAsString()); // item의 속성(명)으로 접근해서 String으로 값 뽑아 air 객체의 관련 필드의 값으로 set; 속성명 오타 나서 제대로 set 안 되면 null로 출력될 것임
			air.setDataTime(item.get("dataTime").getAsString());
			air.setKhaiValue(item.get("khaiValue").getAsString());
			
			air.setPm10Value(item.get("pm10Value").getAsString());
			air.setSo2Value(item.get("so2Value").getAsString());
			air.setCoValue(item.get("coValue").getAsString());
			air.setNo2Value(item.get("no2Value").getAsString());
			air.setO3Value(item.get("o3Value").getAsString());
			
			list.add(air);
		}
		
//		System.out.println(list);
		// 위 출력문의 가독성 떨어져서 for문으로 출력하기로 함 
		for (AirVO a : list) {
			System.out.println(a);
		}
		
		// 절차5) 다 사용한 스트림 반납
		br.close();
		urlConnection.disconnect();
		
		// 이렇게 받아서 ArrayList에 담은 응답 데이터를 client에게 응답 -> client 측에서는 돌려받은 응답 데이터를 가지고 parsing 작업 후, 웹 페이지에 시각화
		// timed out 오류 = 내 요청을 받는 그 쪽의 문제
		// e.g. 공공데이터는 요청 받을 수 있는 traffic 한계 있는 경우, 요청 받았는데 응답 못하면 대기(?) 상태
		// 보안 솔루션에서 차단
	} // main() 종료

}
