<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>이메일 입력</title>
</head>
<body>
	<!--2022.3.15(화) 11h40-->
	<h1>이메일 입력</h1>
	
	<form action="input" method="post">
		<!--서버에서 넘기는 값으로써의 value + 그렇게 넘기는 값 value의 key 값인 name-->
		<input type="email" name="email"> <!--강사님께서는 input text 타입으로 하심; 이 input 태그에 입력된 값 value는 서버로 넘길 값 + 그 value의 key 값은 name 속성의 값-->
		<input type="submit" value="인증 요청"> <!--html 태그에서 value 속성 = 보여지는 것 -> 이 input 태그로부터 서버로 값 넘길 것 아니므로, name 속성 필요 없음-->
	</form>

</body>
</html>