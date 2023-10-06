<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<!--2022.3.2(수) 10h45
		계획 = saveId라는 쿠키가 있다면, id를 저장한 것으로 간주 -> 저장된 value를 불러와서, id 입력 창에 자동 설정 + id 저장하기 체크박스에 체크 수행-->
	
	<form>
		<c:choose>
			<c:when test="${ not empty cookie.saveId }">
				ID : <input type="text" value="${ cookie.saveId.value }"><br>
				ID 저장 <input type="checkbox" name="saveId" checked>
			</c:when>
			<c:otherwise>
				ID : <input type="text" value=""><br>
				ID 저장 <input type="checkbox" name="saveId">
			</c:otherwise>
		</c:choose>
	</form>

</body>
</html>