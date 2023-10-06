<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>인증 확인</title>
</head>
<body>
	<!--2022.3.15(화) 11h35-->
	<h1>인증 번호 확인</h1>
	
	<!--12h25-->
	<form action="check" method="post">
		<input type="text" name="secret"> <!--사용자가 입력한 이메일 주소로 전송받은 인증 번호를 입력하는 칸-->
		<input type="submit" value="인증하기">
	</form>

</body>
</html>