<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>index.jsp</title>
</head>
<body>
	<!--2022.2.15(화) 11h15 views 안에 있으면 접근 못 함 -> webapp 폴더 바로 아래에 이 jsp 파일 만듦; http://localhost:8008/spring/-->
	여긴 index.jsp에요~
	
	<!--2022.2.15(화) 12h5 표준 액션 태그(=taglib 지시어 작성 필요 없음) 사용해서 main.jsp로 forwarding-->
	<jsp:forward page="WEB-INF/views/main.jsp" />
	<!--slash 없이 시작 = 상대 경로 표시 -> 현재 url(localhost:8008/spring/index.jsp)에서 가장 뒤/마지막의 slash에 붙음 -> forwarding하며 url = localhost:8008/spring/WEB-INF/views/main.jsp
		vs /(=webapp 의미)로 시작 = 절대 경로(로 감)-->
	
</body>
</html>