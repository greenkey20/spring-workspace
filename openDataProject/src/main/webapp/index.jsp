<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index.jsp</title>
<!-- jQuery 라이브러리 -->
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>
	<!--2022.2.25(금) 9h15 http://localhost:8008/opendata/-->
	<h2>실시간 대기 오염 정보</h2>
	
	지역 :
	<select id="location">
		<option>서울</option>
		<option>대전</option>
		<option>부산</option>
	</select>
	
	<button id="btn1">해당 지역 대기 오염 정보</button>
	<br><br>
	
	<table id="result1" border="1" align="center">
		<thead>
			<tr>
				<th>측정소명</th>
				<th>측정 일시</th>
				<th>통합 대기 환경 수치</th>
				<th>미세먼지 농도</th>
				<th>일산화탄소 농도</th>
				<th>이산화질소 농도</th>
				<th>아황산가스 농도</th>
				<th>오존 농도</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	
	<script>
		$(function() {
			$("#btn1").click(function() {
				/* 공공 데이터 사이트로부터 json 데이터를 응답받아 client 페이지에 표시/출력하기
				$.ajax({
					url : "air.do",
					data : {location : $("#location").val()},
					success : function(data) {
						// console.log(data); // 2022.2.25(금) 10h 테스트 -> JavaScript의 객체 형태
						// console.log(data.response.body.items); // JavaScript에서 객체의 속성에 접근 시 . 찍어서 접근
						
						const itemArr = data.response.body.items;
						
						let value = "";
						for (let i in itemArr) {
							// console.log(itemArr[i]);
							let item = itemArr[i];
							
							value += "<tr>"
										+ "<td>" + item.stationName + "</td>"
										+ "<td>" + item.dataTime + "</td>"
										+ "<td>" + item.khaiValue + "</td>"
										+ "<td>" + item.pm10Value + "</td>"
										+ "<td>" + item.coValue + "</td>"
										+ "<td>" + item.no2Value + "</td>"
										+ "<td>" + item.so2Value + "</td>"
										+ "<td>" + item.o3Value + "</td>"
									+ "</tr>";
						}
						
						$("#result1>tbody").html(value);
					},
					error : function() {
						console.log("AJAX 통신 실패");
					}
				})
				*/
				
				// 2022.2.25(금) 10h45 xml로 응답 받은 자료를 출력해 보기 실습
				$.ajax({
					url : "air.do",
					data : {location : $("#location").val()},
					success : function(data) {
						// console.log(data); // #document
						// console.log(data.response.body.items); // 
						// console.log(data['item']);
						
						// 11h 나의 시도 = googling -> http://parkjuwan.dothome.co.kr/wordpress/2017/02/13/xml-parsing-js/
						const itemArr1 = data.getElementsByTagName('item'); // 이렇게 선택해도 되긴 하는데, JavaScript 선택 코드는 복잡 + jQuery 선택자 써야 jQuery 메소드 사용 가능; HTMLCollection(10) [item, item, item, item, item, item, item, item, item, item]
						// console.log(itemArr1);
						
						for (let i in itemArr1) {
							// console.log(itemArr[i].val().getElementByTagName('stationName'));
							// console.log(itemArr1.children().eq(i)); // Uncaught TypeError: itemArr1.children is not a function
						}
						
						// 11h20 강사님 설명 = jQuery find() 메소드 -> 기준이 되는 요소의 하위 요소들 중 특정 요소를 찾을 때 사용
						// console.log(data.find("item")); // Uncaught TypeError: data.find is not a function
						// console.log($(data).find("item")); // find()는 jQuery용 메소드(선택된 요소를 기준으로 모든 후손 요소들 중에서 제시한 값과 일치하는 요소들만 선택)니까, jQuery 객체로 바꿔줘야 사용 가능 -> $(data)(o) data(x)로 사용 -> k.fn.init(10) [item, item, item, item, item, item, item, item, item, item, prevObject: k.fn.init(1)]
						
						// 단계1) 응답 데이터 안에 실제 데이터가 담겨있는 요소 선택
						let itemArr2 = $(data).find("item"); // 응답받은 데이터 data 중에 'item' 태그들(? 정확히 어떤 선택자?인지 공부 필요) 선택
				
						// 단계2) 반복문을 통해 실제 데이터가 담긴 요소들에 접근해서 동적으로 요소 만들기
						let value = "";
						itemArr2.each(function(i, item) { // jQuery 반복문(? 공부해야 함 ㅠ.ㅠ)
							// console.log(item); // <item>...</item>
							// console.log($(item).find("stationName")); // find() 메소드는 요소 찾음 -> <stationName>중구</stationName>; k.fn.init [stationName, prevObject: k.fn.init(1)]
							// console.log($(item).find("stationName").text()); // 중구
							
							value += "<tr>"
										+ "<td>" + $(item).find("stationName").text() + "</td>"
										+ "<td>" + $(item).find("dataTime").text() + "</td>"
										+ "<td>" + $(item).find("khaiValue").text() + "</td>"
										+ "<td>" + $(item).find("pm10Value").text() + "</td>"
										+ "<td>" + $(item).find("coValue").text() + "</td>"
										+ "<td>" + $(item).find("no2Value").text() + "</td>"
										+ "<td>" + $(item).find("so2Value").text() + "</td>"
										+ "<td>" + $(item).find("o3Value").text() + "</td>"
									+ "</tr>"
						})
						
						$("#result1 tbody").html(value);
					},
					error : function() {
						console.log("AJAX 통신 실패");
					}
				})
				
			})
		})
	</script>
	
	<hr>
	<!--2022.2.25(금) 12h 실습-->
	<h2>지진/해일 대피소</h2>
	
	<!--내가 실습 때 한 것-->
	<input type="button" value="실행(나의 실습)" id="btn2">
	
	<table id="result2" border="1" align="center">
		<thead>
			<tr>
				<th>일련 번호</th>
				<th>시도명</th>
				<th>시군구명</th>
				<th>대피 지구명</th>
				<th>대피 장소명</th>
				<th>주소</th>
				<th>수용 가능 인원 수(명)</th>
				<th>해변으로부터 거리(m)</th>
				<th>대피소 분류명</th>
				<th>내진 적용 여부</th>
				<th>해발 높이</th>
			</tr>
		</thead>
		<tbody>
		</tbody>
	</table>
	
	<div id="map" style="width:100%;height:350px;"></div>
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=91a9ef71ada79edd0d3a2bfeabf6a17e"></script>
	
	<!--강사님 방식-->
	<input type="button" value="실행(강사님 설명)" id="btn3">
	
	<div id="result3"></div>
	
	<script>
		$(() => {
			/* 2022.2.25(금) 14h15
			   Java에서 -> = lo 연산자; 함수형 프로그래밍 구현을 위해 사용; 람다식
					
			   JavaScript에서는 익명함수들을 =>로 작성할 수 있음
			   "function() { }"를 "() => { }"로,
			   "function(data) { }"를 "data => { }"로,
			       매개변수가 2개 이상인 경우, "function(a, b) { }"를 "(a, b) => { }"로,
			   return만 해주면 되는 함수 "function() { return 10; }"를 "() => 10"로 작성 가능; 바로 return하고자 하는 값만 쓸 수 있음
			*/
			
			$("#btn3").click(() => {
				$.ajax({
					url : "disaster.do",
					success : data => { // data = xml 형식의 자료
						// console.log(data); // #document
						
						// console.log($(data).find("row"));
						
						let $table = $("<table border='1' align='center'></table>"); // jQuery 객체로 만듦 -> 21h15 그런데 table 태그를 jQuery 객체로 만드는 것의 의미를 정확히 모르겠음
						let $thead = $("<thead></thead>");
						let headTr = "<tr>"
										+ "<th>일련번호</th>"
										+ "<th>시도명</th>"
										+ "<th>시군구명</th>"
										+ "<th>대피장소명</th>"
										+ "<th>주소</th>"
										+ "<th>수용가능인원(명)</th>"
										+ "<th>해변으로부터의 거리</th>"
										+ "<th>대표소 분류명</th>"
									+ "</tr>";
						$thead.html(headTr);
						
						let $tbody = $("<tbody></tbody>");
						let bodyTr = "";
						$(data).find("row").each((i, row) => {
							console.log($(data).find("shel_nm").text());
							
							bodyTr += "<tr>"
										+ "<td>" + $(row).find("id").text() + "</td>"
										+ "<td>" + $(row).find("sido_name").text() + "</td>"
										+ "<td>" + $(row).find("sigungu_name").text() + "</td>"
										+ "<td>" + $(row).find("shel_nm").text() + "</td>"
										+ "<td>" + $(row).find("address").text() + "</td>"
										+ "<td>" + $(row).find("shel_av").text() + "</td>"
										+ "<td>" + $(row).find("lenth").text() + "</td>"
										+ "<td>" + $(row).find("shel_div_type").text() + "</td>"
									+ "</tr>"
						})
						$tbody.html(bodyTr);
						
						$table.append($thead, $tbody).appendTo("#result3"); // append() -> 자식 요소로 넣음?; appendTo()? 14h40 강사님 설명 제대로 못 들음 ㅠ.ㅠ -> frontend_workspace > jquery '09_요소 생성 및 제거.html' 참고
								
						/* 만약 반복문을 돌리지 않고, 아래와 같이 진행할 경우
						$table.append($thead)
							  .append(data)
							  .appendTo("#result3");
						문제점 = xml 파일을 html 형식으로 parsing할 수 없기 때문에, 위와 같이 쓸 수 없음
						*/
					},
					error : () => {
						console.log("AJAX 통신 실패");
					}
				})
			})
			
			// 12h 내가 실습 때 만든 것
			$("#btn2").click(function() {
				$.ajax({
					url : "shelter.do",
					data : {},
					success : function(data) {
						// console.log(data); // #document
						
						let rowArr = $(data).find("row");
						// console.log(rowArr); // k.fn.init(50) [row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, row, prevObject: k.fn.init(1)]
						
						let value = "";
						rowArr.each(function(i, row) {
							value += "<tr>"
										+ "<td>" + $(row).find("id").text() + "</td>"
										+ "<td>" + $(row).find("sido_name").text() + "</td>"
										+ "<td>" + $(row).find("sigungu_name").text() + "</td>"
										+ "<td>" + $(row).find("remarks").text() + "</td>"
										+ "<td>" + $(row).find("shel_nm").text() + "</td>"
										+ "<td>" + $(row).find("address").text() + "</td>"
										+ "<td>" + $(row).find("shel_av").text() + "</td>"
										+ "<td>" + $(row).find("lenth").text() + "</td>"
										+ "<td>" + $(row).find("shel_div_type").text() + "</td>"
										+ "<td>" + $(row).find("seismic").text() + "</td>"
										+ "<td>" + $(row).find("height").text() + "</td>"
									+ "</tr>"
						})
						
						$("#result2 tbody").html(value);
						
						// 13h10 kakao map api 추가
						var mapContainer = document.getElementById('map'), // 지도를 표시할 div  
						    mapOption = { 
						        center: new kakao.maps.LatLng(37.5335, 126.9896), // 지도의 중심좌표
						        level: 14 // 지도의 확대 레벨
						    };
						
						var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다
						 
						// 마커를 표시할 위치와 title 객체 배열입니다 
						var positions = [];
						
						rowArr.each(function(i, row) {
							var markerPosition  = new kakao.maps.LatLng($(row).find("lat").text(), $(row).find("lon").text());
							
							var marker = new kakao.maps.Marker({position: markerPosition});
							
							positions[i] = {
									title: $(row).find("shel_nm").text(),
									latlng: markerPosition
							}
						})
						
						// 마커 이미지의 이미지 주소입니다
						var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
						    
						for (var i = 0; i < positions.length; i ++) {
						    // 마커 이미지의 이미지 크기 입니다
						    var imageSize = new kakao.maps.Size(24, 35); 
						    
						    // 마커 이미지를 생성합니다    
						    var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
						    
						    // 마커를 생성합니다
						    var marker = new kakao.maps.Marker({
						        map: map, // 마커를 표시할 지도
						        position: positions[i].latlng, // 마커를 표시할 위치
						        title : positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
						        image : markerImage // 마커 이미지 
						    });
						}
						
					},
					error : function() {
						console.log("AJAX 통신 실패");
					}
				})
			})
		})
		
	</script>
	
	<!--2022.2.25(금) 13h5 기본 기능 구현 완료 -> 추가로 하고 싶은 것 = paging 처리 + 가나다순 정렬 + 강원도/경상북도/부산/울산 등 큰 분류 선택해서 조회 + 지도에 위/경도로 pins 표시-->

</body>
</html>